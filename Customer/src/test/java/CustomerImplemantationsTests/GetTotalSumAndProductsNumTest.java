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
import CustomerContracts.ACustomerExceptions.AmountBiggerThanAvailable;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerContracts.ACustomerExceptions.ProductPackageDoesNotExist;
import CustomerContracts.ICustomer;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/** 
 * @author Lior Ben Ami
 * @since 2017-06-29
 * */
@RunWith(MockitoJUnitRunner.class)
public class GetTotalSumAndProductsNumTest {

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
	public void getTotalSumTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.addProductToCart(sc, catalogProduct, amount);
		} catch (CriticalError | CustomerNotConnected | AmountBiggerThanAvailable | ProductPackageDoesNotExist | InvalidParameter e) {
			fail();
		}
		assert(customer.getTotalSum().equals(220.0));
	}
	
	@Test
	public void getCartProductsNumTest() {	
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_GROCERY_LIST,
						Serialization.serialize(pp)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.addProductToCart(sc, catalogProduct, amount);
		} catch (CriticalError | CustomerNotConnected | AmountBiggerThanAvailable | ProductPackageDoesNotExist | InvalidParameter e) {
			fail();
		}
		assert(customer.getCartProductsNum().equals(10));
	}

}
