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
		return "Sale for product: " + sale.getProduct().getName() + " (barcode: " + sale.getProduct().getBarcode() + ")"
				+ " with amount of: " + sale.getTotalAmount() + " in price of: " + sale.getTotalPrice()
				+ " (discount of: " + (int) (100 * sale.getdiscount()) + "%)";
	}

	@Override
	public int hashCode() {
		return 31 * super.hashCode() + ((sale == null) ? 0 : sale.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!super.equals(o) || getClass() != o.getClass())
			return false;
		ProductSaleByMulFactorProperty other = (ProductSaleByMulFactorProperty) o;
		if (sale == null) {
			if (other.sale != null)
				return false;
		} else if (!sale.equals(other.sale))
			return false;
		return true;
	}

}
