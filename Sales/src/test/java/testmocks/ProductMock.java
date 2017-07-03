package testmocks;

import java.util.HashSet;
import java.util.Set;

import api.contracts.IIngredient;
import api.contracts.IProduct;

public class ProductMock implements IProduct {

	private static final int NUM_OF_PRODUCTS = 100; 
	long barcode;
	String name;
	ManufacturerMock manufacturer;
	double price;
	Set<IngredientMock> ingredients = new HashSet<>();
	
	public ProductMock(long barcode) {
		this.barcode = barcode;
	}
	
	@Override
	public long getBarcode() {
		return barcode;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ManufacturerMock getManufacturer() {
		return manufacturer;
	}

	@Override
	public double getNormalizeDistanceFrom(IProduct other) {
		return Math.abs(barcode - other.getBarcode()) / NUM_OF_PRODUCTS;
	}

	@Override
	public double getPrice() {
		return price;
	}

	@Override
	public HashSet<? extends IIngredient> getIngredients() {
		return (HashSet<? extends IIngredient>) ingredients;
	}

	public ProductMock setName(String name) {
		this.name = name;
		
		return this;
	}

	public ProductMock setManufacturer(ManufacturerMock m) {
		this.manufacturer = m;
		
		return this;
	}
	
	public ProductMock setManufacturer(String manufacturerName) {
		this.manufacturer = new ManufacturerMock(manufacturerName);
		
		return this;
	}

	public ProductMock setPrice(double price) {
		this.price = price;
		
		return this;
	}

	public ProductMock addIngredients(IngredientMock m) {
		this.ingredients.add(m);
		
		return this;
	}
	
	public ProductMock addIngredients(String ingredientName) {
		this.ingredients.add(new IngredientMock(ingredientName));
		
		return this;
	}

	@Override
	public int hashCode() {
		return (int) (barcode ^ (barcode >>> 32)) + 31;
	}

	@Override
	public boolean equals(Object o) {
		return o == this || (o != null && getClass() == o.getClass() && barcode == ((ProductMock) o).barcode);
	}

}
