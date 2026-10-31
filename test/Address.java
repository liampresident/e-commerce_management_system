package test;
// address object as defined in the java docs
public class Address {
	String Address;
	
	public Address(String houseNumber, String postcode, String city) {
		this.Address = String.format("%s %s %s", houseNumber,postcode, city);
	}
	
	public String getAddress() {
		return Address;
	}
}
