package EmployeeGui;

import CommonDefs.GuiDefs;
import EmployeeDefs.AEmployeeException.*;
import GuiUtils.DialogMessagesService;
import GuiUtils.GuiExceptionHandler;
import SMExceptions.SMException;

public class EmployeeGuiExeptionHandler extends GuiExceptionHandler {
	public static void handle(SMException ¢) {
		if (¢ instanceof AuthenticationError)
			DialogMessagesService.showErrorDialog(GuiDefs.loginFailureDialogTitle, null,
					GuiDefs.wrongUserNamePasswordFailureMsg);
		else if (¢ instanceof ProductNotExistInCatalog)
			DialogMessagesService.showErrorDialog(GuiDefs.productOperationFailureTitle, null,
					GuiDefs.productNotExistsInCatalogMsg);
		else if (¢ instanceof ProductAlreadyExistInCatalog)
			DialogMessagesService.showErrorDialog(GuiDefs.productOperationFailureTitle, null,
					GuiDefs.productAlreadyExistsInCatalogMsg);
		else if (¢ instanceof ProductStillForSale)
			DialogMessagesService.showErrorDialog(GuiDefs.productOperationFailureTitle, null,
					GuiDefs.productStillForSaleMsg);
		else if (¢ instanceof AmountBiggerThanAvailable)
			DialogMessagesService.showErrorDialog(GuiDefs.productOperationFailureTitle, null,
					GuiDefs.productCapacityIsNotEnoughMsg);
		else if (¢ instanceof ConnectionFailure)
			DialogMessagesService.showErrorDialog(GuiDefs.productOperationFailureTitle, null,
					GuiDefs.connectionFailureMsg);
		else if (!(¢ instanceof ProductPackageDoesNotExist))
			DialogMessagesService.showErrorDialog(GuiDefs.criticalErrorTitle, null,
					GuiDefs.criticalErrorMsg);
		
		else
			DialogMessagesService.showErrorDialog(GuiDefs.productOperationFailureTitle, null,
					GuiDefs.productPackageDoesNotExistMsg);
	}
}
