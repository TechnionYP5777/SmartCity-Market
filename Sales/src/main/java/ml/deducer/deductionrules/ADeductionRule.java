package ml.deducer.deductionrules;

import java.util.Set;

import api.preferences.SalesPreferences;
import ml.common.property.AProperty;

/**
 * This is abstract of any Deduction rule
 * 
 * @author noam
 * 
 */
public abstract class ADeductionRule {

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		return o == this || (o != null && (o.getClass() == AnyDeductionRule.class || getClass() == o.getClass()));
	}
	
	public abstract Set<? extends AProperty> deduceProperties(SalesPreferences p, Set<AProperty> ps);
	
	public abstract boolean canDeduceProperty(AProperty p);
	
	public abstract Set<AProperty> whatNeedToDeduceProperty(AProperty p);
	
}
	
