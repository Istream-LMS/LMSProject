package org.mifosplatform.organisation.taxmapping.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "m_loan_tax_mapping")
public class LoanTaxMap extends AbstractPersistable<Long> {

	@Column(name = "loan_id", length = 10, nullable = false)
	private Long loanId;
	
	@Column(name = "tax_id", length = 20, nullable = false)
	private Long taxId;
	
	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	@Column(name = "start_date", nullable = false)
	private Date startDate;
	
	@Column(name = "tax_inclusive")
	Integer taxInclusive;

	public LoanTaxMap() {
	}
	
	public LoanTaxMap(final Long loanId, final Long taxId, 
			final BigDecimal amount, final Date startDate,final int taxInclusive) {
		this.loanId = loanId;
		this.taxId = taxId;
		this.startDate = startDate;
		this.amount = amount;
		this.taxInclusive = taxInclusive;
	}

	public Long getLoanId() {
		return loanId;
	}

	public Long getTaxId() {
		return taxId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public void updateLoanId(Long loanId) {
		this.loanId = loanId;
	}

}
