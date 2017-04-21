package CustomerImplemantationsTests;

import static org.junit.Assert.fail;

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
import CustomerContracts.ACustomerExceptions.ProductNotInCart;
import CustomerContracts.ACustomerExceptions.ProductPackageDoesNotExist;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)

/**
 * 
 * @author idan atias
 *
 */

public class ReturnProductToShelfTest {
	private ICustomer customer;
	static int amount = 10;
	static SmartCode sc = new SmartCode(111, LocalDate.now());
	static ProductPackage pp = new ProductPackage(sc, amount, null);
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
					clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId,
							CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG, Serialization.serialize(sc)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(catalogProduct))
							.serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
	}

	@Test
	public void returnProductToShelfSuccessfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(customer.getId(),
					CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}

		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(customer.getId(),
					CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}

		try {
			customer.addProductToCart(sc, amount);
		} catch (CriticalError | CustomerNotConnected | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| InvalidParameter e1) {
			e1.printStackTrace();
			fail();
		}

		try {
			customer.returnProductToShelf(sc, amount);
		} catch (CriticalError | CustomerNotConnected | AmountBiggerThanAvailable | ProductPackageDoesNotExist
				| ProductNotInCart e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void returnProductToShelfOfProductNotInCartBadPathTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(customer.getId(),
					CommandDescriptor.REMOVE_PRODUCT_FROM_GROCERY_LIST, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}

		try {
			customer.returnProductToShelf(sc, amount);
		} catch (CriticalError | CustomerNotConnected | AmountBiggerThanAvailable | ProductPackageDoesNotExist | ProductNotInCart e) {
			/* success */
			return;
		}
		fail();
	}
}
