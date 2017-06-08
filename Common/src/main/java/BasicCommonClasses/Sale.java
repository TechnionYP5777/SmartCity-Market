package BasicCommonClasses;

/**
 * Sale - This class represents a sale.
 * 
 * @author Aviad Cohen
 * @since 2017-06-07
 */
public class Sale {
	Integer id;
	CatalogProduct product;
	Double discount;
	
	public enum SaleType {
		OnePlusOneDiscount,
		FixedDiscount,
		PercentageDiscount,
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CatalogProduct getProduct() {
		return product;
	}

	public void setProduct(CatalogProduct product) {
		this.product = product;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
}
