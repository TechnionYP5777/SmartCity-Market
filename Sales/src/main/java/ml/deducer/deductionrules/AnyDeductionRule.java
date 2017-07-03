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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		return (obj instanceof ADeductionRule);
	}

	@Override
	public Set<? extends AProperty> deduceProperties(SalesPreferences preferences, Set<AProperty> properties) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean canDeduceProperty(AProperty property) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<AProperty> whatNeedToDeduceProperty(AProperty property) {
		throw new UnsupportedOperationException();
	}
}
