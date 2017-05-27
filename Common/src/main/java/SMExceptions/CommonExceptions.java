package SMExceptions;

import CommonDefs.GuiCommonDefs;
import GuiUtils.DialogMessagesService;


/**
 * CommonExceptions - here we define common exceptions 
 * @author Shimon Azulay
 * @since 21.5.17
 */
public class CommonExceptions extends SMException {
	
	private static final long serialVersionUID = -0x5A017A1CD122C372L;

	/**
	 * Thrown when unexpected error occurred
	 *
	 */
	public static class CriticalError extends CommonExceptions {

		private static final long serialVersionUID = -0x11C82633E497958AL;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.criticalErrorTitle, GuiCommonDefs.unexpectedFailureMsg,
					GuiCommonDefs.criticalErrorMsg);
		}
	}

	@Override
	public void showInfoToUser() {
		/* empty print */
	}

}
