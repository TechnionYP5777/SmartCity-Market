package EmployeeCommon;

import CommonDefs.CLIENT_TYPE;

public interface IEmployeeScreensParameterService {

	void setClientType(CLIENT_TYPE e);

	CLIENT_TYPE getClientType();

}
