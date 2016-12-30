package EmployeeGui;

import EmployeeDefs.EmployeeGuiDefs;
import EmployeeDefs.AEmployeeException.*;
import GuiUtils.DialogMessagesService;
import GuiUtils.GuiExceptionHandler;
import SMExceptions.SMException;

public class EmployeeGuiExeptionHandler extends GuiExceptionHandler {
	static public void handle(SMException e) {
		//TODO - get each content of an "if block" to external handler
		// for example - authenticationErrorHandler should replace the first "if" block
		// this for enabling more than just showing a dialog
		
		if (e instanceof AuthenticationError) {
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.loginFailureDialogTitle, null,
					EmployeeGuiDefs.wrongUserNamePasswordFailureMsg);

		} else if (e instanceof ProductNotExistInCatalog) {
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.productOperationFailureTitle, null,
					EmployeeGuiDefs.productNotExistsInCatalogMsg);

		} else if (e instanceof ProductAlreadyExistInCatalog){
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.productOperationFailureTitle, null,
					EmployeeGuiDefs.productAlreadyExistsInCatalogMsg);

		} else if (e instanceof ProductStillForSale){
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.productOperationFailureTitle, null,
					EmployeeGuiDefs.productStillForSaleMsg);
		
		} else if (e instanceof AmountBiggerThanAvailable){
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.productOperationFailureTitle, null,
					EmployeeGuiDefs.productCapacityIsNotEnoughMsg);
		
		} else if (e instanceof ProductPackageDoesNotExist){
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.productOperationFailureTitle, null,
					EmployeeGuiDefs.productPackageDoesNotExistMsg);
		
		} else {
			//Critical Errors
			//CriticalError, InvalidCommandDescriptor, InvalidParameter, UnknownSenderID
			//EmployeeNotConnected, EmployeeAlreadyConnected
			DialogMessagesService.showErrorDialog(EmployeeGuiDefs.criticalErrorTitle, null,
					EmployeeGuiDefs.criticalErrorMsg);
		}
	}
}
