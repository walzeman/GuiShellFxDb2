package gui;

import javafx.beans.property.SimpleStringProperty;

public class OrderItem {
	private int itemID;
	private int orderID;
	private final SimpleStringProperty productName = new SimpleStringProperty();
	private final SimpleStringProperty quantity = new SimpleStringProperty();
	private final SimpleStringProperty unitPrice = new SimpleStringProperty();
	//private final SimpleStringProperty totalPrice = new SimpleStringProperty();
	public OrderItem(String name, String quantity, String price) {
		productName.set(name);
		this.quantity.set(quantity);
		this.unitPrice.set(price);
		
	}
	OrderItem() {}
	public String getTotalPrice() {
		return (GuiUtils.multiplyStringProps(unitPrice, quantity)).get();
	}
	public String getProductName() {
		return productName.get();
	}
	public String getQuantity() {
		return quantity.get();
	}
	public String getUnitPrice() {
		return unitPrice.get();
	}
	public int getItemID() {
		return itemID;
	}
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	public int getOrderID() {
		return orderID;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}


}
