package ManagerUnitTests;

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
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Lior Ben Ami, Aviad Cohen
 * @since 2016-01-02 */
@RunWith(MockitoJUnitRunner.class)
public class AddProductToCatalogTest {
	private IManager manager;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		manager = new Manager(clientRequestHandler);
	}
	
	static ProductPackage pp = new ProductPackage(new SmartCode(111,LocalDate.now()), 
			5, new Location(1,1,PlaceInMarket.WAREHOUSE));
	
	@Test
	public void AddProductToCatalogSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}

		try {
			manager.addProductToWarehouse(pp);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductNotExistInCatalog ¢) {
			¢.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void AddProductToCatalogInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}

		try {
			manager.addProductToWarehouse(pp);
			
			fail();
		} catch (CriticalError | EmployeeNotConnected | ProductNotExistInCatalog ¢) {
			¢.printStackTrace();
			
			fail();
		} catch (InvalidParameter ¢) {
			/* test success */
		}
	}
	
	@Test
	public void AddProductToCatalogEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			
			fail();
		}

		try {
			manager.addProductToWarehouse(pp);
			
			fail();
		} catch (CriticalError | InvalidParameter | ProductNotExistInCatalog ¢) {
			¢.printStackTrace();
			
			fail();
		} catch (EmployeeNotConnected ¢) {
			/* test success */
		}
	}
	
	@Test
	public void AddProductToCatalogProductNotExistInCatalogTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.ADD_PRODUCT_PACKAGE_TO_WAREHOUSE,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			
			fail();
		}

		try {
			manager.addProductToWarehouse(pp);
			
			fail();
		} catch (CriticalError | InvalidParameter | EmployeeNotConnected ¢) {
			¢.printStackTrace();
			
			fail();
		} catch (ProductNotExistInCatalog ¢) {
			/* test success */
		}
	}
}
