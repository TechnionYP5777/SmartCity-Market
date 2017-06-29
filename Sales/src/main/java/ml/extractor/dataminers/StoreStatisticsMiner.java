package ml.extractor.dataminers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

import api.contracts.IGroceryList;
import api.contracts.IGroceryPackage;
import api.contracts.IManufacturer;
import api.contracts.IProduct;
import api.contracts.IStorePackage;
import api.preferences.InputPreferences;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.basicproperties.storestatistics.AboutToExpireLateStorePackageProperty;
import ml.common.property.basicproperties.storestatistics.AboutToExpireSoonStorePackageProperty;
import ml.common.property.basicproperties.storestatistics.HealthyRatedProductProperty;
import ml.common.property.basicproperties.storestatistics.LastPopularProductProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularManufacturerProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProductProperty;
import testmocks.StorePackageMock;

/**
 * 
 * @author noam yefet
 * @author idan atias
 *
 * @since Jun 11, 2017
 */
public class StoreStatisticsMiner extends AMiner {

	public StoreStatisticsMiner(InputPreferences inputPreferences, StoreData storeData, IGroceryList currentGrocery,
			IGroceryPackage currentProduct) {
		super(inputPreferences, storeData, currentGrocery, currentProduct);
	}

	@Override
	public Set<ABasicProperty> extractProperties() {
		Set<ABasicProperty> result = new HashSet<>();

		result.addAll(extractMostPopularProducts());
		result.addAll(extractLastPopularProducts());
		result.addAll(extractAboutToExpireSoonStorePackages());
		result.addAll(extractAboutToExpireLateStorePackages());
		result.addAll(extractMostPopularManufacturers());
		result.addAll(extractHealthyRatedProducts());
		
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

		return new HashSet<>(ProductsOrederdByPopularity.subList(0, 
				Math.min(MostPopularProductProperty.numOfTop, ProductsOrederdByPopularity.size())));

	}
	
	/**
	 * this methods generate property of the most popular items (the number of
	 * top popular item declared in {@link MostPopularProductProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractLastPopularProducts() {

		Map<? extends IProduct, Long> productsCount = getHistory().stream().flatMap(g -> g.getProductsList().stream())
				.collect(Collectors.groupingBy(g -> ((IGroceryPackage) g).getProduct(), Collectors.counting()));

		List<LastPopularProductProperty> ProductsOrederdByPopularity = productsCount.entrySet().stream()
				.sorted(new Comparator<Entry<? extends IProduct, Long>>() {

					@Override
					public int compare(Entry<? extends IProduct, Long> arg0, Entry<? extends IProduct, Long> arg1) {
						return -Long.compare(arg1.getValue(), arg0.getValue());
					}
				}).map(e -> {
					Entry<? extends IProduct, Long> entry = e;
					return new LastPopularProductProperty(entry.getKey(), entry.getValue());
				}).collect(Collectors.toList());

		return new HashSet<>(ProductsOrederdByPopularity.subList(0, 
				Math.min(LastPopularProductProperty.numOfBottom, ProductsOrederdByPopularity.size())));

	}

	/**
	 * this methods generate property of the products that are about to expire
	 * (the number of "about to expire products" to extract,
	 * as well as the threshold that defines what is "about to be expire" is in {@link AboutToExpireSoonStorePackageProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractAboutToExpireSoonStorePackages() {

		LocalDate currentDate = LocalDate.now();

		List<? extends IStorePackage> aboutToExpireStorePackages = 
				getStock()
					.stream()
					.filter(sp -> ChronoUnit.DAYS.between(currentDate, sp.getExpirationDate()) <= AboutToExpireSoonStorePackageProperty.threshold)
					.collect(Collectors.toList());
		
		List<AboutToExpireSoonStorePackageProperty> storePackagesOrderedByDiff =
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
					return new AboutToExpireSoonStorePackageProperty(storePackage);
					})
					.collect(Collectors.toList());

		return new HashSet<>(storePackagesOrderedByDiff.subList(0, 
				Math.min(AboutToExpireSoonStorePackageProperty.numOfTop, storePackagesOrderedByDiff.size())));
	}
	
	/**
	 * this methods generate property of the products that are about to expire late
	 * (the threshold of "about to expire products late" to extract is in {@link AboutToExpireLateStorePackageProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractAboutToExpireLateStorePackages() {

		LocalDate currentDate = LocalDate.now();

		Set<AboutToExpireLateStorePackageProperty> aboutToExpireStorePackages = 
				getStock()
					.stream()
					.filter(sp -> {
						long periodInDays = ChronoUnit.DAYS.between(currentDate, sp.getExpirationDate());
						return periodInDays >= AboutToExpireLateStorePackageProperty.minDaysThreshold  && 
								periodInDays <= AboutToExpireLateStorePackageProperty.maxDaysThreshold;
					}).map(sp -> new AboutToExpireLateStorePackageProperty(
							ChronoUnit.DAYS.between(currentDate, sp.getExpirationDate()), sp))
					.collect(Collectors.toSet());

		return aboutToExpireStorePackages;
	}
	
	/**
	 * this methods generate property of the most popular manufactures (the number of
	 * top manufactures declared in {@link MostPopularManufacturerProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractMostPopularManufacturers() {

		Map<? extends IManufacturer, Long> manufacturersCount = 
				getHistory()
					.stream()
					.flatMap(gl -> gl.getProductsList().stream())
					.map(gp -> gp.getProduct())
					.collect(Collectors.groupingBy(p -> p.getManufacturer(), Collectors.counting()));

		List<MostPopularManufacturerProperty> manufacturersOrederdByPopularity = manufacturersCount.entrySet().stream()
				.sorted(new Comparator<Entry<? extends IManufacturer, Long>>() {

					@Override
					public int compare(Entry<? extends IManufacturer, Long> arg0, Entry<? extends IManufacturer, Long> arg1) {
						return Long.compare(arg1.getValue(), arg0.getValue());
					}
				}).map(e -> {
					Entry<? extends IManufacturer, Long> entry = e;
					return new MostPopularManufacturerProperty(entry.getKey(), entry.getValue());
				}).collect(Collectors.toList());

		return new HashSet<>(manufacturersOrederdByPopularity.subList(0, 
				Math.min(MostPopularManufacturerProperty.numOfTop, manufacturersOrederdByPopularity.size())));
	}
	
	
	/**
	 * This method returns set of healthy rated products as defined in {@link HealthyRatedProductProperty}
	 *
	 */
	private Set<? extends ABasicProperty> extractHealthyRatedProducts(){
		Set<HealthyRatedProductProperty> healthyRatedProducts = 
				getCatalog()
				.stream()
				.filter(p -> HealthyRatedProductProperty.isProductRatedHealthy(p))
				.map(p -> new HealthyRatedProductProperty(p))
				.collect(Collectors.toSet());
		return healthyRatedProducts;
	}

}
