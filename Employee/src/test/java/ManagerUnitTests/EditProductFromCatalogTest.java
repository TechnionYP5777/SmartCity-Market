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
import EmployeeDefs.AEmployeeException.CriticalError;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.AEmployeeException.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Lior Ben Ami,Aviad Cohen
 * @since 2016-01-03 */
@RunWith(MockitoJUnitRunner.class)
public class EditProductFromCatalogTest {
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
	public void EditProductFromCatalogSuccesfulTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
							Serialization.serialize(catalogProduct)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException e) {
			fail();
		}
		
		try {
			manager.editProductFromCatalog(catalogProduct);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ProductNotExistInCatalog | ConnectionFailure e) {
			
			fail();
		}
	}
	
	@Test
	public void EditProductFromCatalogInvalidParameterTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
							Serialization.serialize(catalogProduct)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		} catch (IOException e) {
			fail();
		}
		try {
			manager.editProductFromCatalog(catalogProduct);
			
			fail();
		} catch (CriticalError | EmployeeNotConnected | ProductNotExistInCatalog | ConnectionFailure e) {
			
			fail();
		} catch (InvalidParameter ¢) {
			/* test success */
		}
	}
	
	@Test
	public void EditProductFromCatalogEmployeeNotConnectedTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
							Serialization.serialize(catalogProduct)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException e) {
			fail();
		}
		
		try {
			manager.editProductFromCatalog(catalogProduct);
		} catch (InvalidParameter | CriticalError | ProductNotExistInCatalog | ConnectionFailure e) {
			
			fail();
		} catch (EmployeeNotConnected ¢) {
			/* test success */
		}
	}
	
	@Test
	public void EditProductFromCatalogProductNotExistInCatalogTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
							Serialization.serialize(catalogProduct)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST).serialize());
		} catch (IOException e) {
			fail();
		}
		
		try {
			manager.editProductFromCatalog(catalogProduct);
		} catch (InvalidParameter | CriticalError | EmployeeNotConnected | ConnectionFailure e) {
			
			fail();
		} catch (ProductNotExistInCatalog ¢) {
			/* test success */
		}
	}
	
	@Test
	public void EditProductFromCatalogIllegalResultTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
							Serialization.serialize(catalogProduct)).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_INGREDIENT_STILL_IN_USE).serialize());
		} catch (IOException e) {
			fail();
		}
		
		try {
			manager.editProductFromCatalog(catalogProduct);
		} catch (InvalidParameter | ProductNotExistInCatalog | EmployeeNotConnected | ConnectionFailure e) {
			
			fail();
		} catch (CriticalError ¢) {
			/* test success */
		}
	}
}