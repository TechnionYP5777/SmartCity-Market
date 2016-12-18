package BasicCommonClasses;
/*
 * @author Lior Ben Ami
 */

import java.awt.image.BufferedImage;
import java.util.HashSet;


/** CatalogProduct - The info of a product of the market's catalog. 
 * @param barcode - Is the unique key.
 * @param locations - A product may exists in different locations.  
 * @author Lior Ben Ami
 * @since 2016-12-09 */
public class CatalogProduct {
	long barcode;
	String name;
	HashSet<Ingredient> ingredients;
	Manufacturer manufacturer;
	String description;
	double price;
	BufferedImage image;
	HashSet<Location> locations;
	
	public CatalogProduct(long barcode, String name, HashSet<Ingredient> ingredients, Manufacturer manufacturer,
			String description, double price, BufferedImage image,HashSet<Location> locations) {
		this.barcode = barcode;
		this.name = name;
		this.ingredients = ingredients;
		this.manufacturer = manufacturer;
		this.description = description;
		this.price = price;
		this.image = image;
		this.locations = locations;
	}

	public long getBarcode() {
		return barcode;
	}

	public void setBarcode(long barcode) {
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashSet<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(HashSet<Ingredient> ¢) {
		this.ingredients = ¢;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer ¢) {
		this.manufacturer = ¢;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage ¢) {
		this.image = ¢;
	}

	public HashSet<Location> getLocations() {
		return locations;
	}

	public void setLocations(HashSet<Location> ¢) {
		this.locations = ¢;
	}
	
	public void addIngredient(Ingredient ¢) {
		if (ingredients == null)
			ingredients = new HashSet<Ingredient>();
		ingredients.add(¢);
	}
	
	public void removeIngredient(Ingredient ¢) {
		if (ingredients != null)
			ingredients.remove(¢);
	}
	
	public void addLocation(Location ¢) {
		if (locations == null)
			locations = new HashSet<Location>();
		locations.add(¢);
	}
	
	public void addLocation (int passage, int column, PlaceInMarket m) {
		if (locations == null)
			locations = new HashSet<Location>();
		locations.add(new Location(passage, column, m));
	}
	
	public void removeLocation(Location ¢) {
		if (locations != null)
			locations.remove(¢);
	}
	
	public void removeLocation (int passage, int column, PlaceInMarket m) {
		if (locations != null)
			locations.remove(new Location(passage, column, m));
	}

	@Override
	public int hashCode() {
		return (int) (barcode ^ (barcode >>> 32)) + 31;
	}

	@Override
	public boolean equals(Object ¢) {
		return ¢ == this || (¢ != null && getClass() == ¢.getClass() && barcode == ((CatalogProduct) ¢).barcode);
	}	
}
