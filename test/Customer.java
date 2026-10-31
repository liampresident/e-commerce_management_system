package test;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Customer extends Users {
	//Cart structure is HashMap<productID, Quantity>
	private HashMap<Integer, Integer> cart = new HashMap<Integer, Integer>();
	
	public Customer(int userID, String name, String houseNumber, String postcode,
			String city, String role, HashMap<Integer, Product> productList
			, HashMap<Integer, Integer> cart) {
		super(userID,name, houseNumber, postcode, city, role, productList);
		this.cart = cart;
		
		
	}
	 // add to cart
	public void addToCart(int productID, int quantity) {
		Product product = productList.get(productID);
		if (product == null) {
	        System.out.println("Product not found");
	        return;
	    }
		int quantityInStock = product.getQuantityInStock();
		if (quantity <= quantityInStock) {
			cart.put(productID, quantity);
			product.setQuantityInStock(quantityInStock - quantity);
			System.out.println("Successfully added to cart");
		}
		else {
			System.out.println("Not enough stock");
		}
	}
	// remove from cart
	public void removeFromCart(int productID, int quantityToRemove) {// either remove all of it or some
		
		if (cart.containsKey(productID) == false) {
			return;
		}
		int currentQuan = cart.get(productID);
		if (quantityToRemove >= currentQuan) { // if entered amount is higher than cart, remove from cart
			cart.remove(productID);
		} else {
			// Update number of item in the basket
			int newQuan = currentQuan - quantityToRemove;
			cart.put(productID, newQuan);
		}
	
		
	}
	// clear cart
	public void clearCart() {

	    // Loop through all items in the cart and return
	    for (Integer productID : cart.keySet()) {
	        int quantity = cart.get(productID);

	        Product product = productList.get(productID);
	        if (product != null) {
	            // Return stock
	            int newStock = product.getQuantityInStock() + quantity;
	            product.setQuantityInStock(newStock);
	        }
	    }
	    
	    DBFunctions.updateStock(productList);
	    cart.clear();
	}
	// let user pay
	public Receipt pay(Scanner scanner) { //////////// COMPLETE PAYMENT

	    if (getCart().isEmpty()) {
	        System.out.println("Cart is empty.");
	        return null;
	    }

	    Receipt receipt = PaymentSystem.processPayment(scanner, this);

	    if (receipt == null) {
	        System.out.println("Payment cancelled.");
	        return null;
	    }

	    clearCart();
	    return receipt;
	}
	//get cart
	public HashMap<Integer, Integer> getCart() {
		return cart;
	}
	// optional new customer method
	public static Customer newCustomer(Scanner consoleInput, HashMap<Integer, Product> productList) {

	    //101;Alex;12;LE11 3TU;Loughborough;admin
	    ArrayList<String> customerInformation = new ArrayList<>();
	    
	    String name = CLI.promptString(consoleInput, "Enter name");
	    if (name == null) { return null; }
	    customerInformation.add(name);
	    
	    int houseNo = CLI.promptInt(consoleInput, "Enter house number",-1);
	    if (houseNo == -1) { return null; }
	    String houseNumber = String.valueOf(houseNo);
	    customerInformation.add(String.valueOf(houseNumber));
	    
	    String postcode = CLI.promptPostcode(consoleInput, "Enter postcode");
	    if (postcode == null) { return null; }
	    customerInformation.add(postcode);

	    String city = CLI.promptString(consoleInput, "Enter city");
	    if (city == null) { return null; }
	    customerInformation.add(city);
	    
	    String role = "customer";
	    customerInformation.add(role);
	    
	    int newID = DBFunctions.getNewID();
	    HashMap<Integer, Integer> cart = new HashMap<Integer, Integer>();
	    Customer customer = new Customer(newID, name, houseNumber ,postcode,
	            city, "customer", productList, cart);
	    
	    //Save to DB
	    ArrayList<String> newInfo = new ArrayList<String>();
	    newInfo.add(String.valueOf(newID));
	    newInfo.add(name);
	    newInfo.add(houseNumber);
	    newInfo.add(postcode);
	    newInfo.add(city);
	    newInfo.add(role);
	    try {
	        DBFunctions.write(newInfo, "UserAccounts.txt");
	    } catch (FileNotFoundException e) {}
	    return customer;
	    
	}
	// sum cart total
	public double getCartTotal() {
	    double total = 0.0;

	    for (HashMap.Entry<Integer, Integer> entry : cart.entrySet()) {
	        int productID = entry.getKey();
	        int quantity = entry.getValue();

	        Product p = productList.get(productID);
	        if (p != null) {
	            total += p.getPrice() * quantity;
	        }
	    }

	    return total;
	}
	
	//view products for a customer
	public String viewProducts() { // exclude purchase cost
	    HashMap<Integer, Product> productList = this.productList;

	    // Sort by price in Descending order
	    List<Product> sortedProducts = new ArrayList<>(productList.values());
	    sortedProducts.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));

	    String output = "";

	    // header
	    output += String.format("%-8s %-12s %-30s %-10s %-10s %-20s %-20s%n",
	            "ID", "Category", "Name", "Price", "Stock", "Type", "Additional Info");

	    output += "-----------------------------------------------------------"
	            + "----------------------------------------"
	            + "-----------------\n";

	    // data
	    for (Product p : sortedProducts) {

	        int productID = p.getProductID();
	        ProductCategory productCategory = p.getProductCategory();
	        String productName = p.getProductName();
	        Double price = p.getPrice();
	        int quantityInStock = p.getQuantityInStock();

	        String type = "";
	        String extra = "";

	        if (p instanceof BoardGame) {
	            BoardGame bg = (BoardGame) p;
	            type = bg.getGameType();
	            extra = String.valueOf(bg.getMaxPlayers());
	        } else if (p instanceof Accessory) {
	            Accessory acc = (Accessory) p;
	            type = acc.getAccessoryType();
	            extra = acc.getCompatibilityType();
	        }

	        output += String.format("%-8d %-12s %-30s %-10.2f %-10d %-20s %-20s%n",
	                productID, productCategory, productName,
	                price, quantityInStock, type, extra
	        );
	    }

	    return output;
	}


	// print out basket contents
	public String viewBasket() {
		
		// builds table as before for the users cart
	    HashMap<Integer, Integer> cart = this.cart;

	    String output = "";

	    output += String.format("%-8s %-30s %-8s %-10s%n",
	            "ID", "Name", "Qty", "Cost (£)");

	    output += "-----------------------------"
	            + "-------------------------------------\n";

	    for (HashMap.Entry<Integer, Integer> entry : cart.entrySet()) {
	        int productID = entry.getKey();
	        int quantity = entry.getValue();
	        Product p = productList.get(productID);
	        double total = p.getPrice() * quantity;

	        output += String.format("%-8d %-30s %-8d %-10.2f%n",
	                productID,
	                p.getProductName(),
	                quantity,
	                total);
	    }

	    return output;
	}



}
