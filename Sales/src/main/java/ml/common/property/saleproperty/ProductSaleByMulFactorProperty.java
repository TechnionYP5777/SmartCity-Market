package ml.common.property.saleproperty;

import api.contracts.IStorePackage;
import api.types.sales.ProductSale;
import ml.deducer.deductionrules.ADeductionRule;

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
				" in price of: " + sale.getTotalPrice() + " (discount of: " + ((int)(sale.getdiscount()*100)) + ")";
	}

}
