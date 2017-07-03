package ml.deducer.deductionrules;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import api.preferences.SalesPreferences;
import ml.common.property.AProperty;
import ml.common.property.basicproperties.storestatistics.AboutToExpireLateStorePackageProperty;
import ml.common.property.deducedproperties.MayGetRidOfPackageProperty;

/**
 * This class represents the rule: if About to expire but later, then tag this product as "may get rid of"
 *  
 * @author noam
 *
 */
public class IfAboutToExpireLate_ThenMayGetRidOf_Rule extends ADeductionRule {

	@Override
	public Set<? extends AProperty> deduceProperties(SalesPreferences preferences, Set<AProperty> ps) {
		return ps.stream().filter(p -> p instanceof AboutToExpireLateStorePackageProperty)
				.map(p -> (AboutToExpireLateStorePackageProperty) p).map((AboutToExpireLateStorePackageProperty p) -> new MayGetRidOfPackageProperty(p.getStorePackage(),
						difftoUrgency(p.getDiff()), this)).collect(Collectors.toSet());
	}

	@Override
	public boolean canDeduceProperty(AProperty p) {

		return p instanceof MayGetRidOfPackageProperty;
	}

	@Override
	public Set<AProperty> whatNeedToDeduceProperty(AProperty p) {
		if (!canDeduceProperty(p))
			return null;
		
		MayGetRidOfPackageProperty actualProperty = (MayGetRidOfPackageProperty) p;
		
		Set<AProperty> result = new HashSet<>();
		result.add(new AboutToExpireLateStorePackageProperty(actualProperty.getStorePackage()));
		
		return result;
	}
	
	private static double difftoUrgency(double diff){
		return 1 - diff / AboutToExpireLateStorePackageProperty.maxDaysThreshold;
	}

}
