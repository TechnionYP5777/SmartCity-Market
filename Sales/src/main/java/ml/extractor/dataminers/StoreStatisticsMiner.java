package ml.extractor.dataminers;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import api.contracts.IGroceryList;
import api.contracts.IGroceryPackage;
import api.contracts.IProduct;
import api.preferences.InputPreferences;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProductProperty;

public class StoreStatisticsMiner extends AMiner {

	public StoreStatisticsMiner(InputPreferences inputPreferences, StoreData storeDate,
			IGroceryList currentGrocery, IGroceryPackage currentProduct) {
		super(inputPreferences, storeDate, currentGrocery, currentProduct);
	}

	@Override
	public Set<ABasicProperty> extractProperties() {
		Set<ABasicProperty> result = new HashSet<>();
		
		result.addAll(extractMostPopularProducts());
		
		return result;
	}
	
	/**
	 * this methods generate property of the most popular items
	 * (the number of top popular item declared in {@link MostPopularProductProperty}
	 * @return
	 */
	private Set<? extends ABasicProperty> extractMostPopularProducts() {
		
		Map<? extends IProduct, Long> productsCount = getHistory().stream().flatMap(g -> g.getProductsList().stream())
			.collect(Collectors.groupingBy(g -> ((IGroceryPackage)g).getProduct(), 
					Collectors.counting()));
		
		List<MostPopularProductProperty> ProductsOrederdByPopularity = productsCount.entrySet().stream().sorted(
				new Comparator<Entry<? extends IProduct, Long>>() {

			@Override
			public int compare(Entry<? extends IProduct, Long> arg0, Entry<? extends IProduct, Long> arg1) {
				return Long.compare(arg1.getValue(), arg0.getValue());
			}
		}).map(e -> {
				Entry<? extends IProduct, Long> entry = e;
				return new MostPopularProductProperty(entry.getKey(), entry.getValue());
		})
				.collect(Collectors.toList());

		return new HashSet<>(ProductsOrederdByPopularity.subList(0, MostPopularProductProperty.numOfTop));
		
	}
	
	

}
