package CommonDefs;

import GuiUtils.DialogMessagesService;
import SMExceptions.SMException;

/**
 * This class define all the Exceptions {@link CustomerProfile} could throw.
 * 
 * @author Idan Atias
 * @since 2016-12-09
 */
public class CustomerProfileException extends SMException {

	private static final long serialVersionUID = 0x5E13EDCB0D5A33A5L;

	public static class InvalidParameter extends SMException{

		/**
		 * Thrown when one of the parameters is illegal.
		 */
		private static final long serialVersionUID = -0x48DF51694A3699AFL;

		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.criticalErrorTitle, GuiCommonDefs.unexpectedFailureMsg,
					GuiCommonDefs.invalidParamFailureMsg);
		}
		
	}
	
	public static class EmptyAllergensSet extends SMException{

		/**
		 * Thrown when trying to remove ingredients from empty list
		 */
		private static final long serialVersionUID = -0x5F5FB37F5A7FEDB9L;

		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.criticalErrorTitle, GuiCommonDefs.unexpectedFailureMsg,
					GuiCommonDefs.emptyAllergensSet);
		}

	}

	@Override
	public void showInfoToUser() {
		/* empty print */
	}
	
}