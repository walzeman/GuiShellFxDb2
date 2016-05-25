package gui;

import javafx.beans.property.SimpleStringProperty;

public class Customer {
	private final SimpleStringProperty firstName = new SimpleStringProperty();
	private final SimpleStringProperty lastName = new SimpleStringProperty();
	private Address address;
	public Customer(String fName, String lName, Address addr) {
		firstName.set(fName);
		lastName.set(lName);
		address = addr;
	}
	public Customer(String fName, String lName) {
		this(fName, lName, null);
	}
	Customer() {
		this(null,null,null);
	}
	public String getFirstName() {
		return firstName.get();
	}
	public String getLastName() {
		return lastName.get();
	}
	public String getFullName() {
		return firstName.get() + " " + lastName.get();
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address a) {
		address = a;
	}
	public String getStreet() {
		return address.getStreet();
	}
	public String getCity() {
		return address.getCity();
	}
	public String getState() {
		return address.getState();
	}
	public String getZip() {
		return address.getZip();
	}
	public void setStreet(String s) {
		address.setStreet(s);
	}
	public void setCity(String s) {
		address.setCity(s);
	}
	public void setState(String s) {
		address.setState(s);
	}
	public void setZip(String s) {
		address.setZip(s);
	}
	
}
