package ml.extractor.dataminers;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import BasicCommonClasses.PlaceInMarket;
import api.contracts.IStorePackage;
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
		for (long i = 1; i <= DBMock.NUM_OF_PRODUCTS; ++i)
			history.add(new GroceryListMock("alice").addProdcut(DBMock.getProduct(i)));

		for (long i = 1; i <= MostPopularProductProperty.numOfTop; ++i)
			history.add(new GroceryListMock("bob").addProdcut(DBMock.getProduct(i)));

		// testAboutToExpireProperty
		for (long i = 1; i <= DBMock.NUM_OF_PRODUCTS; ++i)
			stock.add(new StorePackageMock(i, i <= DBMock.NUM_OF_PRODUCTS - AboutToExpireSoonStorePackageProperty.numOfTop
					? LocalDate.MAX : LocalDate.now()));

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
				DBMock.getProduct(1)).extractProperties();

		long numOfRightAmount = result.stream().filter(
				p -> p instanceof MostPopularProductProperty && ((MostPopularProductProperty) p).getAmount() == 2)
				.count();

		for (long i = 1; i <= MostPopularProductProperty.numOfTop; ++i)
			assert result.contains(new MostPopularProductProperty(DBMock.getProduct(i), 2));

		assertEquals(MostPopularProductProperty.numOfTop, numOfRightAmount);

	}
	
	@Test
	public void test50LastPopularProductsProperty() {
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				DBMock.getProduct(1)).extractProperties();

		long numOfRightAmount = result.stream().filter(
				p -> p instanceof MostPopularProductProperty && ((MostPopularProductProperty) p).getAmount() == 2)
				.count();

		for (long i = 1; i <= LastPopularProductProperty.numOfBottom; ++i)
			assert result.contains(new LastPopularProductProperty(DBMock.getProduct(i + 50), 1));

		assertEquals(LastPopularProductProperty.numOfBottom, numOfRightAmount);

	}

	@Test
	public void testAboutToExpireSoonProperty() {
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				DBMock.getProduct(1)).extractProperties();

		assertEquals(AboutToExpireSoonStorePackageProperty.numOfTop, result.stream().filter(p -> p instanceof AboutToExpireSoonStorePackageProperty).count());
		
		for (IStorePackage spm : stock)
			if (spm.getProduct().getBarcode() > DBMock.NUM_OF_PRODUCTS - AboutToExpireSoonStorePackageProperty.numOfTop)
				assert result.contains(new AboutToExpireSoonStorePackageProperty(spm));
			else
				assert !result.contains(new AboutToExpireSoonStorePackageProperty(spm));

	}
	
	@Test
	public void testAboutToExpireLateProperty() {
		
		StorePackageMock packageAboutToExpiredLate = new StorePackageMock(
				2, LocalDate.now().plusDays(AboutToExpireLateStorePackageProperty.minDaysThreshold + 5));
		
		List<StorePackageMock> stock = new StockMockBuilder()
				.addPackage(1, LocalDate.now().plusDays(2))
				.addPackage(packageAboutToExpiredLate)
				.addPackage(3, LocalDate.now().plusDays(AboutToExpireLateStorePackageProperty.maxDaysThreshold + 5))
				.build();
		
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), DBMock.getStoreDataByStock(stock),
				new GroceryListMock("alice"), DBMock.getProduct(1)).extractProperties();

		assertEquals(1, result.stream().filter(p -> p instanceof AboutToExpireLateStorePackageProperty).count());
		assert result.contains(new AboutToExpireLateStorePackageProperty(packageAboutToExpiredLate));

	}
	
	@Test
	public void testHighRatioAmountExpirationProperty() {
		
		StorePackageMock highRatioPackage = new StorePackageMock(2,
				5, LocalDate.now().plusDays(5), PlaceInMarket.STORE);
		double ratio = 1. * highRatioPackage.getAmount() /
				ChronoUnit.DAYS.between(LocalDate.now(),highRatioPackage.getExpirationDate());
		
		List<StorePackageMock> stock = new StockMockBuilder()
				.addPackage(1, LocalDate.now().plusDays(10))
				.addPackage(highRatioPackage)
				.build();
		
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), DBMock.getStoreDataByStock(stock),
				new GroceryListMock("alice"), DBMock.getProduct(1)).extractProperties();

		assertEquals(1, result.stream().filter(p -> p instanceof HighRatioAmountExpirationTimeProperty).count());
		assert result
				.contains(new HighRatioAmountExpirationTimeProperty(ratio, new CombinedStorePackage(highRatioPackage)));

	}
	
	@Test
	public void testHighRatioCombinePackages() {
		
		StorePackageMock highRatioStorePackage = new StorePackageMock(2, 5, LocalDate.now().plusDays(5),
				PlaceInMarket.STORE),
				highRatioWarehousePackage = new StorePackageMock(2, 5, LocalDate.now().plusDays(5),
						PlaceInMarket.WAREHOUSE);
		double ratio = (highRatioWarehousePackage.getAmount() + 1. * highRatioStorePackage.getAmount()) /
				ChronoUnit.DAYS.between(LocalDate.now(),highRatioStorePackage.getExpirationDate());
		
		List<StorePackageMock> stock = new StockMockBuilder()
				.addPackage(1, LocalDate.now().plusDays(10))
				.addPackage(highRatioStorePackage)
				.addPackage(highRatioWarehousePackage)
				.build();
		
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), DBMock.getStoreDataByStock(stock),
				new GroceryListMock("alice"), DBMock.getProduct(1)).extractProperties();

		assertEquals(1, result.stream().filter(p -> p instanceof HighRatioAmountExpirationTimeProperty).count());
		assert result.contains(new HighRatioAmountExpirationTimeProperty(ratio, new CombinedStorePackage(
				highRatioStorePackage.getProduct(), highRatioStorePackage.getExpirationDate(), 10)));

	}
	
	@Test
	public void testMostPopularManufacturersProperty() {
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				DBMock.getProduct(1)).extractProperties();
		
		int expectedAmountOfMostPopularManufacturer = 3;
		long numOfRightAmount = result.stream().filter(
				p -> p instanceof MostPopularManufacturerProperty && ((MostPopularManufacturerProperty) p).getAmount() == expectedAmountOfMostPopularManufacturer)
				.count();

		assertEquals(MostPopularManufacturerProperty.numOfTop, numOfRightAmount);
		
		for (long i = 1; i <= numOfRightAmount; ++i)
			assert result.contains(new MostPopularManufacturerProperty(DBMock.getProduct(i).getManufacturer(),
					expectedAmountOfMostPopularManufacturer));
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
				new GroceryListMock("alice"), DBMock.getProduct(1)).extractProperties();
		
		assertEquals(6, result.stream().filter(p -> p instanceof NumOfBuyersPerMonthProperty).count());
		
		NumOfBuyersPerMonthProperty foundMonth[] = new NumOfBuyersPerMonthProperty[NumOfBuyersPerMonthProperty.goMonthesBackLimit],
				realMonth0 = new NumOfBuyersPerMonthProperty(0, 1), realMonth1 = new NumOfBuyersPerMonthProperty(1, 2),
				realMonth2 = new NumOfBuyersPerMonthProperty(2, 1);
		foundMonth[0] = findInCollection(result, realMonth0);
		foundMonth[1] = findInCollection(result, realMonth1);
		foundMonth[2] = findInCollection(result, realMonth2);
		assertNotNull(foundMonth[0]);
		assertNotNull(foundMonth[1]);
		assertNotNull(foundMonth[2]);
		assertEquals(realMonth0.getNumOfBuyers(), foundMonth[0].getNumOfBuyers());
		assertEquals(realMonth1.getNumOfBuyers(), foundMonth[1].getNumOfBuyers());
		assertEquals(realMonth2.getNumOfBuyers(), foundMonth[2].getNumOfBuyers());
		for (int i=3; i<NumOfBuyersPerMonthProperty.goMonthesBackLimit; ++i){
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
				new GroceryListMock("alice"), DBMock.getProduct(1)).extractProperties();
		
		assertEquals(SumOfPurchasesPerMonthProperty.goMonthesBackLimit, result.stream().filter(p -> p instanceof SumOfPurchasesPerMonthProperty).count());
		
		SumOfPurchasesPerMonthProperty foundMonth[] = new SumOfPurchasesPerMonthProperty[SumOfPurchasesPerMonthProperty.goMonthesBackLimit],
				realMonth0 = new SumOfPurchasesPerMonthProperty(0, 1.5),
				realMonth1 = new SumOfPurchasesPerMonthProperty(1, 3),
				realMonth2 = new SumOfPurchasesPerMonthProperty(2, 2.5);
		foundMonth[0] = findInCollection(result, realMonth0);
		foundMonth[1] = findInCollection(result, realMonth1);
		foundMonth[2] = findInCollection(result, realMonth2);
		assertNotNull(foundMonth[0]);
		assertNotNull(foundMonth[1]);
		assertNotNull(foundMonth[2]);
		assertEquals(realMonth0.getSumOfPurchases(), foundMonth[0].getSumOfPurchases(), DELTA);
		assertEquals(realMonth1.getSumOfPurchases(), foundMonth[1].getSumOfPurchases(), DELTA);
		assertEquals(realMonth2.getSumOfPurchases(), foundMonth[2].getSumOfPurchases(), DELTA);
		for (int i=3; i<NumOfBuyersPerMonthProperty.goMonthesBackLimit; ++i){
			SumOfPurchasesPerMonthProperty temp = findInCollection(result, new SumOfPurchasesPerMonthProperty(i, 0));
			assertNotNull(temp);
			assertEquals(temp.getSumOfPurchases(), 0, DELTA);
		}
	}
	
	@Test
	public void testHealthyRatedProductProperty(){
		assertEquals((int) (0.8 * DBMock.NUM_OF_PRODUCTS),
				new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"), DBMock.getProduct(1))
						.extractProperties().stream().filter(p -> p instanceof HealthyRatedProductProperty).count());
	}

	@Test
	public void testStoreStatisticsMiner() {
		new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryListMock("alice"),
				DBMock.getProduct(1));
	}
	
	

}
