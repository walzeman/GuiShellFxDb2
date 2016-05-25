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

public class CatalogListWindow extends Stage {
	private static final boolean USE_DEFAULT_DATA = false;
	
	//These lines of code make CatalogListWindow a singleton -- singleton implementation
	//is not thread-safe, but doesn't need to be.
	private CatalogListWindow(Stage pStage, ObservableList<Catalog> list) {
		table = new TableView<Catalog>();
		setStage(pStage);
		setData(list);
		
	}
	private static CatalogListWindow instance = null;
	public static CatalogListWindow getInstance(Stage primary, ObservableList<Catalog> list) {
		if(instance == null) {
			instance = new CatalogListWindow(primary, list);
		}
		return instance;
	}
	//useful if window is known to be populated
	public static CatalogListWindow getInstance() {
		if(instance == null) {
			throw new RuntimeException("This window has not been populated.");
		}
		return instance;
	}
	
	private TableView<Catalog> table;
	private Stage primaryStage;
	private Catalog selected;
	private Text messageBar = new Text();
	
	/**
	 * Data is set into table using this method.
	 */
	public void setData(ObservableList<Catalog> cats) {	
		table.setItems(cats);	
	}
	
	
	private CatalogListWindow() {
		//empty because of the technique used for making it a singleton
		//the only necessary parameter is passed in by setStage
	}
	@SuppressWarnings("unchecked")
	public void setStage(Stage ps) {
		primaryStage = ps;
		messageBar.setFill(Color.FIREBRICK);
		setTitle("Catalog List");
		
		//create label
		HBox browseCatalogsLabel = createTopLabel();
		
		//set up table 
		TableColumn<Catalog, String> catalogNameCol = 
			TableUtil.makeTableColumn(
				new Catalog(), "Catalog", "name", GuiConstants.GRID_PANE_WIDTH);
		table.getColumns().addAll(catalogNameCol);
		
		//set up button box at bottom
		HBox btnBox = setUpButtons();
		
		//set up main grid and add label and table
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10); 
		grid.setHgap(10);
		grid.add(browseCatalogsLabel, 0, 1);
		grid.add(table, 0, 2);
		grid.add(messageBar, 0, 3);
		grid.add(btnBox,0,5);
		grid.add(new HBox(10), 0, 6);
		
        //set in scene and this stage
        Scene scene = new Scene(grid, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT);  
		setScene(scene);
	}
	private HBox createTopLabel() {
		final Label label = new Label("Browse Catalogs");
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	private HBox setUpButtons() {
		Button viewButton = new Button("View Catalog");
		Button cartButton = new Button("View Shopping Cart");
		Button backButton = new Button("Back to Start");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(viewButton);
		btnBox.getChildren().add(cartButton);
		btnBox.getChildren().add(backButton);
		backButton.setOnAction(evt -> {
			primaryStage.show();
			hide();
		});
		
		cartButton.setOnAction(evt -> {
			ShoppingCartWindow.INSTANCE.show();
			hide();
		});
	       
		viewButton.setOnAction(evt -> {
			selected = table.getSelectionModel().getSelectedItem();
			if(selected == null) {
				messageBar.setText("Please select a row.");
				
			} else {
				messageBar.setText("");
				ProductListWindow prodList = new ProductListWindow(this, selected);
				List<Product> prods = readDataFromSource();
				prodList.setData(FXCollections.observableList(prods));
				hide();
				prodList.show();	
			}
		});
        return btnBox;
	}
	
	private List<Product> readDataFromSource() {
		if(USE_DEFAULT_DATA) {
			return DefaultData.PRODUCT_LIST_DATA.get(selected);
		} else {
			List<Product> list = new ArrayList<Product>();
			Connection con = null;
        	PreparedStatement stmt = null;
    		try {
    			con = ConnectManager.getConnection(ConnectManager.DB.PROD);
    			String query = "SELECT * FROM Product WHERE catalogid = ?";
    			stmt = con.prepareStatement(query);
    			stmt.setString(1, selected.getId());
    			ResultSet rs = stmt.executeQuery();
    			while(rs.next()){
    				int productId = rs.getInt("productid");
    				String name = rs.getString("productname");
    				int quantAvail = rs.getInt("totalquantity");
    				double unitPrice = rs.getDouble("priceperunit");
    				LocalDate date = GuiUtils.localDateForString(rs.getString("mfgdate"));
    				String description = rs.getString("description"); 
    				
    				System.out.println("id: "+ productId + " name: "+ name);
    				list.add(new Product(selected, productId, name, quantAvail,unitPrice, date, description));
    			}
    			stmt.close();
    			con.close();
    		}
    		catch(SQLException s){
    			s.printStackTrace();
    		}
    		return list;
    		     	
        }

	}
	
                		
}
