package test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Admin extends Users {
	
	public Admin(int userID, String name, String houseNumber, String postcode, String city,
			String role, HashMap<Integer, Product> productList) {
		super(userID, name, houseNumber, postcode, city,
				role, productList);
		
	}
	
	//add a new product to stock
	public void addProduct(int productID, ProductCategory category,
	        String type, String productName, double purchaseCost,
	        int quantityInStock, double price, String additionalInfo
	) {

	    Product product;
	    // instantiate & add to productList
	    if (category == ProductCategory.BOARDGAME) {
	        int maxPlayers = Integer.parseInt(additionalInfo);
	        product = new BoardGame(productID, category, type, productName,
	                purchaseCost, quantityInStock, price, maxPlayers);
	    } else {
	    	AccessoryType accessoryType;
	    	String t = type.trim().toLowerCase();
	    	if (t.equals("dice")) {
	    		 accessoryType = AccessoryType.dice;
	    	} else if (t.equals("miniature")) {
	    		 accessoryType = AccessoryType.miniature;
	    	}else if (t.equals("accessory kit")) {
	    		 accessoryType = AccessoryType.accessory_kit;
	    	}else {
	    		accessoryType = null;
	    		return;
	    	}
	    	
	        product = new Accessory(productID, category, accessoryType, productName,
	                purchaseCost, quantityInStock, price, additionalInfo);
	    }

	    productList.put(productID, product);
	   
	}

	// remove product from productList & stock
	public void removeProduct(int productID) {
			productList.remove(productID);
			DBFunctions.updateStock(productList);
		}
	
	// optional change price of a product
	public void changePrice(int productID, Double newPrice) {
		
			Product p = productList.get(productID);

		    if (p instanceof BoardGame) { // cant modify product class so two seperate changeprice methods
		        ((BoardGame) p).changePrice(newPrice);
		    } else if (p instanceof Accessory) {
		        ((Accessory) p).changePrice(newPrice);}

		    
		}
	
	    
	public String viewStock() { // Prints table like customer but shows purchase cost too

	    HashMap<Integer, Product> productList = getProductList();

	    List<Product> sortedProducts = new ArrayList<>(productList.values());
	    sortedProducts.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));

	    String output = "";

	    output += String.format("%-8s %-12s %-30s %-12s %-15s %-20s %-20s %-15s%n",
	            "ID", "Category", "Name", "Price", "Purchase Cost", "Type", "Additional Info", "Stock");

	    output += "---------------------------------------------------"
	            + "-----------------------------------------------------------"
	            + "------------------\n";

	    for (Product p : sortedProducts) {

	        String type = "";
	        String extra = "";

	        if (p instanceof BoardGame) {
	            BoardGame bg = (BoardGame) p;
	            type = bg.getGameType();
	            extra = String.valueOf(bg.getMaxPlayers());
	        }
	        else if (p instanceof Accessory) {
	            Accessory acc = (Accessory) p;
	            type = acc.getAccessoryType();
	            extra = acc.getCompatibilityType();
	        }

	        output += String.format("%-8d %-12s %-30s %-12.2f %-15.2f %-20s %-20s %-15d%n",
	                p.getProductID(), p.getProductCategory(), p.getProductName(),
	                p.getPrice(), p.getPurchaseCost(), type, extra,
	                p.getQuantityInStock()
	        );
	    }

	    return output;
	}

	// optional order more stock
	public void orderMoreStock(int quantity, Product p) {
	   p.setQuantityInStock(p.getQuantityInStock() + quantity);
	}
	    
	// create a new admin 
	public static void newAdmin( String name, String houseNumber, String postcode,
	        String city, HashMap<Integer, Product> productList) {

	    String role = "admin";
	    int newID = DBFunctions.getNewID();
	    // Save to DB
	    ArrayList<String> newInfo = new ArrayList<>();
	    newInfo.add(String.valueOf(newID));
	    newInfo.add(name);
	    newInfo.add(houseNumber);
	    newInfo.add(postcode);
	    newInfo.add(city);
	    newInfo.add(role);

	    try {
	        DBFunctions.write(newInfo, "UserAccounts.txt");
	    } catch (FileNotFoundException e) {
	        System.out.println("Error writing to file.");
	    }

	    return;
	}

	
}
