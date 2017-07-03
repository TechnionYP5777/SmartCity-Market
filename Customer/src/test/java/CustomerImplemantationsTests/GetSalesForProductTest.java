package CustomerImplemantationsTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

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
public class GetSalesForProductTest {

	private ICustomer customer;
	
	long barcode = 1234567;
	Sale sale = new Sale(1, barcode, 3, 22.0), testSale;
	
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}
	
	@Test
	public void GetSaleForProductSuccessfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.GET_SALE_FOR_PRODUCT,
							Serialization.serialize(barcode)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(sale)).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {
			testSale = customer.getSaleForProduct(barcode);
		} catch (CriticalError | CustomerNotConnected | ProductCatalogDoesNotExist | InvalidParameter e) {
			fail();
		}
		assertEquals(testSale, sale);
	}
	
	@Test
	public void GetSaleForProductCriticalErrorTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.GET_SALE_FOR_PRODUCT,
							Serialization.serialize(barcode)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY, Serialization.serialize(sale)).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {
			customer.getSaleForProduct(barcode);
		} catch ( CustomerNotConnected | ProductCatalogDoesNotExist | InvalidParameter e) {
			fail();
		} catch (CriticalError e) {
			//test passed
		}
	}
	
	@Test
	public void GetSaleForProductCustomerNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.GET_SALE_FOR_PRODUCT,
							Serialization.serialize(barcode)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, Serialization.serialize(sale)).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {
			customer.getSaleForProduct(barcode);
		} catch ( CriticalError | ProductCatalogDoesNotExist | InvalidParameter e) {
			fail();
		} catch ( CustomerNotConnected e) {
			//test passed
		}
	}
	
	@Test
	public void GetSaleForProductProductCatalogDoesNotExistTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.GET_SALE_FOR_PRODUCT,
							Serialization.serialize(barcode)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST, Serialization.serialize(sale)).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {
			customer.getSaleForProduct(barcode);
		} catch ( CriticalError |  CustomerNotConnected | InvalidParameter e) {
			fail();
		} catch (ProductCatalogDoesNotExist e) {
			//test passed
		}
	}
	
	@Test
	public void GetSaleForProductInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.GET_SALE_FOR_PRODUCT,
							Serialization.serialize(barcode)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER, Serialization.serialize(sale)).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {
			customer.getSaleForProduct(barcode);
		} catch ( CriticalError |  CustomerNotConnected | ProductCatalogDoesNotExist  e) {
			fail();
		} catch (InvalidParameter e) {
			//test passed
		}
	}
}
