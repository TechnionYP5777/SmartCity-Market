package api.contracts;

public interface IGroceryPackage<P extends IProduct> {
	P getProduct();
	
	int getAmount();
}
