package ml.deducer.deductionrules;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import api.preferences.SalesPreferences;
import ml.common.property.AProperty;
import ml.common.property.basicproperties.storestatistics.AboutToExpireSoonStorePackageProperty;
import ml.common.property.deducedproperties.MustGetRidOfPackageProperty;

/**
 * This class represents the rule: if About to expire but soon, then tag this product as "must get rid of"
 * 
 * @author noam
 *
 */
public class IfAboutToExpireSoon_ThenMustGetRidOfPackage_Rule extends ADeductionRule {

	@Override
	public Set<? extends AProperty> deduceProperties(SalesPreferences preferences, Set<AProperty> properties) {
		
		Set<MustGetRidOfPackageProperty> result = properties.stream()
				.filter(p -> p instanceof AboutToExpireSoonStorePackageProperty)
				.map(p -> (AboutToExpireSoonStorePackageProperty)p)
				.map((AboutToExpireSoonStorePackageProperty p) -> {
					
					return new MustGetRidOfPackageProperty(p.getStorePackage(), difftoUrgency(p.getDiff()), this);
				}).collect(Collectors.toSet());
		
		return result;
	}

	@Override
	public boolean canDeduceProperty(AProperty property) {
		return property instanceof MustGetRidOfPackageProperty;
	}

	@Override
	public Set<AProperty> whatNeedToDeduceProperty(AProperty property) {
		if (!canDeduceProperty(property))
			return null;
		
		MustGetRidOfPackageProperty actualProperty = (MustGetRidOfPackageProperty) property;
		
		Set<AProperty> result = new HashSet<>();
		result.add(new AboutToExpireSoonStorePackageProperty(actualProperty.getStorePackage()));
		
		return result;
	}
	
	private static double difftoUrgency(double diff){
		double urgency = diff / AboutToExpireSoonStorePackageProperty.threshold;
		urgency = 1 - urgency;
		return urgency;
	}

}
