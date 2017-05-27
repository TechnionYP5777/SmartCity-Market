package SMExceptions;

/**
 * SMException is a base class that all our module-based-exceptions should inherit from. (like AEmployeeException)
 * This allows us a more generic way for catching & handling our exceptions.
 * @author idan atias Shimon Azulay
 * @since  2017-01-02
 */

public abstract class SMException extends Exception{

	private static final long serialVersionUID = 0x56203EA4FEC111A6L;
	
	//currently no further logic in here.
	//if you find something common between all module-base-exception add this here.
	
	/**
	 * Should be implement if you want to show the info to the user by using the dialog service 
	 */
	public abstract void showInfoToUser();
}
