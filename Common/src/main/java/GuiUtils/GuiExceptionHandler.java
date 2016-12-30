package GuiUtils;

import SMExceptions.SMException;

/**
 * 
 * GuiExceptionHandler is our generic way for dealing with exceptions that are caught in GUI level.
 * Each module we have that interacts with the GUI should implement this handler. (for example: Employee module should have EmployeeGuiHandler)
 * @author idan atias
 *
 */

public abstract class GuiExceptionHandler {

	public abstract void handle(SMException e);
}
