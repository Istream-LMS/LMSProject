package org.mifosplatform.organisation.feemaster.data;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.organisation.mcodevalues.data.MCodeData;
import org.mifosplatform.portfolio.charge.data.ChargeData;

public class FeeMasterData {

	private Long id;
	private String feeCode;
	private String feeDescription;
	private String transactionType;
	private String chargeCode;
	private List<ChargeData> chargeDatas;
	private Long feeId;
	private Long regionId;
	private BigDecimal amount;
	private FeeMasterData feeMasterData;
	private Collection<MCodeData> transactionTypeDatas;
	private String isRefundable;
	
	private EnumOptionData depositTimeType;
	private EnumOptionData depositCalculationType;
	private EnumOptionData depositOnType;
	
	private List<EnumOptionData> depositTimeTypeOptions;
	private List<EnumOptionData> depositCalculationTypeOptions;
	private List<EnumOptionData> depositOnTypeOptions;

	public FeeMasterData(Long id, String feeCode, String feeDescription,String transactionType,EnumOptionData depositTimeType,
			EnumOptionData depositCalculationType,EnumOptionData depositOnType,BigDecimal amount, String isRefundable) {

		this.id = id;
		this.feeCode = feeCode;
		this.feeDescription = feeDescription;
		this.transactionType = transactionType;
		this.depositTimeType = depositTimeType;
		this.depositCalculationType = depositCalculationType;
		this.depositOnType = depositOnType;
		this.amount = amount;
		this.isRefundable = isRefundable;

	}

	public FeeMasterData(Collection<MCodeData> transactionTypeDatas,List<EnumOptionData> depositTimeTypeOptions,
			List<EnumOptionData> depositCalculationTypeOptions,List<EnumOptionData> depositOnTypeOptions) {

		this.transactionTypeDatas = transactionTypeDatas;
		this.depositTimeTypeOptions = depositTimeTypeOptions;
		this.depositCalculationTypeOptions = depositCalculationTypeOptions;
		this.depositOnTypeOptions = depositOnTypeOptions;
	}

	public FeeMasterData(Long id, Long feeId, Long regionId, BigDecimal amount) {

		this.id = id;
		this.feeId = feeId;
		this.regionId = regionId;
		this.amount = amount;
	}

	public FeeMasterData(FeeMasterData feeMasterData,Collection<MCodeData> transactionTypeDatas,
			List<EnumOptionData> depositTimeTypeOptions, List<EnumOptionData> depositCalculationTypeOptions,
			List<EnumOptionData> depositOnTypeOptions) {

		this.feeMasterData = feeMasterData;
		this.transactionTypeDatas = transactionTypeDatas;
		this.depositTimeTypeOptions = depositTimeTypeOptions;
		this.depositCalculationTypeOptions = depositCalculationTypeOptions;
		this.depositOnTypeOptions = depositOnTypeOptions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public List<ChargeData> getChargeDatas() {
		return chargeDatas;
	}

	public void setChargeDatas(List<ChargeData> chargeDatas) {
		this.chargeDatas = chargeDatas;
	}

	public Long getFeeId() {
		return feeId;
	}

	public void setFeeId(Long feeId) {
		this.feeId = feeId;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public FeeMasterData getFeeMasterData() {
		return feeMasterData;
	}

	public void setFeeMasterData(FeeMasterData feeMasterData) {
		this.feeMasterData = feeMasterData;
	}

	public Collection<MCodeData> getTransactionTypeDatas() {
		return transactionTypeDatas;
	}

	public void setTransactionTypeDatas(Collection<MCodeData> transactionTypeDatas) {
		this.transactionTypeDatas = transactionTypeDatas;
	}

	public String getIsRefundable() {
		return isRefundable;
	}


}
