package test;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class AdminCLI extends CLI {
    
    public static void run(Scanner scanner, Admin admin) {
        System.out.println("ADMIN VIEW");

        HashMap<Integer, Product> productList = admin.getProductList();

        while (true) {
        	System.out.printf("\nADMIN VIEW: %s", admin.getName());
            printAdminMenu();
            int selection = CLI.promptInt(scanner, "Enter selection", -1);
            if (selection != -1 ) {
            switch (selection) {
	            case 0:
	        		//Log out
	        		Users.logout(admin);
					// Should go back to main menu
	        		return;
				
                case 1:
                	// View all products
                	System.out.println(admin.viewStock());
                    break;

                case 2: // Add new product
                	// gather details
                	int productID = DBFunctions.getNewProductID();
                    System.out.println("Assigned Product ID: " + productID);

                    ProductCategory category = CLI.promptCategory(scanner, "Enter category \n1) BOARDGAME\n"
                    		+ "2) ACCESSORY");
                    if (category == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

                    String type;
                    if (category == ProductCategory.BOARDGAME) {
                        type = CLI.promptString(scanner, "Enter game type");
                    } else {
                    	
                        int AT = CLI.promptInt(scanner, "Enter accessory type\n1) Dice\n2) miniature\n3) Accessory kit",3);
                        if (AT == 1) {
                        	type = "dice";
                        } else if (AT == 2) {
                        	type = "miniature";
                        } else if (AT == 2) {
                        	type = "accessory kit";
                        }
                        else {
                        	type = null;
                        }
                    }
                    if (type == null) {break;}

                    
                    String productName = CLI.promptString(scanner, "Enter product name");
                    if (productName == null) {break;}

                    
                    double purchaseCost = CLI.promptDouble(scanner, "Enter purchase cost", -1);
                    if (purchaseCost == -1) { break;}

                    
                    int quantityInStock = CLI.promptInt(scanner, "Enter quantity in stock", -1);
                    if (quantityInStock == -1) { break;}

                    
                   
                    double price = CLI.promptDouble(scanner, "Enter selling price", -1);
                    if (price == -1) {break;}

                    
                    String additionalInfo;
                    if (category == ProductCategory.BOARDGAME) {
                        additionalInfo = CLI.promptString(scanner, "Enter max players");
                    } else {
                        additionalInfo = CLI.promptString(scanner, "Enter compatibility info");
                    }
                    if (additionalInfo == null) {break;}

                    
                    admin.addProduct(productID, category, type, productName,
                            purchaseCost, quantityInStock, price, additionalInfo);
                    
                    DBFunctions.updateStock(productList);

            	    System.out.println("Product added successfully");
                    break;

                case 3: // Remove product
                	int ID = CLI.promptInt(scanner,"Enter product ID: ",-1);
                	
                	
            		if (ID == -1) { // user pressed go back
            			break;
            		} else {
            			
            			admin.removeProduct(ID);
            			System.out.println("Product removed successfully");
            			break;
            		}

                case 4: 
                	// Change product price
                	int ID4 = CLI.promptInt(scanner,"Enter product ID: ",-1);
            		if (ID4 == -1) { // user pressed go back
            			break;
            		} else {
            			double newPrice = CLI.promptDouble(scanner, "Enter new price", -1);
            			if (newPrice == -1) { // user pressed go back
            				break;
            			}
            		
                	admin.changePrice(ID4, newPrice);
                	DBFunctions.updateStock(productList);
        		    System.out.println("Price updated");
                    break;
            		}

                case 5: // Lookup product by ID
                	int SearchID = promptInt(scanner,"Enter product ID: ", -1);
                	
                	// true means user is admin thus granting admin view
                	// substring search, product list, is_admin
	        		DBFunctions.search(String.valueOf(SearchID), productList,true);
	        		break;

                case 6: // Search/filter based on compatibility
                	String compatibility = promptString(scanner, "Enter compatibility:");
                	if (compatibility == null) {
                	    System.out.println("Cancelled."); // user pressed go back
                	    break;
                	}
                	DBFunctions.search(compatibility, productList,true);
                	break;


                case 7: // Order more stock
                	int ID7 = CLI.promptInt(scanner, "Enter product ID", -1);
            	    if (ID7 == -1) {
            	        System.out.println("Cancelled."); // user pressed go back
            	        break;
            	    }
            	    Product p = productList.get(ID7); // check exists
            	    if (p != null) {// add stock to existing stock
	
	            	    int quantity = CLI.promptInt(scanner, "Enter quantity to add", -1);
	            	    if (quantity == -1) {
	            	        System.out.println("Cancelled."); // user pressed go back
	            	        break;
	            	    }
	            	    
	                	admin.orderMoreStock(quantity, p);
	                	DBFunctions.updateStock(productList);
	         	        System.out.println("Successfully added to stock.");
	                    break;
	                    
            	    } else {
            	        System.out.println("Product not found");
            	        break;
            	    }
            	    

                case 8: // Add new admin
                		// previously returned an Admin object but its instantiation is not needed
                    String name = CLI.promptString(scanner, "Enter name");
                    if (name == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

                    int houseNo = CLI.promptInt(scanner, "Enter house number", -1);
                    if (houseNo == -1) {
                        System.out.println("Cancelled.");
                        break;
                    }
                    String houseNumber = String.valueOf(houseNo);

                    String postcode = CLI.promptPostcode(scanner, "Enter postcode");
                    if (postcode == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

                    String city = CLI.promptString(scanner, "Enter city");
                    if (city == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

                    Admin.newAdmin(name, houseNumber, postcode, city,
                            admin.getProductList());

                    System.out.println("New admin created: " + name);
                    break;


                case 9: // Display users
				try {
					Users.viewUsers();
				} catch (FileNotFoundException e) {
					System.out.println("File not Found");
				}
                    break;
                    
                case 10:
                	System.out.println("Are you sure you want to delete your account?");
                	int answer = CLI.promptInt(scanner, "1) Yes\n2) No", 2);

                	if (answer == 1) {
                	    admin.deleteAccount();
                	    return;
                	} else if (answer == 2) {
                	    System.out.println("Cancelled.");
                	    break;
                	}

	                
                

                default:
                    System.out.println("Invalid selection");
            }
        } else {
        	Users.logout(admin);
        	return;
			
}}}
    

    private static void printAdminMenu() {
        System.out.println("\n\n\nPLEASE SELECT ACTION BY INPUTTING THE CORRESPONDING NUMBER (or 0 for logout)");
        System.out.println("1) View all products");
        System.out.println("2) Add new product");
        System.out.println("3) Remove Product");
        System.out.println("4) Change product price");
        System.out.println("5) Lookup with product ID");
        System.out.println("6) Search/filter based on compatibility");
        System.out.println("7) Order more stock");
        System.out.println("8) Add new admin");
        System.out.println("9) Display Users");
        System.out.println("10) Delete Account");
        
    }
}
