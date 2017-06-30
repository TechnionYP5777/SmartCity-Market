package CustomerImplemantationsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Sale;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ICustomer;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerContracts.ACustomerExceptions.ProductCatalogDoesNotExist;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Lior Ben Ami
 * @since  2017-06-30
 */
@RunWith(MockitoJUnitRunner.class)
public class GetMarketCatalogTest {

private ICustomer customer;
	
	CatalogProduct cp1 = new CatalogProduct(111111111, "a", null, null, null, 10.0, null, null),
			cp2 = new CatalogProduct(222222222, "b", null, null, null, 10.0, null, null);
	
	List<CatalogProduct> products = Arrays.asList(cp1, cp2), testProducts = null;
	
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}
	
	@Test
	public void  GetMarketCatalogSuccessfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.GET_MARKET_CATALOG).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(products)).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {
			testProducts = customer.getMarketCatalog();
		} catch (CriticalError e) {
			fail();
		}
		assertEquals(testProducts, products);
	}
	
	@Test
	public void  GetMarketCatalogCriticalErrorTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.GET_MARKET_CATALOG).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY, Serialization.serialize(products)).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {
			testProducts = customer.getMarketCatalog();
		} catch (CriticalError e) {
			//test passed
		}
	}
}
