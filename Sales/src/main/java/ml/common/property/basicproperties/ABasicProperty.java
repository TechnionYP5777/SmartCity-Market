package ml.common.property.basicproperties;

import ml.common.property.AProperty;
import ml.deducer.deductionrules.ADeductionRule;

public abstract class ABasicProperty extends AProperty {

	public ABasicProperty(ADeductionRule deducer) {
		super(deducer);
	}
	
	public ABasicProperty() {
		super();
	}
}
