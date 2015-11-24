package org.mifosplatform.portfolio.loanaccount.data;

import java.math.BigDecimal;

public class LoanCalculatorData {

	private int key;
	private BigDecimal retailPrice;
	private BigDecimal vatAmount;
	private BigDecimal purchasePrice;

	private BigDecimal coiForYear;
	private BigDecimal cofForYear;
	private BigDecimal maintenanceForYear;
	private BigDecimal replacementTyresForYear;
	private BigDecimal comprehensiveInsuranceForYear;
	private BigDecimal deprecisationForYear;
	private BigDecimal totalForYear;

	private BigDecimal coi;
	private BigDecimal cof;
	private BigDecimal maintenance;
	private BigDecimal replacementTyres;
	private BigDecimal comprehensiveInsurance;
	private BigDecimal deprecisation;
	private BigDecimal totalWithOutMaintenance;
	private BigDecimal totalMaintenance;

	
	private BigDecimal rateWithOutMaintenance;
	private BigDecimal costWithOutMaintenance;
	private BigDecimal rateWithMaintenance;

	private BigDecimal residualDeprecisation;
	private BigDecimal residualCost;
	private BigDecimal residualAmountVEP;
	private BigDecimal residualAmountVIP;

	private BigDecimal quoteWithOutMaintenance;
	private BigDecimal quoteWithMaintenance;
	
	private BigDecimal mileage;
	private BigDecimal excess;
	
	/*private BigDecimal financialLeasePayoutForYear;
	private BigDecimal financialLeasePayout;*/
	
	private BigDecimal payoutAdminChargesForYear;
	private BigDecimal payoutAdminCharges;

	private BigDecimal accountWDV;
	private BigDecimal taxWDV;

	public LoanCalculatorData() {

	}

	public LoanCalculatorData(BigDecimal retailPrice, BigDecimal vatAmount,
			BigDecimal purchasePrice, BigDecimal residualDeprecisation,
			BigDecimal residualCost, BigDecimal residualAmountVEP,
			BigDecimal residualAmountVIP, int key, BigDecimal awAmount, 
			BigDecimal twAmount, BigDecimal mileage, BigDecimal fLPForYear, 
			BigDecimal financialLeasePayout) {
		
		this.retailPrice = retailPrice;
		this.vatAmount = vatAmount;
		this.purchasePrice = purchasePrice;
		this.residualDeprecisation = residualDeprecisation;
		this.residualCost = residualCost;
		this.residualAmountVEP = residualAmountVEP;
		this.residualAmountVIP = residualAmountVIP;
		this.key = key;
		this.accountWDV = awAmount;
		this.taxWDV = twAmount;
		this.mileage = mileage;
		/*this.financialLeasePayoutForYear = fLPForYear;
		this.financialLeasePayout = financialLeasePayout;*/
		
		this.payoutAdminChargesForYear = fLPForYear;
		this.payoutAdminCharges = financialLeasePayout;
	}

	public LoanCalculatorData(BigDecimal coiForYear, BigDecimal cofForYear,
			BigDecimal maintenanceForYear, BigDecimal replacementTyresForYear,
			BigDecimal comprehensiveInsuranceForYear,
			BigDecimal deprecisationForYear, BigDecimal totalForYear,
			BigDecimal coi, BigDecimal cof, BigDecimal maintenance,
			BigDecimal replacementTyres, BigDecimal comprehensiveInsurance,
			BigDecimal deprecisation, BigDecimal totalwoMaintenance,
			BigDecimal totalMaintenance, BigDecimal rateWOMaintenance,
			BigDecimal costWOMaintenance, BigDecimal rateWithMaintenance,
			BigDecimal quoteWOMaintenance, BigDecimal quoteWMaintenance) {
		
		this.coiForYear = coiForYear;
		this.cofForYear = cofForYear;
		this.maintenanceForYear = maintenanceForYear;
		this.replacementTyresForYear = replacementTyresForYear;
		this.comprehensiveInsuranceForYear = comprehensiveInsuranceForYear;
		this.deprecisationForYear = deprecisationForYear;
		this.totalForYear = totalForYear;
		this.coi = coi;
		this.cof = cof;
		this.maintenance = maintenance;
		this.replacementTyres = replacementTyres;
		this.comprehensiveInsurance = comprehensiveInsurance;
		this.deprecisation = deprecisation;
		this.totalWithOutMaintenance = totalwoMaintenance;
		this.totalMaintenance = totalMaintenance;
		this.rateWithOutMaintenance = rateWOMaintenance;
		this.costWithOutMaintenance = costWOMaintenance;
		this.rateWithMaintenance = rateWithMaintenance;
		this.quoteWithOutMaintenance = quoteWOMaintenance;
		this.quoteWithMaintenance = quoteWMaintenance;	
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
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

	public BigDecimal getReplacementTyresForYear() {
		return replacementTyresForYear;
	}

	public void setReplacementTyresForYear(BigDecimal replacementTyresForYear) {
		this.replacementTyresForYear = replacementTyresForYear;
	}

	public BigDecimal getComprehensiveInsuranceForYear() {
		return comprehensiveInsuranceForYear;
	}

	public void setComprehensiveInsuranceForYear(
			BigDecimal comprehensiveInsuranceForYear) {
		this.comprehensiveInsuranceForYear = comprehensiveInsuranceForYear;
	}

	public BigDecimal getDeprecisationForYear() {
		return deprecisationForYear;
	}

	public void setDeprecisationForYear(BigDecimal deprecisationForYear) {
		this.deprecisationForYear = deprecisationForYear;
	}

	public BigDecimal getTotalForYear() {
		return totalForYear;
	}

	public void setTotalForYear(BigDecimal totalForYear) {
		this.totalForYear = totalForYear;
	}

	public BigDecimal getCoi() {
		return coi;
	}

	public void setCoi(BigDecimal coi) {
		this.coi = coi;
	}

	public BigDecimal getCof() {
		return cof;
	}

	public void setCof(BigDecimal cof) {
		this.cof = cof;
	}

	public BigDecimal getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(BigDecimal maintenance) {
		this.maintenance = maintenance;
	}

	public BigDecimal getReplacementTyres() {
		return replacementTyres;
	}

	public void setReplacementTyres(BigDecimal replacementTyres) {
		this.replacementTyres = replacementTyres;
	}

	public BigDecimal getComprehensiveInsurance() {
		return comprehensiveInsurance;
	}

	public void setComprehensiveInsurance(BigDecimal comprehensiveInsurance) {
		this.comprehensiveInsurance = comprehensiveInsurance;
	}

	public BigDecimal getDeprecisation() {
		return deprecisation;
	}

	public void setDeprecisation(BigDecimal deprecisation) {
		this.deprecisation = deprecisation;
	}

	public BigDecimal getTotalWithOutMaintenance() {
		return totalWithOutMaintenance;
	}

	public void setTotalWithOutMaintenance(BigDecimal totalWithOutMaintenance) {
		this.totalWithOutMaintenance = totalWithOutMaintenance;
	}

	public BigDecimal getTotalMaintenance() {
		return totalMaintenance;
	}

	public void setTotalMaintenance(BigDecimal totalMaintenance) {
		this.totalMaintenance = totalMaintenance;
	}

	public BigDecimal getRateWithOutMaintenance() {
		return rateWithOutMaintenance;
	}

	public void setRateWithOutMaintenance(BigDecimal rateWithOutMaintenance) {
		this.rateWithOutMaintenance = rateWithOutMaintenance;
	}

	public BigDecimal getCostWithOutMaintenance() {
		return costWithOutMaintenance;
	}

	public void setCostWithOutMaintenance(BigDecimal costWithOutMaintenance) {
		this.costWithOutMaintenance = costWithOutMaintenance;
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

	public BigDecimal getQuoteWithOutMaintenance() {
		return quoteWithOutMaintenance;
	}

	public void setQuoteWithOutMaintenance(BigDecimal quoteWithOutMaintenance) {
		this.quoteWithOutMaintenance = quoteWithOutMaintenance;
	}

	public BigDecimal getQuoteWithMaintenance() {
		return quoteWithMaintenance;
	}

	public void setQuoteWithMaintenance(BigDecimal quoteWithMaintenance) {
		this.quoteWithMaintenance = quoteWithMaintenance;
	}

	public BigDecimal getMileage() {
		return mileage;
	}

	public void setMileage(BigDecimal mileage) {
		this.mileage = mileage;
	}

	public BigDecimal getExcess() {
		return excess;
	}

	public void setExcess(BigDecimal excess) {
		this.excess = excess;
	}

	public BigDecimal getPayoutAdminChargesForYear() {
		return payoutAdminChargesForYear;
	}

	public void setPayoutAdminChargesForYear(BigDecimal payoutAdminChargesForYear) {
		this.payoutAdminChargesForYear = payoutAdminChargesForYear;
	}

	public BigDecimal getPayoutAdminCharges() {
		return payoutAdminCharges;
	}

	public void setPayoutAdminCharges(BigDecimal payoutAdminCharges) {
		this.payoutAdminCharges = payoutAdminCharges;
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

	
	
}
