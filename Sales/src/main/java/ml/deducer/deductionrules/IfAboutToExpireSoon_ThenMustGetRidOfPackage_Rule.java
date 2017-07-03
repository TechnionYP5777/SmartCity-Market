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
	public Set<? extends AProperty> deduceProperties(SalesPreferences preferences, Set<AProperty> ps) {
		
		return ps.stream().filter(p -> p instanceof AboutToExpireSoonStorePackageProperty)
				.map(p -> (AboutToExpireSoonStorePackageProperty) p).map((AboutToExpireSoonStorePackageProperty p) -> new MustGetRidOfPackageProperty(p.getStorePackage(),
						difftoUrgency(p.getDiff()), this)).collect(Collectors.toSet());
	}

	@Override
	public boolean canDeduceProperty(AProperty p) {
		return p instanceof MustGetRidOfPackageProperty;
	}

	@Override
	public Set<AProperty> whatNeedToDeduceProperty(AProperty p) {
		if (!canDeduceProperty(p))
			return null;
		
		MustGetRidOfPackageProperty actualProperty = (MustGetRidOfPackageProperty) p;
		
		Set<AProperty> result = new HashSet<>();
		result.add(new AboutToExpireSoonStorePackageProperty(actualProperty.getStorePackage()));
		
		return result;
	}
	
	private static double difftoUrgency(double diff){
		return 1 - diff / AboutToExpireSoonStorePackageProperty.threshold;
	}

}
