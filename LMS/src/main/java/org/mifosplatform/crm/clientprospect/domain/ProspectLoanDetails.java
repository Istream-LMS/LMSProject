package org.mifosplatform.crm.clientprospect.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "m_prospect_loan_details")
public class ProspectLoanDetails extends AbstractPersistable<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@OneToOne
    @JoinColumn(name = "prospect_id", nullable = false)
    private ClientProspect clientProspect;
	
	@Column(name = "product_id")
	private Long productId;
	
	@Column(name = "client_id")
	private Long clientId;
	
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
	
	@Column(name = "location", length = 50)
	private String location;
	
	public ProspectLoanDetails() {
		
	}

	public ProspectLoanDetails(final ClientProspect clientProspect, final Long productId,
			final int term, final BigDecimal vehicleCostPrice, final BigDecimal interestRate,
			final BigDecimal depositAmount, final BigDecimal cof,
			final BigDecimal maintenance, final BigDecimal replacementTyres,
			final BigDecimal insurance, final BigDecimal deprecisation,
			final BigDecimal residualCostVEP, final BigDecimal residualAmountVEP,
			final BigDecimal residualAmountVIP, final String location) {
		
		this.clientProspect = clientProspect;
		this.productId = productId;
		this.term = term;
		this.vehicleCostPrice = vehicleCostPrice;
		this.interestRate = interestRate;
		this.depositAmount = depositAmount;
		this.cof = cof;
		this.maintenance = maintenance;
		this.replacementTyres = replacementTyres;
		this.insurance = insurance;
		this.deprecisation = deprecisation;
		this.residualCostVEP = residualCostVEP;
		this.residualAmountVEP = residualAmountVEP;
		this.residualAmountVIP = residualAmountVIP;
		this.location = location;
	}

	public static ProspectLoanDetails createData(final ClientProspect clientProspect,
			final ProspectLoanCalculator prospectLoanCalculator) {
		
	
		final Long productId = prospectLoanCalculator.getProductId();
		final int term = prospectLoanCalculator.getTerm();
		final BigDecimal vehicleCostPrice = prospectLoanCalculator.getVehicleCostPrice();
		final BigDecimal interestRate = prospectLoanCalculator.getInterestRate();
		final BigDecimal depositAmount = prospectLoanCalculator.getDepositAmount();
		final BigDecimal cof = prospectLoanCalculator.getCof();
		final BigDecimal maintenance = prospectLoanCalculator.getMaintenance();
		final BigDecimal replacementTyres = prospectLoanCalculator.getReplacementTyres();
		final BigDecimal insurance = prospectLoanCalculator.getInsurance();
		final BigDecimal deprecisation = prospectLoanCalculator.getDeprecisation();
		final BigDecimal residualCostVEP = prospectLoanCalculator.getResidualCostVEP();
		final BigDecimal residualAmountVEP = prospectLoanCalculator.getResidualAmountVEP();
		final BigDecimal residualAmountVIP = prospectLoanCalculator.getResidualAmountVIP();
		final String location = clientProspect.getLocation();
		
		return new ProspectLoanDetails(clientProspect, productId, term, vehicleCostPrice, interestRate, depositAmount, 
				cof, maintenance, replacementTyres, insurance, deprecisation, residualCostVEP, residualAmountVEP,
				residualAmountVIP, location);
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public ClientProspect getClientProspect() {
		return clientProspect;
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

	public String getLocation() {
		return location;
	}
	
	
	
	
	
}
