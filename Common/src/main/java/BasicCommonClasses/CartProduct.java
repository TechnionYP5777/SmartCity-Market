package BasicCommonClasses;
/**
 * @author Lior Ben Ami
 * @since 
 */

import java.time.LocalDate;

/** CartProduct - The product from the Cart (/costumer) perspective. 
 * @param amount - The number of items of the same product in the cart.
 * unique keys: only catalogProduct and expirationDate!
 * @author Lior Ben Ami
 * @since 2016-12-09 */
public class CartProduct {
	CatalogProduct catalogProduct;
	LocalDate expirationDate;
	int amount;
	
	public CartProduct(CatalogProduct catalogProduct, LocalDate expirationDate, 
			int amount) {
		this.catalogProduct = catalogProduct;
		this.expirationDate = expirationDate;
		this.amount = amount;
	}

	public CatalogProduct getCatalogProduct() {
		return catalogProduct;
	}

	public void setCatalogProduct(CatalogProduct ¢) {
		this.catalogProduct = ¢;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void incrementAmount(int ¢) {
		amount += ¢;
	}
	
	@Override
	public int hashCode() {
		return 31 * (((catalogProduct == null) ? 0 : catalogProduct.hashCode()) + 31)
				+ ((expirationDate == null) ? 0 : expirationDate.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CartProduct other = (CartProduct) o;
		if (catalogProduct == null) {
			if (other.catalogProduct != null)
				return false;
		} else if (!catalogProduct.equals(other.catalogProduct))
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		return true;
	}
}