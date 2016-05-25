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

public class FinalOrderWindow extends Stage {
	private TableView<CartItem> table = new TableView<CartItem>();
	private Text messageBar = new Text();
	
	//special location to put total for the shopping cart
	private Text total = new Text();
	private Label totalLabel = new Label("Total:");
	
	//editable column
	private TableColumn<CartItem, String> quantityCol;
	
	public FinalOrderWindow() {
		setTitle("Final Order Window");
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
	
	//sets data from outside into the table
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
	
	
	private HBox setUpTopLabel() {
		Label label = new Label(String.format("Final Order"));
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
		Button submitButton = new Button("Submit Order");
		Button cancelButton = new Button("Cancel");
		Button shopCartButton = new Button("Back to Shopping Cart");
		
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(submitButton);
		btnBox.getChildren().add(cancelButton);
		btnBox.getChildren().add(shopCartButton);
		
		submitButton.setOnAction(evt -> {
			OrderCompleteWindow ocw = new OrderCompleteWindow();
			ocw.show();
			hide();
			messageBar.setText("");
		});
		cancelButton.setOnAction(evt -> {
			messageBar.setText("Order Cancelled");
		});
		
		shopCartButton.setOnAction(evt -> {
			ShoppingCartWindow.INSTANCE.show();
			hide();
			messageBar.setText("");
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
}
