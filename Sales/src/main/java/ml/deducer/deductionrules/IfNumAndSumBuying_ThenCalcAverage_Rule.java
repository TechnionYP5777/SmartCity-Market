package ml.deducer.deductionrules;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


import api.preferences.SalesPreferences;
import ml.common.property.AProperty;
import ml.common.property.basicproperties.storestatistics.NumOfBuyersPerMonthProperty;
import ml.common.property.basicproperties.storestatistics.SumOfPurchasesPerMonthProperty;
import ml.common.property.deducedproperties.AverageCartPricePerMonthProperty;

public class IfNumAndSumBuying_ThenCalcAverage_Rule extends ADeductionRule {

	
	@Override
	public Set<? extends AProperty> deduceProperties(SalesPreferences preferences, Set<AProperty> properties) {

		Map<Integer, Integer> buyersCount =  properties.stream()
				.filter(p -> p instanceof NumOfBuyersPerMonthProperty)
				.map(p -> (NumOfBuyersPerMonthProperty) p)
				.collect(Collectors.groupingBy((NumOfBuyersPerMonthProperty p) ->  p.getMonthAgo(),
						Collectors.summingInt((NumOfBuyersPerMonthProperty p) ->  p.getNumOfBuyers())));
		
		Map<Integer, Double> buyersSum =  properties.stream()
				.filter(p -> p instanceof SumOfPurchasesPerMonthProperty)
				.map(p -> (SumOfPurchasesPerMonthProperty) p)
				.collect(Collectors.groupingBy((SumOfPurchasesPerMonthProperty p) ->  p.getMonthAgo(),
						Collectors.summingDouble((SumOfPurchasesPerMonthProperty p) ->  p.getSumOfPurchases())));
		
		Set<AverageCartPricePerMonthProperty> result = new HashSet<>();
		
		for (int i = 0; i < AverageCartPricePerMonthProperty.goMonthesBackLimit; i++)
			result.add(new AverageCartPricePerMonthProperty(i,
					!buyersCount.containsKey(i) || !buyersSum.containsKey(i) ||
					buyersCount.get(i) == 0 ? 0 : buyersSum.get(i)/buyersCount.get(i)));
		
		return result;
	}
	
	@Override
	public boolean canDeduceProperty(AProperty property) {
		return property instanceof AverageCartPricePerMonthProperty;
	}

	@Override
	public Set<AProperty> whatNeedToDeduceProperty(AProperty property) {
		if (!canDeduceProperty(property))
			return null;
		
		AverageCartPricePerMonthProperty actualProperty = (AverageCartPricePerMonthProperty) property;
		
		Set<AProperty> result = new HashSet<>();
		result.add(new NumOfBuyersPerMonthProperty(actualProperty.getMonthAgo(), 0));
		result.add(new SumOfPurchasesPerMonthProperty(actualProperty.getMonthAgo(), 0));
		
		return result;
	}

}
