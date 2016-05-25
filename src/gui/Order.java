package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
	private List<OrderItem> orderItems;
	private String orderID;
	private LocalDate date;
	
	public Order() {
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	
	public String getTotalPrice() {
		if(orderItems == null) {
			return "0.00";
		} else {
			 DoubleSummaryStatistics summary 
			  = orderItems.stream().collect(
					  Collectors.summarizingDouble((OrderItem item) -> Double.parseDouble(
							  GuiUtils.stringDoublesMultiply(item.getUnitPrice(), item.getQuantity()))));
			 return String.format("%.2f", new Double(summary.getSum()));
		}
	}

	public String getDate() {
		return date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
}
