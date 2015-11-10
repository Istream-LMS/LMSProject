package org.mifosplatform.portfolio.loanaccount.data;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class LoanCalculatorData {

	private int key;
	private BigDecimal retailPrice;
	private BigDecimal vatAmount;
	private BigDecimal purchasePrice;

	private BigDecimal coiForYear;
	private BigDecimal cofForYear;
	private BigDecimal maintenanceForYear;
	private BigDecimal deprecisationForYear;
	private BigDecimal totalForYear;

	private BigDecimal coi;
	private BigDecimal cof;
	private BigDecimal maintenance;
	private BigDecimal deprecisation;
	private BigDecimal totalWOMaintenance;
	private BigDecimal totalMaintenance;

	
	private BigDecimal rateWOMaintenance;
	private BigDecimal costWOMaintenance;
	private BigDecimal rateWithMaintenance;

	
	private BigDecimal residualDeprecisation;
	private BigDecimal residualCost;
	private BigDecimal residualAmountVEP;
	private BigDecimal residualAmountVIP;

	private BigDecimal quoteWOMaintenance;
	private BigDecimal quoteWMaintenance;

	private BigDecimal accountWDV;
	private BigDecimal taxWDV;

	public LoanCalculatorData() {

	}

	public LoanCalculatorData(BigDecimal retailPrice, BigDecimal vatAmount,
			BigDecimal purchasePrice, BigDecimal coiForYear,
			BigDecimal cofForYear, BigDecimal maintenanceForYear,
			BigDecimal deprecisationForYear, BigDecimal totalForYear,
			BigDecimal coi, BigDecimal cof,
			BigDecimal maintenance, BigDecimal deprecisation,
			BigDecimal totalwoMaintenance, BigDecimal totalMaintenance,
			BigDecimal rateWOMaintenance, BigDecimal costWOMaintenance,
			BigDecimal rateWithMaintenance, BigDecimal residualDeprecisation,
			BigDecimal residualCost, BigDecimal residualAmountVEP,
			BigDecimal residualAmountVIP, BigDecimal quoteWOMaintenance,
			BigDecimal quoteWMaintenance, int key,BigDecimal awAmount, BigDecimal twAmount) {
		// TODO Auto-generated constructor stub
		
		this.retailPrice = retailPrice;
		this.vatAmount = vatAmount;
		this.purchasePrice = purchasePrice;
		this.coiForYear = coiForYear;
		this.cofForYear = cofForYear;
		this.maintenanceForYear = maintenanceForYear;
		this.deprecisationForYear = deprecisationForYear;
		this.totalForYear = totalForYear;
		this.coi = coi;
		this.cof = cof;
		this.maintenance = maintenance;
		this.deprecisation = deprecisation;
		this.totalWOMaintenance = totalwoMaintenance;
		this.totalMaintenance = totalMaintenance;
		this.rateWOMaintenance = rateWOMaintenance;
		this.costWOMaintenance = costWOMaintenance;
		this.rateWithMaintenance = rateWithMaintenance;
		this.residualDeprecisation = residualDeprecisation;
		this.residualCost = residualCost;
		this.residualAmountVEP = residualAmountVEP;
		this.residualAmountVIP = residualAmountVIP;
		this.quoteWOMaintenance = quoteWOMaintenance;
		this.quoteWMaintenance = quoteWMaintenance;
		this.key = key;
		this.accountWDV = awAmount;
		this.taxWDV = twAmount;
		
	}

	public BigDecimal getResidualAmountVIP() {
		return residualAmountVIP;
	}
}
