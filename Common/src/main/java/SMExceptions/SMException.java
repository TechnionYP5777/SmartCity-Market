package SMExceptions;

/**
 * SMException is a base class that all our module-based-exceptions should inherit from. (like AEmployeeException)
 * This allows us a more generic way for catching & handling our exceptions.
 * @author idan atias
 *
 */

public class SMException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6206029164886167974L;
	
	//currently no further logic in here.
	//if you find something common between all module-base-exception add this here.
}
