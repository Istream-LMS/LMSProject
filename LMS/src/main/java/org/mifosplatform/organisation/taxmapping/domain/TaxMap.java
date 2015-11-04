package org.mifosplatform.organisation.taxmapping.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "m_tax_mapping")
// , uniqueConstraints = @UniqueConstraint(name = "taxcode", columnNames = {
// "tax_code" }))
public class TaxMap extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 1L;

	@Column(name = "tax_code", length = 10, nullable = false)
	private String taxCode;
	
	@Column(name = "charge_type", length = 20, nullable = false)
	private String chargeType;

	@Column(name = "start_date", nullable = false)
	private Date startDate;

	@Column(name = "tax_type", length = 15, nullable = false)
	private String taxType;

	@Column(name = "rate", nullable = false)
	private BigDecimal rate;

	@Column(name = "tax_inclusive")
	Integer taxInclusive;

	public TaxMap() {
	}

	public TaxMap(final String chargeType, final String taxCode,
			final LocalDate startDate, final String taxType, final BigDecimal rate,
			final Integer taxInclusive) {
		this.chargeType = chargeType;
		this.taxCode = taxCode;
		this.startDate = startDate.toDate();
		this.taxType = taxType;
		this.rate = rate;
		this.taxInclusive = taxInclusive;
	}

	public static TaxMap fromJson(final JsonCommand command) {

		final String chargeType = command.stringValueOfParameterNamed("chargeType");
		final String taxCode = command.stringValueOfParameterNamed("taxCode");
		final LocalDate startDate = command.localDateValueOfParameterNamed("startDate");
		final String taxType = command.stringValueOfParameterNamed("taxType");
		final BigDecimal rate = command.bigDecimalValueOfParameterNamed("rate");
		final boolean taxInclusive = command
				.booleanPrimitiveValueOfParameterNamed("taxInclusive");

		Integer tax = null;

		if (taxInclusive) {
			tax = 1;
		} else {
			tax = 0;
		}

		return new TaxMap(chargeType, taxCode, startDate, taxType,
				rate, tax);
	}

	public Map<String, Object> update(JsonCommand command) {
		final Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(
				1);
		if (command.isChangeInStringParameterNamed("chargeType",this.chargeType)) {
			final String newValue = command.stringValueOfParameterNamed("chargeType");
			actualChanges.put("chargeType", newValue);
			this.chargeType = StringUtils.defaultIfEmpty(newValue, null);
		}
		if (command.isChangeInStringParameterNamed("taxCode", this.taxCode)) {
			final String newValue = command.stringValueOfParameterNamed("taxCode");
			actualChanges.put("taxCode", newValue);
			this.taxCode = StringUtils.defaultIfEmpty(newValue, null);
		}
		if (command.isChangeInDateParameterNamed("startDate", this.startDate)) {
			final LocalDate newValue = command.localDateValueOfParameterNamed("startDate");
			actualChanges.put("startDate", newValue);
			this.startDate = newValue.toDate();

		}
		if (command.isChangeInStringParameterNamed("taxType", this.taxType)) {
			final String newValue = command.stringValueOfParameterNamed("taxType");
			actualChanges.put("taxType", newValue);
			this.taxType = StringUtils.defaultIfEmpty(newValue, null);
		}
		if (command.isChangeInBigDecimalParameterNamed("rate", this.rate)) {
			final BigDecimal newValue = command.bigDecimalValueOfParameterNamed("rate");
			actualChanges.put("rate", newValue);
			this.rate = newValue;
		}
		if (command.isChangeInBooleanParameterNamed("taxInclusive",
				this.taxInclusive == 1 ? true : false)) {
			final boolean taxInclusive = command
					.booleanPrimitiveValueOfParameterNamed("taxInclusive");

			Integer newValue = null;

			if (taxInclusive) {
				newValue = 1;
			} else {
				newValue = 0;
			}
			actualChanges.put("taxInclusive", newValue);
			this.taxInclusive = newValue;
		}

		return actualChanges;
	}

	/**
	 * @return the chargeCode
	 */
	public String getChargeType() {
		return chargeType;
	}
	
	public void setChargeType(final String chargeType) {
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
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(final Date startDate) {
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

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	
	/**
	 * @return the taxInclusive
	 */
	public Integer getTaxInclusive() {
		return taxInclusive;
	}

	public void setTaxInclusive(Integer taxInclusive) {
		this.taxInclusive = taxInclusive;
	}

}
