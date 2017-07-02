package BasicCommonClasses;

import java.util.HashMap;
import java.util.HashSet;

import api.contracts.IIngredient;
import api.contracts.IManufacturer;
import api.contracts.IProduct;

/**
 * CartProduct - The product from the Cart (/costumer) perspective.
 * 
 * @author Lior Ben Ami
 * @since 2016-12-09
 */
public class CartProduct implements IProduct {
	CatalogProduct catalogProduct;
	HashMap<SmartCode, ProductPackage> packages;
	int totalAmount;

	public CartProduct(CatalogProduct catalogProduct, HashMap<SmartCode, ProductPackage> packages, int totalAmount) {
		this.catalogProduct = catalogProduct;
		this.packages = packages != null ? packages : new HashMap<SmartCode, ProductPackage>();
		this.totalAmount = totalAmount;
	}

	public CatalogProduct getCatalogProduct() {
		return catalogProduct;
	}

	public void setCatalogProduct(CatalogProduct ¢) {
		this.catalogProduct = ¢;
	}

	public HashMap<SmartCode, ProductPackage> getPackages() {
		return packages;
	}

	public void setPackages(HashMap<SmartCode, ProductPackage> ¢) {
		this.packages = ¢;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void addProductPackage(ProductPackage p) {
		SmartCode sc = p.getSmartCode();
		ProductPackage inCartProductPackage = packages.get(sc);
		if (inCartProductPackage == null)
			packages.put(sc, p);
		else {
			inCartProductPackage.incrementAmount(p.getAmount());
			packages.put(sc, inCartProductPackage);
		}
		totalAmount += p.getAmount();
	}

	public Boolean removeProductPackage(ProductPackage p) {
		SmartCode smartCode = p.getSmartCode();
		ProductPackage inCartProductPackage = packages.get(smartCode);
		if (inCartProductPackage == null || inCartProductPackage.getAmount() < p.getAmount())
			return false;
		int prevAmount = inCartProductPackage.getAmount();
		inCartProductPackage.setAmount(prevAmount - p.getAmount());
		if (inCartProductPackage.getAmount() == 0)
			packages.remove(smartCode);
		else
			packages.put(smartCode, inCartProductPackage);
		totalAmount -= p.getAmount();
		return true;
	}

	public double getTotalSum() {
		return totalAmount * catalogProduct.getPrice();
	}

	@Override
	public String toString() {
		return catalogProduct.name + "		" + catalogProduct.price;
	}

	@Override
	public long getBarcode() {
		return catalogProduct.getBarcode();
	}

	@Override
	public String getName() {
		return catalogProduct.getName();
	}

	@Override
	public IManufacturer getManufacturer() {
		return catalogProduct.getManufacturer();
	}

	@Override
	public double getNormalizeDistanceFrom(IProduct other) {
		return 0;
	}

	@Override
	public double getPrice() {
		return catalogProduct.getPrice();
	}

	@Override
	public HashSet<? extends IIngredient> getIngredients() {
		return catalogProduct.getIngredients();
	}

}