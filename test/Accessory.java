package test;

public class Accessory extends Product {
	private String compatabilityType;
	private AccessoryType accessoryType;
	public Accessory(int productID, ProductCategory productCategory,
			AccessoryType accessoryType, String productName, double purchaseCost, 
			int quantityInStock,double price, String compatabilityType) {
		
		super(productID,  productCategory, productName,  purchaseCost, 
				 quantityInStock, price);
		this.compatabilityType = compatabilityType;
		this.accessoryType = accessoryType;
	}
	public String getAccessoryType() {
		return accessoryType.toString();
	}
	public String getCompatibilityType() {
		return compatabilityType;
	}
	public void changePrice(Double newPrice) {
		this.price = newPrice;
	}
} 
//product id; product category; type; product name; price; stock quantity; purchase cost;additional info