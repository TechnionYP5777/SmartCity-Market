package WorkerUnitTests;

import static org.junit.Assert.assertEquals;

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
		CatalogProduct catalogProduct = new CatalogProduct(1234567890, "name", null,
				new Manufacturer(1, "Manufacturer"), "description", 22.0, null);
		CommandWrapper commandWrapper = new CommandWrapper(ResultDescriptor.SM_OK,
				Serialization.serialize(catalogProduct));
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(new SmartCode(1234567890, null))).serialize())))
					.thenReturn(commandWrapper.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		CatalogProduct testCatalogProduct = worker.viewProductFromCatalog(1234567890);
		assertEquals(testCatalogProduct.getBarcode(), 1234567890);
		assertEquals(testCatalogProduct.getManufacturer().getId(), 1);
		assertEquals(testCatalogProduct.getManufacturer().getName(), "Manufacturer");
		assertEquals(testCatalogProduct.getName(), "name");
	}

	@Test
	public void ViewProductFromCatalogproductNotFoundTest() {
		CommandWrapper commandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
		try {
			Mockito.when(clientRequestHandler.sendRequestWithRespond(
					(new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.VIEW_PRODUCT_FROM_CATALOG,
							Serialization.serialize(new SmartCode(1234567890, null))).serialize())))
					.thenReturn(commandWrapper.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			worker.viewProductFromCatalog(1234567890);
		} catch (RuntimeException e) {
			assertEquals(e.getMessage(), WorkerDefs.loginCmdCatalogProductNotFound);
		}
	}
}
