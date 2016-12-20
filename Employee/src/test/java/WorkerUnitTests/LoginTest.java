package WorkerUnitTests;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import ClientServerCommunication.ClientRequestHandler;
import EmployeeContracts.IWorker;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Worker;
import UtilsContracts.IClientRequestHandler;
import UtilsContracts.ISerialization;
import UtilsImplementations.Serialization;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {

	private IWorker worker;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Mock
	private ISerialization serialization;

	@Before
	public void setup() {
		worker = new Worker(serialization, clientRequestHandler);
	}
	
	@Test
	public void loginSuccessfulTest() {
//		CommandWrapper commandWrapper = new CommandWrapper(ResultDescriptor.SM_OK,)
//		Mockito.when(serialization.serialize(toSerialize));
//		worker.login("test", "test");
//		try {
//			Mockito.verify(clientRequestHandler).createSocket(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
//		} catch (UnknownHostException | RuntimeException e) {
//			e.printStackTrace();
//		}	
	}
		
	@Test
	public void loginWrongUsernameTest() {
			
	}
	
	@Test
	public void loginWrongPasswordTest() {
		
	}
	
	@Test
	public void loginAlreadyConnectedTest() {


		
	}
	
	
	

}
