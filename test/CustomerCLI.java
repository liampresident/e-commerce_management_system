package test;

import java.util.HashMap;
import java.util.Scanner;


public class CustomerCLI extends CLI {
	// the code that runs, handles inputs and displays options for customers
	// it is only a caller class
    public static void run(Scanner scanner, Customer customer) {
	    while (true) {
	    	HashMap<Integer, Product> productList = customer.getProductList();
	    	
	    	
	        System.out.printf("\nCUSTOMER VIEW: %s", customer.getName());
	        printCustomerMenu();
	        int selection = promptInt(scanner, "Enter choice: ", 9);
	        
	        if (selection != -1 ) { //  requests to go back
	        	switch (selection) {
	        	case 0:
	        		//Log out
	        		Users.logout(customer);
					// Should go back to main menu
	        		return;
	        	case 1:
	        		//view all products (customers view)
	        		System.out.println(customer.viewProducts());
	        		break;
	        		
	        	case 2:
	        		//Add product to shopping basket
	        		int ID = promptInt(scanner, "Enter ProductID: ", -1);
	        		if (ID == -1) {
	        		    System.out.println("Cancelled.");
	        		    break;
	        		}
	        		int quantity = promptInt(scanner, "Enter quantity: ", -1);
	        		if (quantity == -1) {
	        		    System.out.println("Cancelled.");
	        		    break;
	        		}
	        		customer.addToCart(ID, quantity);
	        		DBFunctions.updateStock(productList);
	        		break;

	        		
	        	case 3:
	        		// view basket
	        		System.out.println(customer.viewBasket());
	        		break;
	        	case 4:
	        		//Purchase items in the basket
	        		Receipt receipt = customer.pay(scanner);
	        		if (receipt == null) {
	        			break;
	        		}
	        		receipt.printReceipt();
	        		
	        		
	        		break;
	        	case 5:
	        		//Cancel shopping basket
	        		customer.clearCart();
	        		break;
	        		
	        	// remove from basket#
	        		// remove from basket
	        	case 6:

	        	    // Ask for ProductID
	        	    int removeID = CLI.promptInt(scanner, "Enter ProductID to remove:", -1);
	        	    if (removeID == -1) {
	        	        System.out.println("Cancelled.");
	        	        break;
	        	    }

	        	    // Check product exists
	        	    Product p = productList.get(removeID);
	        	    if (p == null) {
	        	        System.out.println("Product not found.");
	        	        break;
	        	    }

	        	    // Check if product is in user's cart
	        	    Integer cartQuantity = customer.getCart().get(removeID);
	        	    if (cartQuantity == null) {
	        	        System.out.println("This item is not in your basket.");
	        	        break;
	        	    }

	        	    // Ask for quantity to remove
	        	    int removeQuantity = CLI.promptInt(scanner, "Enter quantity to remove:", -1);
	        	    if (removeQuantity == -1) {
	        	        System.out.println("Cancelled.");
	        	        break;
	        	    }

	        	    // Cap removal to the amount in the basket
	        	    if (removeQuantity > cartQuantity) {
	        	        removeQuantity = cartQuantity;
	        	    }

	        	    // Return removed quantity to stock
	        	    int currentStock = p.getQuantityInStock();
	        	    p.setQuantityInStock(currentStock + removeQuantity);

	        	    DBFunctions.updateStock(productList);

	        	    // Remove from cart
	        	    customer.removeFromCart(removeID, removeQuantity);

	        	    System.out.println("Item removed from cart.");
	        	    break;



	        	case 7:
	        		//Lookup with product ID
	        		int SearchID = promptInt(scanner,"Enter product ID: ", -1);
	        		if (SearchID == -1) {
	        		    System.out.println("Cancelled.");
	        		    break;
	        		}
	        		DBFunctions.search(String.valueOf(SearchID), productList,false);
	        		break;

	        	case 8:
	        		//Search/filter based on compatibility
	        		String compatibility = promptString(scanner, "Enter compatibility:");
	        		if (compatibility == null) {
	        		    System.out.println("Cancelled.");
	        		    break;
	        		}
	        		DBFunctions.search(compatibility, productList, false);
	        		break;

	        	case 9: //delete accoutn
	                System.out.println("Are you sure you want to delete your account?");
	                int answer = promptInt(scanner,"1)Yes\n2)No",2);
	                if (answer == 1 ) {
	                	customer.deleteAccount();
	                	return;
	                }
	                
	        	default:
	        		break;
	        	}
        	
	        } else {
	        	Users.logout(customer);
	        	return;
				
				}
	        }
	    }
    
        
    
    
    private static void printCustomerMenu() {
        System.out.println("\n\n\nPLEASE SELECT ACTION BY INPUTTING THE CORRESPONDING NUMBER (or 0 for logout)");
        System.out.println("1) View all products");
        System.out.println("2) Add product to shopping basket");
        System.out.println("3) View contents of shopping basket");
        System.out.println("4) Purchase items in the basket");
        System.out.println("5) Cancel shopping basket");
        System.out.println("6) Remove item from basket");
        System.out.println("7) Lookup with product ID");
        System.out.println("8) Search/filter based on compatibility");
        System.out.println("9) Delete Account");

    }
}