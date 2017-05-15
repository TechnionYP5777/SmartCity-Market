package CustomerGuiHelpers;

import CommonDefs.GuiCommonDefs;
import CustomerContracts.ACustomerExceptions.*;
import GuiUtils.DialogMessagesService;
import GuiUtils.GuiExceptionHandler;
import SMExceptions.SMException;

/**
 * 
 * CustomerGuiExceptionsHandler - Handles the gui customer exceptions
 * 
 * @author Lior Ben Ami
 * @since 2017-01-20
 */
public class CustomerGuiExceptionsHandler extends GuiExceptionHandler {
	public static void handle(SMException ¢) {
		if (¢ instanceof InvalidCommandDescriptor)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.criticalErrorTitle, null,
					GuiCommonDefs.criticalErrorMsg);
		else if (¢ instanceof InvalidParameter)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.criticalErrorTitle, null,
					GuiCommonDefs.criticalErrorMsg);
		else if (¢ instanceof CriticalError)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.criticalErrorTitle, null,
					GuiCommonDefs.criticalErrorMsg);
		else if (¢ instanceof AuthenticationError)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.loginFailureDialogTitle, null,
					GuiCommonDefs.criticalErrorMsg);
		else if (¢ instanceof CustomerNotConnected)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.loginFailureDialogTitle, null,
					GuiCommonDefs.criticalErrorMsg);
		else if (¢ instanceof ProductNotInCart)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productNotInCartMsg);
		else if (¢ instanceof AmountBiggerThanAvailable)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.notEnoughAmountInStore, null,
					GuiCommonDefs.productCapacityIsNotEnoughMsg);
		else if (¢ instanceof ProductPackageDoesNotExist)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productNotExistsInCatalogMsg);
		else if (¢ instanceof GroceryListIsEmpty)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.purchaseOperationFailureTitle, null,
					GuiCommonDefs.groceryListIseEmptyMsg);
		else
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, "",
					!(¢ instanceof ProductCatalogDoesNotExist) ? GuiCommonDefs.criticalErrorTitle
							: GuiCommonDefs.productDoesNotExistInDatabase);
	}
}
