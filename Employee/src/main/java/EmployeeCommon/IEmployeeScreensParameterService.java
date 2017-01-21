package EmployeeCommon;

import CommonDefs.CLIENT_TYPE;

public interface IEmployeeScreensParameterService {

	void setClientType(CLIENT_TYPE client);

	public CLIENT_TYPE getClientType();

}
