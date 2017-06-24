package testmocks;

import java.util.HashSet;
import java.util.Set;

import api.contracts.IProduct;

public class ProductMock implements IProduct {

	final private static int NUM_OF_PRODUCTS = 100; 
	long barcode;
	String name;
	ManufacturerMock manufacturer;
	double price;
	Set<IngredientMock> ingredients = new HashSet<>();
	
	public ProductMock(long barcode) {
		super();
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
	public Set<IngredientMock> getIngredients() {
		return ingredients;
	}

	public ProductMock setName(String name) {
		this.name = name;
		
		return this;
	}

	public ProductMock setManufacturer(ManufacturerMock manufacturer) {
		this.manufacturer = manufacturer;
		
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

	public ProductMock addIngredients(IngredientMock ingredient) {
		this.ingredients.add(ingredient);
		
		return this;
	}
	
	public ProductMock addIngredients(String ingredientName) {
		this.ingredients.add(new IngredientMock(ingredientName));
		
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (barcode ^ (barcode >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductMock other = (ProductMock) obj;
		if (barcode != other.barcode)
			return false;
		return true;
	}

}
