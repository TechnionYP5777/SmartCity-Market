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
		return 1 - price / (amount * product.getPrice());
	}
	
	public static ProductSale makeSaleByDiscount(IProduct p, int amount, double discount){
		return new ProductSale(p, amount, amount * (1 - discount) * p.getPrice());
		
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + amount;
		long temp = Double.doubleToLongBits(price);
		return result = prime * (prime * result + (int) (temp ^ (temp >>> 32)))
				+ ((product == null) ? 0 : product.hashCode());
	}


	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductSale other = (ProductSale) o;
		if (amount != other.amount || Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

}
