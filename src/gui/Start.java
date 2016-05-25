package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Start extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	private static final boolean USE_DEFAULT_DATA = false;
	private static Stage primaryStage;
	
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("E-Bazaar Welcome Page");
		VBox topContainer = new VBox();		
		//create components
		HBox embeddedText = createLabelBox();
		MenuBar menuBar = createMenuBar();
		//add components to container
		topContainer.getChildren().add(menuBar);
		topContainer.getChildren().add(embeddedText);

		//place into scene and into stage
		primaryStage.setScene(new Scene(topContainer, 500, 200));
		primaryStage.show();
	}
	
	private HBox createLabelBox() {
		Text label = new Text("E-Bazaar");
		label.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 60));
		label.setFill(Color.DARKRED);
		HBox labelBox = new HBox(10);
		labelBox.setAlignment(Pos.CENTER);
		labelBox.getChildren().add(label);
		return labelBox;
		
	}
	
	private MenuBar createMenuBar() {
		MenuBar retval = new MenuBar();
		
		//create menus to put into menu bar
		Menu custMenu = new Menu("Customer");
		custMenu.getItems().addAll(onlinePurchase(), retrieveCart(), reviewOrders(), exitApp());
		Menu adminMenu = new Menu("Administrator");
		adminMenu.getItems().addAll(maintainCatalogs(), maintainProducts());
		
		//add menus to menubar
		retval.getMenus().addAll(custMenu, adminMenu);
		return retval;
	
	}
	private MenuItem maintainCatalogs() {
		MenuItem retval = new MenuItem("Maintain Catalogs");
		retval.setOnAction(evt -> {	
			MaintainCatalogsWindow maintain = new MaintainCatalogsWindow(primaryStage);
			ObservableList<Catalog> list = FXCollections.observableList(
					DefaultData.CATALOG_LIST_DATA);
			maintain.setData(list);
			maintain.show();  
	        primaryStage.hide();
					
		});
		return retval;
	}
	private MenuItem maintainProducts() {
		MenuItem retval = new MenuItem("Maintain Products");
		retval.setOnAction(evt -> {	
			MaintainProductsWindow maintain = new MaintainProductsWindow(primaryStage);
			ObservableList<Product> list = FXCollections.observableList(
					DefaultData.PRODUCT_LIST_DATA.get(DefaultData.BOOKS_CATALOG));
			maintain.setData(DefaultData.CATALOG_LIST_DATA, list);
			maintain.show();  
	        primaryStage.hide();
					
		});
		return retval;
	}
	private MenuItem onlinePurchase() {
		MenuItem retval = new MenuItem("Online Purchase");
		retval.setOnAction(evt -> {	
			CatalogListWindow catalogs = CatalogListWindow.getInstance(primaryStage, readDataFromSource());
			//catalogs.setStage(primaryStage);
	        //catalogs.setData(DefaultData.CATALOG_LIST_DATA);
	        catalogs.show();  
	        primaryStage.hide();
					
		});
		return retval;
	}
	private ObservableList<Catalog> readDataFromSource() {
		if(USE_DEFAULT_DATA) {
			return DefaultData.CATALOG_LIST_DATA;
		} else {
			System.out.println("using db");
			System.out.println(DefaultData.PRODUCT_LIST_DATA);
			
        	Connection con = null;
        	Statement stmt = null;
        	List<Catalog> list = new ArrayList<Catalog>();
        	
    		try {
    			con = ConnectManager.getConnection(ConnectManager.DB.PROD);
    			stmt = con.createStatement();
    			ResultSet rs = stmt.executeQuery("SELECT * FROM CatalogType");
    			
    			while(rs.next()){
    				String id = rs.getString("catalogid");
    				String name = rs.getString("catalogname");
    				System.out.println("id: "+ id + " name: "+name);
    				list.add(new Catalog(id, name));
    			}
    			stmt.close();
    			con.close();
    		}
    		catch(SQLException s){
    			s.printStackTrace();
    		}
    		return FXCollections.observableList(list);
		}
	}
	private MenuItem retrieveCart() {
		MenuItem retval = new MenuItem("Retrieve Saved Cart");
		retval.setOnAction(evt -> {	
			ShoppingCartWindow cartWindow = ShoppingCartWindow.INSTANCE;
			cartWindow.setData(FXCollections.observableList(DefaultData.savedCartItems));
			cartWindow.setPrimaryStage(primaryStage);
			cartWindow.show(); 
	        primaryStage.hide();					
		});
		return retval;
	}
	
	private MenuItem reviewOrders() {
		MenuItem retval = new MenuItem("Review Orders");
		retval.setOnAction(evt -> {	
			OrdersWindow ordWin = new OrdersWindow(primaryStage);
			ordWin.setData(FXCollections.observableList(readOrdersFromSource()));
	        ordWin.show();
	        primaryStage.hide();
					
		});
		return retval;
	}
	private List<Order> readOrdersFromSource() {
		if(USE_DEFAULT_DATA) {
			return DefaultData.ALL_ORDERS;
		} else {
			//implement
			//Tips:
			//read orderid and orderdate from Ord table in AccountsDB
			//To create an Order object, you will need to find the OrderItems associated
			//with each orderid that is found
			//From OrderItem table, read orderid, productid, quantity, totalprice
			//and compute unitPrice.
			//To create an OrderItem object, you will need to look up productName using
			//productid; for this, read Product table in ProductsDB. 
			//Insert all OrderItems found for an orderid into the Order object.
			//Collect all Order objects into a list, and return the list
			

			List<Order> orderList = new ArrayList<Order>();
			List<OrderItem> orderItemList = new ArrayList<OrderItem>();
			
			Connection con = null;
        	PreparedStatement stmt = null;
    		try {
    			LocalDate date;
    			int orderid;
    			con = ConnectManager.getConnection(ConnectManager.DB.ACCT);
   
    			String query1 = "SELECT orderid, orderdate FROM Ord";
    			stmt = con.prepareStatement(query1);
    			ResultSet rs = stmt.executeQuery();
    			while(rs.next()){
    				 orderid = rs.getInt("orderid");
    			     date = GuiUtils.localDateForString(rs.getString("orderdate"));
    			     System.out.println("orderid = "+orderid+" and date = "+date);
    			     
    			    String query2 = "SELECT orderid, productid, quantity, totalprice FROM OrderItem WHERE orderid="+orderid;
    			    stmt = con.prepareStatement(query2);
    			    ResultSet rs2 = stmt.executeQuery();
    			    while(rs2.next()){
    			    	int id = rs2.getInt("orderid");
    			    	int productid = rs2.getInt("productid");
    			    	int quantity = rs2.getInt("quantity");
    			    	
    			    	double totalprice = rs2.getDouble("totalprice");
    			    	
    			    	double unitprice = totalprice/quantity;
    			    	
    			    	con = ConnectManager.getConnection(ConnectManager.DB.PROD);
    			    	String query3 = "SELECT productName FROM Product WHERE productid ="+ productid;
    			    	stmt = con.prepareStatement(query3);
    			    
    			    	 ResultSet rs3 = stmt.executeQuery();
    			    	 String productNAME="";
    			    	 while(rs3.next()){
    			    		  productNAME = rs3.getString("productname");
    			    	 }
    			    	
    			    	
    			    	orderItemList.add(new OrderItem(productNAME, Integer.toString(quantity), Double.toString(unitprice)));
    			    	stmt.close();
    	    			con.close();
    			    	
    			    }
    			     
    			     Order order = new Order();
    			     order.setOrderID(Integer.toString(orderid));
    			     order.setDate(date);
    	     	     order.setOrderItems(orderItemList);
    			     orderList.add(order);
    			}		
    			
    			stmt.close();
    			con.close();
    		}
    		catch(SQLException s){
    			s.printStackTrace();
    		}
    		return orderList;
			
			
		}
	}
	private MenuItem exitApp() {
		MenuItem retval = new MenuItem("Exit");
		retval.setOnAction(evt -> Platform.exit());
		return retval;
	}
	
}
