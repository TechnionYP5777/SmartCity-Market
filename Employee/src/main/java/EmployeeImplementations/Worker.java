package EmployeeImplementations;

import java.net.UnknownHostException;
import java.util.logging.Level;

import ClientCommon.ClientCommunicationHandler;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import EmployeeContracts.IWorker;
import EmployeeDefs.WorkerDefs;

/** Worker - This class represent the worker functionality implementation.
 * 
 * @author Shimon Azulay
 * @since 2016-12-17 */

public class Worker extends ClientCommunicationHandler implements IWorker {

	@Override
	public void login(String username, String password) {
		try {
			LOGGER.log(Level.FINE,
					"Establish communication with server: port: " + WorkerDefs.port + " host: " + WorkerDefs.host);
			// TODO AVIAD: ask about the timeout mili or sec?
			establishCommunication(WorkerDefs.port, WorkerDefs.host, WorkerDefs.timeout);
		} catch (UnknownHostException | RuntimeException e) {
			// TODO handle exceptions
			e.printStackTrace();
		}

		LOGGER.log(Level.FINE,
				"Creating login command wrapper with username: " + username + " and password: " + password);
		@SuppressWarnings("unused")
		CommandWrapper commandWrapper = new CommandWrapper(WorkerDefs.loginCommandSenderId, CommandDescriptor.LOGIN);

		// TODO - FOR IDAN: HOW TO SET DATA STRING ?
		// TODO - FOR AVIAD: I WANT SYNC REQUEST, SOULD SEND GSON OR STRING?

	}

	@Override
	public void logout() {
		LOGGER.log(Level.FINE, "Terminate the communiction with server");
		terminateCommunication();
	}

	@Override
	public int viewProductFromCatalog() {
		// TODO WHAT SHOUD IT GET FOR PARAMETER ?
		return 0;	
	}

}
