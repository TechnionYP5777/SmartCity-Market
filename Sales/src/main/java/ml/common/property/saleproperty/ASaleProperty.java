package ml.common.property.saleproperty;


import api.types.sales.ProductSale;
import ml.common.property.AProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class is abstarct of sale property
 * 
 * @author noam
 * 
 */
public abstract class ASaleProperty extends AProperty {
	
	
	public ASaleProperty(ADeductionRule deducer) {
		super(deducer);
	}
	
	public ASaleProperty() {
		super();
	}
	
	public abstract ProductSale getOffer();
}
