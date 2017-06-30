package ml.common.property.saleproperty;


import api.types.sales.ProductSale;
import ml.common.property.AProperty;

public abstract class ASaleProperty extends AProperty {
	public abstract ProductSale getOffer();
}
