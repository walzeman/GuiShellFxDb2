package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OrdersWindow extends Stage {
	private TableView<Order> table = new TableView<Order>();
	Text messageBar = new Text();
	Order selectedOrder;
	Stage primaryStage;
	
	public void setData(ObservableList<Order> orders) {
		table.setItems(orders);
	}
	private HBox createTopLabel() {
		Label label = new Label("All Orders");
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	private HBox setUpButtons() {
		Button viewButton = new Button("View Details");
		Button cancelButton = new Button("Back to Welcome Screen");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(viewButton);
		btnBox.getChildren().add(cancelButton);
		cancelButton.setOnAction(evt -> {
			primaryStage.show();
			hide();
		});
	
		viewButton.setOnAction(evt -> {
			selectedOrder = table.getSelectionModel().getSelectedItem();
			if(selectedOrder == null) {
				messageBar.setText("Please select a row.");
			} else {
				messageBar.setText("");
				OrderDetailWindow ordDetails = new OrderDetailWindow(this, selectedOrder);
				ordDetails.setData(FXCollections.observableList(selectedOrder.getOrderItems()));
				hide();
				ordDetails.show();
			}
		});
		return btnBox;
	}
	@SuppressWarnings("unchecked")
	public OrdersWindow(Stage primaryStage) {
		this.primaryStage = primaryStage;
		setTitle("Orders");
		messageBar.setFill(Color.FIREBRICK);
		
		//set up top label
		HBox labelHbox = createTopLabel();
		
		//set up table
        TableColumn<Order, String> orderIDCol = 
        		TableUtil.makeTableColumn(new Order(), 
        				"Order ID", "orderID", 80);
        TableColumn<Order, String> dateCol = 
        		TableUtil.makeTableColumn(new Order(), 
        				"Date of Order", "date",100);
        TableColumn<Order, String> totalCostCol = 
        		TableUtil.makeTableColumn(new Order(), 
        				"Total Price", "totalPrice", 80);
		table.getColumns().addAll(orderIDCol, dateCol, totalCostCol);
		
		//set up row of buttons
		HBox btnBox = setUpButtons();
		
		//set up grid pane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10); 
		grid.setHgap(10);
		grid.add(labelHbox, 0, 1);
		grid.add(table, 0, 2);
		grid.add(messageBar, 0, 3);
		grid.add(btnBox,0,5);
		grid.add(new HBox(10), 0, 6);
		    
		//set in scene and stage
        Scene scene = new Scene(grid, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT);  
		setScene(scene);
	}
}
