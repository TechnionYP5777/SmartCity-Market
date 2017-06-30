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
	
	public static ISale makeSaleByDiscount(IProduct product, int amount, double discount){
		return new ProductSale(product, amount, product.getPrice() * (1 - discount));
		
	}

}
