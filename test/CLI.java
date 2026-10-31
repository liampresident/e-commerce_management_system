package test;

import java.util.Scanner;

public abstract class CLI {
    
    // get an integer from the user
    public static int promptInt(Scanner scanner, String message, int max) {
        while (true) {
            System.out.println(message);
            System.out.println("0) for exit");
            String input = scanner.nextLine().trim();

            // exit
            if (input.equals("0")) {
                return -1;
            }

            // parse and validate
            try {
                int number = Integer.parseInt(input);
                if (number < 0) {
                    System.out.println("Invalid number. Please enter a valid positive value.");
                    continue;
                }
                if (max == -1) {
                    return number; // any positive integer
                } else if (number <= max) {
                    return number; // number within cap
                } else {
                    System.out.println("Invalid number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid positive value.");
            }
        }
    }
    
 // get a double from the user
    public static double promptDouble(Scanner scanner, String message, double max) {
        while (true) {
            System.out.println(message);
            System.out.println("0) for exit");
            String input = scanner.nextLine().trim();

            // exit
            if (input.equals("0")) {
                return -1;
            }

            // parse and validate
            try {
                double number = Double.parseDouble(input);
                if (number < 0) {
                    System.out.println("Invalid number. Please enter a valid positive value.");
                    continue;
                }
                if (max == -1 && number >= 0) {
                    return number;
                }
                if (number <= max) {
                    return number;
                } else {
                    System.out.println("Invalid number. ");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid positive value.");
            }
        }
    }

 // get a String from the user
    public static String promptString(Scanner scanner, String message) {
        while (true) {
            System.out.println(message);
            System.out.println("0) for exit");
            String input = scanner.nextLine();
            if (input == null) {
                System.out.println("Invalid input. Please try again.");
                continue;
            }
            String trimmed = input.trim();
            if (trimmed.equals("0")) {
                return null;
            }
            if (trimmed.isEmpty()) {
                System.out.println("Invalid input. Please try again.");
                continue;
            }
            return trimmed;
        }
    }

    // get a postcode from the user
    public static String promptPostcode(Scanner scanner, String message) {
        while (true) {
            System.out.println(message);
            System.out.println("0) for exit");
            String input = scanner.nextLine();
            if (input == null) {
                System.out.println("Invalid postcode. Example formats: LE11 3AB, SW1A 1AA");
                continue;
            }
            String normalized = input.trim().toUpperCase();
            if (normalized.equals("0")) {
                return null;
            }
            if (postcodeIsValid(normalized)) {
                return normalized;
            }
            System.out.println("Invalid postcode. Example formats: LE11 3AB, SW1A 1AA");
        }
    }
    // check a string to see if its a valid postcode
    private static boolean postcodeIsValid(String postcode) {
        return postcode.matches("^[A-Z]{1,2}[0-9][0-9A-Z]? ?[0-9][A-Z]{2}$");
    }

    // get a ProductCategory from the user
    public static ProductCategory promptCategory(Scanner scanner, String message) {
        ProductCategory category = null;

        while (category == null) {
            int cat = promptInt(scanner, message, 2);
            if (cat == -1) {
                return null;
            }
            if (cat == 1) {
                category = ProductCategory.BOARDGAME;
            } else if (cat == 2) {
                category = ProductCategory.ACCESSORY;
            } else {
                System.out.println("Invalid category. Please choose 1 or 2.");
            }
        }
        return category;
    }
}
