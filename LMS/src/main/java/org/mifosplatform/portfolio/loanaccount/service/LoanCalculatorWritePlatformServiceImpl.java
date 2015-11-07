package org.mifosplatform.portfolio.loanaccount.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.organisation.taxmapping.domain.TaxMap;
import org.mifosplatform.organisation.taxmapping.domain.TaxMapRepositoryWrapper;
import org.mifosplatform.portfolio.loanaccount.data.LoanCalculatorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class LoanCalculatorWritePlatformServiceImpl implements
		LoanCalculatorWritePlatformService {

	private final PlatformSecurityContext context;
	private final FromJsonHelper fromApiJsonHelper;
	private final TaxMapRepositoryWrapper taxMapRepository;

	public final String ACCOUNTWDV = "ACCT.TAX";
	public final String TAXWDV = "TAX_DEP";
	public final String VAT = "VAT";
	private final BigDecimal TWELVE = new BigDecimal(12);
	private final BigDecimal TWO = new BigDecimal(2);
	private final BigDecimal ONEHALF = new BigDecimal(1.15);
	private final BigDecimal HUNDERED = new BigDecimal(100);
	private final BigDecimal ONE = BigDecimal.ONE;
	//private final int payTerms[] = { 12, 24, 36, 48, 60 };

	private final MathContext mc = new MathContext(8, RoundingMode.HALF_EVEN);
	private final MathContext mc2 = new MathContext(8, RoundingMode.HALF_UP);

	private TaxMap accountWDV;
	private TaxMap taxWDV;
	private TaxMap vatWDV;

	@Autowired
	public LoanCalculatorWritePlatformServiceImpl(final PlatformSecurityContext context,
			final FromJsonHelper fromApiJsonHelper, final TaxMapRepositoryWrapper taxMapRepository) {

		this.context = context;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.taxMapRepository = taxMapRepository;
	}

	@Override
	public CommandProcessingResult createLoanCalculator(JsonCommand command) {

		this.context.authenticatedUser();
		generateData();
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		Gson gson = new Gson();		

		final JsonElement parsedJson = this.fromApiJsonHelper.parse(command.json());
		String[] payTerms = this.fromApiJsonHelper.extractArrayNamed("payTerms", parsedJson);
		final BigDecimal principal = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("principal", parsedJson);
		final BigDecimal interest = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("interestRatePerPeriod", parsedJson);
		final BigDecimal costOfFund = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("costOfFund", parsedJson);
		final BigDecimal maintenance = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("maintenance", parsedJson);

		JsonElement jsonParser;
		BigDecimal totalPrincipal = principal;
		final BigDecimal accountWDVRate = accountWDV.getRate();
		final BigDecimal taxWDVRate = taxWDV.getRate();
		final BigDecimal amountvwRate = vatWDV.getRate().divide(HUNDERED, mc);
		
		BigDecimal vatA = principal.multiply(amountvwRate, mc);
		BigDecimal processingAmount = principal.subtract(vatA, mc);

		if(payTerms.length == 0 ) {
			payTerms = new String[] { "12", "24", "36", "48", "60" };
		}
		for (String payTerm : payTerms) {

			LoanCalculatorData loanCalculatorData = generateCalculation(
					Integer.parseInt(payTerm), totalPrincipal, interest, costOfFund, maintenance,
					accountWDVRate, taxWDVRate, amountvwRate, processingAmount);

			totalPrincipal = loanCalculatorData.getResidualAmountVIP();
			jsonParser = this.fromApiJsonHelper.parse(gson.toJson(loanCalculatorData));
			jsonArray.add(jsonParser);
		}
		jsonObject.addProperty("principal", principal);
		jsonObject.add("payTerms", jsonArray);

		Map<String, Object> withChanges = new HashMap<String, Object>();
		withChanges.put("data", jsonObject.toString());

		return new CommandProcessingResultBuilder().with(withChanges).build();
	}

	private TaxMap getTaxMapData(String taxCode) {
		return this.taxMapRepository.findByTaxCode(taxCode);
	}

	private void generateData() {
		accountWDV = getTaxMapData(ACCOUNTWDV);
		taxWDV = getTaxMapData(TAXWDV);
		vatWDV = getTaxMapData(VAT);
	}
	
	private BigDecimal divideAtCalc(BigDecimal residual, BigDecimal value) {
		
		try {
			if(isGreaterThanZero(residual)) {
				return residual.divide(value, mc);
			}
			return BigDecimal.ZERO;
		} catch (ArithmeticException e) {
			return residual.divide(value, mc2);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static boolean isGreaterThanZero(final BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) == 1;
    }

	private LoanCalculatorData generateCalculation(int key, BigDecimal retailPrice, BigDecimal intrestRate,
			BigDecimal cofForMonth, BigDecimal maintenanceForMonth, BigDecimal accountWDVRate, 
			BigDecimal taxWDVRate, BigDecimal amountvwRate, BigDecimal processingAmount) {

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
		BigDecimal coiForMonthD = processingAmount.multiply(intrestRate, mc);
		BigDecimal coiForMonth = divideAtCalc(coiForMonthD, HUNDERED);
		
		BigDecimal deprecisationForMonth = processingAmount.multiply(residualDeprecisation, mc); // (10) //=$B$18*D43
		BigDecimal totalForMonth = coiForMonth.add(cofForMonth, mc).add(maintenanceForMonth, mc).add(deprecisationForMonth, mc); // (11)

		BigDecimal coiForYear = keyPercent.multiply(coiForMonth, mc);// (12) //=$B$18*$B$8*(D15/12)/100
		BigDecimal cofForYear = keyPercent.multiply(cofForMonth, mc);// (13) //=B6*D15/12
		BigDecimal maintenanceForYear = keyPercent.multiply(maintenanceForMonth, mc);// (14) //=$B$7*D15/12
		BigDecimal deprecisationForYear = deprecisationForMonth.multiply(keyPercent, mc);// (15) //=$B$18*D43*D15/12

		BigDecimal totalwoMaintenance = coiForYear.add(deprecisationForYear, mc); // (16)
		BigDecimal totalMaintenance = totalwoMaintenance.add(cofForYear, mc).add(maintenanceForYear, mc); // (17)

		//BigDecimal rateWOMaintenance = totalwoMaintenance.divide(keyBigDecimal, mc);// (18) //=D36/D15
		BigDecimal rateWOMaintenance = divideAtCalc(totalwoMaintenance, keyBigDecimal);
		
		//BigDecimal costWOMaintenance = totalwoMaintenance.divide(keyBigDecimal, mc);// (19) //=D36/D15
		BigDecimal costWOMaintenance = divideAtCalc(totalwoMaintenance, keyBigDecimal);
		
		//BigDecimal rateWithMaintenance = totalMaintenance.divide(keyBigDecimal, mc);// (20) //=D37/D15
		BigDecimal rateWithMaintenance = divideAtCalc(totalMaintenance, keyBigDecimal);
		
		BigDecimal quoteWOMaintenance = rateWOMaintenance; // (21)
		BigDecimal quoteWMaintenance = rateWithMaintenance; // (22)

		return new LoanCalculatorData(retailPrice, vatAmount, purchasePrice, coiForMonth, cofForMonth, 
				maintenanceForMonth, deprecisationForMonth, totalForMonth, coiForYear, cofForYear,
				maintenanceForYear, deprecisationForYear, totalwoMaintenance, totalMaintenance, 
				rateWOMaintenance, costWOMaintenance, rateWithMaintenance, residualDeprecisation, 
				residualCost, residualAmountVEP, residualAmountVIP, quoteWOMaintenance, quoteWMaintenance, key);
	}

	

}