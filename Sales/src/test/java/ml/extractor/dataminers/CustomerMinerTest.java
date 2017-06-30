package ml.extractor.dataminers;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.basicproperties.storestatistics.LastPopularProductOfCustomerProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProductOfCustomerProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProductProperty;
import testmocks.DBMock;
import testmocks.GroceryListMock;
import testmocks.GroceryPackageMock;
import testmocks.HistoryMockBuilder;

public class CustomerMinerTest {

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
				new GroceryListMock("bob"),new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long numOfRightAmount = result.stream().filter(
				p -> p instanceof MostPopularProductOfCustomerProperty && ((MostPopularProductOfCustomerProperty) p).getAmount() == 1)
				.count();
		
		List<MostPopularProductOfCustomerProperty> r = result.stream().filter(p -> p instanceof MostPopularProductOfCustomerProperty)
				.map(p -> (MostPopularProductOfCustomerProperty) p).collect(Collectors.toList());

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
				new GroceryListMock("alice"),new GroceryPackageMock(DBMock.getProduct(1))).extractProperties();

		long numOfRightAmount = result.stream().filter(
				p -> p instanceof LastPopularProductOfCustomerProperty && ((LastPopularProductOfCustomerProperty) p).getAmount() == 1)
				.count();
		
		List<LastPopularProductOfCustomerProperty> r = result.stream().filter(p -> p instanceof LastPopularProductOfCustomerProperty)
				.map(p -> (LastPopularProductOfCustomerProperty) p).collect(Collectors.toList());

		for (long i = LastPopularProductOfCustomerProperty.numOfBottom + 1; i <= 2*LastPopularProductOfCustomerProperty.numOfBottom ; i++)
			assertTrue(result.contains(new LastPopularProductOfCustomerProperty(DBMock.getProduct(i), 1)));

		assertEquals(LastPopularProductOfCustomerProperty.numOfBottom, numOfRightAmount);

	}

}
