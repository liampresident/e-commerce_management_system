package test;

public abstract class Receipt implements PaymentMethod {
	protected Double amount;
	protected Address billingAddress;
	protected String date;
	
	public Receipt(Double amount, Address billingAddress,String date) {
		this.amount = amount;
		this.billingAddress = billingAddress;
		this.date = date;
	}
	
	protected void printReceipt() { // Gets overrided by subclasses
	}
	
	
}
