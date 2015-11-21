package org.mifosplatform.portfolio.loanaccount.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.mifosplatform.finance.depositandrefund.domain.DepositCalculationType;
import org.mifosplatform.finance.depositandrefund.domain.DepositTimeType;
import org.mifosplatform.organisation.feemaster.domain.FeeMaster;
import org.mifosplatform.portfolio.loanaccount.command.LoanFeeMasterCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "m_loan_fee_master")
public class LoanFeeMaster extends AbstractPersistable<Long> {

	@ManyToOne(optional = false)
	@JoinColumn(name = "loan_id",  nullable = false)
    private Loan loan;

	@ManyToOne(optional = false)
	@JoinColumn(name = "fee_master_id", nullable = false)
    private FeeMaster feeMaster;

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
	
	@Column(name = "calculation_percentage", scale = 6, precision = 19, nullable = true)
    private BigDecimal percentage;

    @Column(name = "calculation_on_amount", scale = 6, precision = 19, nullable = true)
    private BigDecimal amountPercentageAppliedTo;

    @Column(name = "deposit_amount_or_percentage", scale = 6, precision = 19, nullable = false)
    private BigDecimal amountOrPercentage;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "min_cap", scale = 6, precision = 19, nullable = true)
    private BigDecimal minCap;

    @Column(name = "max_cap", scale = 6, precision = 19, nullable = true)
    private BigDecimal maxCap;
	
	@Column(name = "is_deleted", nullable = false)
	private char deleted = 'N';

	@Column(name = "is_refundable")
	private String isRefundable;
	
	public LoanFeeMaster() {
	}
	
	public LoanFeeMaster(final Loan loan, final FeeMaster depositDefinition, final BigDecimal loanPrincipal, final BigDecimal amount,
			final DepositTimeType depositTime, final DepositCalculationType depositCalculation,
			final DepositTimeType depositOn,final String isRefundable) {
        this.loan = loan;
        this.feeMaster = depositDefinition;
        this.feeCode = depositDefinition.getFeeCode();
        this.feeDescription = depositDefinition.getFeeDescription();
        this.transactionType = depositDefinition.getTransactionType();

        this.depositTime = depositDefinition.getDepositTime();
        if (depositTime != null) {
            this.depositTime = depositTime.getValue();
        }
        
        this.depositCalculation = depositDefinition.getDepositCalculation();
        if (depositCalculation != null) {
            this.depositCalculation = depositCalculation.getValue();
        }
        
        this.depositOn = depositDefinition.getDepositOn();
        if (depositOn != null) {
        	this.depositOn = depositOn.getValue();
        }

        BigDecimal depositAmount = depositDefinition.getAmount();
        if (amount != null) {
            depositAmount = amount;
        }
        
        this.isRefundable = isRefundable == null ? "N" : isRefundable;

        populateDerivedFields(loanPrincipal, depositAmount, BigDecimal.ZERO);
    }
	
	private void populateDerivedFields(final BigDecimal amountPercentageAppliedTo, final BigDecimal depositAmount,BigDecimal loanDeposit) {

        switch (DepositCalculationType.fromInt(this.depositCalculation)) {
            case INVALID:
            	this.percentage = null;
                this.amount = null;
                this.amountPercentageAppliedTo = null;
            break;
            case FLAT:
            	this.percentage = null;
                this.amountPercentageAppliedTo = null;
                this.amount = depositAmount;
            break;
            case PERCENT_OF_AMOUNT:
            	this.percentage = depositAmount;
                this.amountPercentageAppliedTo = amountPercentageAppliedTo;
                if(loanDeposit.compareTo(BigDecimal.ZERO) == 0){
                	loanDeposit = percentageOf(this.amountPercentageAppliedTo);
                }
                this.amount = minimumAndMaximumCap(loanDeposit);
            break;
        }
        this.amountOrPercentage = depositAmount;
    }
	
	public BigDecimal percentageOf(final BigDecimal value) {
        return percentageOf(value, this.percentage);
    }
	
	public static BigDecimal percentageOf(final BigDecimal value,final BigDecimal percentage) {

        BigDecimal percentageOf = BigDecimal.ZERO;

        if (isGreaterThanZero(value)) {
            final MathContext mc = new MathContext(8, RoundingMode.HALF_EVEN);
            final BigDecimal multiplicand = percentage.divide(BigDecimal.valueOf(100l), mc);
            percentageOf = value.multiply(multiplicand, mc);
        }
        return percentageOf;
    }
	
	private static boolean isGreaterThanZero(final BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) == 1;
    }
	
	private BigDecimal minimumAndMaximumCap(final BigDecimal percentageOf){
        BigDecimal minMaxCap = BigDecimal.ZERO;
        if(this.minCap !=null){
            final int minimumCap = percentageOf.compareTo(this.minCap);
            if(minimumCap == -1){
                minMaxCap = this.minCap;
                return minMaxCap;
            }
        }
        if(this.maxCap !=null){
            final int maximumCap = percentageOf.compareTo(this.maxCap);
            if(maximumCap == 1)  {
               minMaxCap = this.maxCap;
               return minMaxCap;
            }
        }
        minMaxCap = percentageOf;
        return minMaxCap;
    }
	
	public static LoanFeeMaster createNewWithoutLoan(final FeeMaster depositDefinition, final BigDecimal loanPrincipal, final BigDecimal amount,
            final DepositTimeType depositTime, final DepositCalculationType depositCalculation,
            final DepositTimeType depositOn,final String isRefundable) {
        return new LoanFeeMaster(null, depositDefinition, loanPrincipal, amount, depositTime, depositCalculation, depositOn, isRefundable);
    }
	
	public void update(final BigDecimal amount){
        BigDecimal amountPercentageAppliedTo = BigDecimal.ZERO;
        if(this.loan!=null){
            switch (DepositCalculationType.fromInt(this.depositCalculation)) {
                case PERCENT_OF_AMOUNT:
                    amountPercentageAppliedTo = this.loan.getPrincpal().getAmount();
                    break;
                default:
                   break;
            }
        }
        
        update(amount, amountPercentageAppliedTo, BigDecimal.ZERO);
    }
	
	public void update(final BigDecimal amount, final BigDecimal loanPrincipal, BigDecimal loanDeposit) {

        if (amount != null) {
            switch (DepositCalculationType.fromInt(this.depositCalculation)) {
                case INVALID:
                break;
                case FLAT:
                        this.amount = amount;
                break;
                case PERCENT_OF_AMOUNT:
                    this.percentage = amount;	
                    this.amountPercentageAppliedTo = loanPrincipal;
                    if(loanDeposit.compareTo(BigDecimal.ZERO) == 0){
                    	loanDeposit = percentageOf(this.amountPercentageAppliedTo);
                    }
                    this.amount = minimumAndMaximumCap(loanDeposit);
               break;
            }
            this.amountOrPercentage = amount;
        }
    }
	
	public void update(final Loan loan) {
        this.loan = loan;
    }
	
	public LoanFeeMasterCommand toCommand() {
        return new LoanFeeMasterCommand(getId(), this.feeMaster.getId(), this.amount, this.depositTime, this.depositCalculation, this.depositOn);
    }

	public Loan getLoan() {
		return loan;
	}

	public FeeMaster getFeeMaster() {
		return feeMaster;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public String getFeeDescription() {
		return feeDescription;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public DepositTimeType getDepositTime() {
		return DepositTimeType.fromInt(depositTime);
	}

	public DepositCalculationType getDepositCalculation() {
		return DepositCalculationType.fromInt(this.depositCalculation);
	}

	public DepositTimeType getDepositOn() {
		return DepositTimeType.fromInt(depositOn);
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public BigDecimal getAmountPercentageAppliedTo() {
		return amountPercentageAppliedTo;
	}

	public BigDecimal getAmountOrPercentage() {
		return amountOrPercentage;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getMinCap() {
		return minCap;
	}

	public BigDecimal getMaxCap() {
		return maxCap;
	}

	public char getDeleted() {
		return deleted;
	}

	public String getIsRefundable() {
		return isRefundable;
	}
	
	
}
