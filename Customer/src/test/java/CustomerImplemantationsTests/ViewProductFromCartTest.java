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

public class ViewProductFromCartTest {
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
	public void ViewProductFromCartConnectionFailureTest() {	
		
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
						Serialization.serialize(sc)).serialize()))
				.thenThrow(new SocketTimeoutException());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.addProductToCart(sc, amount);
		} catch (InvalidParameter | CustomerNotConnected | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist e) {
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
	
	@Test
	public void ViewProductFromCartIllegalResultTest() {	
		
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
						Serialization.serialize(sc)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_INGREDIENT_STILL_IN_USE, Serialization.serialize(catalogProduct)).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.addProductToCart(sc, amount);
		} catch (InvalidParameter | CustomerNotConnected | AmountBiggerThanAvailable
				| ProductPackageDoesNotExist e) {
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
}
