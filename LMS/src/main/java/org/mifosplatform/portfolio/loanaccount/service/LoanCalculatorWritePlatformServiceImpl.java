package org.mifosplatform.portfolio.loanaccount.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.mifosplatform.crm.clientprospect.domain.ProspectLoanCalculator;
import org.mifosplatform.crm.clientprospect.domain.ProspectLoanCalculatorRepository;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.taxmapping.domain.TaxMap;
import org.mifosplatform.organisation.taxmapping.domain.TaxMapRepositoryWrapper;
import org.mifosplatform.portfolio.loanaccount.data.LoanCalculatorData;
import org.mifosplatform.portfolio.loanaccount.serialization.LoanCalculatorCommandFromApiJsonDeserializer;
import org.mifosplatform.portfolio.loanproduct.exception.PrincipalAmountGreaterThanDepositException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Service
public class LoanCalculatorWritePlatformServiceImpl implements
		LoanCalculatorWritePlatformService {

	private final PlatformSecurityContext context;
	private final FromJsonHelper fromApiJsonHelper;
	private final TaxMapRepositoryWrapper taxMapRepository;
	private final LoanCalculatorCommandFromApiJsonDeserializer fromApiJsonDeserializer;
	private final ProspectLoanCalculatorRepository prospectLoanCalculatorRepository;

	public final String ACCOUNTWDV = "ACCT.TAX";
	public final String TAXWDV = "TAX_DEP";
	public final String VAT = "VAT";
	private final BigDecimal TWELVE = new BigDecimal(12);
	private final BigDecimal TWO = new BigDecimal(2);
	private final BigDecimal ONEHALF = new BigDecimal(1.15);
	private final BigDecimal HUNDERED = new BigDecimal(100);
	private final BigDecimal ONE = BigDecimal.ONE;
	private final BigDecimal ZERO = BigDecimal.ZERO;

	private final MathContext mc = new MathContext(8, RoundingMode.HALF_EVEN);
	private final MathContext mc2 = new MathContext(8, RoundingMode.HALF_UP);
	private static final String PROSPECT = "Prospect";

	private TaxMap accountWDV;
	private TaxMap taxWDV;
	private TaxMap vatWDV;
	
	@Autowired
	public LoanCalculatorWritePlatformServiceImpl(final PlatformSecurityContext context,
			final FromJsonHelper fromApiJsonHelper, final TaxMapRepositoryWrapper taxMapRepository,
			final LoanCalculatorCommandFromApiJsonDeserializer fromApiJsonDeserializer,
			final ProspectLoanCalculatorRepository prospectLoanCalculatorRepository) {

		this.context = context;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.taxMapRepository = taxMapRepository;
		this.fromApiJsonDeserializer = fromApiJsonDeserializer;
		this.prospectLoanCalculatorRepository = prospectLoanCalculatorRepository;
	}
	
	private BigDecimal getValue(String key, JsonElement parsedJson) {

		BigDecimal value = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(key, parsedJson);

		if (null == value)
			return ZERO;
		else
			return value;
	}

	@Override
	public CommandProcessingResult createLoanCalculator(Long entityId, JsonCommand command) {

		this.context.authenticatedUser();
		this.fromApiJsonDeserializer.validateForCreate(command.json());
		
		generateData();
		JsonElement jsonParser;

		final BigDecimal accountWDVRate = accountWDV.getRate();
		final BigDecimal taxWDVRate = taxWDV.getRate();
		final BigDecimal vatRate = vatWDV.getRate().divide(HUNDERED, mc);

		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		Gson gson = new Gson();

		final JsonElement parsedJson = this.fromApiJsonHelper.parse(command.json());
		JsonArray payTerms = this.fromApiJsonHelper.extractJsonArrayNamed("payTerms", parsedJson);
		final BigDecimal deposit = getValue("deposit", parsedJson);
		final BigDecimal principal = getValue("principal", parsedJson);
		final BigDecimal interest = getValue("interestRatePerPeriod", parsedJson);
		BigDecimal costOfFund = getValue("costOfFund", parsedJson);
		BigDecimal maintenance = getValue("maintenance", parsedJson);
		BigDecimal mileage = getValue("mileage", parsedJson);
		final BigDecimal excess = getValue("excess", parsedJson);
		final BigDecimal fLPForYear = getValue("FLPForYear", parsedJson);	
		final BigDecimal replacementTyresForYear = getValue("replacementTyres", parsedJson);
		final BigDecimal comprehensiveInsuranceForYear = getValue("comprehensiveInsurance", parsedJson);
		final Long productId = this.fromApiJsonHelper.extractLongNamed("productId", parsedJson);
		
		mileage = divideAtCalc(mileage, new BigDecimal(payTerms.size()));
		costOfFund = divideAtCalc(costOfFund, new BigDecimal(payTerms.size()));
		maintenance = divideAtCalc(maintenance, new BigDecimal(payTerms.size()));
		
		JsonArray deprecisationArray = this.fromApiJsonHelper.extractJsonArrayNamed("deprecisationArray", parsedJson);

		if(null == deprecisationArray) {
			deprecisationArray = new JsonArray();
		}
		
		JsonArray residualArray = this.fromApiJsonHelper.extractJsonArrayNamed("residualArray", parsedJson);

		if(null == residualArray) {
			residualArray = new JsonArray();
		}
		
		if(null == entityId) {
			entityId = new Long(0);
		}
		
		if (principal.compareTo(deposit) < 1) {
			throw new PrincipalAmountGreaterThanDepositException(principal, deposit);
		}

		BigDecimal totalPrincipal = principal.subtract(deposit);

		BigDecimal vatAmount = totalPrincipal.multiply(vatRate, mc);
		BigDecimal processingAmount = totalPrincipal.subtract(vatAmount, mc);
		
		
		BigDecimal totalcoi = ZERO, totalCof = ZERO, totalMaintenances = ZERO, 
				totalReplacementTyres = ZERO, totalComprehensiveInsurance = ZERO,	
				totalResidualDeprecisation = ZERO, totalResidualCostVEP = ZERO, 
				totalResidualAmountVEP = ZERO, totalResidualAmountVIP = ZERO;
		
		int keyPayTerm = 0;
		
		for (JsonElement payTerm : payTerms) {

			keyPayTerm = payTerm.getAsInt();
			final BigDecimal payterm = new BigDecimal(keyPayTerm);
			final BigDecimal percent = divideAtCalc(payterm, TWELVE);
			
			BigDecimal residualVep = null;
			
			for (JsonElement element : residualArray) {
				
				String key = this.fromApiJsonHelper.extractStringNamed("key", element);
				
				if (payTerm.getAsString().equalsIgnoreCase(key)) {
					residualVep = getValue("residualVep", element);
				}
				
			}
			
			LoanCalculatorData loanCalculatorData = generateCalculation(
					keyPayTerm, totalPrincipal, accountWDVRate, taxWDVRate, vatRate, 
					processingAmount, mileage, fLPForYear, percent, residualVep);
			
			calculateTotalAmount(processingAmount, interest, percent, payterm, loanCalculatorData.getResidualDeprecisation(), 
					costOfFund, maintenance, replacementTyresForYear, comprehensiveInsuranceForYear, loanCalculatorData,
					totalcoi, totalCof, totalMaintenances, totalReplacementTyres, totalComprehensiveInsurance);
				
			for (JsonElement element : deprecisationArray) {
				
				String key = this.fromApiJsonHelper.extractStringNamed("key", element);
				final BigDecimal keyTerm = new BigDecimal(key);
				BigDecimal keyPercent = divideAtCalc(keyTerm, TWELVE);
				
				if (payTerm.getAsString().equalsIgnoreCase(key)) {
					
					BigDecimal subdeprecisation = divideAtCalc(getValue("deprecisation", element), HUNDERED) ;
					
					if(null != residualVep) {
						subdeprecisation = loanCalculatorData.getResidualDeprecisation();
					}
					
					final BigDecimal subCOF = getValue("costOfFund", element);
					final BigDecimal subMaintenance = getValue("maintenance", element);
					final BigDecimal subReplacementTyresForYear = getValue("replacementTyres", element);
					final BigDecimal subComprehensiveInsuranceForYear = getValue("comprehensiveInsurance", element);
					
					calculateTotalAmount(processingAmount, interest, keyPercent, keyTerm, subdeprecisation, 
							subCOF, subMaintenance, subReplacementTyresForYear, subComprehensiveInsuranceForYear, loanCalculatorData,
							totalcoi, totalCof, totalMaintenances, totalReplacementTyres, totalComprehensiveInsurance);
				}
			}		
			
			totalcoi = totalcoi.add(loanCalculatorData.getCoiForYear(), mc);
			totalCof = totalCof.add(loanCalculatorData.getCofForYear(), mc);
			totalMaintenances = totalMaintenances.add(loanCalculatorData.getMaintenanceForYear(), mc);
			totalReplacementTyres = totalReplacementTyres.add(loanCalculatorData.getReplacementTyresForYear(), mc);
			totalComprehensiveInsurance = totalComprehensiveInsurance.add(loanCalculatorData.getComprehensiveInsuranceForYear(), mc);
			
			if(entityId == 1) {
				totalResidualDeprecisation = loanCalculatorData.getResidualDeprecisation();
				totalResidualCostVEP = loanCalculatorData.getResidualCost();
				totalResidualAmountVEP = loanCalculatorData.getResidualAmountVEP();
				totalResidualAmountVIP = loanCalculatorData.getResidualAmountVIP();
			}
			
			loanCalculatorData.setExcess(excess);
			loanCalculatorData.setKey(keyPayTerm);
			totalPrincipal = loanCalculatorData.getResidualAmountVIP();
			jsonParser = this.fromApiJsonHelper.parse(gson.toJson(loanCalculatorData));
			jsonArray.add(jsonParser);
		}
		
		jsonObject.addProperty("principal", principal);
		jsonObject.add("payTerms", jsonArray);
		jsonObject.add("keyTerms", payTerms);

		Map<String, Object> withChanges = new HashMap<String, Object>();
		withChanges.put("data", jsonObject.toString());

		Long entityValue = new Long(-1);
		
		if(entityId == 1) {
			ProspectLoanCalculator prospectLoanCalculator = new ProspectLoanCalculator(productId,
					principal, interest, keyPayTerm, deposit, totalCof, totalMaintenances, 
					totalReplacementTyres, totalComprehensiveInsurance, totalResidualDeprecisation,
					totalResidualCostVEP, totalResidualAmountVEP, totalResidualAmountVIP);
			
			this.prospectLoanCalculatorRepository.save(prospectLoanCalculator);
			entityValue = prospectLoanCalculator.getId();
		}
		
		return new CommandProcessingResultBuilder().withEntityId(entityValue).with(withChanges).build();
	}

	private TaxMap getTaxMapData(String taxCode) {
		return this.taxMapRepository.findByTaxCode(taxCode);
	}

	private void generateData() {
		accountWDV = getTaxMapData(ACCOUNTWDV);
		taxWDV = getTaxMapData(TAXWDV);
		vatWDV = getTaxMapData(VAT);
	}
	
	private BigDecimal divideAtCalc(BigDecimal dividend, BigDecimal divisor) {
		
		try {
			if(isGreaterThanZero(dividend)) {
				return dividend.divide(divisor, mc);
			}
			return BigDecimal.ZERO;
		} catch (ArithmeticException e) {
			return dividend.divide(divisor, mc2);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static boolean isGreaterThanZero(final BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) == 1;
    }

	private LoanCalculatorData generateCalculation(final int key, final BigDecimal retailPrice, 
			final BigDecimal accountWDVRate, final BigDecimal taxWDVRate, 
			final BigDecimal vatRate, final BigDecimal processingAmount, final BigDecimal mileage, 
			final BigDecimal fLPForYear, BigDecimal keyPercent, BigDecimal residualVep) {

		BigDecimal vatAmount = retailPrice.multiply(vatRate, mc); // (1)
		BigDecimal purchasePrice = retailPrice.subtract(vatAmount, mc);// (2)

		BigDecimal amountawRateVal = accountWDVRate.multiply(keyPercent, mc);
		BigDecimal amounttwRateVal = taxWDVRate.multiply(keyPercent, mc);

		BigDecimal awAmount = processingAmount.multiply(ONE.subtract(amountawRateVal, mc), mc);// (3) //=B18*(1-E69*D15/12)
		BigDecimal twAmount = processingAmount.multiply(ONE.subtract(amounttwRateVal, mc), mc);// (4) //=B18*(1-E70*D15/12)
		
		BigDecimal residualAmountVEP = residualVep;
		
		if(null == residualAmountVEP) {
			BigDecimal residual = awAmount.add(twAmount, mc);
	        residualAmountVEP = divideAtCalc(residual, TWO);// (5) //D45=(D55+D56) /2
		}
        
		BigDecimal residualAmountVIP = residualAmountVEP.multiply(ONEHALF, mc);// (6) //=D45*1.15
		
		BigDecimal residualCost = divideAtCalc(residualAmountVEP, processingAmount);// (7) //=D45/B18
		
		BigDecimal residuald = processingAmount.subtract(residualAmountVEP, mc);
		residuald = divideAtCalc(residuald, keyPercent);
		BigDecimal residualDeprecisation = divideAtCalc(residuald, processingAmount);// (8) //(($B$18-C45)/(D15/12))/$B$18
		
		BigDecimal mileageVal = mileage.multiply(keyPercent, mc);
		BigDecimal financialLeasePayout = residualAmountVEP.add(fLPForYear, mc);
		
		return new LoanCalculatorData(retailPrice, vatAmount, purchasePrice, residualDeprecisation, 
				residualCost, residualAmountVEP, residualAmountVIP, key, awAmount, twAmount, 
				mileageVal, fLPForYear, financialLeasePayout);
	}	
	
	private void assignValues(BigDecimal coiForYear,
			BigDecimal cofForYear, BigDecimal maintenanceForYear,
			BigDecimal replacementTyresForYear,
			BigDecimal comprehensiveInsuranceForYear,
			BigDecimal deprecisationForYear, BigDecimal totalForYear,
			BigDecimal coi, BigDecimal cof, BigDecimal maintenance,
			BigDecimal replacementTyres, BigDecimal comprehensiveInsurance,
			BigDecimal deprecisation, BigDecimal totalwoMaintenance,		
			BigDecimal totalMaintenance, BigDecimal rateWOMaintenance,
			BigDecimal costWOMaintenance, BigDecimal rateWithMaintenance,
			BigDecimal quoteWOMaintenance, BigDecimal quoteWMaintenance, 
			BigDecimal residualDeprecisation, LoanCalculatorData loanCalculatorData) {

		loanCalculatorData.setCoiForYear(coiForYear);
		loanCalculatorData.setCofForYear(cofForYear);
		loanCalculatorData.setMaintenanceForYear(maintenanceForYear);
		loanCalculatorData.setReplacementTyresForYear(replacementTyresForYear);
		loanCalculatorData.setComprehensiveInsuranceForYear(comprehensiveInsuranceForYear);
		loanCalculatorData.setDeprecisationForYear(deprecisationForYear);
		loanCalculatorData.setTotalForYear(totalForYear);
		loanCalculatorData.setCoi(coi);
		loanCalculatorData.setCof(cof);
		loanCalculatorData.setMaintenance(maintenance);
		loanCalculatorData.setReplacementTyres(replacementTyres);
		loanCalculatorData.setComprehensiveInsurance(comprehensiveInsurance);
		loanCalculatorData.setDeprecisation(deprecisation);
		loanCalculatorData.setTotalWithOutMaintenance(totalwoMaintenance);
		loanCalculatorData.setTotalMaintenance(totalMaintenance);
		loanCalculatorData.setRateWithOutMaintenance(rateWOMaintenance);
		loanCalculatorData.setCostWithOutMaintenance(costWOMaintenance);
		loanCalculatorData.setRateWithMaintenance(rateWithMaintenance);
		loanCalculatorData.setQuoteWithOutMaintenance(quoteWOMaintenance);
		loanCalculatorData.setQuoteWithMaintenance(quoteWMaintenance);
		loanCalculatorData.setResidualDeprecisation(residualDeprecisation);		
	}

	private void calculateTotalAmount(
			final BigDecimal processingAmount, final BigDecimal intrest,
			final BigDecimal keyPercent, final BigDecimal keyBigDecimal,
			final BigDecimal residualDeprecisation,
			final BigDecimal cofForYear, final BigDecimal maintenanceForYear,
			final BigDecimal replacementTyresForYear,
			final BigDecimal comprehensiveInsuranceForYear,
			LoanCalculatorData loanCalculatorData,
			final BigDecimal totalcoi, final BigDecimal totalCof, final BigDecimal totalMaintenances, 
			final BigDecimal totalReplacementTyres, final BigDecimal totalComprehensiveInsurance) {

		BigDecimal coiForYearRate = processingAmount.multiply(intrest, mc);
		BigDecimal coiForYear = divideAtCalc(coiForYearRate, HUNDERED);// (9)																		// //=($B$18*8*D15/D15)/100
		BigDecimal deprecisationForYear = processingAmount.multiply(residualDeprecisation, mc); // (10) //=$B$18*D43
		
		//BigDecimal coi = keyPercent.multiply(coiForYear, mc);// (12) //=$B$18*$B$8*(D15/12)/100
		//BigDecimal cof = keyPercent.multiply(cofForYear, mc);// (13) //=B6*D15/12
		//BigDecimal maintenance = keyPercent.multiply(maintenanceForYear, mc);// (14) //=$B$7*D15/12
		//BigDecimal replacementTyres = keyPercent.multiply(replacementTyresForYear, mc);
		//BigDecimal comprehensiveInsurance = keyPercent.multiply(comprehensiveInsuranceForYear, mc);
		//BigDecimal deprecisation = keyPercent.multiply(deprecisationForYear, mc);// (15) //=$B$18*D43*D15/12
		
		BigDecimal coi = totalcoi.add(coiForYear, mc);// (12) //=$B$18*$B$8*(D15/12)/100
		BigDecimal cof = totalCof.add(cofForYear, mc);// (13) //=B6*D15/12
		BigDecimal maintenance = totalMaintenances.add(maintenanceForYear, mc);// (14) //=$B$7*D15/12
		BigDecimal replacementTyres = totalReplacementTyres.add(replacementTyresForYear, mc);
		BigDecimal comprehensiveInsurance = totalComprehensiveInsurance.add(comprehensiveInsuranceForYear, mc);
		BigDecimal deprecisation = keyPercent.multiply(deprecisationForYear, mc);// (15) //=$B$18*D43*D15/12

		BigDecimal totalForYear = coiForYear.add(cofForYear, mc).add(maintenanceForYear, mc)
				.add(replacementTyresForYear, mc).add(comprehensiveInsuranceForYear, mc)
				.add(deprecisationForYear, mc); // (11)
		
		BigDecimal totalMaintenance = coi.add(cof, mc).add(maintenance, mc)
				.add(replacementTyres, mc).add(comprehensiveInsurance, mc)
				.add(deprecisation, mc); // (17)

		BigDecimal totalwoMaintenance = totalMaintenance
				.subtract(maintenance, mc).subtract(comprehensiveInsurance, mc)
				.subtract(cof, mc); // (16)

		BigDecimal rateWOMaintenance = divideAtCalc(totalwoMaintenance, keyBigDecimal); // (18) //=D36/D15
		BigDecimal costWOMaintenance = divideAtCalc(totalwoMaintenance, keyBigDecimal); // (19) //=D36/D15
		BigDecimal rateWithMaintenance = divideAtCalc(totalMaintenance, keyBigDecimal); // (20) //=D37/D15
		BigDecimal quoteWOMaintenance = rateWOMaintenance; // (21)
		BigDecimal quoteWMaintenance = rateWithMaintenance; // (22)

		assignValues(coiForYear, cofForYear, maintenanceForYear,
				replacementTyresForYear, comprehensiveInsuranceForYear,
				deprecisationForYear, totalForYear, coi, cof, maintenance,
				replacementTyres, comprehensiveInsurance, deprecisation,
				totalwoMaintenance, totalMaintenance, rateWOMaintenance,
				costWOMaintenance, rateWithMaintenance, quoteWOMaintenance,
				quoteWMaintenance, residualDeprecisation, loanCalculatorData);
	}

	@Override
	public String getExportJsonString(String apiRequestBodyAsJson, boolean isProspect) {
		
		Long entityId = new Long(0);
    	
    	if (isProspect) {
    		entityId = new Long(1);
		}
    	
		final JsonElement jsonElement = this.fromApiJsonHelper.parse(apiRequestBodyAsJson);
    	
    	JsonCommand command = new JsonCommand(null, jsonElement.toString(),jsonElement, fromApiJsonHelper, null, null, null, null, null, null, null, null, null, null, null,null, null);
    	
    	CommandProcessingResult result = createLoanCalculator(entityId, command);
    	
 		entityId = result.resourceId() == null ? 0 : result.resourceId();
         
         Map<String, Object> changes = result.getChanges(); 
         
         JsonObject object = this.fromApiJsonHelper.parse(changes.get("data").toString()).getAsJsonObject(); 
         
         String locale = this.fromApiJsonHelper.extractStringNamed("locale", jsonElement);
                       
         object.addProperty("prospectLoanCalculatorId", entityId);  
         object.addProperty("productName", returnValue(jsonElement, "productName")); 
         object.addProperty("principal", returnValue("principal", jsonElement));  
         object.addProperty("cof", returnValue("costOfFund", jsonElement));  
         object.addProperty("maintenance", returnValue("maintenance", jsonElement));  
         object.addProperty("interest", returnValue("interestRatePerPeriod", jsonElement));  
         object.addProperty("deposit", returnValue("deposit", jsonElement));  
         object.addProperty("locale", locale); 
         object.addProperty("phone", returnValue(jsonElement, "phone")); 
         object.addProperty("address", returnValue(jsonElement, "address")); 
         object.addProperty("customerName", returnValue(jsonElement, "customerName")); 
         object.addProperty("emailId", returnValue(jsonElement, "emailId"));
		
		return object.toString();
	}
	
	private BigDecimal returnValue(String parameter, JsonElement element) {
		
		BigDecimal value = BigDecimal.ZERO;
		
		if(this.fromApiJsonHelper.parameterExists(parameter, element)) {
			
			value = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(parameter, element);
			
			value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		
		return value;
	}
	
	private String returnValue(JsonElement element, String parameter) {
		
		String value = "";
		
		if(this.fromApiJsonHelper.parameterExists(parameter, element)) {
			
			value = this.fromApiJsonHelper.extractStringNamed(parameter, element);
		}
		
		return value;
	}
	
	
	/*private LoanCalculatorData generateCalculation(int key, BigDecimal retailPrice, BigDecimal intrestRate,
			BigDecimal cofForYear, BigDecimal maintenanceForYear, BigDecimal accountWDVRate, 
			BigDecimal taxWDVRate, BigDecimal amountvwRate, BigDecimal processingAmount, 
			BigDecimal mileage, BigDecimal fLPForYear) {

		BigDecimal keyBigDecimal = new BigDecimal(key);
		
		//BigDecimal keyPercent = keyBigDecimal.divide(TWELVE, mc); // D15/12
		BigDecimal keyPercent = divideAtCalc(keyBigDecimal, TWELVE);
		
		BigDecimal vatAmount = retailPrice.multiply(amountvwRate, mc); // (1)
		BigDecimal purchasePrice = retailPrice.subtract(vatAmount, mc);// (2)

		 BigDecimal amountawRateVal = accountWDVRate.multiply(keyPercent, mc);
		 BigDecimal amounttwRateVal = taxWDVRate.multiply(keyPercent, mc);

		BigDecimal awAmount = processingAmount.multiply(ONE.subtract(amountawRateVal, mc), mc);// (3) //=B18*(1-E69*D15/12)
		BigDecimal twAmount = processingAmount.multiply(ONE.subtract(amounttwRateVal, mc), mc);// (4) //=B18*(1-E70*D15/12)
		
		//BigDecimal residualAmountVEP = residual.divide(TWO, mc); // (5) //D45=(D55+D56) /2
        BigDecimal residual = awAmount.add(twAmount, mc);
        BigDecimal residualAmountVEP = divideAtCalc(residual, TWO);
		
		BigDecimal residualAmountVIP = residualAmountVEP.multiply(ONEHALF, mc);// (6) //=D45*1.15
		
		//BigDecimal residualCost = residualAmountVEP.divide(processingAmount, mc);// (7) //=D45/B18
		BigDecimal residualCost = divideAtCalc(residualAmountVEP, processingAmount);
		
		//BigDecimal residualDeprecisation = (processingAmount.subtract(residualAmountVEP, mc)).divide(keyPercent).divide(processingAmount, mc);
		// (8) //(($B$18-C45)/(D15/12))/$B$18
		BigDecimal residuald = processingAmount.subtract(residualAmountVEP, mc);
		residuald = divideAtCalc(residuald, keyPercent);
		BigDecimal residualDeprecisation = divideAtCalc(residuald, processingAmount);
		
		//BigDecimal coiForMonth = (processingAmount.multiply(intrestRate, mc)).divide(HUNDERED, mc);// (9) //=($B$18*8*D15/D15)/100
		BigDecimal coiForYearRate = processingAmount.multiply(intrestRate, mc);
		BigDecimal coiForYear = divideAtCalc(coiForYearRate, HUNDERED);
		
		BigDecimal deprecisationForYear = processingAmount.multiply(residualDeprecisation, mc); // (10) //=$B$18*D43
		BigDecimal totalForYear = coiForYear.add(cofForYear, mc).add(maintenanceForYear, mc).add(deprecisationForYear, mc); // (11)

		BigDecimal coi = keyPercent.multiply(coiForYear, mc);// (12) //=$B$18*$B$8*(D15/12)/100
		BigDecimal cof = keyPercent.multiply(cofForYear, mc);// (13) //=B6*D15/12
		BigDecimal maintenance = keyPercent.multiply(maintenanceForYear, mc);// (14) //=$B$7*D15/12
		BigDecimal deprecisation = deprecisationForYear.multiply(keyPercent, mc);// (15) //=$B$18*D43*D15/12

		BigDecimal totalwoMaintenance = coi.add(deprecisation, mc); // (16)
		BigDecimal totalMaintenance = totalwoMaintenance.add(cof, mc).add(maintenance, mc); // (17)

		//BigDecimal rateWOMaintenance = totalwoMaintenance.divide(keyBigDecimal, mc);// (18) //=D36/D15
		BigDecimal rateWOMaintenance = divideAtCalc(totalwoMaintenance, keyBigDecimal);
		
		//BigDecimal costWOMaintenance = totalwoMaintenance.divide(keyBigDecimal, mc);// (19) //=D36/D15
		BigDecimal costWOMaintenance = divideAtCalc(totalwoMaintenance, keyBigDecimal);
		
		//BigDecimal rateWithMaintenance = totalMaintenance.divide(keyBigDecimal, mc);// (20) //=D37/D15
		BigDecimal rateWithMaintenance = divideAtCalc(totalMaintenance, keyBigDecimal);
		
		BigDecimal quoteWOMaintenance = rateWOMaintenance; // (21)
		BigDecimal quoteWMaintenance = rateWithMaintenance; // (22)
		
		BigDecimal mileageVal = mileage.multiply(keyPercent, mc);
		BigDecimal financialLeasePayout = residualAmountVEP.add(fLPForYear, mc);
				
		
		residualDeprecisation = residualDeprecisation.multiply(HUNDERED, mc);
		residualCost = residualCost.multiply(HUNDERED, mc);

		return new LoanCalculatorData(retailPrice, vatAmount, purchasePrice, coiForYear, cofForYear, 
				maintenanceForYear, deprecisationForYear, totalForYear, coi, cof,
				maintenance, deprecisation, totalwoMaintenance, totalMaintenance, 
				rateWOMaintenance, costWOMaintenance, rateWithMaintenance, residualDeprecisation, 
				residualCost, residualAmountVEP, residualAmountVIP, quoteWOMaintenance, quoteWMaintenance,
				key, awAmount, twAmount, mileageVal, fLPForYear, financialLeasePayout);
	}*/	

}
