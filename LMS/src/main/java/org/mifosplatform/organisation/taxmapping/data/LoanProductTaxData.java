package org.mifosplatform.organisation.taxmapping.data;

import java.math.BigDecimal;

public class LoanProductTaxData {

	private Long productId;
	private Long taxId;
	
	public LoanProductTaxData(final Long productId, final Long taxId) {
		this.productId = productId;
		this.taxId = taxId;
	}

	public Long getProductId() {
		return productId;
	}

	public Long getTaxId() {
		return taxId;
	}
	
	
}
