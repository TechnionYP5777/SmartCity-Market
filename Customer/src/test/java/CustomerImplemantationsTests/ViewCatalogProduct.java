package CustomerImplemantationsTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.ProductCatalogDoesNotExist;
import CustomerContracts.ICustomer;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * 
 * @author Lior Ben Ami
 * @since  2017-06-30
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ViewCatalogProduct {

	private ICustomer customer;
	static SmartCode sc = new SmartCode(111,LocalDate.now());
	CatalogProduct testCatalogProduct = null, catalogProduct = new CatalogProduct(1234567890, "name", null,
			new Manufacturer(1, "Manufacturer"), "description", 22.0, null, null);
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}

	@Test
	public void ViewCatalogProductSuccessfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(sc)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(catalogProduct)).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		try {
			testCatalogProduct = customer.viewCatalogProduct(sc);
		} catch (CriticalError | CustomerNotConnected | ProductCatalogDoesNotExist e) {
			fail();
		}
		assertEquals(testCatalogProduct.getBarcode(), 1234567890);
		assertEquals(testCatalogProduct.getManufacturer().getId(), 1);
		assertEquals(testCatalogProduct.getManufacturer().getName(), "Manufacturer");
		assertEquals(testCatalogProduct.getName(), "name");
	}

	@Test
	public void ViewCatalogProductNotFoundTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(sc)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			testCatalogProduct = customer.viewCatalogProduct(sc);
		} catch (CriticalError | CustomerNotConnected  e) {
			fail();
		} catch (ProductCatalogDoesNotExist e) {
			/* Test Passed */
		}
	}
	
	@Test
	public void ViewProductFromCatalogproductSenderIsNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(sc)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		
		try {
			testCatalogProduct = customer.viewCatalogProduct(sc);
		} catch (CriticalError | ProductCatalogDoesNotExist e) {
			fail();
		} catch (CustomerNotConnected e) {
			/* Test Passed */
		} 
	}
	
	@Test
	public void ViewProductFromCatalogproductllegalResultTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(sc)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_CMD_DESCRIPTOR).serialize());
		} catch (IOException ¢) {
			
			fail();
		}
		
	
		try {
			testCatalogProduct = customer.viewCatalogProduct(sc);
		} catch (CustomerNotConnected | ProductCatalogDoesNotExist e) {
			fail();
		} catch (CriticalError e) {
			/* Test Passed */
		}
	}
}
