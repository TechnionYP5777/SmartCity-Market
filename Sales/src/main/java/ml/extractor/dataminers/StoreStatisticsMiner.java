package ml.extractor.dataminers;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import api.contracts.IGroceryPackage;
import api.contracts.IProduct;
import api.preferences.InputPreferences;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProduct;

public class StoreStatisticsMiner extends AMiner {

	public StoreStatisticsMiner(InputPreferences inputPreferences, StoreData<? extends IProduct> storeDate,
			IGroceryPackage<? extends IProduct> purchasedProduct) {
		super(inputPreferences, storeDate, purchasedProduct);
	}

	@Override
	public Set<ABasicProperty> extractProperties() {
		Set<ABasicProperty> result = new HashSet<>();
		
		result.addAll(extractMostPopularProducts());
		
		return result;
	}
	
	/**
	 * this methods generate property of the most popular items
	 * (the number of top popular item declared in {@link MostPopularProduct}
	 * @return
	 */
	private Set<? extends ABasicProperty> extractMostPopularProducts() {
		
		Map<? extends IProduct, Long> productsCount = getHistory().stream().flatMap(g -> g.getProductsList().stream())
			.collect(Collectors.groupingBy(g -> ((IGroceryPackage<? extends IProduct>)g).getProduct(), 
					Collectors.counting()));
		
		List<MostPopularProduct> ProductsOrederdByPopularity = productsCount.entrySet().stream().sorted(new Comparator<Entry<? extends IProduct, Long>>() {

			@Override
			public int compare(Entry<? extends IProduct, Long> arg0, Entry<? extends IProduct, Long> arg1) {
				return Long.compare(arg1.getValue(), arg0.getValue());
			}
		})
				.map(e -> {
				Entry<? extends IProduct, Long> entry = e;
				return new MostPopularProduct(entry.getValue(), entry.getKey());
		})
				.collect(Collectors.toList());

		return new HashSet<>(ProductsOrederdByPopularity.subList(0, MostPopularProduct.numOfTop));
		
	}
	
	

}
