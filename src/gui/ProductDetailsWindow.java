package gui;

import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ProductDetailsWindow extends Stage {
	private ProductListWindow productList;
	private Product selectedProduct;
	
	public ProductDetailsWindow(ProductListWindow list, Product selectedProduct) {
		this.productList = list;
		this.selectedProduct = selectedProduct;
		setTitle("Product Details");
		BorderPane topContainer = new BorderPane();
		
		//set up top label
		HBox labelHbox = setUpTopLabel();
		
		//prepare display grid
        List<String> displayValues = Arrays.asList(selectedProduct.getProductName(),
		         selectedProduct.getUnitPrice(),
		        (new Integer(selectedProduct.getQuantityAvail())).toString(),
		        selectedProduct.getDescription());
		FourByTwoGridPane dataTable 
		   = new FourByTwoGridPane(DefaultData.DISPLAY_PRODUCT_FIELDS,
				   displayValues, "gray", GuiConstants.PROD_DETAILS_GRID_WIDTH);
						        
		//set up button row
		HBox btnBox = setUpButtons();
		
		//set up grid pane (for center area of scene)
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(20); 
		grid.setHgap(10);
		grid.add(dataTable, 0, 1);
		
		//place label, grid and buttons in the top container
		BorderPane.setMargin(labelHbox, new Insets(12,12,12,12));
		BorderPane.setMargin(btnBox, new Insets(12,12,12,12));
		topContainer.setTop(labelHbox);
		topContainer.setCenter(grid);
		topContainer.setBottom(btnBox);
		
		//set into scene and stage
        Scene scene = new Scene(topContainer, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT-80);  
		setScene(scene);
	}
	
	public HBox setUpTopLabel() {
		final Label label = new Label(String.format("%s: Product Details", selectedProduct.getProductName()));
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	
	private HBox setUpButtons() {
		Button addToCartButton = new Button("Add to Cart");
		Button backButton = new Button("Back to Product List");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(addToCartButton);
		btnBox.getChildren().add(backButton);
		backButton.setOnAction(evt -> {
			productList.show();
			hide();
		});
	

		addToCartButton.setOnAction(evt -> {
			int quant = 1;
			double unitPrice = Double.parseDouble(selectedProduct.getUnitPrice());
			double totalPrice = quant * unitPrice;
			
			CartItem cart = new CartItem();
			cart.setItemName(selectedProduct.getProductName());
			cart.setPrice(unitPrice);
			cart.setQuantity(quant);
			cart.setTotalPrice(totalPrice);
			ObservableList<CartItem> cartData =
               FXCollections.observableArrayList(cart);
			 
			ShoppingCartWindow cartWindow = ShoppingCartWindow.INSTANCE;
			cartWindow.setShoppingCartWindowData(this, selectedProduct);
			cartWindow.setData(cartData);
			cartWindow.show();
			hide();
		});
		return btnBox;
	}
	
}
