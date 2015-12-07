package org.mifosplatform.portfolio.loanaccount.data;

public class UserData {

	private String firstName;
	private String lastName;
	private String age;
	private String sex;
	private String address;
	
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName
				+ ", age=" + age + ", sex=" + sex + ", address=" + address
				+ "]";
	}
	
	public String getFullName() {
		return firstName+" "+lastName;
	}
}
