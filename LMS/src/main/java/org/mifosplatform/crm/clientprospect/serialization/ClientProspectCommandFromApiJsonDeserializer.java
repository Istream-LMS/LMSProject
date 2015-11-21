package org.mifosplatform.crm.clientprospect.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.InvalidJsonException;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Component
public class ClientProspectCommandFromApiJsonDeserializer {

	private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("id", "sourceOfPublicityInt", "loanProductId",
					"firstName", "middleName", "lastName", "mobileNumber", "emailId", 
					"sourceOfPublicity", "preferredCallingTime", "note", "dateFormat",
					"address", "locale", "preferredLoanProduct", "status",
					"statusRemark", "callStatus", "assignedTo", "notes", 
					"isDeleted", "tin", "location", "prospectLoanCalculatorId"));

	private final FromJsonHelper fromApiJsonHelper;

	@Autowired
	ClientProspectCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
		
		this.fromApiJsonHelper = fromApiJsonHelper;
	}

	public void validateForCreate(final String json) {
		
		if (StringUtils.isBlank(json)) {
			throw new InvalidJsonException();
		}

		final Type typeOfMap = new TypeToken<Map<String, Object>>() {
		}.getType();
		
		fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

		final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
		final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("client.prospect");
		
		final JsonElement element = fromApiJsonHelper.parse(json);

		final Long loanProductId = fromApiJsonHelper.extractLongNamed("loanProductId", element);
		baseDataValidator.reset().parameter("loanProductId").value(loanProductId).notBlank();

		final String firstName = fromApiJsonHelper.extractStringNamed("firstName", element);
		baseDataValidator.reset().parameter("firstName").value(firstName).notBlank().notExceedingLengthOf(50);

		final String lastName = fromApiJsonHelper.extractStringNamed("lastName", element);
		baseDataValidator.reset().parameter("lastName").value(lastName).notBlank().notExceedingLengthOf(50);

		/*
		 * final String homePhoneNumber =
		 * fromApiJsonHelper.extractStringNamed("homePhoneNumber", element);
		 * baseDataValidator
		 * .reset().parameter("homePhoneNumber").value(homePhoneNumber
		 * ).notBlank().validateforNumeric().notExceedingLengthOf(20); final
		 * String workPhoneNumber =
		 * fromApiJsonHelper.extractStringNamed("workPhoneNumber", element);
		 * baseDataValidator
		 * .reset().parameter("workPhoneNumber").value(workPhoneNumber
		 * ).notBlank().validateforNumeric().notExceedingLengthOf(20);
		 */
		final String mobileNumber = fromApiJsonHelper.extractStringNamed("mobileNumber", element);
		baseDataValidator.reset().parameter("mobileNumber").value(mobileNumber).notBlank().validatePhoneNumber();

		/*
		 * final String emailId = fromApiJsonHelper.extractStringNamed("emailId",
		 * element);
		 * baseDataValidator.reset().parameter("emailId").value(emailId).notBlank
		 * ().notExceedingLengthOf(100);
		 */

		final String address = fromApiJsonHelper.extractStringNamed("address", element);
		baseDataValidator.reset().parameter("address").value(address).notBlank().notExceedingLengthOf(100);

		final String sourceOfPublicity = fromApiJsonHelper.extractStringNamed("sourceOfPublicity", element);
		baseDataValidator.reset().parameter("sourceOfPublicity").value(sourceOfPublicity).notExceedingLengthOf(50);
		
		final String preferredLoanProduct = fromApiJsonHelper.extractStringNamed("preferredLoanProduct", element);
		baseDataValidator.reset().parameter("preferredLoanProduct").value(preferredLoanProduct).notExceedingLengthOf(50);

		final String note = fromApiJsonHelper.extractStringNamed("note", element);
		baseDataValidator.reset().parameter("note").value(note).notBlank().notExceedingLengthOf(255);

		final String middleName = fromApiJsonHelper.extractStringNamed("middleName", element);
		baseDataValidator.reset().parameter("middleName").value(middleName).notExceedingLengthOf(50);

		throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}

	public void validateForUpdate(final String json) {
		
		if (StringUtils.isBlank(json)) {
			throw new InvalidJsonException();
		}

		final Type typeOfMap = new TypeToken<Map<String, Object>>() {
		}.getType();
		
		fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

		final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
		final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("client.prospect");
		
		final JsonElement element = fromApiJsonHelper.parse(json);

		if (fromApiJsonHelper.parameterExists("type", element)) {
			final String type = fromApiJsonHelper.extractStringNamed("type", element);
			baseDataValidator.reset().parameter("type").value(type).notBlank();
		}

		final Long assignedTo = fromApiJsonHelper.extractLongNamed("assignedTo", element);
		baseDataValidator.reset().parameter("assignedTo").value(assignedTo).notBlank();
		
		final Long callStatus = fromApiJsonHelper.extractLongNamed("callStatus", element);
		baseDataValidator.reset().parameter("callStatus").value(callStatus).notNull();
		
		final String notes = fromApiJsonHelper.extractStringNamed("notes", element);
		baseDataValidator.reset().parameter("notes").value(notes).notBlank();
		
		final String preferredCallingTime = fromApiJsonHelper.extractStringNamed("preferredCallingTime", element);
		baseDataValidator.reset().parameter("preferredCallingTime").value(preferredCallingTime).notBlank();
		
		throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}

	private void throwExceptionIfValidationWarningsExist(
			final List<ApiParameterError> dataValidationErrors) {
		if (!dataValidationErrors.isEmpty()) {
			throw new PlatformApiDataValidationException(
					"validation.msg.validation.errors.exist",
					"Validation errors exist.", dataValidationErrors);
		}
	}
}
