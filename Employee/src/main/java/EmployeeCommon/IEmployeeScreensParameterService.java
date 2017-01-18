package EmployeeCommon;

import CommonDefs.CLIENT_TYPE;
import EmployeeContracts.IWorker;

public interface IEmployeeScreensParameterService {

	<T extends IWorker> T getParameter();

	void updateClientType(CLIENT_TYPE client);

	public CLIENT_TYPE getClientType();

}
