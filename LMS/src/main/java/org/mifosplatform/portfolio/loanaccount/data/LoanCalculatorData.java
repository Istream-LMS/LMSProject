package org.mifosplatform.portfolio.loanaccount.data;

import java.math.BigDecimal;

public class LoanCalculatorData {

	private int key;
	private BigDecimal retailPrice;
	private BigDecimal vatAmount;
	private BigDecimal purchasePrice;

	private BigDecimal coiForMonth;
	private BigDecimal cofForMonth;
	private BigDecimal maintenanceForMonth;
	private BigDecimal deprecisationForMonth;
	private BigDecimal totalForMonth;

	private BigDecimal coiForYear;
	private BigDecimal cofForYear;
	private BigDecimal maintenanceForYear;
	private BigDecimal deprecisationForYear;
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
			BigDecimal purchasePrice, BigDecimal coiForMonth,
			BigDecimal cofForMonth, BigDecimal maintenanceForMonth,
			BigDecimal deprecisationForMonth, BigDecimal totalForMonth,
			BigDecimal coiForYear, BigDecimal cofForYear,
			BigDecimal maintenanceForYear, BigDecimal deprecisationForYear,
			BigDecimal totalwoMaintenance, BigDecimal totalMaintenance,
			BigDecimal rateWOMaintenance, BigDecimal costWOMaintenance,
			BigDecimal rateWithMaintenance, BigDecimal residualDeprecisation,
			BigDecimal residualCost, BigDecimal residualAmountVEP,
			BigDecimal residualAmountVIP, BigDecimal quoteWOMaintenance,
			BigDecimal quoteWMaintenance, int key) {
		// TODO Auto-generated constructor stub
		
		this.retailPrice = retailPrice;
		this.vatAmount = vatAmount;
		this.purchasePrice = purchasePrice;
		this.coiForMonth = coiForMonth;
		this.cofForMonth = cofForMonth;
		this.maintenanceForMonth = maintenanceForMonth;
		this.deprecisationForMonth = deprecisationForMonth;
		this.totalForMonth = totalForMonth;
		this.coiForYear = coiForYear;
		this.cofForYear = cofForYear;
		this.maintenanceForYear = maintenanceForYear;
		this.deprecisationForYear = deprecisationForYear;
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
		
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public BigDecimal getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(BigDecimal vatAmount) {
		this.vatAmount = vatAmount;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public BigDecimal getCoiForMonth() {
		return coiForMonth;
	}

	public void setCoiForMonth(BigDecimal coiForMonth) {
		this.coiForMonth = coiForMonth;
	}

	public BigDecimal getCofForMonth() {
		return cofForMonth;
	}

	public void setCofForMonth(BigDecimal cofForMonth) {
		this.cofForMonth = cofForMonth;
	}

	public BigDecimal getMaintenanceForMonth() {
		return maintenanceForMonth;
	}

	public void setMaintenanceForMonth(BigDecimal maintenanceForMonth) {
		this.maintenanceForMonth = maintenanceForMonth;
	}

	public BigDecimal getDeprecisationForMonth() {
		return deprecisationForMonth;
	}

	public void setDeprecisationForMonth(BigDecimal deprecisationForMonth) {
		this.deprecisationForMonth = deprecisationForMonth;
	}

	public BigDecimal getTotalForMonth() {
		return totalForMonth;
	}

	public void setTotalForMonth(BigDecimal totalForMonth) {
		this.totalForMonth = totalForMonth;
	}

	public BigDecimal getCoiForYear() {
		return coiForYear;
	}

	public void setCoiForYear(BigDecimal coiForYear) {
		this.coiForYear = coiForYear;
	}

	public BigDecimal getCofForYear() {
		return cofForYear;
	}

	public void setCofForYear(BigDecimal cofForYear) {
		this.cofForYear = cofForYear;
	}

	public BigDecimal getMaintenanceForYear() {
		return maintenanceForYear;
	}

	public void setMaintenanceForYear(BigDecimal maintenanceForYear) {
		this.maintenanceForYear = maintenanceForYear;
	}

	public BigDecimal getDeprecisationForYear() {
		return deprecisationForYear;
	}

	public void setDeprecisationForYear(BigDecimal deprecisationForYear) {
		this.deprecisationForYear = deprecisationForYear;
	}

	public BigDecimal getTotalWOMaintenance() {
		return totalWOMaintenance;
	}

	public void setTotalWOMaintenance(BigDecimal totalWOMaintenance) {
		this.totalWOMaintenance = totalWOMaintenance;
	}

	public BigDecimal getTotalMaintenance() {
		return totalMaintenance;
	}

	public void setTotalMaintenance(BigDecimal totalMaintenance) {
		this.totalMaintenance = totalMaintenance;
	}

	public BigDecimal getRateWOMaintenance() {
		return rateWOMaintenance;
	}

	public void setRateWOMaintenance(BigDecimal rateWOMaintenance) {
		this.rateWOMaintenance = rateWOMaintenance;
	}

	public BigDecimal getCostWOMaintenance() {
		return costWOMaintenance;
	}

	public void setCostWOMaintenance(BigDecimal costWOMaintenance) {
		this.costWOMaintenance = costWOMaintenance;
	}

	public BigDecimal getRateWithMaintenance() {
		return rateWithMaintenance;
	}

	public void setRateWithMaintenance(BigDecimal rateWithMaintenance) {
		this.rateWithMaintenance = rateWithMaintenance;
	}

	public BigDecimal getResidualDeprecisation() {
		return residualDeprecisation;
	}

	public void setResidualDeprecisation(BigDecimal residualDeprecisation) {
		this.residualDeprecisation = residualDeprecisation;
	}

	public BigDecimal getResidualCost() {
		return residualCost;
	}

	public void setResidualCost(BigDecimal residualCost) {
		this.residualCost = residualCost;
	}

	public BigDecimal getResidualAmountVEP() {
		return residualAmountVEP;
	}

	public void setResidualAmountVEP(BigDecimal residualAmountVEP) {
		this.residualAmountVEP = residualAmountVEP;
	}

	public BigDecimal getResidualAmountVIP() {
		return residualAmountVIP;
	}

	public void setResidualAmountVIP(BigDecimal residualAmountVIP) {
		this.residualAmountVIP = residualAmountVIP;
	}

	public BigDecimal getQuoteWOMaintenance() {
		return quoteWOMaintenance;
	}

	public void setQuoteWOMaintenance(BigDecimal quoteWoMaintenance) {
		this.quoteWOMaintenance = quoteWoMaintenance;
	}

	public BigDecimal getQuoteWMaintenance() {
		return quoteWMaintenance;
	}

	public void setQuoteWMaintenance(BigDecimal quoteWMaintenance) {
		this.quoteWMaintenance = quoteWMaintenance;
	}

	public BigDecimal getAccountWDV() {
		return accountWDV;
	}

	public void setAccountWDV(BigDecimal accountWDV) {
		this.accountWDV = accountWDV;
	}

	public BigDecimal getTaxWDV() {
		return taxWDV;
	}

	public void setTaxWDV(BigDecimal taxWDV) {
		this.taxWDV = taxWDV;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
}
