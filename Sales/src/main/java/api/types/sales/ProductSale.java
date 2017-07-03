package api.types.sales;

import java.time.LocalDate;

import api.contracts.IProduct;
import api.contracts.ISale;

public class ProductSale implements ISale {

	
	IProduct product;
	int amount;
	double price;
	LocalDate date;

	
	public ProductSale(IProduct product, int amount, double price) {
		super();
		date = LocalDate.now();
		this.product = product;
		this.amount = amount;
		this.price = price;
	}


	/* (non-Javadoc)
	 * @see api.types.sales.ISale#getProduct()
	 */
	@Override
	public IProduct getProduct(){
		return product;
	}

	public LocalDate getDate() {
		return date;
	}

	
	/* (non-Javadoc)
	 * @see api.types.sales.ISale#getTotalPrice()
	 */
	@Override
	public double getTotalPrice() {
		return price;
	}


	
	/* (non-Javadoc)
	 * @see api.types.sales.ISale#getTotalAmount()
	 */
	@Override
	public int getTotalAmount() {
		return amount;
	}
	
	public double getdiscount() {
		return 1 - (price / (amount * product.getPrice()));
	}
	
	public static ProductSale makeSaleByDiscount(IProduct product, int amount, double discount){
		return new ProductSale(product, amount, amount * product.getPrice() * (1 - discount));
		
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		ProductSale other = (ProductSale) obj;
		if (amount != other.amount)
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

}
