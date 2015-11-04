package org.mifosplatform.portfolio.loanaccount.data;

import java.math.BigDecimal;

import org.joda.time.LocalDate;

public class LoanTaxData {

	private  Long id;
	private final Long taxId;
	private Long loanId;
	private final String taxCode;
	private final String chargeType;
	private final LocalDate startDate;
	private final String taxType;
	private final BigDecimal rate;
	private final Integer taxInclusive;
	private BigDecimal amount;
	
	public LoanTaxData( final Long taxId,final String chargeType,final String taxCode,final LocalDate startDate,
			final String taxType, final BigDecimal rate,final Integer taxInclusive) {
		this.taxId = taxId;
		this.chargeType = chargeType;
		this.taxCode = taxCode;
		this.startDate = startDate;
		this.taxType = taxType;
		this.rate = rate;
		this.taxInclusive = taxInclusive;
	}
	
	public LoanTaxData(Long id, Long loanId, Long taxId, String chargeType, String taxCode, LocalDate startDate, String taxType,
							BigDecimal rate, Integer taxInclusive) {
		

		this.id = id;
		this.loanId = loanId;
		this.taxId = taxId;
		this.chargeType = chargeType;
		this.taxCode = taxCode;
		this.startDate = startDate;
		this.taxType = taxType;
		this.rate = rate;
		this.taxInclusive = taxInclusive;
	}

	/**
     * used when populating with details from tax definition (for crud on
     * taxes)
     */
    public static LoanTaxData newLoanTaxDetails(final Long taxId,final String chargeType,final String taxCode,final LocalDate startDate,
			final String taxType, final BigDecimal rate,final Integer taxInclusive) {
    
    	return new LoanTaxData(taxId,chargeType, taxCode, startDate, taxType, rate, taxInclusive);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaxId() {
		return taxId;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public String getChargeType() {
		return chargeType;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public String getTaxType() {
		return taxType;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public Integer getTaxInclusive() {
		return taxInclusive;
	}
    
    

}
