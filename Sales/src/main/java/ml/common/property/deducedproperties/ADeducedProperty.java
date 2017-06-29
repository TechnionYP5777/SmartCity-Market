package ml.common.property.deducedproperties;

import ml.common.property.AProperty;
import ml.deducer.deductionrules.ADeductionRule;

public abstract class ADeducedProperty extends AProperty {

	public ADeducedProperty(ADeductionRule deducer) {
		super(deducer);
	}
	
	public ADeducedProperty() {
		super();
	}
}
