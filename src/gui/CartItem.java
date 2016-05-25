package gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableNumberValue;

public class CartItem {
	private SimpleStringProperty itemName = new SimpleStringProperty();
	private SimpleStringProperty quantity = new SimpleStringProperty("0");
	private SimpleStringProperty price = new SimpleStringProperty("0.00");
	private SimpleStringProperty totalPrice = new SimpleStringProperty("0.00");
			
	public CartItem() {
		
	}
	
	public SimpleStringProperty intToString(ObservableNumberValue val) {
		return new SimpleStringProperty((new Integer(val.intValue()).toString()));
		
	}
	public SimpleStringProperty doubleToString(ObservableNumberValue val) {
		return new SimpleStringProperty((new Double(val.doubleValue()).toString()));
		
	}
	public ObservableNumberValue toDouble(SimpleStringProperty p) {
		if(p != null && p.get() != null)
			return new SimpleDoubleProperty(Double.parseDouble(p.get()));
		return null;
	}
	public ObservableNumberValue toInteger(SimpleStringProperty p) {
		if(p != null && p.get() != null)
			return new SimpleDoubleProperty(Integer.parseInt(p.get()));
		return null;
	}
	public String getItemName() {
		return itemName.get();
	}
	public void setItemName(String itemName) {
		this.itemName.set(itemName);
	}
	public String getQuantity() {
		return quantity.get();
	}
	public void setQuantity(String quantity) {
		this.quantity.set(quantity);
	}
	public void setQuantity(int quantity) {
		setQuantity((new Integer(quantity)).toString());
		
	}
	public String getPrice() {
		return price.get();
	}
	
	public void setPrice(String price) {
		this.price.set(price);
	}
	public void setPrice(double price) {
		setPrice(GuiUtils.formatPrice(price));
	}
	public String getTotalPrice() {
		return totalPrice.get();
		
	}
	public void setTotalPrice(String p) {
		totalPrice.set(p);
	}
	public void setTotalPrice(double price) {
		setTotalPrice(GuiUtils.formatPrice(price));
	}
	
	public StringProperty totalPrice() {
		return totalPrice;
	}
	
	public StringProperty quantity() {
		return quantity;
	}
	
	public StringProperty price() {
		return price;
	}
	
	
	public String toString() {
		return itemName + ", " + quantity + ", " + price;
	}
	
	/* not used but good technique
	quantity.addListener((observable, oldValue, newValue) -> {
    	if(newValue != null) {
    		setTotalPrice(TableUtil.stringDoublesMultiply(newValue, price.get()));
    		System.out.println(TableUtil.stringDoublesMultiply(newValue, price.get()));
    	}
    	
    	//System.out.println(observable);
    	
    });*/
	
}
