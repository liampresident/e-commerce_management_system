package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DBFunctions {
	
	// read file contents and return a list of rows
	public static ArrayList<String[]> read(String fileName) throws FileNotFoundException {
	    File file = new File(fileName);
	    Scanner fileScanner = new Scanner(file);
	    ArrayList<String[]> fileList = new ArrayList<>();

	    while (fileScanner.hasNextLine()) {
	        String raw = fileScanner.nextLine().trim();

	        // skip blank lines in file
	        if (raw.isEmpty()) {
	            continue;
	        }

	        // split and trim each field
	        String[] parts = raw.split(";");
	        for (int i = 0; i < parts.length; i++) {
	            parts[i] = parts[i].trim();  
	        }

	        // skip lines where the first field is blank
	        if (parts[0].isEmpty()) {
	            continue;
	        }

	        fileList.add(parts);
	    }

	    fileScanner.close();
	    return fileList;
	}

	
	//append line at the end of a file - specifically for newProduct
	public static void write(ArrayList<String> list, String fileName) throws FileNotFoundException {
    	String line = String.join(";", list);    	
    	try (PrintWriter writer = new PrintWriter(new java.io.FileOutputStream(fileName, true))) {
    		writer.println(line);

        }
	}
	// get new ID for either admins or customer
	public static int getNewID() {
	    try {
	        ArrayList<String[]> data = DBFunctions.read("UserAccounts.txt");
	        int maxID = 0;

	        for (String[] row : data) {
	            if (row.length == 0 || row[0].trim().isEmpty()) {
	                continue; // blank lines
	            }

	            try {
	                int id = Integer.parseInt(row[0].trim());
	                if (id > maxID) {
	                    maxID = id;
	                }
	            } catch (NumberFormatException e) {
	                // ignore invalid ID rows
	            }
	        }

	        return maxID + 1;

	    } catch (FileNotFoundException e) {
	        return 1;
	    }
	}

	
	//loop through text and instantiate into productList
	public static HashMap<Integer, Product> loadProducts() throws FileNotFoundException{
		ArrayList<String[]> list = read("Stock.txt");
		HashMap<Integer, Product> productList = new HashMap<Integer, Product>();
		for (String[] item : list) {
			if (item[1].trim().equals("board game")) { 
				
				/* Constructor
				int productID, ProductCategory productCategory,
				String gameType, String productName, double purchaseCost,
				int quantityInStock, double price, String type, int maxPlayers
				*/
				
				BoardGame product = new BoardGame(Integer.parseInt(item[0].trim()), ProductCategory.BOARDGAME,
						item[2].trim(), item[3].trim(),  Double.parseDouble(item[6].trim()),
						Integer.parseInt(item[5].trim()),
						Double.parseDouble(item[4].trim()), Integer.parseInt(item[7].trim()));
				productList.put(Integer.parseInt(item[0].trim()), product);
			} 
			else {
				/*
				int productID, ProductCategory productCategory,
				AcessoryType accessoryType, String productName, double purchaseCost, 
				int quantityInStock,double price, String compatabilityType
				*/
				
				Accessory product = new Accessory(Integer.parseInt(item[0].trim()),ProductCategory.ACCESSORY,
						AccessoryType.fromString(item[2].trim()), item[3].trim(),  Double.parseDouble(item[6].trim()),
						Integer.parseInt(item[5].trim()),
						Double.parseDouble(item[4].trim()),item[7].trim());
				
				productList.put(Integer.parseInt(item[0].trim()), product);
			}
		}// productID,  productCategory, productName,  purchaseCost, quantityInStock,  price, String type)
		return productList;
	}
	// print out a table that is filtered by a search value
	public static void search(String filter, HashMap<Integer, Product> productList, boolean isAdmin) {
	    String search = filter.toLowerCase();
	    ArrayList<Product> results = new ArrayList<>();

	    // Search through all products
	    for (Product p : productList.values()) {

	        boolean match =
	                String.valueOf(p.getProductID()).contains(search) ||
	                p.getProductCategory().toString().toLowerCase().contains(search) ||
	                p.getProductName().toLowerCase().contains(search) ||
	                String.valueOf(p.getPrice()).contains(search) ||
	                String.valueOf(p.getQuantityInStock()).contains(search);

	        // board game only fields
	        if (!match && p instanceof BoardGame) {
	            BoardGame bg = (BoardGame) p;

	            match =
            	    bg.getGameType().toLowerCase().contains(search) ||
            	    String.valueOf(bg.getMaxPlayers()).contains(search) ||
            	    (isAdmin && String.valueOf(bg.getPurchaseCost()).contains(search));

	        }

	        // Accessory only fields
	        if (!match && p instanceof Accessory) {
	            Accessory acc = (Accessory) p;

	            match =
            	    acc.getAccessoryType().toLowerCase().contains(search) ||
            	    acc.getCompatibilityType().toLowerCase().contains(search) ||
            	    (isAdmin && String.valueOf(acc.getPurchaseCost()).contains(search));

	        }

	        if (match) {
	            results.add(p);
	        }
	    }

	    // No results
	    if (results.isEmpty()) {
	        System.out.println("No products found matching: " + filter);
	        return;
	    }
	    //sort
	    results.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));

	    // Print header
	    if (isAdmin) {
	        System.out.printf("%-8s %-12s %-30s %-10s %-10s %-15s %-20s %-20s%n",
	                "ID", "Category", "Name", "Price", "Stock", "Cost", "Type", "Additional Info");
	    } else {
	        System.out.printf("%-8s %-12s %-30s %-10s %-10s %-20s %-20s%n",
	                "ID", "Category", "Name", "Price", "Stock", "Type", "Additional Info");
	    }

	    System.out.println("-------------------------------------------"
	            + "-------------------------------------------------"
	            + "------------------------------");

	    // rows
	    for (Product p : results) {

	        String type = "";
	        String extra = "";
	        double cost = 0;

	        if (p instanceof BoardGame) {
	            BoardGame bg = (BoardGame) p;
	            type = bg.getGameType();
	            extra = String.valueOf(bg.getMaxPlayers());
	            cost = bg.getPurchaseCost();
	        }
	        else if (p instanceof Accessory) {
	            Accessory acc = (Accessory) p;
	            type = acc.getAccessoryType();
	            extra = acc.getCompatibilityType();
	            cost = acc.getPurchaseCost();
	        }

	        if (isAdmin) {
	            System.out.printf("%-8d %-12s %-30s %-10.2f %-10d %-15.2f %-20s %-20s%n",
	                    p.getProductID(), p.getProductCategory(), p.getProductName(),
	                    p.getPrice(), p.getQuantityInStock(), cost, type, extra);
	        } else {
	            System.out.printf("%-8d %-12s %-30s %-10.2f %-10d %-20s %-20s%n",
	                    p.getProductID(), p.getProductCategory(), p.getProductName(),
	                    p.getPrice(), p.getQuantityInStock(), type, extra);
	        }
	    }
	}

	// take the productLISt and rewrite the file contents for integrity
	public static void updateStock(HashMap<Integer, Product> productList) {
	    try (PrintWriter writer = new PrintWriter("Stock.txt")) {

	        for (Product p : productList.values()) {

	            if (p instanceof BoardGame) {
	                BoardGame bg = (BoardGame) p;

	                writer.printf("%d;%s;%s;%s;%.2f;%d;%.2f;%d%n",
	                        bg.getProductID(), "board game", bg.getGameType(),
	                        bg.getProductName(), bg.getPrice(), bg.getQuantityInStock(),
	                        bg.getPurchaseCost(), bg.getMaxPlayers()
	                );

	            } else if (p instanceof Accessory) {
	                Accessory acc = (Accessory) p;

	                writer.printf("%d;%s;%s;%s;%.2f;%d;%.2f;%s%n",
	                        acc.getProductID(), "accessory", acc.getAccessoryType(),
	                        acc.getProductName(), acc.getPrice(),  acc.getQuantityInStock(),
	                        acc.getPurchaseCost(), acc.getCompatibilityType()
	                );
	            }
	        }

	    } catch (FileNotFoundException e) {
	        System.out.println("File not found");
	    }
	}
	
	//return number of users
	public static int getUserCount() throws FileNotFoundException {
	    ArrayList<String[]> rows = read("UserAccounts.txt");
	    return rows.size();
	}
	// get a new id for products
	public static int getNewProductID() {
	    try {
	        ArrayList<String[]> rows = read("Stock.txt");
	        int maxID = 0;

	        for (String[] row : rows) {
	            if (row.length > 0) {
	                try {
	                    int id = Integer.parseInt(row[0]);
	                    if (id > maxID) {
	                        maxID = id;
	                    }
	                } catch (NumberFormatException e) {
	                    // ignore bad rows
	                }
	            }
	        }

	        return maxID + 1;

	    } catch (FileNotFoundException e) {
	        return 1; // If file missing, start at 1
	    }
	}
	
	// grab index information for a spefic user when loggin in
	public static String[] grabIndexInformation(int index) throws FileNotFoundException{
    	File UserAccounts = new File("UserAccounts.txt");
    	Scanner fileScanner = new Scanner(UserAccounts);
    	
    	try {
    		String[] line = Files.readAllLines(Paths.get("UserAccounts.txt")).get(index-1).split(";");
    		//Create customer object using lines information
    		fileScanner.close();
        	return line;
    	} catch(IOException e) {
    		System.out.println("Invalid object exception");
    	}
    	fileScanner.close();
		return null;
    	
    	
	
	}
}