package org.mifosplatform.organisation.feemaster.domain;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.portfolio.charge.domain.ChargeCalculationType;
import org.mifosplatform.portfolio.charge.domain.ChargeTimeType;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "m_fee_master", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "fee_code" }, name = "fee_code"),
		@UniqueConstraint(columnNames = { "transaction_type" }, name = "fee_transaction_type") })
public class FeeMaster extends AbstractPersistable<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "fee_code")
	private String feeCode;

	@Column(name = "fee_description")
	private String feeDescription;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "charge_time_enum", nullable = false)
	private Integer chargeTime;
	
	@Column(name = "charge_calculation_enum")
	private Integer chargeCalculation;
	
	@Column(name = "default_fee_amount")
	private BigDecimal defaultFeeAmount;
	
	@Column(name = "is_deleted", nullable = false)
	private char deleted = 'N';

	@Column(name = "is_refundable")
	private String isRefundable;

	public FeeMaster() {
	}

	public FeeMaster(final String feeCode, final String feeDescription,
			final String transactionType,final ChargeTimeType chargeTime, final ChargeCalculationType chargeCalculationType,
			final BigDecimal defaultFeeAmount, final String isRefundable) {
		this.feeCode = feeCode;
		this.feeDescription = feeDescription;
		this.transactionType = transactionType;
		this.chargeTime = chargeTime.getValue();
        this.chargeCalculation = chargeCalculationType.getValue();
		this.defaultFeeAmount = defaultFeeAmount;
		this.isRefundable = isRefundable;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getFeeDescription() {
		return feeDescription;
	}

	public void setFeeDescription(String feeDescription) {
		this.feeDescription = feeDescription;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getDefaultFeeAmount() {
		return defaultFeeAmount;
	}

	public void setDefaultFeeAmount(BigDecimal defaultFeeAmount) {
		this.defaultFeeAmount = defaultFeeAmount;
	}

	public char getDeleted() {
		return deleted;
	}

	public void setDeleted(char deleted) {
		this.deleted = deleted;
	}

	public Map<String, Object> update(JsonCommand command) {
		

		final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(
				1);
		
		final String localeAsInput = command.locale();

		final String itemCodeParamName = "feeCode";
		if (command.isChangeInStringParameterNamed(itemCodeParamName,
				this.feeCode)) {
			final String newValue = command
					.stringValueOfParameterNamed(itemCodeParamName);
			actualChanges.put(itemCodeParamName, newValue);
			actualChanges.put("locale", localeAsInput);
			this.feeCode = StringUtils.defaultIfEmpty(newValue, null);
		}
		final String itemDescriptionParamName = "feeDescription";
		if (command.isChangeInStringParameterNamed(itemDescriptionParamName,
				this.feeDescription)) {
			final String newValue = command
					.stringValueOfParameterNamed(itemDescriptionParamName);
			actualChanges.put(itemDescriptionParamName, newValue);
			actualChanges.put("locale", localeAsInput);
			this.feeDescription = StringUtils.defaultIfEmpty(newValue, null);
		}

		final String itemClassParamName = "transactionType";
		if (command.isChangeInStringParameterNamed(itemClassParamName,
				this.transactionType)) {
			final String newValue = command
					.stringValueOfParameterNamed(itemClassParamName);
			actualChanges.put(itemClassParamName, newValue);
			actualChanges.put("locale", localeAsInput);
			this.transactionType = StringUtils.defaultIfEmpty(newValue, null);
		}

		final String chargeTimeParamName = "chargeTimeType";
        if (command.isChangeInIntegerParameterNamed(chargeTimeParamName, this.chargeTime)) {
            final Integer newValue = command.integerValueOfParameterNamed(chargeTimeParamName);
            actualChanges.put(chargeTimeParamName, newValue);
            actualChanges.put("locale", localeAsInput);
            this.chargeTime = ChargeTimeType.fromInt(newValue).getValue();

        }
        
        final String chargeCalculationParamName = "chargeCalculationType";
        if (command.isChangeInIntegerParameterNamed(chargeCalculationParamName, this.chargeCalculation)) {
            final Integer newValue = command.integerValueOfParameterNamed(chargeCalculationParamName);
            actualChanges.put(chargeCalculationParamName, newValue);
            actualChanges.put("locale", localeAsInput);
            this.chargeCalculation = ChargeCalculationType.fromInt(newValue).getValue();

        }

		final String unitPriceParamName = "defaultFeeAmount";
		if (command.isChangeInBigDecimalParameterNamed(unitPriceParamName,
				this.defaultFeeAmount)) {
			final BigDecimal newValue = command
					.bigDecimalValueOfParameterNamed(unitPriceParamName);
			actualChanges.put(unitPriceParamName, newValue);
			actualChanges.put("locale", localeAsInput);
			this.defaultFeeAmount = newValue;
		}

		final String isRefundableParamName = "isRefundable";
		if (command.hasParameter(isRefundableParamName)) {
			if (command.isChangeInStringParameterNamed(isRefundableParamName,
					this.isRefundable)) {
				final String newValue = command
						.stringValueOfParameterNamed(isRefundableParamName);
				actualChanges.put(isRefundableParamName, newValue);
				actualChanges.put("locale", localeAsInput);
				this.isRefundable = newValue;
			}
		} else {
			this.isRefundable = null;
		}

		return actualChanges;

	}

	public void delete() {
		this.deleted='Y';
		this.feeCode=this.feeCode+"_"+this.getId();
		this.transactionType=this.transactionType+"_"+this.getId();
		
	}

	public static FeeMaster fromJson(JsonCommand command) {
		final String feeCode = command.stringValueOfParameterNamed("feeCode");
		final String feeDescription = command
				.stringValueOfParameterNamed("feeDescription");
		final String transactionType = command
				.stringValueOfParameterNamed("transactionType");
        final ChargeTimeType chargeTimeType = ChargeTimeType.fromInt(command
        		.integerValueOfParameterNamed("chargeTimeType"));
        final ChargeCalculationType chargeCalculationType = ChargeCalculationType.fromInt(command
                .integerValueOfParameterNamed("chargeCalculationType"));
        final BigDecimal defaultFeeAmount = command
        		.bigDecimalValueOfParameterNamed("defaultFeeAmount");
		final String isRefundable = command
				.stringValueOfParameterNamed("isRefundable");
		// final char isRefundable =
		// command.booleanObjectValueOfParameterNamed("isRefundable")?'Y':'N';
		return new FeeMaster(feeCode, feeDescription, transactionType,
				chargeTimeType,chargeCalculationType, defaultFeeAmount, isRefundable);
	}

}
