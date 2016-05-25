package gui;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class OrderDetailWindow extends Stage {
	private TableView<OrderItem> table = new TableView<OrderItem>();
	Order selectedOrder;
	OrdersWindow orders;
	
	public void setData(ObservableList<OrderItem> orderItems) {
		table.setItems(orderItems);
	}
	@SuppressWarnings("unchecked")
	public OrderDetailWindow(OrdersWindow orders, Order selectedOrder) {
		this.selectedOrder = selectedOrder;
		this.orders = orders;
		setTitle("Order Details");
		
		//set up top label
		HBox labelHbox = createTopLabel();
		
		//set up table
        TableColumn<OrderItem, String> productNameCol = 
			TableUtil.makeTableColumn(new OrderItem(), "Product Name",
				 "productName", 100);
        TableColumn<OrderItem, String> quantityCol = 
    		TableUtil.makeTableColumn(new OrderItem(), "Quantity",
    			"quantity", 100);
        TableColumn<OrderItem, String> unitPriceCol = 
        		TableUtil.makeTableColumn(new OrderItem(), "Unit Price",
        			"unitPrice", 100);
        TableColumn<OrderItem, String> totalPriceCol = 
        		TableUtil.makeTableColumn(new OrderItem(), "Total Price",
        			"totalPrice", 100);
		table.getColumns().addAll(productNameCol, quantityCol, unitPriceCol, totalPriceCol);
		
		//set up row of buttons
		HBox btnBox = setUpButtons();
		
		//set up grid pane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10); 
		grid.setHgap(10);
		grid.add(labelHbox, 0, 1);
		grid.add(table, 0, 2);
		grid.add(btnBox,0,4);
		grid.add(new HBox(10), 0, 5);
		grid.setMinWidth(400);
		    
		//set in scene and stage
        Scene scene = new Scene(grid, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT);  
		setScene(scene);
	}
	private HBox createTopLabel() {
		Label label = new Label(String.format("Order Details"));
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	private HBox setUpButtons() {
		Button okButton = new Button("OK");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(okButton);
		okButton.setOnAction(evt -> {
			orders.show();
			hide();
		});
		return btnBox;
	}
}
	
