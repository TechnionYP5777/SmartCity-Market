package WorkerUnitTests;
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

import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Lior Ben Ami
 * @since 2016-01-02 
 */
@RunWith(MockitoJUnitRunner.class)
public class AddProductToWarehouseTest {
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
	public void AddProductToWarehouseSuccessfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.addProductToWarehouse(pp);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductNotExistInCatalog | ConnectionFailure ¢) {
			
			fail();
		}
	}
	
	@Test
	public void AddProductToWarehouseInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.addProductToWarehouse(pp);
			
			fail();
		} catch (CriticalError | EmployeeNotConnected | ProductNotExistInCatalog | ConnectionFailure ¢) {
			
			fail();
		} catch (InvalidParameter ¢) {
			// test success
		}
	}
	
	@Test
	public void AddProductToWarehouseEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.addProductToWarehouse(pp);
			
			fail();
		} catch (CriticalError | InvalidParameter | ProductNotExistInCatalog | ConnectionFailure ¢) {
			
			fail();
		} catch (EmployeeNotConnected ¢) {
			// test success
		}
	}
	
	@Test
	public void AddProductToWarehouseProductNotExistInCatalogTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.addProductToWarehouse(pp);
			
			fail();
		} catch (CriticalError | InvalidParameter | EmployeeNotConnected | ConnectionFailure ¢) {
			
			fail();
		} catch (ProductNotExistInCatalog ¢) {
			// test success 
		}
	}
	
	@Test
	public void AddProductToWarehouseProductIllegalResultTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId,
							CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE, Serialization.serialize(pp)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_STILL_FOR_SALE).serialize());
		} catch (IOException ¢) {
			
			fail();
		}

		try {
			worker.addProductToWarehouse(pp);
			
			fail();
		} catch (ProductNotExistInCatalog | InvalidParameter | EmployeeNotConnected | ConnectionFailure ¢) {
			
			fail();
		} catch (CriticalError ¢) {
			// test success 
		}
	}
}
