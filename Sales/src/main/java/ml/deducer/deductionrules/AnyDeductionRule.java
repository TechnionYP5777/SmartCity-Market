package ml.deducer.deductionrules;

import java.util.Set;

import ml.common.property.AProperty;

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
	public Set<AProperty> deduceProperties(Set<AProperty> properties) {
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
