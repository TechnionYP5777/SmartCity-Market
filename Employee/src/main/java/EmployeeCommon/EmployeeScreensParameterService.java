package EmployeeCommon;

import javax.inject.Singleton;

import CommonDefs.CLIENT_TYPE;

@Singleton
public class EmployeeScreensParameterService implements IEmployeeScreensParameterService {

	private CLIENT_TYPE client;

	@Override
	public void setClientType(CLIENT_TYPE ¢) {
		this.client = ¢;

	}

	@Override
	public CLIENT_TYPE getClientType() {
		return client;
	}

}
