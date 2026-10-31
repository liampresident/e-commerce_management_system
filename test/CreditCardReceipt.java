package test;

public class CreditCardReceipt extends Receipt {
    private String cardNumber;
    private int securityCode;

    public CreditCardReceipt(Double amount, Address billingAddress, String date,
                             String cardNumber, int securityCode) {
        super(amount, billingAddress, date);
        this.cardNumber = cardNumber;
        this.securityCode = securityCode;
    }
    // change card number so it prints  *********xxxx
    private String maskCardNumber(String number) {
        String digits = number;

        String last4 = digits.substring(digits.length() - 4);
        String cardNumber = "*".repeat(digits.length() - 4) + last4;

        return cardNumber;
    }


    @Override
    //[amount] paid by Credit Card [card number] on [today]. Billing address: [full address]

    public void printReceipt() {
        System.out.printf("%.2f paid by Credit Card %s on %s. Billing address: %s\n",
                amount,
                maskCardNumber(cardNumber),
                date,
                billingAddress.getAddress());
    }
}
