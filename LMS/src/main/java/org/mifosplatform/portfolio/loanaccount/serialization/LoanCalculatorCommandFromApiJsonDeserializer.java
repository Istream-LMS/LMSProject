package org.mifosplatform.portfolio.loanaccount.serialization;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Component
public final class LoanCalculatorCommandFromApiJsonDeserializer {

	private final FromJsonHelper fromApiJsonHelper;

	@Autowired
	public LoanCalculatorCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
		this.fromApiJsonHelper = fromApiJsonHelper;
	}

	private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
		if (!dataValidationErrors.isEmpty()) {
			throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", 
					"Validation errors exist.", dataValidationErrors);
		}
	}

	public void validateForCreate(final String json) {

		if (StringUtils.isBlank(json)) {
			throw new InvalidJsonException();
		}

		final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("principal", 
				"interestRatePerPeriod", "costOfFund", "locale", "dateFormat", "maintenance", "payTerms", 
				"deposit", "mileage", "excess", "FLPForYear", "deprecisationArray", "productId",
				"replacementTyres", "comprehensiveInsurance", "residualArray", "productName",
				"customerName", "address", "phone", "repaymentSchedule", "emailId"));

		final Type typeOfMap = new TypeToken<Map<String, Object>>() {
		}.getType();
		
		this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

		final List<ApiParameterError> dataValidationErrors = new ArrayList<ApiParameterError>();
		final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(
				dataValidationErrors).resource("loan.calculator");

		final JsonElement element = this.fromApiJsonHelper.parse(json);

		final BigDecimal principal = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("principal", element);
        baseDataValidator.reset().parameter("principal").value(principal).notNull().positiveAmount();
        
        final BigDecimal interestRatePerPeriod = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("interestRatePerPeriod", element);
        baseDataValidator.reset().parameter("interestRatePerPeriod").value(interestRatePerPeriod).notNull().positiveAmount();
        
        final BigDecimal costOfFund = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("costOfFund", element);
        baseDataValidator.reset().parameter("costOfFund").value(costOfFund).notNull().positiveAmount();
        
        final BigDecimal maintenance = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("maintenance", element);
        baseDataValidator.reset().parameter("maintenance").value(maintenance).notNull().positiveAmount();
        
		final String locale = this.fromApiJsonHelper.extractStringNamed("locale", element);
		baseDataValidator.reset().parameter("locale").value(locale).notBlank();
		
		final Long productId = this.fromApiJsonHelper.extractLongNamed("productId", element);
		baseDataValidator.reset().parameter("productId").value(productId).notBlank();
		
		final JsonArray payTerms = this.fromApiJsonHelper.extractJsonArrayNamed("payTerms", element);
		
		if(null == payTerms || payTerms.size() < 1) {
			baseDataValidator.reset().parameter("payTerms").notBlank();
		}
		
				
		throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}
}
