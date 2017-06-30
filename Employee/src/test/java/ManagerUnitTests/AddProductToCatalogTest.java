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

import BasicCommonClasses.CatalogProduct;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeContracts.IManager;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductAlreadyExistInCatalog;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Aviad Cohen
 * @since 2016-03-21 */
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
	
	static CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 1.5, null, null);
	
	@Test
	public void AddProductToCatalogSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
							Serialization.serialize(catalogProduct)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException e) {
			fail();
		}
		
		try {
			manager.addProductToCatalog(catalogProduct);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductAlreadyExistInCatalog
				| ConnectionFailure e) {
			
			fail();
		}
	}
	
	@Test
	public void AddProductToCatalogInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
							Serialization.serialize(catalogProduct)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException e) {
			fail();
		}
		
		try {
			manager.addProductToCatalog(catalogProduct);
		} catch (CriticalError | EmployeeNotConnected | ProductAlreadyExistInCatalog
				| ConnectionFailure e) {
			
			fail();
		} catch (InvalidParameter ¢) {
			/* test success */
		}
	}
	
	@Test
	public void AddProductToCatalogCriticalErrorTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
							Serialization.serialize(catalogProduct)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException e) {
			fail();
		}
		
		try {
			manager.addProductToCatalog(catalogProduct);
		} catch (InvalidParameter | EmployeeNotConnected | ProductAlreadyExistInCatalog
				| ConnectionFailure e) {
			
			fail();
		} catch (CriticalError ¢) {
			/* test success */
		}
	}
	
	
	
	@Test
	public void AddProductToCatalogEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
							Serialization.serialize(catalogProduct)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException e) {
			fail();
		}
		
		try {
			manager.addProductToCatalog(catalogProduct);
		} catch (CriticalError | InvalidParameter | ProductAlreadyExistInCatalog
				| ConnectionFailure e) {
			
			fail();
		} catch (EmployeeNotConnected ¢) {
			/* test success */
		}
	}
	
	@Test
	public void AddProductToCatalogProductAlreadyExistInCatalogTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
							Serialization.serialize(catalogProduct)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_ALREADY_EXISTS).serialize());
		} catch (IOException e) {
			fail();
		}
		
		try {
			manager.addProductToCatalog(catalogProduct);
		} catch (CriticalError | InvalidParameter | EmployeeNotConnected
				| ConnectionFailure e) {
			
			fail();
		} catch (ProductAlreadyExistInCatalog ¢) {
			/* test success */
		}
	}
	
	@Test
	public void AddProductToCatalogIllegalResultTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
							Serialization.serialize(catalogProduct)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_GROCERY_LIST_IS_EMPTY).serialize());
		} catch (IOException e) {
			fail();
		}
		
		try {
			manager.addProductToCatalog(catalogProduct);
		} catch (ProductAlreadyExistInCatalog | InvalidParameter | EmployeeNotConnected
				| ConnectionFailure e) {
			
			fail();
		} catch (CriticalError ¢) {
			/* test success */
		}
	}
}