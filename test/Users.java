package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public abstract class Users {
	protected int userID;
	protected String role;
	protected String name;
	protected String houseNumber;
	protected String postcode;
	protected String city;
	protected HashMap<Integer, Product> productList; //Load all products into the customer object during runtime
	
	protected Users(int userID, String name, String houseNumber, String postcode, String city,
			String role, HashMap<Integer, Product> productList) {
		this.userID = userID;
		this.role = role;
		this.productList = productList;
		this.houseNumber = houseNumber;
		this.postcode = postcode;
		this.city = city;
		this.name = name;
		
		
	}
	
	//get productList
	public HashMap<Integer, Product> getProductList() {
	    return productList;
	}

	//log out
	protected static void logout(Users user) {
		if (user instanceof Customer) {
			((Customer)user).clearCart();
		}
		user = null;
	}
	
	//log in
	protected static void login(Scanner consoleInput, int index) throws FileNotFoundException {
		// log in the user
		String[] information = DBFunctions.grabIndexInformation(index);
		int userID = Integer.parseInt(information[0].trim());
		String name = information[1].trim();
		String houseNumber = information[2].trim();
		String postcode = information[3].trim();
		String city = information[4].trim();
		String role = information[5].trim();
		HashMap<Integer, Integer> cart = new HashMap<>(); ;
		HashMap<Integer, Product> productList = new HashMap<>();
		
		try {
			productList = DBFunctions.loadProducts();
			if (role.equals("admin")){
				Admin admin = new Admin(userID, name, houseNumber ,postcode,
						city, "admin", productList);
				AdminCLI.run(consoleInput,admin);
			} else {
			Customer customer = new Customer(userID, name, houseNumber ,postcode,
					city, "customer", productList, cart);
    		CustomerCLI.run(consoleInput, customer);
		}} 
			catch (FileNotFoundException e) {System.out.println("File not found");}
		
	}
	//delete user account & log out
	protected void deleteAccount() {

	    ArrayList<String[]> users;
	    try {
	        users = DBFunctions.read("UserAccounts.txt");
	    } catch (FileNotFoundException e) {
	        System.out.println("UserAccounts.txt not found");
	        return;
	    }

	    boolean found = false;

	    // Build a new list without the deleted user
	    ArrayList<String[]> updatedList = new ArrayList<>();

	    for (String[] row : users) {
            try {
                int id = Integer.parseInt(row[0]);
                if (id == userID) {
                    found = true;
                    continue; // dont add this row
                }
            } catch (NumberFormatException e) {
            }
            updatedList.add(row);
        }
	        
	    

	    if (found == false) {
	        System.out.println("User ID not found");
	        return;
	    }
	 // If the deleted user is currently logged in as a customer, clear their cart
	    if (this instanceof Customer) {
	        ((Customer) this).clearCart();
	    }


	    // save the file 
	    try (PrintWriter writer = new PrintWriter("UserAccounts.txt")) {
	        for (String[] row : updatedList) {
	            writer.println(String.join(";", row));
	        }
	    } catch (FileNotFoundException e) {
	        System.out.println("UserAccounts.txt not found");
	        return;
	    }
	    
	    System.out.println("Account deleted successfully");
	    logout(this);
	}
	// view users
	protected static void viewUsers() throws FileNotFoundException {
	    ArrayList<String[]> list = DBFunctions.read("UserAccounts.txt");
	    // make a table
	    System.out.printf("%-5s %-8s %-15s %-12s%n",
	            "No.", "ID", "Name", "Role");

	    System.out.println("----------------------------"
	    		+ "-------------------------");

	    int index = 0;
	    //data
	    for (String[] line : list) {

	        if (line.length < 6 || line[0].trim().isEmpty()) {
	            continue;
	        }
	        index++;

	        String id = line[0];
	        String name = line[1];
	        String role = line[5];

	        System.out.printf("%-5d %-8s %-15s %-12s%n",
	                index, id, name, role);
	    }
	}
	
	// getter methods
	protected String getHouseNumber() {
	    return houseNumber;
	}

	protected String getPostcode() {
	    return postcode;
	}

	protected String getCity() {
	    return city;
	}
	protected String getName() {
	    return name;
	}

	

}
