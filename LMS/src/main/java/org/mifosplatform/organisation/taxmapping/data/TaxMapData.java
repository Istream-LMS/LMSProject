package org.mifosplatform.organisation.taxmapping.data;

import java.math.BigDecimal;
import java.util.Collection;

import org.joda.time.LocalDate;
import org.mifosplatform.organisation.mcodevalues.data.MCodeData;
import org.mifosplatform.portfolio.loanaccount.data.LoanTaxData;

public class TaxMapData {

	private Long id;
	private String taxCode;
	private String chargeType;
	private LocalDate startDate;
	private String taxType;
	private BigDecimal rate;
	private Integer taxInclusive;
	
	private Collection<MCodeData> taxTypeData;
	private Collection<MCodeData> chargeTypeData;

	public TaxMapData() {
	}

	public TaxMapData( final String chargeType,final String taxCode,final LocalDate startDate,
			final String taxType, final BigDecimal rate,final Integer taxInclusive) {
		this.chargeType = chargeType;
		this.taxCode = taxCode;
		this.startDate = startDate;
		this.taxType = taxType;
		this.rate = rate;
		this.taxInclusive = taxInclusive;
	}

	public TaxMapData(final Long id, final String chargeType, final String taxCode,  final LocalDate startDate,
			final String taxType, final BigDecimal rate,final Integer taxInclusive){
		this.id = id;
		this.chargeType = chargeType;
		this.taxCode = taxCode;
		this.startDate = startDate;
		this.taxType = taxType;
		this.rate = rate;
		this.taxInclusive = taxInclusive;
	}

	public TaxMapData(final Collection<MCodeData> taxTypeData,final Collection<MCodeData> chargeTypeData) {
		this.taxTypeData = taxTypeData;
		this.chargeTypeData = chargeTypeData;
	}
	
	public LoanTaxData toLoanTaxData() {

        return LoanTaxData.newLoanTaxDetails(this.id, this.chargeType, this.taxCode, this.startDate, this.taxType, this.rate, this.taxInclusive);
    }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the chargeCode
	 */
	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	/**
	 * @return the taxCode
	 */
	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(final String taxCode) {
		this.taxCode = taxCode;
	}

	/**
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(final LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the type
	 */
	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(final String taxType) {
		this.taxType = taxType;
	}

	/**
	 * @return the rate
	 */
	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(final BigDecimal rate) {
		this.rate = rate;
	}

	/**
	 * @return the taxRegion
	 */
	public Integer getTaxInclusive() {
		return taxInclusive;
	}

	public void setTaxInclusive(Integer taxInclusive) {
		this.taxInclusive = taxInclusive;
	}

	/**
	 * @return the taxTypeData
	 */
	public Collection<MCodeData> getTaxTypeData() {
		return taxTypeData;
	}
	
	public void setTaxTypeData(final Collection<MCodeData> taxTypeData) {
		this.taxTypeData = taxTypeData;

	}
	
	/**
	 * @return the taxTypeData
	 */
	public Collection<MCodeData> getChargeTypeData() {
		return chargeTypeData;
	}
	
	public void setChargeTypeData(final Collection<MCodeData> chargeTypeData) {
		this.chargeTypeData = chargeTypeData;
		
	}


}
