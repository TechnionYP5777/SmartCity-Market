package CommonDefs;

import GuiUtils.DialogMessagesService;
import SMExceptions.SMException;

/**
 * This class define all the Exceptions {@link GroceryList} could throw.
 * 
 * @author Lior Ben Ami
 * @since 2017-01-05
 */
public class GroceryListExceptions extends SMException {

	private static final long serialVersionUID = 0x5E13EDCB0D5A33A5L;
	/**
	 * Thrown when one of the parameters is illegal.
	 */
	public static class InvalidParameter extends SMException {
		private static final long serialVersionUID = -0x32F9853CD5BA357DL;

		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.criticalErrorTitle, GuiCommonDefs.unexpectedFailureMsg,
					GuiCommonDefs.invalidParamFailureMsg);
		}
	}
	
	/**
	 * Thrown when the given product isn't in the grocery list
	 */
	public static class ProductNotInList extends SMException {
		private static final long serialVersionUID = -0xB937D4B98EA05F1L;

		@Override
		public void showInfoToUser() {
			/* empty print */
		}
	}
	
	/**
	 * Thrown when the given amount of specific product is bigger then available in the grocery list
	 */
	public static class AmountIsBiggerThanAvailable extends SMException {
		private static final long serialVersionUID = -0xD16CC0A4B18DBACL;

		@Override
		public void showInfoToUser() {
			/* empty print */
		}
	}

	@Override
	public void showInfoToUser() {
		/* empty print */
	}
	
}