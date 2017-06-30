package ml.deducer.deductionrules;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import api.preferences.SalesPreferences;
import ml.common.property.AProperty;
import ml.common.property.deducedproperties.MustGetRidOfPackageProperty;
import ml.common.property.saleproperty.ProductSaleByMulFactorProperty;

public class IfMustGetRidOf_ThenMulFactorSale extends ADeductionRule {

	@Override
	public Set<? extends AProperty> deduceProperties(SalesPreferences preferences, Set<AProperty> properties) {
		Set<ProductSaleByMulFactorProperty> result = properties.stream()
				.filter(p -> p instanceof MustGetRidOfPackageProperty)
				.map(p -> (MustGetRidOfPackageProperty)p)
				.map((MustGetRidOfPackageProperty p) -> {
					
					return new ProductSaleByMulFactorProperty(p.getStorePackage(), 1, 
							p.getUrgency(), preferences.getMaxDiscount(), this);
				}).collect(Collectors.toSet());
		
		return result;
	}

	@Override
	public boolean canDeduceProperty(AProperty property) {
		return property instanceof ProductSaleByMulFactorProperty;
	}

	@Override
	public Set<AProperty> whatNeedToDeduceProperty(AProperty property) {
		if (!canDeduceProperty(property))
			return null;
		
		ProductSaleByMulFactorProperty actualProperty = (ProductSaleByMulFactorProperty) property;
		
		Set<AProperty> result = new HashSet<>();
		result.add(new MustGetRidOfPackageProperty(actualProperty.getPackageSale(), actualProperty.getMultiplyFactor()));
		
		return result;
	}

}
