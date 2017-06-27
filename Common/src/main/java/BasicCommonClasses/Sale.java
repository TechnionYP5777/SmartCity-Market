package BasicCommonClasses;

/**
 * Sale - This class represents a sale.
 * 
 * @author Aviad Cohen
 * @since 2017-06-07
 */
public class Sale {
	
	Integer id;
	Long productBarcode;
	Integer amountOfProducts;
	Double price;
	
	public Sale(Integer id, Long productBarcode, Integer amountOfProducts, Double price) {
		this.id = id;
		this.productBarcode = productBarcode;
		this.amountOfProducts = amountOfProducts;
		this.price = price;
	}
	
	public Sale() {
		id = -1;
		productBarcode = Long.valueOf(0);
		amountOfProducts = 0;
		price = (double) 0;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Long getProductBarcode() {
		return productBarcode;
	}

	public void setProductBarcode(Long productBarcode) {
		this.productBarcode = productBarcode;
	}

	public Integer getAmountOfProducts() {
		return amountOfProducts;
	}

	public void setAmountOfProducts(Integer amountOfProducts) {
		this.amountOfProducts = amountOfProducts;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public String getSaleAsString() {
		return id == -1 ? "No sale avaiable for this item"
				: "Great Sale: Buy " + amountOfProducts + " In " + price + " NIS";
	}
}
