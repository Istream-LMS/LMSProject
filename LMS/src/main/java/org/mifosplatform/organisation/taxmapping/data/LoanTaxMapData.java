package org.mifosplatform.organisation.taxmapping.data;

import java.math.BigDecimal;

public class LoanTaxMapData {

	private Long id;
	private Long loanId;
	private Long taxId;
	private BigDecimal taxAmount;
	private Integer taxInclusive;

	public LoanTaxMapData(final Long id, final Long loanId, final Long taxId,
			final BigDecimal taxAmount, int taxInclusive) {

		this.id = id;
		this.loanId = loanId;
		this.taxId = taxId;
		this.taxAmount = taxAmount;
		this.taxInclusive = taxInclusive;
	}

	public Long getLoanId() {
		return loanId;
	}

	public Long getTaxId() {
		return taxId;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public Integer getTaxInclusive() {
		return taxInclusive;
	}

	public Long getId() {
		return id;
	}
}
