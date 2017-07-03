package ml.deducer.deductionrules;

import java.util.Set;

import api.preferences.SalesPreferences;
import ml.common.property.AProperty;

/**
 * This class used for comparing properties deduced by different deduction rule
 * 
 * @author noam
 * 
 */
public class AnyDeductionRule extends ADeductionRule {

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		return o == this || (o != null && o instanceof ADeductionRule);
	}

	@Override
	public Set<? extends AProperty> deduceProperties(SalesPreferences p, Set<AProperty> ps) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean canDeduceProperty(AProperty p) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<AProperty> whatNeedToDeduceProperty(AProperty p) {
		throw new UnsupportedOperationException();
	}
}
