package EmployeeCommon;

import CommonDefs.CLIENT_TYPE;

/**
 * IEmployeeScreensParameterService - This class represents class chooses which screen to show - manager or worker
 * 
 * @author Aviad Cohen
 * @since 2017-01-04
 *
 */
public interface IEmployeeScreensParameterService {

	void setClientType(CLIENT_TYPE e);

	CLIENT_TYPE getClientType();
	
	void setNotShowMainScreenVideo(boolean show);
	
	boolean getNotShowMainScreenVideo();

}
