package org.mifosplatform.portfolio.loanaccount.data;

import java.math.BigDecimal;

public class LoanCalculatorPdfGenerate {

	private String keyName;
	private BigDecimal valueFor12;
	private BigDecimal valueFor24;
	private BigDecimal valueFor36;
	private BigDecimal valueFor48;
	private BigDecimal valueFor60;
	
	public LoanCalculatorPdfGenerate(String keyName) {
		this.keyName = keyName;
	}
	
	public LoanCalculatorPdfGenerate() {
		
	}
	
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public BigDecimal getValueFor12() {
		return valueFor12;
	}
	public void setValueFor12(BigDecimal valueFor12) {
		this.valueFor12 = valueFor12;
	}
	public BigDecimal getValueFor24() {
		return valueFor24;
	}
	public void setValueFor24(BigDecimal valueFor24) {
		this.valueFor24 = valueFor24;
	}
	public BigDecimal getValueFor36() {
		return valueFor36;
	}
	public void setValueFor36(BigDecimal valueFor36) {
		this.valueFor36 = valueFor36;
	}
	public BigDecimal getValueFor48() {
		return valueFor48;
	}
	public void setValueFor48(BigDecimal valueFor48) {
		this.valueFor48 = valueFor48;
	}
	public BigDecimal getValueFor60() {
		return valueFor60;
	}
	public void setValueFor60(BigDecimal valueFor60) {
		this.valueFor60 = valueFor60;
	}
	

	
	
}
