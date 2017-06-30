package ml.common.property.saleproperty;

import api.contracts.IStorePackage;
import api.types.sales.ProductSale;

public class ProductSaleByMulFactorProperty extends ASaleProperty {

	
	ProductSale sale;
	double multiplyFactor;
	IStorePackage packageSale;


	public ProductSaleByMulFactorProperty(IStorePackage pack, int amount, double discountfactor, double maxDiscount) {
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

}
