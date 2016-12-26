package WorkerUnitTests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Manufacturer;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeExceptions.CriticalError;
import EmployeeDefs.AEmployeeExceptions.InvalidParameter;
import EmployeeDefs.AEmployeeExceptions.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeExceptions.UnknownSenderID;
import EmployeeDefs.AEmployeeExceptions.WorkerNotConnected;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)
public class ViewProductFromCatalogTest {

	private IWorker worker;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		worker = new Worker(clientRequestHandler);
	}

	@Test
	public void ViewProductFromCatalogSuccessfulTest() {
		CatalogProduct testCatalogProduct = null;
		CatalogProduct catalogProduct = new CatalogProduct(1234567890, "name", null,
				new Manufacturer(1, "Manufacturer"), "description", 22.0, null, null);
		CommandWrapper commandWrapper = new CommandWrapper(ResultDescriptor.SM_OK,
				Serialization.serialize(catalogProduct));
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(new SmartCode(1234567890, null))).serialize())))
					.thenReturn(commandWrapper.serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		try {
			testCatalogProduct = worker.viewProductFromCatalog(1234567890);
		} catch (InvalidParameter | UnknownSenderID | CriticalError | WorkerNotConnected | ProductNotExistInCatalog e) {
			e.printStackTrace();
			fail();
		}
		
		assertEquals(testCatalogProduct.getBarcode(), 1234567890);
		assertEquals(testCatalogProduct.getManufacturer().getId(), 1);
		assertEquals(testCatalogProduct.getManufacturer().getName(), "Manufacturer");
		assertEquals(testCatalogProduct.getName(), "name");
	}

	@Test
	public void ViewProductFromCatalogproductNotFoundTest() {
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(new SmartCode(1234567890, null))).serialize())))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST).serialize());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			worker.viewProductFromCatalog(1234567890);
		} catch (InvalidParameter | UnknownSenderID | CriticalError | WorkerNotConnected e) {
			e.printStackTrace();
			fail();
		} catch (ProductNotExistInCatalog e) {
			/* Test Passed */
		}
	}
}
