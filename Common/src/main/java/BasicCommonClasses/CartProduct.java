package BasicCommonClasses;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;


/** CartProduct - The product from the Cart (/costumer) perspective. 
 * @author Lior Ben Ami
 * @since 2016-12-09 */
public class CartProduct {
	CatalogProduct catalogProduct;
	HashMap<SmartCode,ProductPackage> packages;
	int totalAmount;
	
	public CartProduct(CatalogProduct catalogProduct, HashMap<SmartCode,ProductPackage> packages,
			int totalAmount) {
		this.catalogProduct = catalogProduct;
		this.packages = packages != null ? packages : new HashMap<SmartCode,ProductPackage>();
		this.totalAmount = totalAmount;
	}

	public CatalogProduct getCatalogProduct() {
		return catalogProduct;
	}

	public void setCatalogProduct(CatalogProduct ¢) {
		this.catalogProduct = ¢;
	}

	public HashMap<SmartCode,ProductPackage> getPackages() {
		return packages;
	}

	public void setPackages(HashMap<SmartCode,ProductPackage> ¢) {
		this.packages = ¢;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	/**
	 * @param productPackage
	 */
	public void addProductPackage(ProductPackage productPackage) {
		SmartCode sc = productPackage.getSmartCode();
		ProductPackage inCartProductPackage = packages.get(sc);
		if ( inCartProductPackage != null) {
			inCartProductPackage.incrementAmount(productPackage.getAmount());
		}
		packages.put(sc, productPackage);
		totalAmount += productPackage.getAmount();
	}
	
	public Boolean removeProductPackage(ProductPackage productPackage) {
		SmartCode sc = productPackage.getSmartCode();
		ProductPackage inCartProductPackage = packages.get(sc);
		if (inCartProductPackage == null || inCartProductPackage.getAmount() < productPackage.getAmount())
			return false;
		int prevAmount = inCartProductPackage.getAmount();
		inCartProductPackage.setAmount(prevAmount - productPackage.getAmount());
		totalAmount -= productPackage.getAmount();
		return true;
	}
	
	public double getTotalSum() {
		return totalAmount * catalogProduct.getPrice();
	}
	
	@Override public String toString(){
		return catalogProduct.name + "		" + catalogProduct.price;
	}
}