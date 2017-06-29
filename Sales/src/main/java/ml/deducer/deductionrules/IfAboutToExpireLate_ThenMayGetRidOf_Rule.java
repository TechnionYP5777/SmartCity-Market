package ml.deducer.deductionrules;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ml.common.property.AProperty;
import ml.common.property.basicproperties.storestatistics.AboutToExpireLateStorePackageProperty;
import ml.common.property.deducedproperties.MayGetRidOfPackageProperty;

public class IfAboutToExpireLate_ThenMayGetRidOf_Rule extends ADeductionRule {

	@Override
	public Set<? extends AProperty> deduceProperties(Set<AProperty> properties) {
		Set<MayGetRidOfPackageProperty> result = properties.stream()
				.filter(p -> p instanceof AboutToExpireLateStorePackageProperty)
				.map(p -> (AboutToExpireLateStorePackageProperty)p)
				.map((AboutToExpireLateStorePackageProperty p) -> {
					
					return new MayGetRidOfPackageProperty(p.getStorePackage(), difftoUrgency(p.getDiff()), this);
				}).collect(Collectors.toSet());
		
		return result;
	}

	@Override
	public boolean canDeduceProperty(AProperty property) {

		return property instanceof MayGetRidOfPackageProperty;
	}

	@Override
	public Set<AProperty> whatNeedToDeduceProperty(AProperty property) {
		if (!canDeduceProperty(property))
			return null;
		
		MayGetRidOfPackageProperty actualProperty = (MayGetRidOfPackageProperty) property;
		
		Set<AProperty> result = new HashSet<>();
		result.add(new AboutToExpireLateStorePackageProperty(actualProperty.getStorePackage()));
		
		return result;
	}
	
	private static double difftoUrgency(double diff){
		double urgency = diff / AboutToExpireLateStorePackageProperty.maxDaysThreshold;
		urgency = 1 - urgency;
		return urgency;
	}

}
