package ManagerUnitTests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.ProductStillForSale;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import SMExceptions.CommonExceptions.CriticalError;
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
	
	static SmartCode smartCode = new SmartCode(1, null);
	
	@Test
	public void RemoveProductFromCatalogSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(smartCode)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.removeProductFromCatalog(smartCode);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductStillForSale
				| ProductNotExistInCatalog | ConnectionFailure e) {
			
			fail();
		}
	}
	
	@Test
	public void RemoveProductFromCatalogInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(smartCode)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException e) {
			
			fail();
		}

		try {
			manager.removeProductFromCatalog(smartCode);
			
			fail();
		} catch (CriticalError | EmployeeNotConnected | ProductStillForSale
				| ProductNotExistInCatalog | ConnectionFailure e) {
			
			fail();
		} catch (InvalidParameter e) {
			/* test success */
		}
	}
	
	@Test
	public void RemoveProductFromCatalogEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(smartCode)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException e) {
			
			fail();
		}

		try {
			manager.removeProductFromCatalog(smartCode);
			
			fail();
		} catch (InvalidParameter | CriticalError | ProductStillForSale
				| ProductNotExistInCatalog | ConnectionFailure e) {
			
			fail();
		} catch (EmployeeNotConnected e) {
			/* test success */
		}
	}
	
	@Test
	public void RemoveProductFromCatalogProductNotExistInCatalogTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(smartCode)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST).serialize());
		} catch (IOException e) {
			
			fail();
		}

		try {
			manager.removeProductFromCatalog(smartCode);
			
			fail();
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductStillForSale | ConnectionFailure e) {
			
			fail();
		} catch (ProductNotExistInCatalog e) {
			/* test success */
		}
	}
	
	@Test
	public void RemoveProductFromCatalogProductStillForSaleTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(smartCode)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_STILL_FOR_SALE).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.removeProductFromCatalog(smartCode);
			
			fail();
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductNotExistInCatalog | ConnectionFailure e) {
			
			fail();
		} catch (ProductStillForSale e) {
			/* test success */
		}
	}
	
	@Test
	public void RemoveProductFromCatalogIllegalResultTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
							Serialization.serialize(smartCode)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY).serialize());
		} catch (IOException e) {
			
			fail();
		}
		
		try {
			manager.removeProductFromCatalog(smartCode);
			
			fail();
		} catch (InvalidParameter | ProductStillForSale | EmployeeNotConnected | ProductNotExistInCatalog | ConnectionFailure e) {
			
			fail();
		} catch (CriticalError e) {
			/* test success */
		}
	}
}
