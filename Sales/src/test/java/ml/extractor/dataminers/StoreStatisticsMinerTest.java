package ml.extractor.dataminers;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import ml.common.property.basicproperties.storestatistics.NumOfBuyersPerMonthProperty;
import ml.common.property.basicproperties.storestatistics.SumOfPurchasesPerMonthProperty;
import testmocks.DBMock;
import testmocks.GroceryListMock;
import testmocks.GroceryPackageMock;
import testmocks.HistoryMockBuilder;
import testmocks.StockMockBuilder;
import testmocks.StorePackageMock;

import static ml.utils.CollectionFunctions.findInCollection;

/**
 * @author noam yefet
 * @author idan atias
 *
 * @since Jun 11, 2017
 */
public class StoreStatisticsMinerTest {

	private static final double DELTA = 1e-15;

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
		
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), DBMock.getStoreDataByStock(stock),
				new GroceryListMock("alice"), new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long totalAboutToExpireLatePackages = result.stream()
				.filter(p -> p instanceof AboutToExpireLateStorePackageProperty).count();

		assertEquals(1, totalAboutToExpireLatePackages);
		assertTrue(result.contains(new AboutToExpireLateStorePackageProperty(packageAboutToExpiredLate)));

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
		
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), DBMock.getStoreDataByStock(stock),
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
		
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), DBMock.getStoreDataByStock(stock),
				new GroceryListMock("alice"), new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long totalAboutToExpireLatePackages = result.stream()
				.filter(p -> p instanceof HighRatioAmountExpirationTimeProperty).count();

		assertEquals(1, totalAboutToExpireLatePackages);
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
	public void testNumberOfBuyersPerMonthProperty(){
		
		List<GroceryListMock> history = new HistoryMockBuilder().addGrocery("alice", 
				LocalDate.now(), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("bob", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(2), DBMock.getProduct(1), 1).build();
				
		
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), DBMock.getStoreDateByHistory(history),
				new GroceryListMock("alice"), new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();
		
		long numOfBuyerPerMonthProperties = result.stream()
				.filter(p -> p instanceof NumOfBuyersPerMonthProperty).count();

		assertEquals(6, numOfBuyerPerMonthProperties);
		
		NumOfBuyersPerMonthProperty foundMonth[] = new NumOfBuyersPerMonthProperty[NumOfBuyersPerMonthProperty.goMonthesBackLimit];
		NumOfBuyersPerMonthProperty realMonth0 = new NumOfBuyersPerMonthProperty(0, 1);
		NumOfBuyersPerMonthProperty realMonth1 = new NumOfBuyersPerMonthProperty(1, 2);
		NumOfBuyersPerMonthProperty realMonth2 = new NumOfBuyersPerMonthProperty(2, 1);
		foundMonth[0] = findInCollection(result, realMonth0);
		foundMonth[1] = findInCollection(result, realMonth1);
		foundMonth[2] = findInCollection(result, realMonth2);
		assertNotNull(foundMonth[0]);
		assertNotNull(foundMonth[1]);
		assertNotNull(foundMonth[2]);
		assertEquals(realMonth0.getNumOfBuyers(), foundMonth[0].getNumOfBuyers());
		assertEquals(realMonth1.getNumOfBuyers(), foundMonth[1].getNumOfBuyers());
		assertEquals(realMonth2.getNumOfBuyers(), foundMonth[2].getNumOfBuyers());
		for (int i=3; i<NumOfBuyersPerMonthProperty.goMonthesBackLimit; i++){
			NumOfBuyersPerMonthProperty temp = findInCollection(result, new NumOfBuyersPerMonthProperty(i, 0));
			assertNotNull(temp);
			assertEquals(temp.getNumOfBuyers(), 0);
		}
	}
	
	@Test
	public void testSumOfPurchasesPerMonthProperty(){
		
		List<GroceryListMock> history = new HistoryMockBuilder().addGrocery("alice", 
				LocalDate.now(), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("bob", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(2), DBMock.getProduct(2), 1).build();
				
		
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), DBMock.getStoreDateByHistory(history),
				new GroceryListMock("alice"), new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();
		
		long numOfBuyerPerMonthProperties = result.stream()
				.filter(p -> p instanceof SumOfPurchasesPerMonthProperty).count();
		
		List<SumOfPurchasesPerMonthProperty> r = result.stream().filter(p -> p instanceof SumOfPurchasesPerMonthProperty)
				.map(p -> (SumOfPurchasesPerMonthProperty) p).collect(Collectors.toList());
		
		assertEquals(SumOfPurchasesPerMonthProperty.goMonthesBackLimit, numOfBuyerPerMonthProperties);
		
		SumOfPurchasesPerMonthProperty foundMonth[] = new SumOfPurchasesPerMonthProperty[SumOfPurchasesPerMonthProperty.goMonthesBackLimit];
		SumOfPurchasesPerMonthProperty realMonth0 = new SumOfPurchasesPerMonthProperty(0, 1.5);
		SumOfPurchasesPerMonthProperty realMonth1 = new SumOfPurchasesPerMonthProperty(1, 3);
		SumOfPurchasesPerMonthProperty realMonth2 = new SumOfPurchasesPerMonthProperty(2, 2.5);
		foundMonth[0] = findInCollection(result, realMonth0);
		foundMonth[1] = findInCollection(result, realMonth1);
		foundMonth[2] = findInCollection(result, realMonth2);
		assertNotNull(foundMonth[0]);
		assertNotNull(foundMonth[1]);
		assertNotNull(foundMonth[2]);
		assertEquals(realMonth0.getSumOfPurchases(), foundMonth[0].getSumOfPurchases(), DELTA);
		assertEquals(realMonth1.getSumOfPurchases(), foundMonth[1].getSumOfPurchases(), DELTA);
		assertEquals(realMonth2.getSumOfPurchases(), foundMonth[2].getSumOfPurchases(), DELTA);
		for (int i=3; i<NumOfBuyersPerMonthProperty.goMonthesBackLimit; i++){
			SumOfPurchasesPerMonthProperty temp = findInCollection(result, new SumOfPurchasesPerMonthProperty(i, 0));
			assertNotNull(temp);
			assertEquals(temp.getSumOfPurchases(), 0, DELTA);
		}
	}
	
	//@Test
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

	@Test
	public void testStoreStatisticsMiner() {
		new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				new GroceryPackageMock(DBMock.getProduct(1)));
	}
	
	

}
