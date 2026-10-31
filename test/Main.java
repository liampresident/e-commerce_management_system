package test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        Scanner consoleInput = new Scanner(System.in);

        System.out.println("WELCOME");
        HashMap<Integer, Product> productList = DBFunctions.loadProducts();
        
        while (true) { // main running loop
        	displayMainMenu(); //0 exit, 1 login, and 2 new customer
            int index = CLI.promptInt(consoleInput,"Enter selection:", 2);
            if (index == -1) {
            	index = 0;
            }
        	switch (index) {
        	case 0: { //Exit case
        		System.out.println("Shutting down...");
        		System.out.println("Terminated Successfully");
        		consoleInput.close();
        		System.exit(0);
        		break;
        	}
        	
        	case 1: { // Login case
        		printLoginOptions();
        		int maxIndex = DBFunctions.getUserCount();
        		int Index = CLI.promptInt(consoleInput, "Enter Account index: ", maxIndex);
        		if (Index == -1) {
        			Main.main(null);
        		}
        		Users.login(consoleInput, Index);
        		break;
        	}
        	
        	
        	case 2: {//New customer 
        		Customer customer = Customer.newCustomer(consoleInput, productList);
        		if (customer != null){ // user goes back at any stage
	        		System.out.println("New customer successfully added\n");
	        		CustomerCLI.run(consoleInput,customer);
        		}
        		break;
        		
        		
        		
        		
        	}
            	
            }
           
        }
    }

    private static void printLoginOptions() { //Display list of accounts to sign in to
        System.out.println("PLEASE SELECT USER BY INPUTTING THE CORRESPONDING NUMBER (or 0 for exit)");
        try {
			Users.viewUsers();
		} catch (FileNotFoundException error) {System.out.println("File Not found");}
    }

    
    private static void displayMainMenu() {
		System.out.println("Game Store Management Pro");
		System.out.println("--- Select An Option ---");
		System.out.println("0) Exit");
		System.out.println("1) Login");
		System.out.println("2) New customer");
	}
}