package ml.common.property.deducedproperties;

import ml.common.property.AProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class the the abstract of each property that deduced (by deduction rule)
 * 
 * @author noam
 * 
 */
public abstract class ADeducedProperty extends AProperty {

	public ADeducedProperty(ADeductionRule deducer) {
		super(deducer);
	}
	
	public ADeducedProperty() {
	}
}
