package ManagerUnitTests;

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
import EmployeeDefs.AEmployeeException.ProductStillForSale;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * @author Lior Ben Ami
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
	public void RemoveProductFromCatalogSuccesfulTest() throws IOException, InvalidParameter, CriticalError, EmployeeNotConnected, ProductStillForSale, ProductNotExistInCatalog {
		Mockito.when(clientRequestHandler.sendRequestWithRespond(
				(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
						CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
						Serialization.serialize(pp)).serialize())))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		manager.removeProductFromCatalog(pp);
	}
	
	@Test
	public void RemoveProductFromCatalogInvalidParameterTest() throws IOException, CriticalError, EmployeeNotConnected, ProductStillForSale, ProductNotExistInCatalog {
		Mockito.when(clientRequestHandler.sendRequestWithRespond(
				(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
						CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
						Serialization.serialize(pp)).serialize())))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_INVALID_PARAMETER).serialize());
		try {
			manager.removeProductFromCatalog(pp);
		} catch (InvalidParameter ¢) {
			//test success
		}
	}
	
	@Test
	public void AddProductToCatalogEmployeeNotConnectedTest() throws IOException, ProductStillForSale, CriticalError, InvalidParameter, ProductNotExistInCatalog {
		Mockito.when(clientRequestHandler.sendRequestWithRespond(
				(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
						CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
						Serialization.serialize(pp)).serialize())))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		try {
			manager.removeProductFromCatalog(pp);
		} catch (EmployeeNotConnected ¢) {
			//test success
		}
	}
	
	@Test
	public void AddProductToCatalogProductNotExistInCatalogTest() throws IOException, ProductStillForSale, CriticalError, InvalidParameter, EmployeeNotConnected {
		Mockito.when(clientRequestHandler.sendRequestWithRespond(
				(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
						CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
						Serialization.serialize(pp)).serialize())))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST).serialize());
		try {
			manager.removeProductFromCatalog(pp);
		} catch (ProductNotExistInCatalog ¢) {
			//test success
		}
	}
	
	@Test
	public void AddProductToCatalogProductStillForSaleTest() throws IOException, ProductNotExistInCatalog, CriticalError, InvalidParameter, EmployeeNotConnected {
		Mockito.when(clientRequestHandler.sendRequestWithRespond(
				(new CommandWrapper(WorkerDefs.loginCommandSenderId, 
						CommandDescriptor.REMOVE_PRODUCT_FROM_CATALOG,
						Serialization.serialize(pp)).serialize())))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST).serialize());
		try {
			manager.removeProductFromCatalog(pp);
		} catch (ProductStillForSale ¢) {
			//test success
		}
	}
}
