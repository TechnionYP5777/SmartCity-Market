package WorkerUnitTests;

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
import EmployeeDefs.AEmployeeException.AmountBiggerThanAvailable;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
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
public class PlaceProductPackageOnShelvesTest {
	private IWorker worker;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		worker = new Worker(clientRequestHandler);
	}
	
	static ProductPackage pp = new ProductPackage(new SmartCode(111,LocalDate.now()), 
			5, new Location(1,1,PlaceInMarket.WAREHOUSE));
	
	@Test
	public void PlaceProductPackageOnShelvesSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.PLACE_PRODUCT_PACKAGE_ON_SHELVES, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.placeProductPackageOnShelves(pp);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected |
				ProductNotExistInCatalog | AmountBiggerThanAvailable |
				ProductPackageDoesNotExist | ConnectionFailure ¢) {
			
			fail();
		}
	}
	
	@Test
	public void PlaceProductPackageOnShelvesInvalidParamsTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.PLACE_PRODUCT_PACKAGE_ON_SHELVES, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.placeProductPackageOnShelves(pp);
			
			fail();
		} catch (CriticalError | EmployeeNotConnected |
				ProductNotExistInCatalog | AmountBiggerThanAvailable |
				ProductPackageDoesNotExist | ConnectionFailure ¢) {
			
			fail();
		} catch (InvalidParameter  ¢) {
			//test success
		}
	}
	
	@Test
	public void PlaceProductPackageOnShelvesEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.PLACE_PRODUCT_PACKAGE_ON_SHELVES, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.placeProductPackageOnShelves(pp);
			
			fail();
		} catch (CriticalError | InvalidParameter | 
				ProductNotExistInCatalog | AmountBiggerThanAvailable |
				ProductPackageDoesNotExist | ConnectionFailure ¢) {
			
			fail();
		} catch (EmployeeNotConnected ¢) {
			//test success
		}
	}
	
	@Test
	public void PlaceProductPackageOnShelvesAmountBiggerThanAvailableTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.PLACE_PRODUCT_PACKAGE_ON_SHELVES, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_PRODUCT_PACKAGE_AMOUNT_BIGGER_THEN_AVAILABLE).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.placeProductPackageOnShelves(pp);
			
			fail();
		} catch (CriticalError | InvalidParameter | 
				ProductNotExistInCatalog | EmployeeNotConnected  |
				ProductPackageDoesNotExist | ConnectionFailure ¢) {
			
			fail();
		} catch ( AmountBiggerThanAvailable ¢) {
			//test success
		}
	}
	
	@Test
	public void PlaceProductPackageOnShelvesProductNotExistInCatalogTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.PLACE_PRODUCT_PACKAGE_ON_SHELVES, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.placeProductPackageOnShelves(pp);
			
			fail();
		} catch (CriticalError | InvalidParameter | 
				AmountBiggerThanAvailable | EmployeeNotConnected  |
				ProductPackageDoesNotExist | ConnectionFailure ¢) {
			
			fail();
		} catch (ProductNotExistInCatalog ¢) {
			//test success
		}
	}
	
	@Test
	public void PlaceProductPackageOnShelvesProductPackageDoesNotExistTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.PLACE_PRODUCT_PACKAGE_ON_SHELVES, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_PRODUCT_PACKAGE_DOES_NOT_EXIST).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.placeProductPackageOnShelves(pp);
			
			fail();
		} catch (CriticalError | InvalidParameter | 
				AmountBiggerThanAvailable | EmployeeNotConnected  |
				ProductNotExistInCatalog | ConnectionFailure ¢) {
			
			fail();
		} catch (ProductPackageDoesNotExist ¢) {
			//test success
		}
	}
	
	@Test
	public void PlaceProductPackageOnShelvesIllegalResultTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.PLACE_PRODUCT_PACKAGE_ON_SHELVES, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_MANUFACTURER_STILL_IN_USE).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.placeProductPackageOnShelves(pp);
			
			fail();
		} catch (ProductPackageDoesNotExist | InvalidParameter | 
				AmountBiggerThanAvailable | EmployeeNotConnected  |
				ProductNotExistInCatalog | ConnectionFailure ¢) {
			
			fail();
		} catch (CriticalError ¢) {
			//test success
		}
	}
}
