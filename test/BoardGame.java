package test;

public class BoardGame extends Product {
	private String gameType;
	private int maxPlayers;
	public BoardGame(int productID, ProductCategory productCategory,
			String gameType, String productName, double purchaseCost,
			int quantityInStock, double price, int maxPlayers) {
		
		super(productID,  productCategory, productName,  purchaseCost, 
				 quantityInStock, price);
		this.gameType = gameType;
		this.maxPlayers = maxPlayers;
	}
	// optional change price method
	public void changePrice(Double newPrice) {
		this.price = newPrice;
	}
	
	// getter methods
	public String getGameType() {
		return gameType;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
}
	
