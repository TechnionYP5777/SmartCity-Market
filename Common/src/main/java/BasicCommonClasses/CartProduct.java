package BasicCommonClasses;

import java.time.LocalDate;
import java.util.HashSet;

/** CartProduct - The product from the Cart (/costumer) perspective. 
 * @param amount - The number of items of the same product in the cart.
 * unique keys: only catalogProduct and expirationDate!
 * @author Lior Ben Ami
 * @since 2016-12-09 */
public class CartProduct {
	CatalogProduct catalogProduct;
	HashSet<ProductPackage> packages;
	int totalAmount;
	
	public CartProduct(CatalogProduct catalogProduct, HashSet<ProductPackage> packages,
			int totalAmount) {
		this.catalogProduct = catalogProduct;
		this.packages =packages;
		this.totalAmount = totalAmount;
	}

	public CatalogProduct getCatalogProduct() {
		return catalogProduct;
	}

	public void setCatalogProduct(CatalogProduct ¢) {
		this.catalogProduct = ¢;
	}

	public HashSet<ProductPackage> getPackages() {
		return packages;
	}

	public void setPackages(HashSet<ProductPackage> ¢) {
		this.packages = ¢;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
}