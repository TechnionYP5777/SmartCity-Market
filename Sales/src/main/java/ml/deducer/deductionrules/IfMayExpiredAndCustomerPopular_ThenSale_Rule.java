package ml.deducer.deductionrules;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import api.preferences.SalesPreferences;
import ml.common.property.AProperty;
import ml.common.property.basicproperties.storestatistics.AverageAmountOfProductForCustomerProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProductOfCustomerProperty;
import ml.common.property.deducedproperties.MayGetRidOfPackageProperty;
import ml.common.property.saleproperty.ProductSaleByMulFactorProperty;
import ml.utils.CollectionFunctions;

/**
 * This class represents the rule: if product is close to expire, and its popular for the customer
 * then suggest sale
 *  
 * @author noam
 *
 */
public class IfMayExpiredAndCustomerPopular_ThenSale_Rule extends ADeductionRule {

	@Override
	public Set<? extends AProperty> deduceProperties(SalesPreferences preferences, Set<AProperty> properties) {
		Set<AProperty> result = new HashSet<>();
		
		Set<MostPopularProductOfCustomerProperty> customer = properties.stream()
				.filter(p -> p instanceof MostPopularProductOfCustomerProperty)
				.map(p -> (MostPopularProductOfCustomerProperty)p)
				.collect(Collectors.toSet());
		
		Set<MayGetRidOfPackageProperty> resultAll = properties.stream()
				.filter(p -> p instanceof MayGetRidOfPackageProperty)
				.map(p -> (MayGetRidOfPackageProperty)p)
				.collect(Collectors.toSet());
		
		
		for (MostPopularProductOfCustomerProperty propery : customer) 
			for (MayGetRidOfPackageProperty mayProperty : resultAll)
				if (mayProperty.getStorePackage().getProduct().equals(propery.getProduct())){
					AverageAmountOfProductForCustomerProperty amountProperty = CollectionFunctions.findInCollection(properties,
							new AverageAmountOfProductForCustomerProperty(propery.getProduct(), 0));
					
					int amount = (int) Math.ceil((amountProperty == null) ? 1 : amountProperty.getAverageAmount());
					
					result.add(new ProductSaleByMulFactorProperty(mayProperty.getStorePackage(), amount, 
							mayProperty.getUrgency() * 0.25, preferences.getMaxDiscount(), this));
				}
	
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
		
		result.add(new MostPopularProductOfCustomerProperty(actualProperty.getPackageSale().getProduct(), 0));
		result.add(new MayGetRidOfPackageProperty(actualProperty.getPackageSale(), 0));
		
		return result;
	}

}
