package gui;

import java.util.List;
import java.util.function.Consumer;

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
import javafx.stage.StageStyle;

public class SelectAddress extends Stage {
	ShippingBillingWindow shipBillWindow;
	TableView<Customer> table = new TableView<Customer>();
	String labelStr;
	List<Customer> shipCusts;
	
	public void setData(List<Customer> addrData) {
		shipCusts = addrData;
		ObservableList<Customer> shipData = FXCollections.observableList(shipCusts);
		table.setItems(shipData);
	}
	@SuppressWarnings("unchecked")
	public SelectAddress(ShippingBillingWindow w, String labelStr, Consumer<Customer> addressSetter) {
		initStyle(StageStyle.UTILITY);
		shipBillWindow = w;
		this.labelStr = labelStr;
		Text messageBar = new Text();
		messageBar.setFill(Color.FIREBRICK);
		
		Label label = new Label(String.format(labelStr));
        label.setFont(new Font("Arial", 13));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        
        table = new TableView<Customer>();
        table.setEditable(true);
        
        //create columns
        TableColumn<Customer, String> nameCol 
        	= TableUtil.makeTableColumn(new Customer(), "Name", "fullName", 100);
        
        TableColumn<Customer, String> streetCol 
    	    = TableUtil.makeEditableTableColumn(table, new Customer(), "Street", "street", 100);
        
        streetCol.setOnEditCommit(t -> {
 		   Customer instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
 		   String newStreet = t.getNewValue();
 		   instance.setStreet(newStreet);
 		   TableUtil.refreshTable(table);
 	     }); 
        
        TableColumn<Customer, String> cityCol 
	       = TableUtil.makeEditableTableColumn(table, new Customer(), "City", "city", 100);
    
        cityCol.setOnEditCommit(t -> {
		   Customer instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   String newCity = t.getNewValue();
		   instance.setCity(newCity);
		   TableUtil.refreshTable(table);
	     }); 
        
        TableColumn<Customer, String> stateCol 
	       = TableUtil.makeEditableTableColumn(table, new Customer(), "State", "state", 100);
 
        stateCol.setOnEditCommit(t -> {
		   Customer instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   String newState = t.getNewValue();
		   instance.setState(newState);
		   TableUtil.refreshTable(table);
	     }); 
        
        TableColumn<Customer, String> zipCol 
	       = TableUtil.makeEditableTableColumn(table, new Customer(), "Zipcode", "zip", 100);

        zipCol.setOnEditCommit(t -> {
		   Customer instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   String newzip = t.getNewValue();
		   instance.setZip(newzip);
		   TableUtil.refreshTable(table);
	     }); 
        table.getColumns().addAll(nameCol, streetCol, cityCol, stateCol, zipCol);	
    	
      //add buttons
  		Button selectButton = new Button("Select");
  		Button deleteButton = new Button("Delete Selected");
  		Button closeButton = new Button("Close Window");
  		
  		//assemble all in a GridPane
  		GridPane grid = new GridPane();
  		grid.setMinWidth(500);
  		grid.setAlignment(Pos.CENTER);
  		grid.setVgap(10); 
  		grid.setHgap(10);
  		grid.add(labelHbox, 0, 1);
  		grid.add(table, 0, 2);
  		grid.add(messageBar, 0, 4);
  		HBox btnBox = new HBox(10);
  		btnBox.setAlignment(Pos.CENTER);
  		btnBox.getChildren().add(selectButton);
  		btnBox.getChildren().add(deleteButton);
  		btnBox.getChildren().add(closeButton);
  		grid.add(btnBox,0,6);
  		grid.add(new HBox(10), 0, 7);
  		  
  		deleteButton.setOnAction(evt -> {
  			TableUtil.selectByRow(table);
  		    ObservableList<Customer> tableItems = table.getItems();
  		    ObservableList<Integer> selectedIndices = table.getSelectionModel().getSelectedIndices();
  			
  		    if(!tableItems.isEmpty() && selectedIndices != null && !selectedIndices.isEmpty()) {
  			    tableItems.remove(table.getSelectionModel().getSelectedItems().get(0));
  				table.setItems(tableItems);	
  		    }
  		});
  		closeButton.setOnAction(evt -> hide());
  		selectButton.setOnAction(evt -> {
			Customer c = table.getSelectionModel().getSelectedItem();
			if(c == null) {
				messageBar.setText("Please select a row");
			} else {
				addressSetter.accept(c);
				hide();
			}
  		});
  		
  		Scene scene = new Scene(grid,650, 300);
  		setScene(scene);
	}
}
