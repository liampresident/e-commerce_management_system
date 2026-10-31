package test;

import java.time.LocalDate;
import java.util.Scanner;

public class PaymentSystem implements PaymentMethod{
    
	public static Receipt processPayment(Scanner scanner, Customer customer) {
	    int paymentType = CLI.promptInt(scanner, "Enter Payment Type:\n1) Credit Card\n2) PayPal", 2);
	    if (paymentType == -1) {
	        System.out.println("Cancelled.");
	        return null;
	    }

	    String date = String.valueOf(LocalDate.now());
	    double total = customer.getCartTotal();
	    Address billingAddress = new Address(customer.getHouseNumber(), customer.getPostcode(),
	            customer.getCity());

	    switch (paymentType) {

	        case 1:
	            // Credit Card
	        	String cardNumber = CLI.promptString(scanner, "Enter Card Number");
	        	if (cardNumber == null) {
	        	    System.out.println("Cancelled.");
	        	    return null;
	        	}
	        	// must be digits only
	        	if (cardNumber.matches("\\d+") == false) {
	        	    System.out.println("Invalid card number. Digits only.");
	        	    return null;
	        	}
	        	// an must be 16 digits
	        	if (cardNumber.length() != 6) {
	        	    System.out.println("Invalid card number. Must be 6 digits.");
	        	    return null;
	        	}

	        
	            int securityCode = CLI.promptInt(scanner, "Enter Security Code", -1);
	            if (securityCode == -1) {
	                System.out.println("Cancelled.");
	                return null;
	            }

	            // must be 3 digits
	            String secStr = String.valueOf(securityCode);
	            if (secStr.length() != 3) {
	                System.out.println("Invalid security code. Must be 3 digits.");
	                return null;
	            }

	            CreditCardReceipt CCR = new CreditCardReceipt(total, billingAddress, date,
	                    cardNumber, securityCode);

	            return CCR;

	        case 2:
	            // PayPal
	            String email = CLI.promptString(scanner, "Enter email: ");
	            if (email == null) {
	                System.out.println("Cancelled.");
	                return null;
	            }

	            // Validate email format
	            if (email.contains("@") == false || email.contains(".com") == false) {
	                System.out.println("Invalid email. Must contain '@' and end with '.com'.");
	                return null;
	            }

	            PayPalReceipt PPR = new PayPalReceipt(total, billingAddress, date,
	                    email);

	            return PPR;
	    }

	    return null;
	}

}

