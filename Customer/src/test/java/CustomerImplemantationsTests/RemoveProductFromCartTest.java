package CustomerImplemantationsTests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.SocketTimeoutException;
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
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ICustomer;
import CustomerContracts.ACustomerExceptions.AmountBiggerThanAvailable;
import CustomerContracts.ACustomerExceptions.CriticalError;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerContracts.ACustomerExceptions.ProductPackageDoesNotExist;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)

public class RemoveProductFromCartTest {
	private ICustomer customer;

	static int amount = 10;
	static SmartCode sc = new SmartCode(111,LocalDate.now());
	static ProductPackage pp = new ProductPackage(sc, 
			amount, null);
	static CatalogProduct catalogProduct = new CatalogProduct(sc.getBarcode(), "name", null,
			new Manufacturer(1, "Manufacturer"), "description", 22.0, null, null);
	
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
		
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
						Serialization.serialize(sc)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(catalogProduct)).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.addProductToCart(sc, amount);
		} catch (CriticalError | CustomerNotConnected | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| InvalidParameter e1) {
			fail();
		}
	}
	
	@Test
	public void removeProductFromCartSuccessfulTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.returnProductToShelf(sc, amount);
		} catch (CriticalError | CustomerNotConnected | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist e) {
			fail();	
		}
	}
	
	@Test
	public void removeProductFromCartCriticalErrorTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.returnProductToShelf(sc, amount);
		} catch (CustomerNotConnected | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist e) {
			fail();	
		} catch (CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void removeProductFromCartCustomerNotConnectedTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.returnProductToShelf(sc, amount);
		} catch (CriticalError | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist e) {
			fail();	
		} catch (CustomerNotConnected e) {
			/* success */
		}
	}
	
	@Test
	public void removeProductFromCartAmountBiggerThanAvailableTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.returnProductToShelf(sc, amount);
		} catch (CustomerNotConnected | CriticalError | ProductPackageDoesNotExist e) {
			fail();	
		} catch (AmountBiggerThanAvailable e) {
			/* success */
		}
	}
	
	@Test
	public void removeProductFromCartConnectionFailureTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
			.thenThrow(new SocketTimeoutException());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.returnProductToShelf(sc, amount);
		} catch (CustomerNotConnected | AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			fail();	
		} catch (CriticalError e) {
			/* success */
		}
	}
	
	@Test
	public void removeProductFromCartIllegalResultTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_ALREADY_CONNECTED).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.returnProductToShelf(sc, amount);
		} catch (CustomerNotConnected | AmountBiggerThanAvailable | ProductPackageDoesNotExist e) {
			fail();	
		} catch (CriticalError e) {
			/* success */
		}
	}
}
