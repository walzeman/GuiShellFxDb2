package gui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class DefaultData {
	public static final Catalog BOOKS_CATALOG = new Catalog("1", "Books");
	public static final Catalog CLOTHES_CATALOG = new Catalog("2", "Clothing");
	public static final HashMap<String, Catalog> CATALOG_MAP = new HashMap<String, Catalog>() {
		{
			put(BOOKS_CATALOG.getName(), BOOKS_CATALOG);
			put(CLOTHES_CATALOG.getName(), CLOTHES_CATALOG);
		}
	};
	public static final Product MESSIAH_BOOK = new Product(BOOKS_CATALOG,"Messiah Of Dune", LocalDate.of(2000, 11, 11), 20, 15.00);
	public static final Product GONE_BOOK = new Product(BOOKS_CATALOG,"Gone with the Wind", LocalDate.of(1995, 12, 5), 15, 12.00);
	public static final Product GARDEN_BOOK = new Product(BOOKS_CATALOG,"Garden of Rama", LocalDate.of(2005, 1, 1), 5, 18.00);
	public static final Product PANTS = new Product(CLOTHES_CATALOG, "Pants", LocalDate.of(2000, 11, 1), 20, 15.00);
	public static final Product SKIRTS = new Product(CLOTHES_CATALOG, "Skirts", LocalDate.of(1995, 1, 5), 15, 12.00);
	public static final Product TSHIRTS = new Product(CLOTHES_CATALOG, "T-Shirts", LocalDate.of(2003, 6, 18), 10, 22.00);
	public static final ObservableList<Catalog> CATALOG_LIST_DATA =
            FXCollections.observableArrayList(
            BOOKS_CATALOG, CLOTHES_CATALOG);
	public final static ObservableMap<Catalog, List<Product>> PRODUCT_LIST_DATA =
            FXCollections.observableHashMap();
	public static final List<String> DISPLAY_PRODUCT_FIELDS 
	   = Arrays.asList("Item Name", "Price", "Quantity Available", "Review");
	public static final List<String> DISPLAY_ADDRESS_FIELDS
	   = Arrays.asList("Name", "Street", "City", "State", "Zip");
	public static final List<String> DISPLAY_CREDIT_CARD_FIELDS
	   = Arrays.asList("Name", "Card Number", "Card Type", "Expiration Date");
	public static final List<String> DEFAULT_SHIP_DATA
	   = Arrays.asList("John Thomas", "101 Adams St.", "Fairfield", "IA", "52556");
	public static final List<String> DEFAULT_BILLING_DATA
	   = Arrays.asList("John Thomas", "21 Berkeley Ave.", "Cincinnati", "OH", "45201");
	public static final List<Address> ADDRESSES_ON_FILE 
	   = Arrays.asList(new Address("101 Jackson St", "Fairfield", "IA", "52556", true, true),
			   new Address("300 W. Washington Ave", "Fairfield", "IA", "52556", true, false),
			   new Address("1000 N. 4th St.", "Fairfield", "IA", "52557", false, true),
			   new Address("1435 Channing Ave", "Palo Alto", "CA", "94301", true, true));
	public static final List<Customer> CUSTS_ON_FILE
		= Arrays.asList(new Customer("John", "Smith"), new Customer("Andrew", "Anderson"),
				new Customer("Ralph", "Horowitz"), new Customer("Donald", "Knuth"));
	public static final List<String> CREDIT_CARD_TYPES
		= Arrays.asList("Visa", "Master Card", "Discover");
	public static final String DEFAULT_CARD_TYPE = "Visa";
	public static final CartItem savedItem1 = new CartItem();
	public static final CartItem savedItem2 = new CartItem();
	public static final List<CartItem> savedCartItems = new ArrayList<>();
	//String name, String quantity, String price
	public static final OrderItem orderItem1 = new OrderItem(MESSIAH_BOOK.getProductName(),
			"2", (new Double(MESSIAH_BOOK.getUnitPrice()).toString()));
	public static final OrderItem orderItem2 = new OrderItem(GARDEN_BOOK.getProductName(),
			"3", (new Double(GARDEN_BOOK.getUnitPrice()).toString()));
	public static final OrderItem orderItem3 = new OrderItem(PANTS.getProductName(),
			"2", (new Double(PANTS.getUnitPrice()).toString()));
	public static final OrderItem orderItem4 = new OrderItem(SKIRTS.getProductName(),
			"1", (new Double(SKIRTS.getUnitPrice()).toString()));
	public static final Order order1 = new Order();
	public static final Order order2 = new Order();
	public static final List<Order> ALL_ORDERS = Arrays.asList(order1, order2);
	
	static {
		savedCartItems.add(savedItem1);
		savedCartItems.add(savedItem2);
		orderItem1.setItemID(1001);
		orderItem2.setItemID(1002);
		orderItem3.setItemID(1003);
		orderItem4.setItemID(1004);
		orderItem1.setOrderID(101);
		orderItem2.setOrderID(101);
		orderItem3.setOrderID(102);
		orderItem4.setOrderID(102);
		order1.setOrderID("101");
		order2.setOrderID("102");
		order1.setDate(LocalDate.of(2014, 11, 11));
		order2.setDate(LocalDate.of(2015, 2, 5));
		order1.setOrderItems(Arrays.asList(orderItem1, orderItem2));
		order2.setOrderItems(Arrays.asList(orderItem3, orderItem4));
		
		MESSIAH_BOOK.setDescription("You saw how good Dune was. \nThis is Part 2 of this \nunforgettable trilogy.");
		GONE_BOOK.setDescription("A moving classic that tells \na tale of love and \na tale of courage.");
		GARDEN_BOOK.setDescription("Highly acclaimed Book \nof Isaac Asimov. A real \nnail-biter.");
		PANTS.setDescription("I've seen the Grand Canyon. I've camped \nat Yellowstone. But nothing on \nearth compares to the look and feel of \nthese pants.");
		SKIRTS.setDescription("Once this brand of skirts \nbecomes well-known, watch out!");
		TSHIRTS.setDescription("Can be worn by men or women. \nAlways in style.");
		MESSIAH_BOOK.setProductId("11");
		GONE_BOOK.setProductId("12");
		GARDEN_BOOK.setProductId("13");
		PANTS.setProductId("14");
		SKIRTS.setProductId("15");
		TSHIRTS.setProductId("16");
		
		savedItem1.setItemName(MESSIAH_BOOK.getProductName());
		savedItem1.setPrice(MESSIAH_BOOK.getUnitPrice());
		savedItem1.setQuantity(2);
		savedItem1.setTotalPrice(GuiUtils.stringDoublesMultiply(savedItem1.getQuantity(), savedItem1.getPrice()));
		savedItem2.setItemName(PANTS.getProductName());
		savedItem2.setPrice(PANTS.getUnitPrice());
		savedItem2.setQuantity(3);
		savedItem2.setTotalPrice(GuiUtils.stringDoublesMultiply(savedItem2.getQuantity(), savedItem2.getPrice()));
		
		PRODUCT_LIST_DATA.put(BOOKS_CATALOG, 
				new ArrayList<Product>() {
					{
						add(MESSIAH_BOOK);
						add(GONE_BOOK);
						add(GARDEN_BOOK);
					}
				});
		
						
		PRODUCT_LIST_DATA.put(CLOTHES_CATALOG, 
			new ArrayList<Product>() {
				{
					add(PANTS);
					add(SKIRTS);
					add(TSHIRTS);
				}
		});
				
		for(int i = 0; i < 4; ++i) {
			CUSTS_ON_FILE.get(i).setAddress(ADDRESSES_ON_FILE.get(i));
			
		}
		
								
	}
	//produces a random int between x and x+80
	public static String generateId(int x) {
		Random rand = new Random();
		Integer next = x + rand.nextInt(80);
		return next.toString();
	}
}
