package EmployeeGui;

import CommonDefs.GuiCommonDefs;
import EmployeeDefs.AEmployeeException.*;
import GuiUtils.DialogMessagesService;
import GuiUtils.GuiExceptionHandler;
import SMExceptions.SMException;

public class EmployeeGuiExeptionHandler extends GuiExceptionHandler {
	public static void handle(SMException ¢) {
		if (¢ instanceof AuthenticationError)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.loginFailureDialogTitle, null,
					GuiCommonDefs.wrongUserNamePasswordFailureMsg);
		else if (¢ instanceof ProductNotExistInCatalog)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productNotExistsInCatalogMsg);
		else if (¢ instanceof ProductAlreadyExistInCatalog)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productAlreadyExistsInCatalogMsg);
		else if (¢ instanceof ProductStillForSale)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productStillForSaleMsg);
		else if (¢ instanceof AmountBiggerThanAvailable)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productCapacityIsNotEnoughMsg);
		else if (¢ instanceof ConnectionFailure)
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.connectionFailureMsg);
		else if (!(¢ instanceof ProductPackageDoesNotExist))
			DialogMessagesService.showErrorDialog(GuiCommonDefs.criticalErrorTitle, null,
					GuiCommonDefs.criticalErrorMsg);
		
		else
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productPackageDoesNotExistMsg);
	}
}
