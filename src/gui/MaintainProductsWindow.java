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

public class MaintainProductsWindow extends Stage {
	private Stage primaryStage;
	private TableView<Product> table = new TableView<Product>();
	
	private Text messageBar = new Text();
	private ComboBox<String> catalogCombo = new ComboBox<String>();
	
	//editable column
	private TableColumn<Product, String> quantityCol;
	private TableColumn<Product, String> nameCol;
	private TableColumn<Product, String> unitPriceCol;
	private TableColumn<Product, String> mfgDateCol;
	
	public MaintainProductsWindow (Stage primaryStage) {
		this.primaryStage = primaryStage;
		setTitle("Maintain Products");
		messageBar.setFill(Color.FIREBRICK);
		
		//set up top label
		HBox labelHbox = setUpTopLabel();
		
		setUpCombo();
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
		grid.add(catalogCombo, 0, 2);
		grid.add(table, 0, 3);
		grid.add(messageBar, 0, 4);	
		grid.add(btnBox,0,5);
		grid.add(new HBox(10), 0, 6);
		
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
	
	public void setData(ObservableList<Catalog> catalogs, ObservableList<Product> items) {
		setData(items);
		ObservableList<String> comboItems 
		   = FXCollections.observableList(
				catalogs.stream()
		        .map(c -> c.getName())
		        .collect(Collectors.toList()));
		catalogCombo.setItems(comboItems);
		if(comboItems.size() > 0) catalogCombo.setValue(comboItems.get(0));
		//turns on cell selection 
		//TableView.TableViewSelectionModel<Product> selModel = TableUtil.selectByCell(table);
	}
	public void setData(ObservableList<Product> items) {		
		table.setItems(items);
	}
	
	private HBox setUpTopLabel() {
		Label label = new Label(String.format("Maintain Products"));
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	
	private void setUpCombo() {
		catalogCombo.valueProperty().addListener(new ChangeListener<String>() {
	        @Override 
	        public void changed(ObservableValue ov, String oldval, String newval) {
	        	Optional<Catalog> temp 
	        	   = DefaultData.CATALOG_LIST_DATA
	        	                .stream()
	        	                .filter(c -> c.getName().equals(newval))
	        	                .findFirst();
	        	ObservableList<Product> list 
	        		= FXCollections.observableArrayList(
	        				DefaultData.PRODUCT_LIST_DATA.get(temp.get()));	
	        	setData(list);
	        }
		});
	}
	
	public void addItem(Product item) {
		ObservableList<Product> items = table.getItems();
		items.add(item);
		setData(items);
		TableUtil.refreshTable(table);
	}
	
	@SuppressWarnings("unchecked")
	private void setUpTable() {
		table.setEditable(true);
		table.setPrefWidth(360);
        
        
        //quantity column is trickier
        quantityCol 
	      = TableUtil.makeEditableTableColumn(table, new Product(), "Quantity", "quantityAvail", 80);
        quantityCol.setOnEditCommit(t -> {
		   Product instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   int quantAvail = Integer.parseInt(t.getNewValue());
		   instance.setQuantityAvail(quantAvail);		   
		   TableUtil.refreshTable(table);
		   
	     }); 
        
        nameCol= TableUtil.makeEditableTableColumn(table, new Product(), "Name", "productName", 80);  
        nameCol.setOnEditCommit(t -> {
		   Product instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   instance.setProductName(t.getNewValue());		   
		   TableUtil.refreshTable(table);
		   
	     }); 
    	unitPriceCol= TableUtil.makeEditableTableColumn(table, new Product(), "Unit Price", "unitPrice", 80);  
    	unitPriceCol.setOnEditCommit(t -> {
		   Product instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   instance.setUnitPrice(Double.parseDouble(t.getNewValue()));		   
		   TableUtil.refreshTable(table);
		   
	     }); 
    	mfgDateCol= TableUtil.makeEditableTableColumn(table, new Product(), "Mfg Date", "mfgDate", 80);  
    	mfgDateCol.setOnEditCommit(t -> {
		   Product instance = t.getTableView().getItems().get(t.getTablePosition().getRow());
		   String date =t.getNewValue();
		   instance.setMfgDate(LocalDate.parse(date));		   
		   TableUtil.refreshTable(table);
		   
	     }); 

		table.getColumns().addAll(nameCol, unitPriceCol, mfgDateCol, quantityCol);
		
		//make sure row selection is enabled after any mouse click
		table.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	            TableUtil.selectByRow(table);
	        }
	    });
	}
	
	private HBox setUpButtons() {
		Button addButton = new Button("Add New Product");
		Button deleteButton = new Button("Delete Product");
		Button backButton = new Button("Back to Main");
		
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(addButton);
		btnBox.getChildren().add(deleteButton);
		btnBox.getChildren().add(backButton);
	
		deleteButton.setOnAction(evt -> {
			TableUtil.selectByRow(table);
		    ObservableList<Product> tableItems = table.getItems();
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
			AddProductPopup popup = new AddProductPopup(this);
			String catNameSelected = "Not found";
			for(int i = 0; i < DefaultData.CATALOG_LIST_DATA.size(); ++i) {
				if(catalogCombo.getSelectionModel().isSelected(i)) {
					catNameSelected = DefaultData.CATALOG_LIST_DATA.get(i).getName();
					break;
				}
			}		
			popup.setCatalog(catNameSelected);
			popup.show(this);
		});
		return btnBox;
	}
	
}
