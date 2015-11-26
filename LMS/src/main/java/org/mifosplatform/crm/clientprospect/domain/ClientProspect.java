package org.mifosplatform.crm.clientprospect.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.domain.AbstractAuditableCustom;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.google.gson.JsonElement;

@Entity
@Table(name = "m_prospect")
public class ClientProspect extends AbstractAuditableCustom<AppUser, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "first_name", length = 50)
	private String firstName;

	@Column(name = "middle_name", length = 50)
	private String middleName;

	@Column(name = "last_name", length = 50)
	private String lastName;

	@Column(name = "mobile_no", length = 20)
	private String mobileNumber;

	@Column(name = "email_id", length = 50)
	private String emailId;

	@Column(name = "source_of_publicity", length = 50)
	private String sourceOfPublicity;

	@Column(name = "preferred_loan_product")
	private String preferredLoanProduct;

	@Column(name = "preferred_calling_time")
	private Date preferredCallingTime;

	@Column(name = "note", length = 100)
	private String note;

	@Column(name = "address", length = 100)
	private String address;

	@Column(name = "status", length = 100)
	private String status = "New";

	@Column(name = "tin", length = 50)
	private String tin;

	@Column(name = "is_deleted")
	private char isDeleted = 'N';
	
	@Column(name = "location", length = 50)
	private String location;

	public ClientProspect() {

	}

	public ClientProspect(final String firstName,
			final String middleName, final String lastName,
			final String mobileNumber, final String emailId,
			final String sourceOfPublicity, final String preferredLoanProduct,
			final Date preferredCallingTime, final String note, final String address, 
			final String tin) {
		
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.sourceOfPublicity = sourceOfPublicity;
		this.preferredLoanProduct = preferredLoanProduct;
		this.preferredCallingTime = preferredCallingTime;
		this.note = note;
		this.address = address;
		this.tin = tin;
	}

	/*public ClientProspect(final Short prospectType, final String firstName,
			final String middleName, final String lastName,
			final String homePhoneNumber, final String workPhoneNumber,
			final String mobileNumber, final String emailId,
			final String sourceOfPublicity, final Date preferredCallingTime,
			final String note, final String address, final String streetArea,
			final String cityDistrict, final String state,
			final String country, final String preferredPlan,
			final String status, final String statusRemark, final String zipCode) {
		this.prospectType = prospectType;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.homePhoneNumber = homePhoneNumber;
		this.workPhoneNumber = workPhoneNumber;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.sourceOfPublicity = sourceOfPublicity;
		this.preferredPlan = preferredPlan;
		this.preferredCallingTime = preferredCallingTime;
		this.note = note;
		this.address = address;
		this.streetArea = streetArea;
		this.cityDistrict = cityDistrict;
		this.state = state;
		this.country = country;
		this.status = status;
		this.statusRemark = statusRemark;
		this.zipCode = zipCode;
	}*/

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getSourceOfPublicity() {
		return sourceOfPublicity;
	}

	public void setSourceOfPublicity(String sourceOfPublicity) {
		this.sourceOfPublicity = sourceOfPublicity;
	}

	public Date getPreferredCallingTime() {
		return preferredCallingTime;
	}

	public void setPreferredCallingTime(Date preferredCallingTime) {
		this.preferredCallingTime = preferredCallingTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public static ClientProspect fromJson(final FromJsonHelper fromJsonHelper,
			final JsonCommand command) throws ParseException {

		final JsonElement element = fromJsonHelper.parse(command.json());

		ClientProspect clientProspect = new ClientProspect();

		if (fromJsonHelper.parameterExists("firstName", element)) {
			String firstName = command.stringValueOfParameterNamed("firstName");
			clientProspect.setFirstName(firstName);
		}
		
		if (fromJsonHelper.parameterExists("middleName", element)) {
			String middleName = command.stringValueOfParameterNamed("middleName");
			clientProspect.setMiddleName(middleName);
		}

		if (fromJsonHelper.parameterExists("lastName", element)) {
			String lastName = command.stringValueOfParameterNamed("lastName");
			clientProspect.setLastName(lastName);
		}

		if (fromJsonHelper.parameterExists("location", element)) {
			String location = command.stringValueOfParameterNamed("location");
			clientProspect.setLocation(location);
		}
		
		if (fromJsonHelper.parameterExists("mobileNumber", element)) {
			String mobileNumber = command.stringValueOfParameterNamed("mobileNumber");
			clientProspect.setMobileNumber(mobileNumber);
		}

		if (fromJsonHelper.parameterExists("emailId", element)) {
			String emailId = command.stringValueOfParameterNamed("emailId");
			clientProspect.setEmailId(emailId);
		}

		if (fromJsonHelper.parameterExists("sourceOfPublicity", element)
				|| fromJsonHelper.parameterExists("sourceOther", element)) {
			String sourceOfPublicity = command.stringValueOfParameterNamed("sourceOfPublicity");

			if (sourceOfPublicity.equalsIgnoreCase("Other")) {
				String sourceOther = command.stringValueOfParameterNamed("sourceOther");
				clientProspect.setSourceOfPublicity(sourceOther);
			} else {
				clientProspect.setSourceOfPublicity(sourceOfPublicity);
			}

		}

		if (fromJsonHelper.parameterExists("preferredCallingTime", element)) {
			String startDateString = command.stringValueOfParameterNamed("preferredCallingTime");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date preferredCallingTime = df.parse(startDateString);
			clientProspect.setPreferredCallingTime(preferredCallingTime);
		}

		if (fromJsonHelper.parameterExists("note", element)) {
			String note = command.stringValueOfParameterNamed("note");
			clientProspect.setNote(note);
		}

		if (fromJsonHelper.parameterExists("address", element)) {
			String address = command.stringValueOfParameterNamed("address");
			clientProspect.setAddress(address);
		}

		if (fromJsonHelper.parameterExists("preferredLoanProduct", element)) {
			String preferredLoanProduct = command.stringValueOfParameterNamed("preferredLoanProduct");
			clientProspect.setPreferredLoanProduct(preferredLoanProduct);
		}

		if (fromJsonHelper.parameterExists("status", element)) {
			String status = command.stringValueOfParameterNamed("status");
			clientProspect.setStatus(status);
		}
		
		if (fromJsonHelper.parameterExists("tin", element)) {
			String tin = command.stringValueOfParameterNamed("tin");
			clientProspect.setStatus(tin);
		}

		return clientProspect;
	}

	public String getPreferredLoanProduct() {
		return preferredLoanProduct;
	}

	public void setPreferredLoanProduct(String preferredLoanProduct) {
		this.preferredLoanProduct = preferredLoanProduct;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public Map<String, Object> update(final JsonCommand command) {
		Map<String, Object> actualChanges = new LinkedHashMap<String, Object>(1);

		// prospectType","firstName","middleName","lastName","homePhoneNumber","workPhoneNumber","mobileNumber","emailId",
		// "sourceOfPublicity","preferredCallingTime","note","address","streetArea","cityDistrict","state
		// ","country","locale","preferredPlan","status","statusRemark","callStatus","assignedTo","notes","isDeleted","zipCode"
		
		
		final String firstName = "firstName";
		final String middleName = "middleName";
		final String lastName = "lastName";
		final String mobileNumber = "mobileNumber";
		final String emailId = "emailId";
		final String address = "address";
		final String sourceOfPublicity = "sourceOfPublicity";
		final String preferredCallingTime = "preferredCallingTime";
		final String preferredLoanProduct = "preferredLoanProduct";
		final String note = "note";
		final String tin = "tin";

		
		if (command.isChangeInStringParameterNamed(firstName, this.firstName)) {
			final String newValue = command.stringValueOfParameterNamed("firstName");
			actualChanges.put(firstName, newValue);
			this.firstName = newValue;
		}
		
		if (command.isChangeInStringParameterNamed(middleName, this.middleName)) {
			final String newValue = command.stringValueOfParameterNamed("middleName");
			actualChanges.put(middleName, newValue);
			this.middleName = newValue;
		}
		
		if (command.isChangeInStringParameterNamed(lastName, this.lastName)) {
			final String newValue = command.stringValueOfParameterNamed("lastName");
			actualChanges.put(lastName, newValue);
			this.lastName = newValue;
		}
		
		if (command.isChangeInStringParameterNamed(mobileNumber, this.mobileNumber)) {
			final String newValue = command.stringValueOfParameterNamed("mobileNumber");
			actualChanges.put(mobileNumber, newValue);
			this.mobileNumber = newValue;
		}
		
		if (command.isChangeInStringParameterNamed(emailId, this.emailId)) {
			final String newValue = command.stringValueOfParameterNamed("emailId");
			actualChanges.put(emailId, newValue);
			this.emailId = newValue;
		}
		
		if (command.isChangeInStringParameterNamed(address, this.address)) {
			final String newValue = command.stringValueOfParameterNamed("address");
			actualChanges.put(address, newValue);
			this.address = newValue;
		}
		
		/*
		 * if (command.isChangeInStringParameterNamed(sourceOfPublicity,
		 * this.sourceOfPublicity)) { final String newValue =
		 * command.stringValueOfParameterNamed("sourceOfPublicity");
		 * actualChanges.put(sourceOfPublicity, newValue);
		 * this.sourceOfPublicity = newValue; }
		 */

		if (command.isChangeInStringParameterNamed(sourceOfPublicity, this.sourceOfPublicity)) {
			final String newValue = command.stringValueOfParameterNamed("sourceOfPublicity");
			if (newValue.equalsIgnoreCase("Other")) {
				final String otherSource = command.stringValueOfParameterNamed("sourceOther");
				actualChanges.put(sourceOfPublicity, otherSource);
				this.sourceOfPublicity = otherSource;
			} else {
				actualChanges.put(sourceOfPublicity, newValue);
				this.sourceOfPublicity = newValue;
			}
		}

		if (command.isChangeInStringParameterNamed(preferredLoanProduct, this.preferredLoanProduct)) {
			final String newValue = command.stringValueOfParameterNamed("preferredLoanProduct");
			actualChanges.put(preferredLoanProduct, newValue);
			this.preferredLoanProduct = newValue;
		}
		
		if (command.isChangeInStringParameterNamed(preferredCallingTime,
				this.preferredCallingTime.toString())) {

			final String startDateString = command.stringValueOfParameterNamed("preferredCallingTime");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date newValue;
			try {
				newValue = df.parse(startDateString);
			} catch (ParseException pe) {
				throw new PlatformDataIntegrityException(
						"invalid.date.and.time.format",
						"invalid.date.and.time.format", "preferredCallingTime");
			}
			actualChanges.put(preferredCallingTime, newValue);
			this.preferredCallingTime = newValue;
		}
		
		if (command.isChangeInStringParameterNamed(note, this.note)) {
			final String newValue = command.stringValueOfParameterNamed("note");
			actualChanges.put(note, newValue);
			this.note = newValue;
		}
		if (command.isChangeInStringParameterNamed(note, this.tin)) {
			final String newValue = command.stringValueOfParameterNamed("tin");
			actualChanges.put(tin, newValue);
			this.tin = newValue;
		}

		return actualChanges;
	}

	public char getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(char isDeleted) {
		this.isDeleted = isDeleted;
	}

}
