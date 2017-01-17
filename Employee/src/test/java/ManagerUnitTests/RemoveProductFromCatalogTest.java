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
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductStillForSale;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Lior Ben Ami, Aviad Cohen
 * @since 2016-01-03 */
@RunWith(MockitoJUnitRunner.class)
public class RemoveProductFromCatalogTest {
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
	public void RemoveProductFromCatalogSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeProductFromCatalog(pp);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductStillForSale
				| ProductNotExistInCatalog | ConnectionFailure e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void RemoveProductFromCatalogInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		try {
			manager.removeProductFromCatalog(pp);
			
			fail();
		} catch (CriticalError | EmployeeNotConnected | ProductStillForSale
				| ProductNotExistInCatalog | ConnectionFailure e) {
			e.printStackTrace();
			fail();
		} catch (InvalidParameter e) {
			/* test success */
		}
	}
	
	@Test
	public void RemoveProductFromCatalogEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		try {
			manager.removeProductFromCatalog(pp);
			
			fail();
		} catch (InvalidParameter | CriticalError | ProductStillForSale
				| ProductNotExistInCatalog | ConnectionFailure e) {
			e.printStackTrace();
			fail();
		} catch (EmployeeNotConnected e) {
			/* test success */
		}
	}
	
	@Test
	public void RemoveProductFromCatalogProductNotExistInCatalogTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		try {
			manager.removeProductFromCatalog(pp);
			
			fail();
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductStillForSale | ConnectionFailure e) {
			e.printStackTrace();
			fail();
		} catch (ProductNotExistInCatalog e) {
			/* test success */
		}
	}
	
	@Test
	public void RemoveProductFromCatalogProductStillForSaleTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
							CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(pp)).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_STILL_FOR_SALE).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			manager.removeProductFromCatalog(pp);
			
			fail();
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductNotExistInCatalog | ConnectionFailure e) {
			e.printStackTrace();
			fail();
		} catch (ProductStillForSale e) {
			/* test success */
		}
	}
}
