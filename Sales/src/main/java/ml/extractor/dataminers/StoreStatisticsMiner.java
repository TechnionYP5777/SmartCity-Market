package ml.extractor.dataminers;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.Period;
import java.time.LocalDate;

import api.contracts.IGroceryList;
import api.contracts.IGroceryPackage;
import api.contracts.IProduct;
import api.contracts.IStorePackage;
import api.preferences.InputPreferences;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.basicproperties.storestatistics.AboutToExpireStorePackageProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProductProperty;

/**
 * 
 * @author noam yefet
 * @author idan atias
 *
 * @since Jun 11, 2017
 */
public class StoreStatisticsMiner extends AMiner {

	public StoreStatisticsMiner(InputPreferences inputPreferences, StoreData storeDate, IGroceryList currentGrocery,
			IGroceryPackage currentProduct) {
		super(inputPreferences, storeDate, currentGrocery, currentProduct);
	}

	@Override
	public Set<ABasicProperty> extractProperties() {
		Set<ABasicProperty> result = new HashSet<>();

		result.addAll(extractMostPopularProducts());
		result.addAll(extractAboutToExpireStorePackages());

		return result;
	}

	/**
	 * this methods generate property of the most popular items (the number of
	 * top popular item declared in {@link MostPopularProductProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractMostPopularProducts() {

		Map<? extends IProduct, Long> productsCount = getHistory().stream().flatMap(g -> g.getProductsList().stream())
				.collect(Collectors.groupingBy(g -> ((IGroceryPackage) g).getProduct(), Collectors.counting()));

		List<MostPopularProductProperty> ProductsOrederdByPopularity = productsCount.entrySet().stream()
				.sorted(new Comparator<Entry<? extends IProduct, Long>>() {

					@Override
					public int compare(Entry<? extends IProduct, Long> arg0, Entry<? extends IProduct, Long> arg1) {
						return Long.compare(arg1.getValue(), arg0.getValue());
					}
				}).map(e -> {
					Entry<? extends IProduct, Long> entry = e;
					return new MostPopularProductProperty(entry.getKey(), entry.getValue());
				}).collect(Collectors.toList());

		return new HashSet<>(ProductsOrederdByPopularity.subList(0, MostPopularProductProperty.numOfTop));

	}

	/**
	 * this methods generate property of the products that are about to expire
	 * (the number of "about to expire products" to extract,
	 * as well as the threshold that defines what is "about to be expire" is in {@link AboutToExpireStorePackageProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractAboutToExpireStorePackages() {

		LocalDate currentDate = LocalDate.now();

		List<? extends IStorePackage> aboutToExpireStorePackages = 
				getStock()
					.stream()
					.filter(sp -> Period.between(currentDate, sp.getExpirationDate()).getDays() <= AboutToExpireStorePackageProperty.threshold)
					.collect(Collectors.toList());
		
		List<AboutToExpireStorePackageProperty> storePackagesOrderedByDiff =
				aboutToExpireStorePackages
					.stream()
					.sorted(new Comparator<IStorePackage>() {

						@Override
						public int compare(IStorePackage arg0, IStorePackage arg1) {
							LocalDate arg0ed = arg0.getExpirationDate();
							LocalDate arg1ed = arg1.getExpirationDate();
							if (arg0ed.isAfter(arg1ed))
								return 1;
							if (arg0ed.isBefore(arg1ed))
								return -1;
							return 0;
						}
					})
					.map(sp -> {
					IStorePackage storePackage = sp;
					return new AboutToExpireStorePackageProperty(storePackage);
					})
					.collect(Collectors.toList());

		return new HashSet<>(storePackagesOrderedByDiff.subList(0, AboutToExpireStorePackageProperty.numOfTop));
	}

}
