package ml.deducer.deductionrules;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import api.preferences.SalesPreferences;
import ml.common.property.AProperty;
import ml.common.property.deducedproperties.MustGetRidOfPackageProperty;
import ml.common.property.saleproperty.ProductSaleByMulFactorProperty;

/**
 * This class represents the rule: if we must get rid of product (because exp. date) then suggest sale
 *  
 * @author noam
 *
 */
public class IfMustGetRidOf_ThenMulFactorSale extends ADeductionRule {

	@Override
	public Set<? extends AProperty> deduceProperties(SalesPreferences preferences, Set<AProperty> ps) {
		return ps.stream().filter(p -> p instanceof MustGetRidOfPackageProperty).map(p -> (MustGetRidOfPackageProperty) p)
				.map((MustGetRidOfPackageProperty p) -> new ProductSaleByMulFactorProperty(p.getStorePackage(), 1,
						p.getUrgency(), preferences.getMaxDiscount(), this)).collect(Collectors.toSet());
	}

	@Override
	public boolean canDeduceProperty(AProperty p) {
		return p instanceof ProductSaleByMulFactorProperty;
	}

	@Override
	public Set<AProperty> whatNeedToDeduceProperty(AProperty p) {
		if (!canDeduceProperty(p))
			return null;
		
		ProductSaleByMulFactorProperty actualProperty = (ProductSaleByMulFactorProperty) p;
		
		Set<AProperty> result = new HashSet<>();
		result.add(new MustGetRidOfPackageProperty(actualProperty.getPackageSale(), actualProperty.getMultiplyFactor()));
		
		return result;
	}

}
