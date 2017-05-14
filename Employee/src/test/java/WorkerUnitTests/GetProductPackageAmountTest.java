package WorkerUnitTests;

import static org.junit.Assert.assertEquals;
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

import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductPackageDoesNotExist;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Lior Ben Ami
 * @since 2016-01-02 */
@RunWith(MockitoJUnitRunner.class)
public class GetProductPackageAmountTest {
	private IWorker worker;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		worker = new Worker(clientRequestHandler);
	}
	
	static ProductPackage pp = new ProductPackage(new SmartCode(111,LocalDate.now()), 
			5, new Location(1,1,PlaceInMarket.STORE));
	
	@Test 
	public void GetProductPackageAmountSuccesfulTest() {
		int retAmmount = 0; 
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, "2").serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			retAmmount = worker.getProductPackageAmount(pp);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | 
				ProductPackageDoesNotExist | ConnectionFailure ¢) {
			
			fail();
		}
		
		assertEquals(retAmmount, 2);
	}
	
	@Test 
	public void GetProductPackageAmountInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER, "2").serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.getProductPackageAmount(pp);
			
			fail();
		} catch (CriticalError | EmployeeNotConnected | 
				ProductPackageDoesNotExist | ConnectionFailure ¢) {
			
			fail();
		} catch (InvalidParameter ¢) {
			/* Test success */
		}
	}
	
	@Test 
	public void GetProductPackageAmountEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, "2").serialize());
		} catch (IOException ¢) {
			

			fail();
		}

		try {
			worker.getProductPackageAmount(pp);
			
			fail();
		} catch (CriticalError | InvalidParameter | 
				ProductPackageDoesNotExist | ConnectionFailure ¢) {
			
			fail();
		} catch (EmployeeNotConnected  ¢) {
			/* Test success */
		}
	}
	
	@Test
	public void GetProductPackageAmountProductPackageDoesNotExistTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_PRODUCT_PACKAGE_DOES_NOT_EXIST, "2").serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.getProductPackageAmount(pp);
			
			fail();
		} catch (CriticalError | InvalidParameter | 
				EmployeeNotConnected | ConnectionFailure ¢) {
			
			fail();
		} catch (ProductPackageDoesNotExist ¢) {
			//Test success
		}
	}
	
	@Test
	public void GetProductPackageAmountIllegalResultTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.GET_PRODUCT_PACKAGE_AMOUNT,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INGREDIENT_STILL_IN_USE, "2").serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.getProductPackageAmount(pp);
			
			fail();
		} catch (InvalidParameter | ProductPackageDoesNotExist |
				EmployeeNotConnected | ConnectionFailure ¢) {
			
			fail();
		} catch (CriticalError ¢) {
			//Test success
		}
	}
}