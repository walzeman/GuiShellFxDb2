package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.beans.property.*;

public class Product {
	private String productName;
    private String productId;     
	private int quantityAvail; 
    private Catalog catalog;
    private double unitPrice;
    private LocalDate mfgDate;
    private String description;
    private int quantityRequested;
    //convert all non-string types to strings if used in a table; otherwise, don't convert
    public int getQuantityRequested() {
		return quantityRequested;
	}
	public void setQuantityRequested(int quantityRequested) {
		this.quantityRequested=quantityRequested;
	}
	public Product(Catalog c, Integer pi, String pn, int qa, double up, LocalDate md, String d){
        Optional.ofNullable(pi).ifPresent(p -> productId=pi.toString());
        this.productName=pn;
        quantityAvail=qa;
        unitPrice=up;
        mfgDate=md;
        catalog = c;
        Optional.ofNullable(d).ifPresent(x -> description=x);
    }
    //this constructor is used when getting user-entered data in adding a new product
    public Product(Catalog c, String name, LocalDate date, int numAvail, double price){
    	this(c, null, name, numAvail, price, date, null);
    }
    
    //for ProductListWindow
    Product() {}
    /**
     * @return Returns the catalogId.
     */
    public Catalog getCatalog() {
        return catalog;
    }
    /**
     * return as String
     */
    public String getMfgDate() {    	
    	return mfgDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
    /**
     * @return Returns the productId.
     */
    public String getProductId() {
        return productId;
    }
    /**
     * @return Returns the productName.
     */
    public String getProductName() {
        return productName;
    }
    /**
     * @return Returns the quantityAvail.
     */
    public String getQuantityAvail() {
        return (new Integer(quantityAvail)).toString();
    }
    /**
     * @return Returns the unitPrice.
     */
    public String getUnitPrice() {
        return String.format("%.2f", unitPrice);
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
	public void setDescription(String description) {
		this.description = description;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setQuantityAvail(int quantityAvail) {
		this.quantityAvail = quantityAvail;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public void setMfgDate(LocalDate mfgDate) {
		this.mfgDate = mfgDate;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
}
