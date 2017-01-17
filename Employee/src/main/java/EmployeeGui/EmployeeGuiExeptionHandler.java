package EmployeeGui;

import EmployeeDefs.EmployeeGuiDefs;
import EmployeeDefs.AEmployeeException.*;
import GuiUtils.DialogMessagesService;
import GuiUtils.GuiExceptionHandler;
import SMExceptions.SMException;

public class EmployeeGuiExeptionHandler extends GuiExceptionHandler {
	public static void handle(SMException ¢) {
		if (¢ instanceof AuthenticationError)
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.loginFailureDialogTitle, null,
					EmployeeGuiDefs.wrongUserNamePasswordFailureMsg);
		else if (¢ instanceof ProductNotExistInCatalog)
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.productOperationFailureTitle, null,
					EmployeeGuiDefs.productNotExistsInCatalogMsg);
		else if (¢ instanceof ProductAlreadyExistInCatalog)
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.productOperationFailureTitle, null,
					EmployeeGuiDefs.productAlreadyExistsInCatalogMsg);
		else if (¢ instanceof ProductStillForSale)
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.productOperationFailureTitle, null,
					EmployeeGuiDefs.productStillForSaleMsg);
		else if (¢ instanceof AmountBiggerThanAvailable)
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.productOperationFailureTitle, null,
					EmployeeGuiDefs.productCapacityIsNotEnoughMsg);
		else if (¢ instanceof ConnectionFailure)
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.productOperationFailureTitle, null,
					EmployeeGuiDefs.connectionFailureMsg);
		else if (!(¢ instanceof ProductPackageDoesNotExist))
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.criticalErrorTitle, null,
					EmployeeGuiDefs.criticalErrorMsg);
		
		else
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.productOperationFailureTitle, null,
					EmployeeGuiDefs.productPackageDoesNotExistMsg);
	}
}
