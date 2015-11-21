package org.mifosplatform.crm.clientprospect.data;

import java.util.Collection;
import java.util.Date;

import org.mifosplatform.organisation.mcodevalues.data.MCodeData;

/**
 * @author Praveen
 * 
 */
public class ClientProspectData {

	private Long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNumber;
	private String emailId;
	private String sourceOfPublicity;
	private String sourceOfPublicityInt;
	private String preferredLoanProduct;
	private Long preferredPlanInt;
	private Date preferredCallingTime;
	private String address;
	private String status;
	private String isDeleted;
	private Collection<MCodeData> sourceOfPublicityData;
	private Collection<ProspectProductData> productData;
	private String tin;
	private String note;

	public ClientProspectData() {

	}

	public ClientProspectData(Long id, final String firstName, final String middleName, final String lastName,
			final String mobileNumber, final String emailId,
			final String sourceOfPublicity, final Date preferredCallingTime,
			final String address, final String tin, final String preferredLoanProduct,
			final String status, final String isDeleted, String note) {
		
		this.id = id;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.sourceOfPublicity = sourceOfPublicity;
		this.preferredCallingTime = preferredCallingTime;
		this.address = address;
		this.tin = tin;
		this.preferredLoanProduct = preferredLoanProduct;
		this.status = status;
		this.isDeleted = isDeleted;
		this.note = note;
		
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getSourceOfPublicity() {
		return sourceOfPublicity;
	}

	public String getSourceOfPublicityInt() {
		return sourceOfPublicityInt;
	}

	public Long getPreferredPlanInt() {
		return preferredPlanInt;
	}

	public Date getPreferredCallingTime() {
		return preferredCallingTime;
	}

	public String getAddress() {
		return address;
	}

	public String getStatus() {
		return status;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public Collection<MCodeData> getSourceOfPublicityData() {
		return sourceOfPublicityData;
	}
	
	public Collection<ProspectProductData> getProductData() {
		return productData;
	}

	public String getTin() {
		return tin;
	}

	public String getNote() {
		return note;
	}

	public void setSourceOfPublicity(String sourceOfPublicity) {
		this.sourceOfPublicity = sourceOfPublicity;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSourceOfPublicityData(Collection<MCodeData> sourceOfPublicityData) {
		this.sourceOfPublicityData = sourceOfPublicityData;
	}

	public void setProductData(Collection<ProspectProductData> productData) {
		this.productData = productData;
	}

	
}
