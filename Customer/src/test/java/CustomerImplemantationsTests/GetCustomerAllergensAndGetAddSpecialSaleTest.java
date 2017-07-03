package CustomerImplemantationsTests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.Ingredient;
import BasicCommonClasses.Sale;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.IRegisteredCustomer;
import CustomerContracts.ACustomerExceptions.AuthenticationError;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerImplementations.CustomerDefs;
import CustomerImplementations.RegisteredCustomer;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/** 
 * @author Lior Ben Ami
 * @since 2017-06-30
 * */
@RunWith(MockitoJUnitRunner.class)
public class GetCustomerAllergensAndGetAddSpecialSaleTest {

	private IRegisteredCustomer customer;
	long barcode = 1234567;
	Sale sale = new Sale(1, barcode, 3, 22.0), testSale;
	
	static String username = "username";
	static CustomerProfile customerProfile = new CustomerProfile(username);
	HashSet<Ingredient> allergens = new  HashSet<Ingredient>(Arrays.asList(new Ingredient(1, "milk"), new Ingredient(2, "meat"))),
			testAllergens;
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new RegisteredCustomer(clientRequestHandler);
	}
	
	@Test
	public void getCustomerAllergansTest() {	
		customerProfile.setAllergens(allergens);
		
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond(new CommandWrapper(CustomerDefs.loginCommandSenderId, CommandDescriptor.UPDATE_CUSTOMER_PROFILE,
						Serialization.serialize(customerProfile)).serialize()))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			fail();
		}
		
		try {
			customer.updateCustomerProfile(customerProfile);
		} catch (InvalidParameter | AuthenticationError | CriticalError | CustomerNotConnected e) {
			fail();
		}
		testAllergens = customer.getCustomerAllergens();
		assert(testAllergens.equals(allergens));
	}
	
	@Test
	public void addAndGetSpecialSalesTest() {	
		customer.addSpecialSale(sale, false);
		HashMap<Sale,Boolean> sales = customer.getSpecialSales();
		assert(sales.containsKey(sale));
	}
}
