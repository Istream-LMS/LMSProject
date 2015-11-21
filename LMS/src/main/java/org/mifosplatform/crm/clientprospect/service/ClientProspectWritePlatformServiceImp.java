package org.mifosplatform.crm.clientprospect.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.service.CommandWrapperBuilder;
import org.mifosplatform.commands.service.PortfolioCommandSourceWritePlatformService;
import org.mifosplatform.crm.clientprospect.domain.ClientProspect;
import org.mifosplatform.crm.clientprospect.domain.ClientProspectJpaRepository;
import org.mifosplatform.crm.clientprospect.domain.ProspectDetail;
import org.mifosplatform.crm.clientprospect.domain.ProspectDetailJpaRepository;
import org.mifosplatform.crm.clientprospect.domain.ProspectLoanCalculator;
import org.mifosplatform.crm.clientprospect.domain.ProspectLoanCalculatorRepository;
import org.mifosplatform.crm.clientprospect.domain.ProspectLoanDetails;
import org.mifosplatform.crm.clientprospect.domain.ProspectLoanDetailsRepository;
import org.mifosplatform.crm.clientprospect.serialization.ClientProspectCommandFromApiJsonDeserializer;
import org.mifosplatform.infrastructure.codes.exception.CodeNotFoundException;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.core.service.DateUtils;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.portfolio.loanaccount.api.LoansApiResource;
import org.mifosplatform.portfolio.loanaccount.data.LoanAccountData;
import org.mifosplatform.portfolio.loanaccount.data.LoanChargeData;
import org.mifosplatform.portfolio.loanaccount.data.LoanTaxData;
import org.mifosplatform.useradministration.domain.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class ClientProspectWritePlatformServiceImp implements
		ClientProspectWritePlatformService {

	private final static Logger LOGGER = (Logger) LoggerFactory
			.getLogger(ClientProspectWritePlatformServiceImp.class);

	private final PlatformSecurityContext context;
	private final ClientProspectJpaRepository clientProspectJpaRepository;
	private final ProspectDetailJpaRepository prospectDetailJpaRepository;
	private final ClientProspectCommandFromApiJsonDeserializer clientProspectCommandFromApiJsonDeserializer;
	private final FromJsonHelper fromApiJsonHelper;
	private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
	private final ProspectLoanCalculatorRepository prospectLoanCalculatorRepository;
	private final ProspectLoanDetailsRepository prospectLoanDetailsRepository;
	private final LoansApiResource loansApiResource;
	
	private final String dateFormat = "dd MMMM yyyy";
	private final String locale = "en";
	private final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
	
	private final MathContext mc = new MathContext(8, RoundingMode.HALF_EVEN);
	private final MathContext mc2 = new MathContext(8, RoundingMode.HALF_UP);

	@Autowired
	public ClientProspectWritePlatformServiceImp(
			final PlatformSecurityContext context,
			final ClientProspectJpaRepository clientProspectJpaRepository,
			final ClientProspectCommandFromApiJsonDeserializer clientProspectCommandFromApiJsonDeserializer,
			final FromJsonHelper fromApiJsonHelper,
			final ProspectDetailJpaRepository prospectDetailJpaRepository,
			final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService,
			final ProspectLoanCalculatorRepository prospectLoanCalculatorRepository,
			final ProspectLoanDetailsRepository prospectLoanDetailsRepository,
			final LoansApiResource loansApiResource) {
		
		this.context = context;
		this.clientProspectJpaRepository = clientProspectJpaRepository;
		this.clientProspectCommandFromApiJsonDeserializer = clientProspectCommandFromApiJsonDeserializer;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.prospectDetailJpaRepository = prospectDetailJpaRepository;
		this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
		this.prospectLoanCalculatorRepository = prospectLoanCalculatorRepository;
		this.prospectLoanDetailsRepository = prospectLoanDetailsRepository;
		this.loansApiResource = loansApiResource;
	}

	@Transactional
	@Override
	public CommandProcessingResult createProspect(JsonCommand command) {

		try {
			context.authenticatedUser();
			this.clientProspectCommandFromApiJsonDeserializer.validateForCreate(command.json());
			
			final ClientProspect entity = ClientProspect.fromJson(fromApiJsonHelper, command);
			this.clientProspectJpaRepository.save(entity);
			
			if (command.parameterExists("prospectLoanCalculatorId")) {

				Long loanCalcId = command.longValueOfParameterNamed("prospectLoanCalculatorId");
				ProspectLoanCalculator prospectLoanCalculator = this.prospectLoanCalculatorRepository.findOne(loanCalcId);

				ProspectLoanDetails prospectLoanDetails = ProspectLoanDetails.createData(entity, prospectLoanCalculator);
				this.prospectLoanDetailsRepository.save(prospectLoanDetails);
			}

			return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(entity.getId()).build();
			
		} catch (DataIntegrityViolationException dve) {
			handleDataIntegrityIssues(command, dve);
			return new CommandProcessingResult(Long.valueOf(-1));
		} catch (ParseException pe) {
			throw new PlatformDataIntegrityException("invalid.date.and.time.format",
					"invalid.date.and.time.format", "invalid.date.and.time.format");
		}
	}

	@Transactional
	@Override
	public CommandProcessingResult followUpProspect(final JsonCommand command, final Long prospectId) {
		try {
			context.authenticatedUser();
			this.clientProspectCommandFromApiJsonDeserializer.validateForUpdate(command.json());
			
			final ProspectDetail prospectDetail = ProspectDetail.fromJson(command, prospectId);
			prospectDetailJpaRepository.save(prospectDetail);
			
			return new CommandProcessingResultBuilder().withCommandId(command.commandId())
					.withEntityId(prospectDetail.getProspectId()).build();
		
		} catch (DataIntegrityViolationException dve) {
			handleDataIntegrityIssues(command, dve);
			return CommandProcessingResult.empty();
		} catch (ParseException e) {
			throw new PlatformDataIntegrityException(
					"invalid.date.and.time.format",
					"invalid.date.and.time.format",
					"invalid.date.and.time.format");
		}
	}

	@Transactional
	@Override
	public CommandProcessingResult deleteProspect(JsonCommand command) {
		
		context.authenticatedUser();
		final ClientProspect clientProspect = retrieveCodeBy(command.entityId());
		clientProspect.setIsDeleted('Y');
		clientProspect.setStatus("Canceled");
		//clientProspect.setStatusRemark(command.stringValueOfParameterNamed("statusRemark"));
		
		this.clientProspectJpaRepository.saveAndFlush(clientProspect);
		
		return new CommandProcessingResultBuilder().withEntityId(
				clientProspect.getId()).build();
	}

	private ClientProspect retrieveCodeBy(final Long prospectId) {
		
		final ClientProspect clientProspect = this.clientProspectJpaRepository.findOne(prospectId);
		
		if (clientProspect == null) {
			throw new CodeNotFoundException(prospectId.toString());
		}
		
		return clientProspect;
	}

	@Override
	public CommandProcessingResult convertToClient(final Long entityId) {

		final AppUser currentUser = context.authenticatedUser();
		final ClientProspect clientProspect = retrieveCodeBy(entityId);

		Long clientId = null;

		final JSONObject newClientJsonObject = new JSONObject();
		/*{"officeId":1,"firstname":"Ashok","lastname":"reddy","mailId":"ashokreddyg55126@gmail.com","mobileNo":"9414430931",
			"address":"","locale":"en","active":true,"dateFormat":"dd MMMM yyyy","activationDate":"20 November 2015"}*/
		try {
			
			String activationDate = formatter.format(DateUtils.getDateOfTenant());

			final Long officeId = currentUser.getOffice().getId();
			
			newClientJsonObject.put("dateFormat", dateFormat);
			newClientJsonObject.put("locale", locale);
			newClientJsonObject.put("officeId", officeId);
			newClientJsonObject.put("firstname", clientProspect.getFirstName());
			newClientJsonObject.put("middlename", clientProspect.getMiddleName());
			newClientJsonObject.put("lastname", clientProspect.getLastName());
			newClientJsonObject.put("mailId", clientProspect.getEmailId());
			newClientJsonObject.put("mobileNo", clientProspect.getMobileNumber());
			newClientJsonObject.put("address", clientProspect.getAddress() == null ? "" : clientProspect.getAddress());
			newClientJsonObject.put("active", true);
			newClientJsonObject.put("activationDate", activationDate);
			
			final CommandWrapper commandNewClient = new CommandWrapperBuilder().createClient()
					.withJson(newClientJsonObject.toString().toString()).build(); //
			
			final CommandProcessingResult clientResult = this.commandsSourceWritePlatformService.logCommandSource(commandNewClient);

			clientId = clientResult.getClientId();
			
			Long loanId = createLoan(entityId, clientId);
			clientProspect.setNote("created loanId:"+ loanId);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		clientProspect.setStatus("Closed");
		
		// clientProspect.setIsDeleted('Y');

		// clientProspect.setStatusRemark(command.stringValueOfParameterNamed("statusRemark"));
		
		this.clientProspectJpaRepository.saveAndFlush(clientProspect);
		
		return new CommandProcessingResultBuilder().withEntityId(clientId).build();

	}
	
	private Long createLoan(Long prospectId, Long clientId) {
		
		final ClientProspect clientProspect = retrieveCodeBy(prospectId);
		
		ProspectLoanDetails prospectLoanDetails = this.prospectLoanDetailsRepository.findByProspectId(clientProspect);
		
		String loanType = "individual";
		
		LoanAccountData newLoanAccount = this.loansApiResource.getTemplate(clientId, null, 
				prospectLoanDetails.getProductId(), loanType, false);
		
		String activationDate = formatter.format(DateUtils.getDateOfTenant());
		
		String elementData = returnTaxCharges(newLoanAccount.getTaxes(), prospectLoanDetails.getVehicleCostPrice());
		
		JsonElement element = this.fromApiJsonHelper.parse(elementData);
		
		final BigDecimal principal = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("finalAmount", element);
		
		JsonArray charges = returnCharges(newLoanAccount.getCharges(), prospectLoanDetails);
		
		JsonObject object = new JsonObject();
		
		object.addProperty("clientId", clientId);
		
		object.addProperty("productId", newLoanAccount.getLoanProductId());
		object.addProperty("principal", principal);
		
		object.addProperty("loanTermFrequency", newLoanAccount.getTermFrequency());
		object.addProperty("loanTermFrequencyType", newLoanAccount.getTermPeriodFrequencyType().getId());
		object.addProperty("numberOfRepayments", newLoanAccount.getNumberOfRepayments());
		object.addProperty("repaymentEvery", newLoanAccount.getRepaymentEvery());
		object.addProperty("repaymentFrequencyType", newLoanAccount.getRepaymentFrequencyType().getId());
		object.addProperty("interestRatePerPeriod", newLoanAccount.getInterestRatePerPeriod());
		object.addProperty("interestType", newLoanAccount.getInterestType().getId());
		object.addProperty("interestCalculationPeriodType", newLoanAccount.getInterestCalculationPeriodType().getId());
		object.addProperty("transactionProcessingStrategyId", newLoanAccount.getTransactionProcessingStrategyId());
		object.addProperty("amortizationType", newLoanAccount.getAmortizationType().getId());
		
		object.addProperty("dateFormat", dateFormat);
		object.addProperty("locale", locale);
		object.addProperty("loanType", loanType);
		object.addProperty("expectedDisbursementDate", activationDate);
		object.addProperty("submittedOnDate", activationDate);
		
		object.add("charges", charges);
		
		String loanJsonString = this.loansApiResource.calculateLoanScheduleOrSubmitLoanApplication(null, null, object.toString());
		
		//{"officeId":1,"clientId":62,"loanId":290,"resourceId":290}
		object = element.getAsJsonObject();
		
		element = this.fromApiJsonHelper.parse(loanJsonString);
		
		Long loanId = this.fromApiJsonHelper.extractLongNamed("loanId", element);
		
		object.remove("vatAmount");
		object.remove("finalAmount");
		object.addProperty("loanId", loanId);
		prospectLoanDetails.setClientId(clientId);
		this.prospectLoanDetailsRepository.save(prospectLoanDetails);
		this.loansApiResource.updateLoanTaxMapping(object.toString());
		
		return loanId;
	}
	
	//{"principal":150000,"locale":"en","taxes":[{"id":38,"type":"Percentage","taxValue":13.043478}]}
	private String returnTaxCharges(Collection<LoanTaxData> taxes, BigDecimal principle) {
		
		JsonObject object = new JsonObject();
		
		object.addProperty("principal", principle);
		object.addProperty("locale", locale);
		
		JsonArray taxArray = new JsonArray();
		
		for (LoanTaxData tax : taxes) {
			JsonObject taxObj = new JsonObject();
			
			taxObj.addProperty("id", tax.getTaxId());
			taxObj.addProperty("type", tax.getTaxType());
			taxObj.addProperty("taxValue", tax.getRate());
			
			taxArray.add(taxObj);
		}
		
		object.add("taxes", taxArray);
		
		String output = this.loansApiResource.calculateTaxForLoanApplication(1L, object.toString());
		
		JsonElement element = this.fromApiJsonHelper.parse(output);
		
		String obj = this.fromApiJsonHelper.extractStringNamed("resourceIdentifier", element);
		
		return obj;	
	}

	private static boolean isGreaterThanZero(final BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) == 1;
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

	/*{"clientId":"61","productId":16,"principal":130434.783,"loanTermFrequency":60,"loanTermFrequencyType":2,

	"numberOfRepayments":60,"repaymentEvery":1,"repaymentFrequencyType":2,"interestRatePerPeriod":8,
	"interestType":1,"interestCalculationPeriodType":1,"transactionProcessingStrategyId":1,"amortizationType":1,

	"charges":[{"chargeId":19,"amount":333.73},{"chargeId":20,"amount":336.905},{"chargeId":25,"amount":5}],

	"locale":"en","dateFormat":"dd MMMM yyyy","loanType":"individual","expectedDisbursementDate":"20 November 2015",
	"submittedOnDate":"20 November 2015"}
	
	*/

	private JsonArray returnCharges(Collection<LoanChargeData> charges,
			ProspectLoanDetails prospectLoanDetails) {
		
		BigDecimal key = new BigDecimal(prospectLoanDetails.getTerm());
		
		BigDecimal cof = divideAtCalc(prospectLoanDetails.getCof(), key);
		BigDecimal maintenance = divideAtCalc(prospectLoanDetails.getMaintenance(), key);
		
		JsonArray array = new JsonArray();
		
		for(LoanChargeData chargesData : charges) {
			
			JsonObject obj = new JsonObject();
			obj.addProperty("chargeId", chargesData.getChargeId());
			
			if(chargesData.getName().equalsIgnoreCase("COF")) {
				obj.addProperty("amount", cof);
			} else if(chargesData.getName().equalsIgnoreCase("Maintenance charge")) {		
				obj.addProperty("amount", maintenance);
			} else {
				obj.addProperty("amount", chargesData.getAmount());
			}
			
			array.add(obj);
		}
		
		return array;
	}

	@Override
	public CommandProcessingResult updateProspect(JsonCommand command) {
		
		try {
			context.authenticatedUser();
			this.clientProspectCommandFromApiJsonDeserializer.validateForCreate(command.json());

			final ClientProspect pros = retrieveCodeBy(command.entityId());
			final Map<String, Object> changes = pros.update(command);

			if (!changes.isEmpty()) {
				this.clientProspectJpaRepository.save(pros);
			}

			return new CommandProcessingResultBuilder() //
					.withCommandId(command.commandId()) //
					.withEntityId(pros.getId()) //
					.with(changes) //
					.build();
			
		} catch (DataIntegrityViolationException dve) {
			handleDataIntegrityIssues(command, dve);
		}
		return new CommandProcessingResultBuilder().withEntityId(-1L).build();
	}

	private void handleDataIntegrityIssues(final JsonCommand element,
			final DataIntegrityViolationException dve) {

		Throwable realCause = dve.getMostSpecificCause();
		if (realCause.getMessage().contains("serial_no_constraint")) {
			throw new PlatformDataIntegrityException(
					"validation.error.msg.inventory.item.duplicate.serialNumber",
					"validation.error.msg.inventory.item.duplicate.serialNumber",
					"validation.error.msg.inventory.item.duplicate.serialNumber",
					"");
		}

		LOGGER.error(dve.getMessage(), dve);
	}

}
