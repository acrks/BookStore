import java.text.DecimalFormat;

/*
 * @author: Team 4
 * CIS 36B - Book Store Project
 */

public abstract class Items {
    String pattern = "###,###,###.##";
	DecimalFormat df = new DecimalFormat(pattern);
	private String name;
	private String itemID;
	private double price;
	
	public Items() {
		this("Item unknown", "Item ID unknown", 0.0);
	}
	
	public Items(String name, String itemID, double price) {
		this.name = name;
		this.itemID = itemID;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	
	public String getItemID() {
		return itemID;
	}
	
	public double getPrice() {
		return price;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public double getUsedPrice() {
		return price * 0.75;
	}
	
	@Override public String toString() {
		return "\nItem name: " + name
				+"\nItem ID: " + itemID
				+ "\nPrice: $" + df.format(price);
	}
}

