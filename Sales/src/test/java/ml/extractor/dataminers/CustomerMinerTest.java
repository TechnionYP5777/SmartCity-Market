package ml.extractor.dataminers;

import static ml.utils.CollectionFunctions.findInCollection;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.basicproperties.storestatistics.AverageAmountOfProductForCustomerProperty;
import ml.common.property.basicproperties.storestatistics.LastPopularProductOfCustomerProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProductOfCustomerProperty;
import ml.common.property.basicproperties.storestatistics.NumOfBuyersPerMonthProperty;
import ml.common.property.basicproperties.storestatistics.NumOfCustomerPurchasesPerMonthProperty;
import ml.common.property.basicproperties.storestatistics.SumOfCustomerPurchasesPerMonthProperty;
import ml.common.property.basicproperties.storestatistics.SumOfPurchasesPerMonthProperty;
import testmocks.DBMock;
import testmocks.GroceryListMock;
import testmocks.GroceryPackageMock;
import testmocks.HistoryMockBuilder;

public class CustomerMinerTest {

	private static final double DELTA = 1e-15;
	
	@Test
	public void testMostPopularProductsOfCustomerProperty() {
		List<GroceryListMock> history = new HistoryMockBuilder().build();
		
		for (long i = 1; i <= 10; i++)
			history.add(new GroceryListMock("alice").addProdcut(DBMock.getProduct(i)));
		for (long i = 1; i <= 10; i++)
			history.add(new GroceryListMock("alice").addProdcut(DBMock.getProduct(i)));

		for (long i = 10; i <= 10 + MostPopularProductOfCustomerProperty.numOfTop; i++)
			history.add(new GroceryListMock("bob").addProdcut(DBMock.getProduct(i)));
		
		Set<ABasicProperty> result = new CustomerMiner(DBMock.getInputPref(), DBMock.getStoreDateByHistory(history), 
				new GroceryListMock("bob"),DBMock.getProduct(1)).extractProperties();

		long numOfRightAmount = result.stream().filter(
				p -> p instanceof MostPopularProductOfCustomerProperty && ((MostPopularProductOfCustomerProperty) p).getAmount() == 1)
				.count();

		for (long i = 10; i < 10 + MostPopularProductOfCustomerProperty.numOfTop; i++)
			assertTrue(result.contains(new MostPopularProductOfCustomerProperty(DBMock.getProduct(i), 1)));

		assertEquals(MostPopularProductOfCustomerProperty.numOfTop, numOfRightAmount);

	}
	
	@Test
	public void testLastPopularProductsOfCustomerProperty() {
		List<GroceryListMock> history = new HistoryMockBuilder().build();
		
		for (long i = 1; i <= 2*LastPopularProductOfCustomerProperty.numOfBottom; i++)
			history.add(new GroceryListMock("alice").addProdcut(DBMock.getProduct(i)));
		for (long i = 1; i <= LastPopularProductOfCustomerProperty.numOfBottom; i++)
			history.add(new GroceryListMock("alice").addProdcut(DBMock.getProduct(i)));

		for (long i = 10; i <= 10 + LastPopularProductOfCustomerProperty.numOfBottom; i++)
			history.add(new GroceryListMock("bob").addProdcut(DBMock.getProduct(i)));
		
		Set<ABasicProperty> result = new CustomerMiner(DBMock.getInputPref(), DBMock.getStoreDateByHistory(history), 
				new GroceryListMock("alice"),DBMock.getProduct(1)).extractProperties();

		long numOfRightAmount = result.stream().filter(
				p -> p instanceof LastPopularProductOfCustomerProperty && ((LastPopularProductOfCustomerProperty) p).getAmount() == 1)
				.count();


		for (long i = LastPopularProductOfCustomerProperty.numOfBottom + 1; i <= 2*LastPopularProductOfCustomerProperty.numOfBottom ; i++)
			assertTrue(result.contains(new LastPopularProductOfCustomerProperty(DBMock.getProduct(i), 1)));

		assertEquals(LastPopularProductOfCustomerProperty.numOfBottom, numOfRightAmount);

	}
	
	@Test
	public void testNumberOfCustomerPurchasesPerMonthProperty(){
		
		List<GroceryListMock> history = new HistoryMockBuilder().addGrocery("alice", 
				LocalDate.now(), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("bob", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(2), DBMock.getProduct(1), 1).build();
				
		
		Set<ABasicProperty> result = new CustomerMiner(DBMock.getInputPref(), DBMock.getStoreDateByHistory(history),
				new GroceryListMock("alice"), DBMock.getProduct(1)).extractProperties();
		
		long numOfBuyerPerMonthProperties = result.stream()
				.filter(p -> p instanceof NumOfCustomerPurchasesPerMonthProperty).count();

		assertEquals(6, numOfBuyerPerMonthProperties);
		
		NumOfCustomerPurchasesPerMonthProperty foundMonth[] = new NumOfCustomerPurchasesPerMonthProperty[NumOfBuyersPerMonthProperty.goMonthesBackLimit];
		NumOfCustomerPurchasesPerMonthProperty realMonth0 = new NumOfCustomerPurchasesPerMonthProperty(0, 1);
		NumOfCustomerPurchasesPerMonthProperty realMonth1 = new NumOfCustomerPurchasesPerMonthProperty(1, 2);
		NumOfCustomerPurchasesPerMonthProperty realMonth2 = new NumOfCustomerPurchasesPerMonthProperty(2, 1);
		foundMonth[0] = findInCollection(result, realMonth0);
		foundMonth[1] = findInCollection(result, realMonth1);
		foundMonth[2] = findInCollection(result, realMonth2);
		assertNotNull(foundMonth[0]);
		assertNotNull(foundMonth[1]);
		assertNotNull(foundMonth[2]);
		assertEquals(realMonth0.getNumOfBuyers(), foundMonth[0].getNumOfBuyers());
		assertEquals(realMonth1.getNumOfBuyers(), foundMonth[1].getNumOfBuyers());
		assertEquals(realMonth2.getNumOfBuyers(), foundMonth[2].getNumOfBuyers());
		for (int i=3; i<NumOfCustomerPurchasesPerMonthProperty.goMonthesBackLimit; i++){
			NumOfCustomerPurchasesPerMonthProperty temp = findInCollection(result, new NumOfCustomerPurchasesPerMonthProperty(i, 0));
			assertNotNull(temp);
			assertEquals(temp.getNumOfBuyers(), 0);
		}
	}
	
	@Test
	public void testSumOfCustomerPurchasesPerMonthProperty(){
		
		List<GroceryListMock> history = new HistoryMockBuilder().addGrocery("alice", 
				LocalDate.now(), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("bob", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(2), DBMock.getProduct(2), 1).build();
				
		
		Set<ABasicProperty> result = new CustomerMiner(DBMock.getInputPref(), DBMock.getStoreDateByHistory(history),
				new GroceryListMock("alice"), DBMock.getProduct(1)).extractProperties();
		
		long numOfBuyerPerMonthProperties = result.stream()
				.filter(p -> p instanceof SumOfCustomerPurchasesPerMonthProperty).count();
		
		assertEquals(SumOfCustomerPurchasesPerMonthProperty.goMonthesBackLimit, numOfBuyerPerMonthProperties);
		
		SumOfCustomerPurchasesPerMonthProperty foundMonth[] = new SumOfCustomerPurchasesPerMonthProperty[SumOfPurchasesPerMonthProperty.goMonthesBackLimit];
		SumOfCustomerPurchasesPerMonthProperty realMonth0 = new SumOfCustomerPurchasesPerMonthProperty(0, 1.5);
		SumOfCustomerPurchasesPerMonthProperty realMonth1 = new SumOfCustomerPurchasesPerMonthProperty(1, 3);
		SumOfCustomerPurchasesPerMonthProperty realMonth2 = new SumOfCustomerPurchasesPerMonthProperty(2, 2.5);
		foundMonth[0] = findInCollection(result, realMonth0);
		foundMonth[1] = findInCollection(result, realMonth1);
		foundMonth[2] = findInCollection(result, realMonth2);
		assertNotNull(foundMonth[0]);
		assertNotNull(foundMonth[1]);
		assertNotNull(foundMonth[2]);
		assertEquals(realMonth0.getSumOfPurchases(), foundMonth[0].getSumOfPurchases(), DELTA);
		assertEquals(realMonth1.getSumOfPurchases(), foundMonth[1].getSumOfPurchases(), DELTA);
		assertEquals(realMonth2.getSumOfPurchases(), foundMonth[2].getSumOfPurchases(), DELTA);
		for (int i=3; i<SumOfCustomerPurchasesPerMonthProperty.goMonthesBackLimit; i++){
			SumOfCustomerPurchasesPerMonthProperty temp = findInCollection(result, new SumOfCustomerPurchasesPerMonthProperty(i, 0));
			assertNotNull(temp);
			assertEquals(temp.getSumOfPurchases(), 0, DELTA);
		}
	}
	
	@Test
	public void testAverageAmountOfProductForCustomerProperty(){
		
		List<GroceryListMock> history = new HistoryMockBuilder().addGrocery("alice", 
				LocalDate.now(), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 5)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("bob", 
				LocalDate.now().minusMonths(1), DBMock.getProduct(1), 1)
				.addGrocery("alice", 
				LocalDate.now().minusMonths(2), DBMock.getProduct(2), 2).build();
				
		
		Set<ABasicProperty> result = new CustomerMiner(DBMock.getInputPref(), DBMock.getStoreDateByHistory(history),
				new GroceryListMock("alice"), DBMock.getProduct(1)).extractProperties();
		
		long numOfAmountAverageProperties = result.stream()
				.filter(p -> p instanceof AverageAmountOfProductForCustomerProperty).count();
		
		assertEquals(2, numOfAmountAverageProperties);
		
		AverageAmountOfProductForCustomerProperty realproduct1 = new AverageAmountOfProductForCustomerProperty(
				DBMock.getProduct(1), 7.0/3);
		
		AverageAmountOfProductForCustomerProperty realproduct2 = new AverageAmountOfProductForCustomerProperty(
				DBMock.getProduct(2), 2);
		
		AverageAmountOfProductForCustomerProperty found1 = findInCollection(result, realproduct1);
		AverageAmountOfProductForCustomerProperty found2 = findInCollection(result, realproduct2);
		assertNotNull(found1);
		assertNotNull(found2);

		assertEquals(realproduct1.getAverageAmount(), found1.getAverageAmount(), DELTA);
		assertEquals(realproduct2.getAverageAmount(), found2.getAverageAmount(), DELTA);

	}

}
