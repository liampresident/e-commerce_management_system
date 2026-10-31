package test;

public abstract class Product {
	
	protected int productID;
	protected ProductCategory productCategory;
	protected String productName;
	protected Double price;
	private int quantityInStock;
	protected Double purchaseCost;
	
	
	
	
	public Product(int productID, ProductCategory productCategory, 
			String productName, double purchaseCost, int quantityInStock, 
			double price) {
		this.productID = productID;
		this.productCategory = productCategory;
		this.productName = productName;
		this.purchaseCost = purchaseCost;
		this.quantityInStock = quantityInStock;
		this.price = price;
	}
	
	public Double getPrice() {
		return price;
	}
	public ProductCategory getProductCategory() {
		return productCategory;
	}
	public int getProductID() {
		return productID;
	}
	public String getProductName() {
		return productName;
	}
	public Double getPurchaseCost() {
		return purchaseCost;
	}
	
	public int getQuantityInStock() {
		return quantityInStock;
	}
	public void setQuantityInStock(int newQuantity) {
		quantityInStock = newQuantity;
		return;
	}
	public String toString() {// 
		return (productID + " " + productName + " " + productCategory
				+ " " + price + " " + quantityInStock + " " + purchaseCost);
	}
}
