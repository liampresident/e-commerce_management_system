package test;

public interface PaymentMethod {
	
	public default Receipt processPayment(double total, Address address) {
		return null;
	}
}
