package test;

public class PayPalReceipt extends Receipt{
	private String emailAddress;
	
	
	public PayPalReceipt(Double amount, Address billingAddress,String date,
			String emailAddress) {
		super(amount, billingAddress, date);
		this.emailAddress = emailAddress;
	}
	@Override
	public void printReceipt() {
		//"[amount]1 paid via PayPal using [email]2 on [today]4. Billing address: [full address]5"
		System.out.printf("%.2f paid via PayPal using %s on %s. Billing address: %s",
				amount, emailAddress, date, billingAddress.getAddress());
	}
}
