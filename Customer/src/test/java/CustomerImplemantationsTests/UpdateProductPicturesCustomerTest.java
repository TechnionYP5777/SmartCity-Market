package CustomerImplemantationsTests;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerImplementations.Customer;
import CustomerImplementations.Customer.UpdateProductPictures;
import PicturesHandler.PictureManager;
import UtilsContracts.IClientRequestHandler;
import UtilsImplementations.Serialization;

/**
 * UpdateProductPicturesCustomerTest - This tests the UpdateProductPictures functionality for Customer module
 * @author idan atias
 * @since May 23, 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateProductPicturesCustomerTest {
	Customer c;
	UpdateProductPictures updateProductPicturesThread;
	
	//This is the enconding string in Base64 for a zipfile with a txt file named "just_for_testing"
	String encodedZipFile4Testing = String.valueOf(
			"UEsDBAoAAAAAAMFurEoAAAAAAAAAAAAAAAAUAAAAanVzdF9mb3JfdGVzdGluZy50eHRQSwECPwAKAAAAAADBbqxKAAAAAAAAAAAAAAAAFAAkAAAAAAAAACAAAAAAAAAAanVzdF9mb3JfdGVzdGluZy50eHQKACAAAAAAAAEAGAA02b8PDsvSATTZvw8Oy9IBNNm/Dw7L0gFQSwUGAAAAAAEAAQBmAAAAMgAAAAAA");
	
	static String createdFilePath = String.valueOf("../Common/src/main/resources/ProductsPictures/just_for_testing.txt");
	static boolean doCleanUp = true;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		c = new Customer(clientRequestHandler);
		updateProductPicturesThread = c.new UpdateProductPictures();
	}
	
	@AfterClass
	public static void cleanup() {
		if (doCleanUp)
			try {
				File fileToDelete = new File(createdFilePath);
				fileToDelete.delete();
			} catch (Exception e) {
				System.out.println(e + "");
				fail();
			}
	}

	@Test
	public void updateIsNotNeededTest() {
		try {
			LocalDate currentPicturesDate = PictureManager.getCurrentDate();
			Mockito.when(clientRequestHandler
					.sendRequestWithRespond((new CommandWrapper(c.getId(), CommandDescriptor.UPDATE_PRODUCTS_PICTURES,
							Serialization.serialize(currentPicturesDate))).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_NO_UPDATE_NEEDED, Serialization.serialize(null))
							.serialize());

		} catch (IOException ¢) {
			fail();
		}
		try {
			updateProductPicturesThread.start();
			updateProductPicturesThread.join();
		} catch (Exception e) {
			System.out.println(e + "");
			fail();
		}
	}

	@Test
	public void updateIsNeededTest() {
		try {
			LocalDate currentPicturesDate = PictureManager.getCurrentDate();
			Mockito.when(clientRequestHandler
					.sendRequestWithRespond((new CommandWrapper(c.getId(), CommandDescriptor.UPDATE_PRODUCTS_PICTURES,
							Serialization.serialize(currentPicturesDate))).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(encodedZipFile4Testing))
							.serialize());

		} catch (IOException ¢) {
			fail();
		}
		try {
			updateProductPicturesThread.start();
			updateProductPicturesThread.join();
		} catch (Exception e) {
			System.out.println(e + "");
			fail();
		}
	}
}