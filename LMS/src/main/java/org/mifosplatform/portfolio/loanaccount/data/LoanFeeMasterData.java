package org.mifosplatform.portfolio.loanaccount.data;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.organisation.feemaster.data.FeeMasterData;
import org.mifosplatform.organisation.mcodevalues.data.MCodeData;
import org.mifosplatform.portfolio.charge.data.ChargeData;

/**
 * @author raghu
 *
 */
public class LoanFeeMasterData {

	
	private Long id;
	private final Long feeMasterId;
	private Long loanId;
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
	
	private final BigDecimal percentageof;

    private final BigDecimal amountPercentageAppliedTo;
    private final BigDecimal amountOrPercentage;
	
	public LoanFeeMasterData( final Long feeMasterId,String feeCode, String feeDescription,String transactionType,EnumOptionData depositTimeType,
			EnumOptionData depositCalculationType,EnumOptionData depositOnType, BigDecimal percentageof, 
			BigDecimal amountPercentageAppliedTo, BigDecimal amountOrPercentage,BigDecimal amount, String isRefundable) {

		this.feeMasterId = feeMasterId;
		this.feeCode = feeCode;
		this.feeDescription = feeDescription;
		this.transactionType = transactionType;
		this.depositTimeType = depositTimeType;
		this.depositCalculationType = depositCalculationType;
		this.depositOnType = depositOnType;
		this.percentageof = percentageof;
		this.amountPercentageAppliedTo = amountPercentageAppliedTo;
		if(amountOrPercentage == null){
            if (depositCalculationType != null && depositCalculationType.getId().intValue() > 1) {
                this.amountOrPercentage = this.percentageof;
            } else {
                this.amountOrPercentage = amount;
            }
        }else{
            this.amountOrPercentage = amountOrPercentage;
        }
		this.amount = amount;
		this.isRefundable = isRefundable ==  null ? "N" : isRefundable;

	}
	
	public LoanFeeMasterData(Long id, Long loanId, final Long feeMasterId,String feeCode, String feeDescription,String transactionType,EnumOptionData depositTimeType,
			EnumOptionData depositCalculationType,EnumOptionData depositOnType, BigDecimal percentageof,
			BigDecimal amountPercentageAppliedTo, BigDecimal amountOrPercentage,BigDecimal amount, String isRefundable) {

		this.id = id;
		this.loanId = loanId;
		this.feeMasterId = feeMasterId;
		this.feeCode = feeCode;
		this.feeDescription = feeDescription;
		this.transactionType = transactionType;
		this.depositTimeType = depositTimeType;
		this.depositCalculationType = depositCalculationType;
		this.depositOnType = depositOnType;
		this.percentageof = percentageof;
		this.amountPercentageAppliedTo = amountPercentageAppliedTo;
		if(amountOrPercentage == null){
            if (depositCalculationType != null && depositCalculationType.getId().intValue() > 1) {
                this.amountOrPercentage = this.percentageof;
            } else {
                this.amountOrPercentage = amount;
            }
        }else{
            this.amountOrPercentage = amountOrPercentage;
        }
		this.amount = amount;
		this.isRefundable = isRefundable ==  null ? "N" : isRefundable;

	}

	/**
     * used when populating with details from tax definition (for crud on
     * taxes)
     */
    public static LoanFeeMasterData newLoanFeeMasterData(final Long feeMasterId,String feeCode, String feeDescription,String transactionType,EnumOptionData depositTimeType,
			EnumOptionData depositCalculationType,EnumOptionData depositOnType,BigDecimal amount, String isRefundable) {
    
    	return new LoanFeeMasterData(feeMasterId,feeCode, feeDescription, transactionType, depositTimeType, depositCalculationType, 
    			depositOnType,null,null,null,amount,isRefundable);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
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

	public void setIsRefundable(String isRefundable) {
		this.isRefundable = isRefundable;
	}

	public EnumOptionData getDepositTimeType() {
		return depositTimeType;
	}

	public void setDepositTimeType(EnumOptionData depositTimeType) {
		this.depositTimeType = depositTimeType;
	}

	public EnumOptionData getDepositCalculationType() {
		return depositCalculationType;
	}

	public void setDepositCalculationType(EnumOptionData depositCalculationType) {
		this.depositCalculationType = depositCalculationType;
	}

	public EnumOptionData getDepositOnType() {
		return depositOnType;
	}

	public void setDepositOnType(EnumOptionData depositOnType) {
		this.depositOnType = depositOnType;
	}

	public Long getFeeMasterId() {
		return feeMasterId;
	}

	   
    

}
