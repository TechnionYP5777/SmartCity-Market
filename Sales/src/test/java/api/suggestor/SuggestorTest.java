package api.suggestor;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import BasicCommonClasses.PlaceInMarket;
import api.contracts.ISale;
import api.types.sales.ProductSale;
import ml.common.property.basicproperties.storestatistics.AboutToExpireLateStorePackageProperty;
import testmocks.DBMock;
import testmocks.GroceryListMock;
import testmocks.GroceryPackageMock;
import testmocks.StockMockBuilder;
import testmocks.StorePackageMock;

public class SuggestorTest {

	@Test
	public void testSimpleSuggest() {
		StorePackageMock packageAboutToExpiredSoon = new StorePackageMock(
				DBMock.getProduct(2), 1, LocalDate.now().plusDays(1), PlaceInMarket.STORE);
		
		List<StorePackageMock> stock = new StockMockBuilder()
				.addPackage(packageAboutToExpiredSoon)
				.addPackage(DBMock.getProduct(3), 2,
						LocalDate.now().plusDays(AboutToExpireLateStorePackageProperty.maxDaysThreshold + 5), PlaceInMarket.STORE)
				.build();
		
		Suggestor.updateCatalog(DBMock.getCatalog());
		Suggestor.updateStock(stock);
		ISale sale = Suggestor.suggestSale(new GroceryListMock("alice"),DBMock.getProduct(1));
		
		
		ProductSale realSale = new ProductSale(DBMock.getProduct(2), 1, 1.3392857142857142857142857142857);
		
		assertEquals(realSale.getProduct(), sale.getProduct());
		assertEquals(realSale.getTotalAmount(), sale.getTotalAmount());
		
	}

}
