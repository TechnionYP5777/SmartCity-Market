package CommandHandlerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

import BasicCommonClasses.CatalogProduct;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.IngredientNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerNotExist;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;

@RunWith(MockitoJUnitRunner.class)
public class EditProductFromCatalogTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void editCatalogProductSuccessfulTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 1.5, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).editProductInCatalog(senderID, catalogProduct);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | IngredientNotExist
				| ManufacturerNotExist e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void editCatalogProductInvalidParamTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, -1, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).editProductInCatalog(senderID, catalogProduct);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | IngredientNotExist
				| ManufacturerNotExist e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER, out.getResultDescriptor());
	}
	
	@Test
	public void editCatalogProductCriticalErrorTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 4, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).editProductInCatalog(senderID, catalogProduct);
		} catch (ClientNotConnected | ProductNotExistInCatalog | IngredientNotExist
				| ManufacturerNotExist e1) {
			fail();
		} catch (CriticalError __) {
			/* Success */
		}
				
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void editCatalogProductIllegalCatalogProductTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
	
	@Test
	public void editCatalogProductClientNotConnectedTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 4, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).editProductInCatalog(senderID, catalogProduct);
		} catch (CriticalError | ProductNotExistInCatalog | IngredientNotExist
				| ManufacturerNotExist e1) {
			fail();
		} catch (ClientNotConnected __) {
			/* Success */
		}
				
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
	
	@Test
	public void editCatalogProductNotExistInCatalogTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 4, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ProductNotExistInCatalog()).when(sqlDatabaseConnection).editProductInCatalog(senderID, catalogProduct);
		} catch (CriticalError | ClientNotConnected | IngredientNotExist
				| ManufacturerNotExist e1) {
			fail();
		} catch (ProductNotExistInCatalog __) {
			/* Success */
		}
				
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST, out.getResultDescriptor());
	}
	
	@Test
	public void editCatalogProductIngredientNotExistTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 4, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new IngredientNotExist()).when(sqlDatabaseConnection).editProductInCatalog(senderID, catalogProduct);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog
				| ManufacturerNotExist e1) {
			fail();
		} catch (IngredientNotExist __) {
			/* Success */
		}
				
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER, out.getResultDescriptor());
	}
	
	@Test
	public void editCatalogProductManufacturerNotExistTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 4, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.EDIT_PRODUCT_FROM_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ManufacturerNotExist()).when(sqlDatabaseConnection).editProductInCatalog(senderID, catalogProduct);
		} catch (CriticalError | ClientNotConnected | ProductNotExistInCatalog | IngredientNotExist e1) {
			fail();
		} catch (ManufacturerNotExist __) {
			/* Success */
		}
				
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER, out.getResultDescriptor());
	}
}