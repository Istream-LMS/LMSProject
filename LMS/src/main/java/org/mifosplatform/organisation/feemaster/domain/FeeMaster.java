package org.mifosplatform.organisation.feemaster.domain;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.mifosplatform.finance.depositandrefund.domain.DepositCalculationType;
import org.mifosplatform.finance.depositandrefund.domain.DepositTimeType;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "m_fee_master", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "fee_code" }, name = "fee_code") })
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

	@Column(name = "deposit_time_enum", nullable = false)
	private Integer depositTime;
	
	@Column(name = "deposit_calculation_enum")
	private Integer depositCalculation;
	
	@Column(name = "deposit_on_enum")
	private Integer depositOn;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "is_deleted", nullable = false)
	private char deleted = 'N';

	@Column(name = "is_refundable")
	private String isRefundable;

	public FeeMaster() {
	}

	public FeeMaster(final String feeCode, final String feeDescription,
			final String transactionType,final DepositTimeType depositTime, final DepositCalculationType depositCalculationType,
			final DepositTimeType depositOn,final BigDecimal amount, final String isRefundable) {
		this.feeCode = feeCode;
		this.feeDescription = feeDescription;
		this.transactionType = transactionType;
		this.depositTime = depositTime.getValue();
        this.depositCalculation = depositCalculationType.getValue();
        this.depositOn = depositOn.getValue();
		this.amount = amount;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public char getDeleted() {
		return deleted;
	}

	public void setDeleted(char deleted) {
		this.deleted = deleted;
	}
	
	public Integer getDepositCalculation() {
		return depositCalculation;
	}
	
	public Integer getDepositTime() {
		return depositTime;
	}

	public void setDepositTime(Integer depositTime) {
		this.depositTime = depositTime;
	}

	public Integer getDepositOn() {
		return depositOn;
	}

	public void setDepositOn(Integer depositOn) {
		this.depositOn = depositOn;
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

		final String depositTimeParamName = "depositTimeType";
        if (command.isChangeInIntegerParameterNamed(depositTimeParamName, this.depositTime)) {
            final Integer newValue = command.integerValueOfParameterNamed(depositTimeParamName);
            actualChanges.put(depositTimeParamName, newValue);
            actualChanges.put("locale", localeAsInput);
            this.depositTime = DepositTimeType.fromInt(newValue).getValue();

        }
        
        final String depositCalculationParamName = "depositCalculationType";
        if (command.isChangeInIntegerParameterNamed(depositCalculationParamName, this.depositCalculation)) {
            final Integer newValue = command.integerValueOfParameterNamed(depositCalculationParamName);
            actualChanges.put(depositCalculationParamName, newValue);
            actualChanges.put("locale", localeAsInput);
            this.depositCalculation = DepositCalculationType.fromInt(newValue).getValue();

        }
        
        final String depositOnParamName = "depositOn";
        if (command.isChangeInIntegerParameterNamed(depositOnParamName, this.depositOn)) {
        	final Integer newValue = command.integerValueOfParameterNamed(depositOnParamName);
        	actualChanges.put(depositOnParamName, newValue);
        	actualChanges.put("locale", localeAsInput);
        	this.depositOn = DepositCalculationType.fromInt(newValue).getValue();
        	
        }

		final String unitPriceParamName = "amount";
		if (command.isChangeInBigDecimalParameterNamed(unitPriceParamName,
				this.amount)) {
			final BigDecimal newValue = command
					.bigDecimalValueOfParameterNamed(unitPriceParamName);
			actualChanges.put(unitPriceParamName, newValue);
			actualChanges.put("locale", localeAsInput);
			this.amount = newValue;
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
	}

	public static FeeMaster fromJson(JsonCommand command) {
		final String feeCode = command.stringValueOfParameterNamed("feeCode");
		final String feeDescription = command
				.stringValueOfParameterNamed("feeDescription");
		final String transactionType = command
				.stringValueOfParameterNamed("transactionType");
        final DepositTimeType depositTimeType = DepositTimeType.fromInt(command
        		.integerValueOfParameterNamed("depositTimeType"));
        final DepositCalculationType depositCalculationType = DepositCalculationType.fromInt(command
                .integerValueOfParameterNamed("depositCalculationType"));
        final DepositTimeType depositOn = DepositTimeType.fromInt(command
                .integerValueOfParameterNamed("depositOn"));
        final BigDecimal amount = command
        		.bigDecimalValueOfParameterNamed("amount");
		final String isRefundable = command
				.stringValueOfParameterNamed("isRefundable");
		// final char isRefundable =
		// command.booleanObjectValueOfParameterNamed("isRefundable")?'Y':'N';
		return new FeeMaster(feeCode, feeDescription, transactionType,
				depositTimeType,depositCalculationType,depositOn, amount, isRefundable);
	}

	public boolean isDeleted() {
		
		return this.deleted =='Y';
	}

}
