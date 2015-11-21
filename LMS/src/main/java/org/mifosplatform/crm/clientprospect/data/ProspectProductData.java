package org.mifosplatform.crm.clientprospect.data;

public class ProspectProductData {

	private Long id;
	private String productName;
	private String productDescription;

	public ProspectProductData() {
	}

	public ProspectProductData(final Long id, final String productName, final String productDescription) {
		this.id = id;
		this.productName = productName;
		this.productDescription = productDescription;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	
	
}
