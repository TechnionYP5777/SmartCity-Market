package BasicCommonClasses;

import org.joda.time.LocalDate;

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
	LocalDate startTime;
	LocalDate endTime;
	SaleType type;
	
	public SaleType getType() {
		return type;
	}

	public void setType(SaleType type) {
		this.type = type;
	}

	public LocalDate getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDate startTime) {
		this.startTime = startTime;
	}

	public LocalDate getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDate endTime) {
		this.endTime = endTime;
	}

	public enum SaleType {
		OnePlusOneDiscount,
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
