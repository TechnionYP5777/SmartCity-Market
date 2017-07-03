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
	public Set<? extends AProperty> deduceProperties(SalesPreferences preferences, Set<AProperty> ps) {
		Set<AProperty> result = new HashSet<>();
		
		Set<MostPopularProductOfCustomerProperty> customer = ps.stream()
				.filter(p -> p instanceof MostPopularProductOfCustomerProperty)
				.map(p -> (MostPopularProductOfCustomerProperty)p)
				.collect(Collectors.toSet());
		
		Set<MayGetRidOfPackageProperty> resultAll = ps.stream()
				.filter(p -> p instanceof MayGetRidOfPackageProperty)
				.map(p -> (MayGetRidOfPackageProperty)p)
				.collect(Collectors.toSet());
		
		
		for (MostPopularProductOfCustomerProperty propery : customer) 
			for (MayGetRidOfPackageProperty mayProperty : resultAll)
				if (mayProperty.getStorePackage().getProduct().equals(propery.getProduct())){
					AverageAmountOfProductForCustomerProperty amountProperty = CollectionFunctions.findInCollection(ps,
							new AverageAmountOfProductForCustomerProperty(propery.getProduct(), 0));
					
					result.add(new ProductSaleByMulFactorProperty(mayProperty.getStorePackage(),
							(int) Math.ceil((amountProperty == null) ? 1 : amountProperty.getAverageAmount()),
							0.25 * mayProperty.getUrgency(), preferences.getMaxDiscount(), this));
				}
	
		return result;
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
		
		result.add(new MostPopularProductOfCustomerProperty(actualProperty.getPackageSale().getProduct(), 0));
		result.add(new MayGetRidOfPackageProperty(actualProperty.getPackageSale(), 0));
		
		return result;
	}

}
