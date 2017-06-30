package api.contracts;

public interface ISale {

	IProduct getProduct();

	double getTotalPrice();

	int getTotalAmount();

}