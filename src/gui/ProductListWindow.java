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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProductListWindow extends Stage {
	private TableView<Product> table = new TableView<Product>();
	Stage primaryStage;
	Catalog selectedCatalog;
	CatalogListWindow catalogList;
	Product selectedProduct;
	Text messageBar = new Text();
	
	public void setData(ObservableList<Product> prods) {
		table.setItems(prods);
	}
	private HBox createTopLabel() {
		Label label = new Label(String.format("Available %s", selectedCatalog.getName()));
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	private HBox setUpButtons() {
		Button viewButton = new Button("Select Product");
		Button backButton = new Button("Go Back to Catalogs");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(viewButton);
		btnBox.getChildren().add(backButton);
		backButton.setOnAction(evt -> {
			catalogList.show();
			hide();
		});
	
		viewButton.setOnAction(evt -> {
			selectedProduct = table.getSelectionModel().getSelectedItem();
			if(selectedProduct == null) {
				messageBar.setText("Please select a row.");
			} else {
				messageBar.setText("");
				ProductDetailsWindow prodDetails = new ProductDetailsWindow(this, selectedProduct);
				hide();
				prodDetails.show();
			}
		});
		return btnBox;
	}
	@SuppressWarnings("unchecked")
	public ProductListWindow(CatalogListWindow list, Catalog catalog) {
		this.catalogList = list;
		this.selectedCatalog = catalog;
		setTitle("Product List");
		messageBar.setFill(Color.FIREBRICK);
		
		//set up top label
		HBox labelHbox = createTopLabel();
		
		//set up table
        TableColumn<Product, String> productNameCol = 
			TableUtil.makeTableColumn(new Product(), 
				String.format(catalog.getName()), "productName", GuiConstants.GRID_PANE_WIDTH);
		table.getColumns().addAll(productNameCol);
		
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
