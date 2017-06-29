package ml.common.property.saleproperty;

import api.types.basic.sales.ASale;
import ml.common.property.AProperty;

public abstract class ASaleProperty extends AProperty {
	public abstract ASale getOffer();
}
