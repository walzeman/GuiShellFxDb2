package gui;

import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class ShoppingCartWindow extends Stage {
	public final static ShoppingCartWindow INSTANCE = new ShoppingCartWindow();
	private TableView<CartItem> table = new TableView<CartItem>();
	private ProductDetailsWindow prodDetailsWindow;
	private Product selectedProduct;
	private Text messageBar = new Text();
	
	private Stage primaryStage;//used when retrieved cart needs to navigate back
	public void setPrimaryStage(Stage stage) {
		this.primaryStage = stage;
	}
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	//special location to put total for the shopping cart
	private Text total = new Text();
	private Label totalLabel = new Label("Total:");
	
	//editable column
	private TableColumn<CartItem, String> quantityCol;
	
	public ShoppingCartWindow() {
		setTitle("Shopping Cart");
		messageBar.setFill(Color.FIREBRICK);
		
		//set up top label
		HBox labelHbox = setUpTopLabel();
		
		//set up table
		setUpTable();
        
		//set up buttons
		HBox btnBox = setUpButtons();
		
		//assemble all in a GridPane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10); 
		grid.setHgap(10);
		
		grid.add(labelHbox, 0, 1);
		grid.add(table, 0, 2);
		HBox totalBox = new HBox(10);
		totalBox.setAlignment(Pos.BOTTOM_RIGHT);
		totalBox.getChildren().add(totalLabel);
		totalBox.getChildren().add(total);
		grid.add(totalBox, 0,3);
		grid.add(messageBar, 0, 5);	
		grid.add(btnBox,0,6);
		grid.add(new HBox(10), 0, 7);
		
		//set in scene
        Scene scene = new Scene(grid, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT);  
		setScene(scene);
		
		//Make sure that mouse click outside of table will also restore row selection
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	messageBar.setText("");
	            TableUtil.selectByRow(table);
	        }
	    });
	}
	
	//sets data from outside into shopping cart
	public void setData(ObservableList<CartItem> items) {
		
		ObservableList<CartItem> current = table.getItems();
		if(current != null) {
			items.addAll(current);
		}
		table.setItems(items);
		setTotalInCart(computeSumTotal());
		
		//turns on cell selection 
		TableView.TableViewSelectionModel<CartItem> selModel = TableUtil.selectByCell(table);
		
		//selects the quantity cell so that it can be edited easily
		selModel.select(0, quantityCol);
	}
	
	public ObservableList<CartItem> getCartItems() {
		return table.getItems();
	}
	//sets data for this window
	public void setShoppingCartWindowData(ProductDetailsWindow prodDetails, 
			Product selectedProd) {
		this.prodDetailsWindow = prodDetails;
		this.selectedProduct = selectedProd;
		
	}
	
	private HBox setUpTopLabel() {
		Label label = new Label(String.format("Shopping Cart"));
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	
	@SuppressWarnings("unchecked")
	private void setUpTable() {
		table.setEditable(true);
        
        //create columns
        TableColumn<CartItem, String> itemNameCol 
        	= TableUtil.makeTableColumn(new CartItem(), "Item Name", "itemName", 200);
        
        //quantity column is trickier
        quantityCol 
	      = TableUtil.makeEditableTableColumn(table, new CartItem(), "Quantity", "quantity", 80);  
        quantityCol.setOnEditCommit(t -> {
		   CartItem instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   int quant = Integer.parseInt(t.getNewValue());
		   if(quant > 3) {  //replace with a correct rule
			   quantityCol.getCellFactory().call(quantityCol).cancelEdit();
			   messageBar.setText("Quantity requested exceeds quantity available");
		   } else {
			   double price = Double.parseDouble(instance.getPrice());
			   String newTotal = GuiUtils.formatPrice(quant * price);
			
			   instance.setQuantity(t.getNewValue());
			   instance.setTotalPrice(newTotal);
			   
			   double sumTotal = computeSumTotal();
			   setTotalInCart(sumTotal);
		   }
			   
			   
			   TableUtil.refreshTable(table);
		   
	     }); 

        TableColumn<CartItem, String> unitPriceCol 
	        = TableUtil.makeTableColumn(new CartItem(), "Unit Price", "price", 80);
        TableColumn<CartItem, String> totalPriceCol 
        	= TableUtil.makeTableColumn(new CartItem(), "Total Price", "totalPrice", 80);

		table.getColumns().addAll(itemNameCol, quantityCol, unitPriceCol, totalPriceCol);
		
		//make sure row selection is enabled after any mouse click
		table.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	            TableUtil.selectByRow(table);
	        }
	    });
	}
	
	private HBox setUpButtons() {
		Button proceedButton = new Button("Proceed to Checkout");
		Button continueButton = new Button("Continue Shopping");
		Button saveButton = new Button("Save Cart");
		Button deleteButton = new Button("Delete Selected");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(proceedButton);
		btnBox.getChildren().add(continueButton);
		btnBox.getChildren().add(saveButton);
		btnBox.getChildren().add(deleteButton);
		deleteButton.setOnAction(evt -> {
			TableUtil.selectByRow(table);
		    ObservableList<CartItem> tableItems = table.getItems();
		    ObservableList<Integer> selectedIndices = table.getSelectionModel().getSelectedIndices();
			
		    if(tableItems.isEmpty()) {
		    	messageBar.setText("Nothing to delete!");
		    } else if (selectedIndices == null || selectedIndices.isEmpty()) {
		    	messageBar.setText("Please select a row.");
		    } else {
			    tableItems.removeAll(table.getSelectionModel().getSelectedItems());
				table.setItems(tableItems);
				setTotalInCart(computeSumTotal());
				messageBar.setText("");
		    }
		});
		continueButton.setOnAction(evt -> {
			CatalogListWindow window 
			  = CatalogListWindow.getInstance(primaryStage, DefaultData.CATALOG_LIST_DATA);
			messageBar.setText("");
			hide();
			TableUtil.selectByRow(table);
			window.show();
		});
		
		saveButton.setOnAction(evt -> {
			messageBar.setText("This needs to be implemented...");
			//TableUtil.selectByRow(table);
		});
	
		proceedButton.setOnAction(evt -> {
			messageBar.setText("");
			TableUtil.selectByRow(table);
			ShippingBillingWindow sbw = new ShippingBillingWindow();
			sbw.show();
			hide();
			
		});
		return btnBox;
	}
	
	
	private double computeSumTotal() {
		ObservableList<CartItem> items = table.getItems();
		DoubleSummaryStatistics summary 
		  = items.stream().collect(
		       Collectors.summarizingDouble(item -> Double.parseDouble(item.getTotalPrice())));
		return summary.getSum();
	}
	private void setTotalInCart(double val) {
		total.setText(GuiUtils.formatPrice(val));
	}
	
	//some techniques that i decided not to use but don't want to forget
	 /* uses generic utility in TableUtil 
    BiFunction<String, String, String> f = (str1, str2) ->
    	TableUtil.stringDoublesMultiply(str1, str2);
    quantityCol 
	   // = TableUtil.makeEditableTableColumn(table, new CartItem(), "Quantity", "quantity", 80);
       = TableUtil.makeEditableTableColumnSideEffect(table, new CartItem(), "Quantity", "quantity", 80,
    		f, "price", "totalPrice");
    */
    
    
    //NOT USED, but good technique: update totalPrice when a change is made to quantity
    //table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    //establish binding between quantity and totalPrice
    /*
    table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    	if(newValue != null) {
    		System.out.println("change listener");
    		newValue.totalPrice().unbind();
    		newValue.totalPrice().bind(TableUtil.multiplyStringProps(newValue.quantity(), newValue.price()));
    	}
    });*/
    /*
     * messageListTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Message>() {

    @Override
    public void changed(ObservableValue<? extends Message> ov, Message old, Message newM) {
        messageNameTextField.textProperty().unbind();
        messageNameTextField.textProperty().bindBidirectional(newM.messageName);
    }
});

/*  good technique but not useful here
		TableView.TableViewFocusModel<ShoppingCart> focusModel 
		   = new TableView.TableViewFocusModel<>(table);
		focusModel.focus(0, quantityCol);
     */
}
