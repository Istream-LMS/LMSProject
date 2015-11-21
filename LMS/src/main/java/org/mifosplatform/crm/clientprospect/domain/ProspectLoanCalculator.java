package org.mifosplatform.crm.clientprospect.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "m_prospect_loan_calculator")
public class ProspectLoanCalculator extends AbstractPersistable<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "product_id")
	private Long productId;
	
	@Column(name = "term")
	private int term;
	
	@Column(name = "vehicle_cost_price", scale = 6, precision = 19)
    private BigDecimal vehicleCostPrice;
	
	@Column(name = "interest_rate", scale = 6, precision = 19)
    private BigDecimal interestRate;
	
	@Column(name = "deposit_amount", scale = 6, precision = 19)
    private BigDecimal depositAmount;
	
	@Column(name = "cof", scale = 6, precision = 19)
    private BigDecimal cof;
	
	@Column(name = "maintenance", scale = 6, precision = 19)
    private BigDecimal maintenance;
	
	@Column(name = "replacement_tyres", scale = 6, precision = 19)
    private BigDecimal replacementTyres;
	
	@Column(name = "insurance", scale = 6, precision = 19)
    private BigDecimal insurance;
	
	@Column(name = "deprecisation", scale = 6, precision = 19)
    private BigDecimal deprecisation;
	
	@Column(name = "residual_cost_VEP", scale = 6, precision = 19)
    private BigDecimal residualCostVEP;
	
	@Column(name = "residual_amount_VEP", scale = 6, precision = 19)
    private BigDecimal residualAmountVEP;
	
	@Column(name = "residual_amount_VIP", scale = 6, precision = 19)
    private BigDecimal residualAmountVIP;
	
	public ProspectLoanCalculator() {
		
	}

	public ProspectLoanCalculator(final Long productId,
			final BigDecimal principal, final BigDecimal interest,
			final int term, final BigDecimal deposit,
			final BigDecimal totalCof, final BigDecimal totalMaintenances,
			final BigDecimal totalReplacementTyres,
			final BigDecimal totalComprehensiveInsurance,
			final BigDecimal totalResidualDeprecisation,
			final BigDecimal totalResidualCostVEP,
			final BigDecimal residualAmountVEP,
			final BigDecimal totalResidualAmountVIP) {
		
		this.productId = productId;
		this.term = term;
		this.vehicleCostPrice = principal;
		this.interestRate = interest;
		this.depositAmount = deposit;
		this.cof = totalCof;
		this.maintenance = totalMaintenances;
		this.replacementTyres = totalReplacementTyres;
		this.insurance = totalComprehensiveInsurance;
		this.deprecisation = totalResidualDeprecisation;
		this.residualCostVEP = totalResidualCostVEP;
		this.residualAmountVEP = residualAmountVEP;
		this.residualAmountVIP = totalResidualAmountVIP;
	}

	public Long getProductId() {
		return productId;
	}

	public int getTerm() {
		return term;
	}

	public BigDecimal getVehicleCostPrice() {
		return vehicleCostPrice;
	}

	public BigDecimal getInterestRate() {
		return interestRate;
	}

	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	public BigDecimal getCof() {
		return cof;
	}

	public BigDecimal getMaintenance() {
		return maintenance;
	}

	public BigDecimal getReplacementTyres() {
		return replacementTyres;
	}

	public BigDecimal getInsurance() {
		return insurance;
	}

	public BigDecimal getDeprecisation() {
		return deprecisation;
	}

	public BigDecimal getResidualCostVEP() {
		return residualCostVEP;
	}

	public BigDecimal getResidualAmountVEP() {
		return residualAmountVEP;
	}

	public BigDecimal getResidualAmountVIP() {
		return residualAmountVIP;
	}
	
}
