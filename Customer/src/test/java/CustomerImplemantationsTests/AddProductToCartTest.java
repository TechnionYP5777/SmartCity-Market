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
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerContracts.ACustomerExceptions.ProductPackageDoesNotExist;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/** 
 * @author Lior Ben Ami
 * @since 2017-06-30
 * */
@RunWith(MockitoJUnitRunner.class)
public class AddProductToCartTest {
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
	}
	
	@Test
	public void addProductToCartSuccessfulTest() {	
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
			customer.addProductToCart(sc, catalogProduct, amount);
		} catch (CriticalError | CustomerNotConnected | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| InvalidParameter e1) {
			fail();
		}
	}
	
	@Test
	public void addProductToCartCriticalErrorTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
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
			customer.addProductToCart(sc, catalogProduct, amount);
		} catch (CustomerNotConnected | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| InvalidParameter e) {
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
	
	@Test
	public void addProductToCartCustomerNotConnectedTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
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
			customer.addProductToCart(sc, catalogProduct, amount);
		} catch (CriticalError | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| InvalidParameter e) {
			fail();
		} catch (CustomerNotConnected __) {
			/* success */
		}
	}
	
	@Test
	public void addProductToCartAmountBiggerThanAvailableTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE).serialize());
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
			customer.addProductToCart(sc, catalogProduct, amount);
		} catch (CriticalError | CustomerNotConnected | ProductPackageDoesNotExist
				| InvalidParameter e) {
			fail();
		} catch (AmountBiggerThanAvailable __) {
			/* success */
		}
	}
	
	@Test
	public void addProductToCartProductPackageDoesNotExistTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_PRODUCT_PACKAGE_DOES_NOT_EXIST).serialize());
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
			customer.addProductToCart(sc, catalogProduct, amount);
		} catch (CriticalError | CustomerNotConnected | AmountBiggerThanAvailable
				| InvalidParameter e) {
			fail();
		} catch (ProductPackageDoesNotExist __) {
			/* success */
		}
	}
	
	@Test
	public void addProductToCartProductInvalidParameterTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
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
			customer.addProductToCart(sc, catalogProduct, amount);
		} catch (CriticalError | CustomerNotConnected | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist e) {
			fail();
		} catch (InvalidParameter __) {
			/* success */
		}
	}
	
	@Test
	public void addProductToCartConnectionFailureTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
			.thenThrow(new SocketTimeoutException());
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
			customer.addProductToCart(sc, catalogProduct, amount);
		} catch (CustomerNotConnected | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| InvalidParameter e) {
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
	
	@Test
	public void IllegalResultTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY).serialize());
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
			customer.addProductToCart(sc, catalogProduct, amount);
		} catch (InvalidParameter | CustomerNotConnected | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist e) {
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
}
