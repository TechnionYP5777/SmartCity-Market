package CartGuiHelpers;

import CartContracts.ACartExceptions.*;
import CommonDefs.GuiCommonDefs;
import GuiUtils.DialogMessagesService;
import GuiUtils.GuiExceptionHandler;
import SMExceptions.SMException;

public class CartGuiExceptionsHandler extends GuiExceptionHandler {
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
		else if (¢ instanceof CartNotConnected)
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
		else if (¢ instanceof ProductCatalogDoesNotExist)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productNotExistsInCatalogMsg);
		else
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productPackageDoesNotExistMsg);
	}
}
