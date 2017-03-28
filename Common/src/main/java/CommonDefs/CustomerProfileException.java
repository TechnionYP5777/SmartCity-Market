package CommonDefs;

import SMExceptions.SMException;

/**
 * This class define all the Exceptions {@link CustomerProfile} could throw.
 * 
 * @author Idan Atias
 */
public class CustomerProfileException extends SMException {

	private static final long serialVersionUID = 6779023320480887717L;

	public static class InvalidParameter extends SMException{

		/**
		 * Thrown when one of the parameters is illegal.
		 */
		private static final long serialVersionUID = -5251005203195795887L;
		
	}
	
	public static class EmptyAllergensSet extends SMException{

		/**
		 * Thrown when trying to remove ingredients from empty list
		 */
		private static final long serialVersionUID = -6872408915951218105L;

	}
	
}