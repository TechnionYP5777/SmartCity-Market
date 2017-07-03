package ml.common.property.saleproperty;

import api.contracts.IStorePackage;
import api.types.sales.ProductSale;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class represent sale conclusion
 * 
 * @author noam
 * 
 */
public class ProductSaleByMulFactorProperty extends ASaleProperty {

	
	ProductSale sale;
	double multiplyFactor;
	IStorePackage packageSale;


	public ProductSaleByMulFactorProperty(IStorePackage pack, int amount, double discountfactor,
			double maxDiscount, ADeductionRule deducer) {
		super(deducer);
		this.sale = ProductSale.makeSaleByDiscount(pack.getProduct(), amount, maxDiscount * discountfactor);
		packageSale = pack;
		multiplyFactor = discountfactor;
	}
	
	public ProductSaleByMulFactorProperty(IStorePackage pack, int amount, double discountfactor,
			double maxDiscount) {
		super();
		this.sale = ProductSale.makeSaleByDiscount(pack.getProduct(), amount, maxDiscount * discountfactor);
		packageSale = pack;
		multiplyFactor = discountfactor;
	}

	public double getMultiplyFactor() {
		return multiplyFactor;
	}
	
	public IStorePackage getPackageSale() {
		return packageSale;
	}

	@Override
	public ProductSale getOffer() {
		return sale;
	}
	
	@Override
	public String getDescription() {
		return "Sale for product: " + sale.getProduct().getName() + " (barcode: " + sale.getProduct().getBarcode() + ")" +
				" with amount of: " + sale.getTotalAmount() +
				" in price of: " + sale.getTotalPrice() + " (discount of: " + ((int)(sale.getdiscount()*100)) + "%)";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((sale == null) ? 0 : sale.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductSaleByMulFactorProperty other = (ProductSaleByMulFactorProperty) obj;
		if (sale == null) {
			if (other.sale != null)
				return false;
		} else if (!sale.equals(other.sale))
			return false;
		return true;
	}

}
