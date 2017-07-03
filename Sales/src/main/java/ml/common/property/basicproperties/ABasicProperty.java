package ml.common.property.basicproperties;

import ml.common.property.AProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * This class is abstract for all the properties extracted using extractor (and not deducer)
 *  
 * @author noam
 *
 */
public abstract class ABasicProperty extends AProperty {

	public ABasicProperty(ADeductionRule deducer) {
		super(deducer);
	}
	
	public ABasicProperty() {
	}
}
