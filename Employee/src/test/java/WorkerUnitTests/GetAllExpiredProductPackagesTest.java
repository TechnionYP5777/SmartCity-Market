package WorkerUnitTests;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeException.ConnectionFailure;
import EmployeeDefs.AEmployeeException.EmployeeNotConnected;
import EmployeeDefs.AEmployeeException.InvalidParameter;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import SMExceptions.CommonExceptions.CriticalError;
import UtilsContracts.IClientRequestHandler;
import UtilsContracts.IForgotPasswordHandler;
import UtilsImplementations.Serialization;
import UtilsImplementations.ForgotPasswordHandler.NoSuchUserName;
import UtilsImplementations.ForgotPasswordHandler.WrongAnswer;

/**
 * This class test worker.getAllExpiredProductPackages method 
 * 
 * @author Lior Ben Ami
 * @since 2017-06-29
 */
@RunWith(MockitoJUnitRunner.class)
public class GetAllExpiredProductPackagesTest { 
	
	private IWorker worker;

	HashSet<ProductPackage> expiredPackages = new HashSet<ProductPackage> (), 
			expiredPackagesTest = new HashSet<ProductPackage> (); 
	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		worker = new Worker(clientRequestHandler);
		
		LocalDate ld1 = LocalDate.of(2017,06,29), ld2 =  LocalDate.of(2017,06,30);
		SmartCode sc1 = new SmartCode(1234567, ld1), sc2 = new SmartCode(1234567, ld2);
		Location lc = new Location(1, 1, PlaceInMarket.STORE);
		ProductPackage p1 = new ProductPackage(sc1, 1, lc), p2 = new ProductPackage(sc2, 3, lc);
		expiredPackages.addAll(Arrays.asList(p1, p2));
	}
	
	@Test
	public void getAllExpiredProductPackagesSuccess() {
		try {
			
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId,
					CommandDescriptor.GET_ALL_EXPIRED_PRODUCT_PACKAGES).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK, Serialization.serialize(expiredPackages)).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			expiredPackagesTest = worker.getAllExpiredProductPackages();
		} catch (ConnectionFailure | CriticalError| EmployeeNotConnected e) {
			fail();
		}
		if (!expiredPackagesTest.containsAll(expiredPackages))
			fail();
	}
	
	@Test
	public void getAllExpiredProductPackagesCriticalError() {
		try {
			
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId,
					CommandDescriptor.GET_ALL_EXPIRED_PRODUCT_PACKAGES).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR, Serialization.serialize(expiredPackages)).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			expiredPackagesTest = worker.getAllExpiredProductPackages();
		} catch ( ConnectionFailure| EmployeeNotConnected e) {
			fail();
		} catch (CriticalError e) {
			//succeed
		}
	}
	
	@Test
	public void getAllExpiredProductPackagesConnectionFailure() {
		try {
			
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId,
					CommandDescriptor.GET_ALL_EXPIRED_PRODUCT_PACKAGES).serialize()))
					.thenThrow(new SocketTimeoutException());
		} catch (IOException ¢) {
			fail();
		}
		try {
			expiredPackagesTest = worker.getAllExpiredProductPackages();
		} catch ( CriticalError | EmployeeNotConnected e) {
			fail();
		} catch (ConnectionFailure e) {
			//succeed
		}
	}
	
	@Test
	public void getAllExpiredProductPackagesEmployeeNotConnected() {
		try {
			
			Mockito.when(clientRequestHandler.sendRequestWithRespond(new CommandWrapper(WorkerDefs.loginCommandSenderId,
					CommandDescriptor.GET_ALL_EXPIRED_PRODUCT_PACKAGES).serialize()))
					.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, Serialization.serialize(expiredPackages)).serialize());
		} catch (IOException ¢) {
			fail();
		}
		try {
			expiredPackagesTest = worker.getAllExpiredProductPackages();
		} catch ( CriticalError | ConnectionFailure e) {
			fail();
		} catch (EmployeeNotConnected e) {
			//succeed
		}
	}
	
}
