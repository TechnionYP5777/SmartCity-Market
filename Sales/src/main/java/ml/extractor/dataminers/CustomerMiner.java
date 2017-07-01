package ml.extractor.dataminers;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import api.contracts.IGroceryList;
import api.contracts.IGroceryPackage;
import api.contracts.IProduct;
import api.preferences.InputPreferences;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.basicproperties.storestatistics.LastPopularProductOfCustomerProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProductOfCustomerProperty;

public class CustomerMiner extends AMiner {

	public CustomerMiner(InputPreferences inputPreferences, StoreData storeData, IGroceryList currentGrocery,
			IGroceryPackage currentProduct) {
		super(inputPreferences, storeData, currentGrocery, currentProduct);
	}
	
	@Override
	public Set<ABasicProperty> extractProperties() {
		Set<ABasicProperty> result = new HashSet<>();

		result.addAll(extractMostPopularCustomerProducts());
		result.addAll(extractLastPopularCustomerProducts());
//		result.addAll(extractAboutToExpireSoonStorePackages());
//		result.addAll(extractAboutToExpireLateStorePackages());
//		result.addAll(extractHighRatioAmountExpirationTime());
//		result.addAll(extractMostPopularManufacturers());
//		result.addAll(extractNumberOfBuyersPerMonth());
//		result.addAll(extractSumOfPurcahsesPerMonth());
//		result.addAll(extractHealthyRatedProducts());
		
		return result;
	}
	
	/**
	 * this methods generate property of the most popular items of the customer (the number of
	 * top popular item declared in {@link MostPopularProductOfCustomerProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractMostPopularCustomerProducts() {

		Map<? extends IProduct, Long> productsCount = getHistory().stream()
				.filter(g -> currentGrocery.getBuyer().equals(g.getBuyer()))
				.flatMap(g -> g.getProductsList().stream())
				.collect(Collectors.groupingBy(g -> ((IGroceryPackage) g).getProduct(), Collectors.counting()));

		List<MostPopularProductOfCustomerProperty> ProductsOrederdByPopularity = productsCount.entrySet().stream()
				.sorted(new Comparator<Entry<? extends IProduct, Long>>() {

					@Override
					public int compare(Entry<? extends IProduct, Long> arg0, Entry<? extends IProduct, Long> arg1) {
						return Long.compare(arg1.getValue(), arg0.getValue());
					}
				}).map(e -> {
					Entry<? extends IProduct, Long> entry = e;
					return new MostPopularProductOfCustomerProperty(entry.getKey(), entry.getValue());
				}).collect(Collectors.toList());

		return new HashSet<>(ProductsOrederdByPopularity.subList(0, 
				Math.min(MostPopularProductOfCustomerProperty.numOfTop, ProductsOrederdByPopularity.size())));

	}
	
	/**
	 * this methods generate property of the last popular items of the customer (the number of
	 * last popular item declared in {@link LastPopularProductOfCustomerProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractLastPopularCustomerProducts() {

		Map<? extends IProduct, Long> productsCount = getHistory().stream()
				.filter(g -> currentGrocery.getBuyer().equals(g.getBuyer()))
				.flatMap(g -> g.getProductsList().stream())
				.collect(Collectors.groupingBy(g -> ((IGroceryPackage) g).getProduct(), Collectors.counting()));

		List<LastPopularProductOfCustomerProperty> ProductsOrederdByPopularity = productsCount.entrySet().stream()
				.sorted(new Comparator<Entry<? extends IProduct, Long>>() {

					@Override
					public int compare(Entry<? extends IProduct, Long> arg0, Entry<? extends IProduct, Long> arg1) {
						return -Long.compare(arg1.getValue(), arg0.getValue());
					}
				}).map(e -> {
					Entry<? extends IProduct, Long> entry = e;
					return new LastPopularProductOfCustomerProperty(entry.getKey(), entry.getValue());
				}).collect(Collectors.toList());

		return new HashSet<>(ProductsOrederdByPopularity.subList(0, 
				Math.min(LastPopularProductOfCustomerProperty.numOfBottom, ProductsOrederdByPopularity.size())));

	}

}
