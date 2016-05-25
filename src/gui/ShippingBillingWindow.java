package gui;

import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ShippingBillingWindow extends Stage {
	private TextField shipNameField = new TextField();
	private TextField shipStreetField = new TextField();
	
	private TextField shipCityField = new TextField();
	private TextField shipStateField = new TextField();
	private TextField shipZipField = new TextField();
	
	private TextField billNameField = new TextField();
	private TextField billStreetField = new TextField();
	
	private TextField billCityField = new TextField();
	private TextField billStateField = new TextField();
	private TextField billZipField = new TextField();	
	private TextField[] shipFields = {shipNameField, shipStreetField, shipCityField, shipStateField, shipZipField};
    private TextField[] billFields = {billNameField, billStreetField, billCityField, billStateField, billZipField};
	
	Text messageBar = new Text();
	private SelectAddress shipAddressWindow;
	private SelectAddress billAddressWindow;
	
	public ShippingBillingWindow() {
		setTitle("Shipping and Billing Addresses");
		BorderPane topContainer = new BorderPane();
		messageBar.setFill(Color.FIREBRICK);
		
		//set up top label
		HBox labelHbox = setUpTopLabel(); 
         
        //set up shipping and billing gridpanes
        FiveByTwoGridPane shipPane = new FiveByTwoGridPane(
        	DefaultData.DISPLAY_ADDRESS_FIELDS,
        	shipFields,
        	"gray", 80);
        FiveByTwoGridPane billPane = new FiveByTwoGridPane(
            	DefaultData.DISPLAY_ADDRESS_FIELDS,
            	billFields,
            	"gray", 80);
        
        //combine each grid with its descriptive label
        VBox shipWithLabel = new VBox(10);
        shipWithLabel.getChildren().add(createShipLabel());
        shipWithLabel.getChildren().add(shipPane);
        
        VBox billWithLabel = new VBox(10);
        billWithLabel.getChildren().add(createBillLabel());
        billWithLabel.getChildren().add(billPane);
              	
        HBox shipBillBox = new HBox(10);
        shipBillBox.setAlignment(Pos.CENTER);
        shipBillBox.getChildren().add(shipWithLabel);
        shipBillBox.getChildren().add(billWithLabel);
    	  
        //set up grid pane occupying center area
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(20); 
		grid.setHgap(10);
		grid.add(shipBillBox, 0, 1);
		grid.add(setUpCheckBoxes(), 0, 2);
		grid.add(messageBar, 0, 3);
		
		//button setup
		HBox btnBox = setUpButtons();
		
		//add components to outer BorderPane
        topContainer.setTop(labelHbox);
        topContainer.setCenter(grid);
        topContainer.setBottom(btnBox);
        BorderPane.setMargin(grid, new Insets(0,12,0,12));
        BorderPane.setMargin(labelHbox, new Insets(12,12,12,12));
        BorderPane.setMargin(btnBox, new Insets(12,12,12,12));
        
        //create select address popups to support button actions
        createPopups();
        loadDefaultData();
        
        //set scene and stage
        Scene scene = new Scene(topContainer, GuiConstants.SCENE_WIDTH+20, GuiConstants.SCENE_HEIGHT);  
		setScene(scene);
	}
	
	private HBox setUpTopLabel() {
		Label label = new Label("Shipping and Billing Addresses");
        label.setFont(new Font("Arial", 16));
        HBox labelHbox = new HBox(10);
        labelHbox.setAlignment(Pos.CENTER);
        labelHbox.getChildren().add(label);
        return labelHbox;
	}
	//label for shipping grid
	private HBox createShipLabel() {
   	 Label shipLabel = new Label("Shipping Address");
        HBox shipBox = new HBox(10);
        shipBox.setAlignment(Pos.CENTER);
        shipBox.getChildren().add(shipLabel);
        return shipBox;
            
   }
	//label for billing grid
   private HBox createBillLabel() {
   	Label billLabel = new Label("Billing Address");
       HBox billBox = new HBox(10);
       billBox.setAlignment(Pos.CENTER);
       billBox.getChildren().add(billLabel);
       return billBox;
   }
   
   //sets up the check boxes
   private HBox setUpCheckBoxes() {
	   CheckBox billSameShipping = new CheckBox("Billing Same As Shipping?");
       CheckBox saveShipAddr = new CheckBox("Save Shipping Address");
       CheckBox saveBillAddr = new CheckBox("Save Billing Address");
       billSameShipping.selectedProperty().addListener( (observed, oldval, newval) -> {
       	if(billSameShipping.isSelected())
       		setBillingAddress(shipNameField.getText(), shipStreetField.getText(),
       				shipCityField.getText(), shipStateField.getText(), shipZipField.getText());
       });
       saveShipAddr.selectedProperty().addListener( (observed, oldval, newval) -> {
       	messageBar.setText("Need to write code for this");
       });
       saveBillAddr.selectedProperty().addListener( (observed, oldval, newval) -> {
       	messageBar.setText("Need to write code for this");
       });
                  
       HBox checkboxBox = new HBox(10);
       checkboxBox.getChildren().add(billSameShipping);
       checkboxBox.getChildren().add(saveShipAddr);
       checkboxBox.getChildren().add(saveBillAddr);
       checkboxBox.setAlignment(Pos.CENTER);
       return checkboxBox;
   }
   
   private HBox setUpButtons() {
	   Button selectShipButton = new Button("Select Ship Address");
		Button selectBillButton = new Button("Select Bill Address");
		Button proceedButton = new Button("Checkout");
		Button backButton = new Button("Back to Cart");
		HBox btnBox = new HBox(10);
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().add(selectShipButton);
		btnBox.getChildren().add(selectBillButton);
		btnBox.getChildren().add(proceedButton);
		btnBox.getChildren().add(backButton);
		btnBox.getChildren().add(new GridPane());
		backButton.setOnAction(evt -> {
			ShoppingCartWindow.INSTANCE.show();
			messageBar.setText("");
			hide();
		});
		
		proceedButton.setOnAction(evt -> {
			messageBar.setText("");
			PaymentWindow payment = new PaymentWindow(this, billNameField.getText());
			payment.show();
			hide();
		});
	       
		selectShipButton.setOnAction(evt -> {
			messageBar.setText("");
			shipAddressWindow.show();
		});
		selectBillButton.setOnAction(evt -> {	
			messageBar.setText("");
			billAddressWindow.show();
		});
		
		return btnBox;
   }
   

	public void setShippingAddress(String name, String st, String city, String state, String zip) {
		if(shipNameField != null) {
			shipNameField.setText(name);
		}
		if(shipStreetField != null) {
			shipStreetField.setText(st);
		}

		if(shipCityField != null) {
			shipCityField.setText(city);
		}
		if(shipStateField != null) {
			shipStateField.setText(state);
		}
		if(shipZipField != null) {
			shipZipField.setText(zip);
		}
	}
    
    public void setBillingAddress(String name, String st, String city, String state, String zip) {
        if(billNameField != null) {
            billNameField.setText(name);
        }
        if(billStreetField != null) {
            billStreetField.setText(st);
        }

        if(billCityField != null) {
            billCityField.setText(city);
        }
        if(billStateField != null) {
            billStateField.setText(state);
        }
        if(billZipField != null) {
            billZipField.setText(zip);
        }
    }
    
    
    private void createPopups() {
  		shipAddressWindow = new SelectAddress(this, "Shipping Addresses", cust -> {
  			this.setShippingAddress(cust.getFullName(), cust.getStreet(), 
  					cust.getCity(), cust.getState(), cust.getZip());
  		});
  		shipAddressWindow.setData(DefaultData.CUSTS_ON_FILE
    			                      .stream()
    			                      .filter(cust -> cust.getAddress().isShippingAddress())
    			                      .collect(Collectors.toList()));
  		shipAddressWindow.setX(100);
  		shipAddressWindow.setY(100);
  		
  		billAddressWindow = new SelectAddress(this, "Billing Addresses", cust -> {
  			this.setBillingAddress(cust.getFullName(), cust.getStreet(), 
  					cust.getCity(), cust.getState(), cust.getZip());
  		});
  		billAddressWindow.setData(DefaultData.CUSTS_ON_FILE
                .stream()
                .filter(cust -> cust.getAddress().isBillingAddress())
                .collect(Collectors.toList()));
  		billAddressWindow.setX(120);
  		billAddressWindow.setY(120);
  		
    }

	
	private void loadDefaultData() {
		List<String> shipData = DefaultData.DEFAULT_SHIP_DATA;
		setShippingAddress(shipData.get(0), shipData.get(1), shipData.get(2), 
				shipData.get(3), shipData.get(4));
		List<String> billData = DefaultData.DEFAULT_BILLING_DATA;
		setBillingAddress(billData.get(0), billData.get(1), billData.get(2), 
				billData.get(3), billData.get(4));
	}
	
	
	
            		
            		
}

