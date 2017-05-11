package GuiUtils;

import SMExceptions.NotImplementedError;
import SMExceptions.SMException;

/**
 * 
 * GuiExceptionHandler is our generic way for dealing with exceptions that are caught in GUI level.
 * Each module we have that interacts with the GUI should implement this handler. (for example: Employee module should have EmployeeGuiHandler)
 * @author idan atias
 * @since 2016-12-27
 *
 */

public abstract class GuiExceptionHandler {

	public static void handle(SMException __) throws NotImplementedError {
		throw new NotImplementedError();
	}
}
