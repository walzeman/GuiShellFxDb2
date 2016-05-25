package gui;

import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PaymentWindow extends Stage {
	ShippingBillingWindow shipBillWindow;
	private TextField nameOnCardField = new TextField();
	private TextField cardNumberField  = new TextField();
	private ComboBox<String> cardTypeField = new ComboBox<String>();
	private TextField expirationField  = new TextField();
	Text messageBar = new Text();

	public PaymentWindow(ShippingBillingWindow window, String billingName) {
		this.nameOnCardField.setText(billingName);
		this.shipBillWindow = window;
		setTitle("Payment");
		messageBar.setFill(Color.FIREBRICK);
		
		BorderPane topContainer = new BorderPane();
		
		//set up top label
		HBox labelHbox = setUpTopLabel();
		
		//set up credit card combo box
		setCreditCardTypes();
		
        //prepare grid
        List<? extends Node> displayValues 
           = Arrays.asList(nameOnCardField, cardNumberField, cardTypeField, expirationField);       
		FourByTwoGridPane dataTable 
		   = new FourByTwoGridPane(DefaultData.DISPLAY_CREDIT_CARD_FIELDS,
				   displayValues, "gray", GuiConstants.PROD_DETAILS_GRID_WIDTH);
			
		//set up buttons
		HBox btnBox = setUpButtons();
		
		//set up central grid 
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(20); 
		grid.setHgap(10);
		grid.add(dataTable, 0, 1);
		grid.add(messageBar, 0, 3);
		
		//set up outer BorderPane
		BorderPane.setMargin(labelHbox, new Insets(12,12,12,12));
		BorderPane.setMargin(btnBox, new Insets(12,12,12,12));
		topContainer.setTop(labelHbox);
		topContainer.setCenter(grid);
		topContainer.setBottom(btnBox);
		
		//set scene and stage
        Scene scene = new Scene(topContainer, GuiConstants.SCENE_WIDTH, GuiConstants.SCENE_HEIGHT-100);  
		setScene(scene);
	}
	private HBox setUpTopLabel() {
		Label label = new Label(String.format("Credit Card Information"));
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);	
        return labelHbox;
	}
	private void setCreditCardTypes() {
		ObservableList<String> comboVals = FXCollections.observableList(DefaultData.CREDIT_CARD_TYPES);
        cardTypeField.setItems(comboVals);
        cardTypeField.setValue(comboVals.get(0));
	}
	private HBox setUpButtons() {
		Button proceedButton = new Button("Checkout");
		Button backButton = new Button("Previous Screen");
		Button backToCartButton = new Button("Back to Shopping Cart");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(proceedButton);
		btnBox.getChildren().add(backToCartButton);
		btnBox.getChildren().add(backButton);
		backButton.setOnAction(evt -> {
			shipBillWindow.show();
			hide();
		});
		
		backToCartButton.setOnAction(evt -> {
			ShoppingCartWindow.INSTANCE.show();
			hide();
		});
	

		proceedButton.setOnAction(evt -> {
			TermsWindow termsWindow = new TermsWindow(this);
			termsWindow.show();
			hide();
		});
		
		return btnBox;
	}
}
