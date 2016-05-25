package gui;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class MaintainCatalogsWindow extends Stage {
	private Stage primaryStage;
	private TableView<Catalog> table = new TableView<>();
	
	private Text messageBar = new Text();
	private ComboBox<String> catalogCombo = new ComboBox<String>();
	
	//editable column
	private TableColumn<Catalog, String> idCol;
	private TableColumn<Catalog, String> nameCol;

	
	public MaintainCatalogsWindow (Stage primaryStage) {
		this.primaryStage = primaryStage;
		setTitle("Maintain Catalogs");
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
		grid.add(messageBar, 0, 3);	
		grid.add(btnBox,0,4);
		grid.add(new HBox(10), 0, 5);
		
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
	
	
	public void setData(ObservableList<Catalog> items) {		
		table.setItems(items);
	}
	
	public void addItem(Catalog item) {
		ObservableList<Catalog> items = table.getItems();
		items.add(item);
		setData(items);
		TableUtil.refreshTable(table);
	}
	
	
	private HBox setUpTopLabel() {
		Label label = new Label(String.format("Maintain Catalogs"));
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
		
	@SuppressWarnings("unchecked")
	private void setUpTable() {
		table.setEditable(true);       
        idCol 
	      = TableUtil.makeEditableTableColumn(table, new Catalog(), "ID", "id", 100);  
        idCol.setOnEditCommit(t -> {
		   Catalog instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   instance.setId(t.getNewValue());		   
		   TableUtil.refreshTable(table);		   
	     }); 
        
        nameCol= TableUtil.makeEditableTableColumn(table, new Catalog(), "Name", "name", 230);  
        nameCol.setOnEditCommit(t -> {
		   Catalog instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   instance.setName(t.getNewValue());		   
		   TableUtil.refreshTable(table);		   
	     }); 
    	
		table.getColumns().addAll(idCol, nameCol);
		
		//make sure row selection is enabled after any mouse click
		table.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	            TableUtil.selectByRow(table);
	        }
	    });
	}
	
	private HBox setUpButtons() {
		Button addButton = new Button("Add New Catalog");
		Button deleteButton = new Button("Delete Catalog");
		Button backButton = new Button("Back to Main");
		
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(addButton);
		btnBox.getChildren().add(deleteButton);
		btnBox.getChildren().add(backButton);
	
		deleteButton.setOnAction(evt -> {
			TableUtil.selectByRow(table);
		    ObservableList<Catalog> tableItems = table.getItems();
		    ObservableList<Integer> selectedIndices = table.getSelectionModel().getSelectedIndices();
			System.out.println(selectedIndices);
		    if(tableItems.isEmpty()) {
		    	messageBar.setText("Nothing to delete!");
		    } else if (selectedIndices == null || selectedIndices.isEmpty()) {
		    	messageBar.setText("Please select a row.");
		    } else {
			    tableItems.remove(table.getSelectionModel().getSelectedItems().get(0));
				table.setItems(tableItems);
				messageBar.setText("");
		    }
		});
		backButton.setOnAction(evt -> {
			primaryStage.show();
			messageBar.setText("");
			hide();
			TableUtil.selectByRow(table);
		});
		
		addButton.setOnAction(evt -> {
			AddCatalogPopup popup = new AddCatalogPopup(this);
			popup.show(this);
			
		});
	
		
		return btnBox;
	}
}
