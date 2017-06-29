package ml.extractor.dataminers;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import api.contracts.IStorePackage;
import api.types.Place;
import api.types.StoreData;
import ml.common.basiccalsses.CombinedStorePackage;
import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.basicproperties.storestatistics.AboutToExpireLateStorePackageProperty;
import ml.common.property.basicproperties.storestatistics.AboutToExpireSoonStorePackageProperty;
import ml.common.property.basicproperties.storestatistics.HealthyRatedProductProperty;
import ml.common.property.basicproperties.storestatistics.HighRatioAmountExpirationTimeProperty;
import ml.common.property.basicproperties.storestatistics.LastPopularProductProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularManufacturerProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProductProperty;
import testmocks.DBMock;
import testmocks.GroceryListMock;
import testmocks.GroceryPackageMock;
import testmocks.ProductMock;
import testmocks.StockMockBuilder;
import testmocks.StorePackageMock;

/**
 * @author noam yefet
 * @author idan atias
 *
 * @since Jun 11, 2017
 */
public class StoreStatisticsMinerTest {

	private static List<GroceryListMock> history = new ArrayList<>();
	private static List<StorePackageMock> stock = new ArrayList<>();
	private static StoreData sd = new StoreData(history, stock, DBMock.getCatalog());

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// test50MostPopularProperty & testMostPopularManufacturersProperty
		for (long i = 1; i <= DBMock.NUM_OF_PRODUCTS; i++)
			history.add(new GroceryListMock("alice").addProdcut(DBMock.getProduct(i)));

		for (long i = 1; i <= MostPopularProductProperty.numOfTop; i++)
			history.add(new GroceryListMock("bob").addProdcut(DBMock.getProduct(i)));

		// testAboutToExpireProperty
		for (long i = 1; i <= DBMock.NUM_OF_PRODUCTS; i++) {
			if (i <= DBMock.NUM_OF_PRODUCTS - AboutToExpireSoonStorePackageProperty.numOfTop)
				stock.add(new StorePackageMock(i, LocalDate.MAX)); // far E.D
			else
				stock.add(new StorePackageMock(i, LocalDate.now())); // close E.D
		}

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test50MostPopularProductsProperty() {
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long numOfRightAmount = result.stream().filter(
				p -> p instanceof MostPopularProductProperty && ((MostPopularProductProperty) p).getAmount() == 2)
				.count();

		for (long i = 1; i <= MostPopularProductProperty.numOfTop; i++)
			assertTrue(result.contains(new MostPopularProductProperty(DBMock.getProduct(i), 2)));

		assertEquals(MostPopularProductProperty.numOfTop, numOfRightAmount);

	}
	
	@Test
	public void test50LastPopularProductsProperty() {
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long numOfRightAmount = result.stream().filter(
				p -> p instanceof MostPopularProductProperty && ((MostPopularProductProperty) p).getAmount() == 2)
				.count();

		for (long i = 1; i <= LastPopularProductProperty.numOfBottom; i++)
			assertTrue(result.contains(new LastPopularProductProperty(DBMock.getProduct(50 +i), 1)));

		assertEquals(LastPopularProductProperty.numOfBottom, numOfRightAmount);

	}

	@Test
	public void testAboutToExpireSoonProperty() {
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long totalAboutToExpireStorePackages = result.stream()
				.filter(p -> p instanceof AboutToExpireSoonStorePackageProperty).count();

		assertEquals(AboutToExpireSoonStorePackageProperty.numOfTop, totalAboutToExpireStorePackages);
		
		for (IStorePackage spm : stock) {
		    long currSpmBarcode = spm.getProduct().getBarcode();
			 if (currSpmBarcode <= DBMock.NUM_OF_PRODUCTS - AboutToExpireSoonStorePackageProperty.numOfTop)
				 assertFalse(result.contains(new AboutToExpireSoonStorePackageProperty(spm)));
			 else
				 assertTrue(result.contains(new AboutToExpireSoonStorePackageProperty(spm)));
		}

	}
	
	@Test
	public void testAboutToExpireLateProperty() {
		
		StorePackageMock packageAboutToExpiredLate = new StorePackageMock(
				2, LocalDate.now().plusDays(AboutToExpireLateStorePackageProperty.minDaysThreshold + 5));
		long diffOfProduct = ChronoUnit.DAYS.between(LocalDate.now(), packageAboutToExpiredLate.getExpirationDate());
		
		List<StorePackageMock> stock = new StockMockBuilder()
				.addPackage(1, LocalDate.now().plusDays(2))
				.addPackage(packageAboutToExpiredLate)
				.addPackage(3, LocalDate.now().plusDays(AboutToExpireLateStorePackageProperty.maxDaysThreshold + 5))
				.build();
		
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), DBMock.getStoreDateByStock(stock),
				new GroceryListMock("alice"), new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long totalAboutToExpireLatePackages = result.stream()
				.filter(p -> p instanceof AboutToExpireLateStorePackageProperty).count();

		assertEquals(1, totalAboutToExpireLatePackages);
		assertTrue(result.contains(new AboutToExpireLateStorePackageProperty(diffOfProduct, packageAboutToExpiredLate)));

	}
	
	@Test
	public void testHighRatioAmountExpirationProperty() {
		
		StorePackageMock highRatioPackage = new StorePackageMock(2,
				5, LocalDate.now().plusDays(5), Place.STORE);
		double ratio = (double) highRatioPackage.getAmount() /
				ChronoUnit.DAYS.between(LocalDate.now(),highRatioPackage.getExpirationDate());
		
		List<StorePackageMock> stock = new StockMockBuilder()
				.addPackage(1, LocalDate.now().plusDays(10))
				.addPackage(highRatioPackage)
				.build();
		
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), DBMock.getStoreDateByStock(stock),
				new GroceryListMock("alice"), new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long totalAboutToExpireLatePackages = result.stream()
				.filter(p -> p instanceof HighRatioAmountExpirationTimeProperty).count();

		assertEquals(1, totalAboutToExpireLatePackages);
		assertTrue(result.contains(new HighRatioAmountExpirationTimeProperty(ratio,
				new CombinedStorePackage(highRatioPackage))));

	}
	
	@Test
	public void testHighRatioCombinePackages() {
		
		StorePackageMock highRatioStorePackage = new StorePackageMock(2,
				5, LocalDate.now().plusDays(5), Place.STORE);
		StorePackageMock highRatioWarehousePackage = new StorePackageMock(2,
				5, LocalDate.now().plusDays(5), Place.WAREHOUSE);
		double ratio = ((double) highRatioStorePackage.getAmount() + highRatioWarehousePackage.getAmount()) /
				ChronoUnit.DAYS.between(LocalDate.now(),highRatioStorePackage.getExpirationDate());
		
		List<StorePackageMock> stock = new StockMockBuilder()
				.addPackage(1, LocalDate.now().plusDays(10))
				.addPackage(highRatioStorePackage)
				.addPackage(highRatioWarehousePackage)
				.build();
		
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), DBMock.getStoreDateByStock(stock),
				new GroceryListMock("alice"), new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long totalAboutToExpireLatePackages = result.stream()
				.filter(p -> p instanceof HighRatioAmountExpirationTimeProperty).count();

		assertEquals(1, totalAboutToExpireLatePackages);
		for (ABasicProperty aBasicProperty : result) {
			if (aBasicProperty instanceof HighRatioAmountExpirationTimeProperty){
				int am;
				am = ((HighRatioAmountExpirationTimeProperty)aBasicProperty).getCombinedPackage().getAmount();
				aBasicProperty.equals(new HighRatioAmountExpirationTimeProperty(ratio,
				new CombinedStorePackage(highRatioStorePackage.getProduct(), highRatioStorePackage.getExpirationDate(),
						10)));
			}
			
		}
		assertTrue(result.contains(new HighRatioAmountExpirationTimeProperty(ratio,
				new CombinedStorePackage(highRatioStorePackage.getProduct(), highRatioStorePackage.getExpirationDate(),
						10))));

	}
	
	@Test
	public void testMostPopularManufacturersProperty() {
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();
		
		int expectedAmountOfMostPopularManufacturer = 3;
		long numOfRightAmount = result.stream().filter(
				p -> p instanceof MostPopularManufacturerProperty && ((MostPopularManufacturerProperty) p).getAmount() == expectedAmountOfMostPopularManufacturer)
				.count();

		assertEquals(MostPopularManufacturerProperty.numOfTop, numOfRightAmount);
		
		for (long i = 1; i <= numOfRightAmount; i++)
			assertTrue(result.contains(new MostPopularManufacturerProperty(DBMock.getProduct(i).getManufacturer(), expectedAmountOfMostPopularManufacturer)));
	}
	
	@Test
	public void testHealthyRatedProductProperty(){
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();
		
		int expectedAmountOfHealthyRatedProductProperties = (int) (DBMock.NUM_OF_PRODUCTS * 0.8);
		long numOfHealthyRatedProductProperties = 
				result
				.stream()
				.filter(p -> p instanceof HealthyRatedProductProperty)
				.count();

		assertEquals(expectedAmountOfHealthyRatedProductProperties, numOfHealthyRatedProductProperties);
	}

	@SuppressWarnings("unused")
	@Test
	public void testStoreStatisticsMiner() {
		new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				new GroceryPackageMock(DBMock.getProduct(1)));
	}

}
