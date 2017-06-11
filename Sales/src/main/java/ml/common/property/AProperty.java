package ml.common.property;

import ml.deducer.deductionrules.ADeductionRule;
import ml.deducer.deductionrules.AnyDeductionRule;

public abstract class AProperty {
	
	ADeductionRule deducer;

	public AProperty(ADeductionRule deducer) {
		super();
		this.deducer = deducer;
	}
	
	public AProperty() {
		super();
		this.deducer = new AnyDeductionRule();
	}

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
		if (getClass() != obj.getClass())
			return false;
		AProperty other = (AProperty) obj;
		if (deducer == null) {
			if (other.deducer != null)
				return false;
		} else if (!deducer.equals(other.deducer))
			return false;
		return true;
	}

	public ADeductionRule getDeductionRule() {
		return deducer;
	}
	
	
}
