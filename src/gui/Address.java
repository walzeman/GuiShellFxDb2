package gui;

import javafx.beans.property.SimpleStringProperty;

public class Address {
	private final SimpleStringProperty street = new SimpleStringProperty();
	private final SimpleStringProperty city = new SimpleStringProperty();
	private final SimpleStringProperty state = new SimpleStringProperty();
	private final SimpleStringProperty zip = new SimpleStringProperty();
	private boolean isShippingAddress = false;
	private boolean isBillingAddress = false;
	public Address(String str, String c, String state, String zip, boolean isShip, boolean isBill) {
		street.set(str);
		city.set(c);;
		this.state.set(state);
		this.zip.set(zip);
		isShippingAddress = isShip;
		isBillingAddress = isBill;
		
	}
	public String getStreet() {
		return street.get();
	}
	public String getCity() {
		return city.get();
	}
	public String getState() {
		return state.get();
	}
	public String getZip() {
		return zip.get();
	}
	public void setStreet(String s) {
		street.set(s);
	}
	public void setCity(String s) {
		city.set(s);
	}
	public void setState(String s) {
		state.set(s);
	}
	public void setZip(String s) {
		zip.set(s);
	}
	public boolean isShippingAddress() {
		return isShippingAddress;
	}
	public void setShippingAddress(boolean isShippingAddress) {
		this.isShippingAddress = isShippingAddress;
	}
	public boolean isBillingAddress() {
		return isBillingAddress;
	}
	public void setBillingAddress(boolean isBillingAddress) {
		this.isBillingAddress = isBillingAddress;
	}
}
