package EmployeeCommon;

import javax.inject.Singleton;

import CommonDefs.CLIENT_TYPE;
import EmployeeContracts.IWorker;
import EmployeeImplementations.Manager;
import EmployeeImplementations.Worker;
import UtilsImplementations.InjectionFactory;

@Singleton
public class EmployeeScreensParameterService implements IEmployeeScreensParameterService {

	CLIENT_TYPE client;

	@SuppressWarnings({ "unchecked" })
	@Override
	public <T extends IWorker> T getParameter() {
		if (client.equals(CLIENT_TYPE.WORKER)) {
			return (T) InjectionFactory.getInstance(Worker.class);
		}
		return (T) InjectionFactory.getInstance(Manager.class);
	}

	@Override
	public void updateClientType(CLIENT_TYPE client) {
		this.client = client;

	}
	
	@Override
	public CLIENT_TYPE getClientType() {
		return client;

	}

}
