package EmployeeDefs;

import CommonDefs.GuiCommonDefs;
import GuiUtils.DialogMessagesService;
import SMExceptions.SMException;

/**
 * This class define all the Exceptions {@link AEmployee} could throw.
 * 
 * @author Aviad Cohen
 * @since 2016-12-26
 */
public class AEmployeeException extends SMException {
	
	

	private static final long serialVersionUID = 8663454971017928972L;

	/**
	 * Thrown when invalid (unsupported) command was sent by worker.
	 *
	 */
	public static class InvalidCommandDescriptor extends AEmployeeException {

		private static final long serialVersionUID = -6013813473192780331L;
	}
	
	/**
	 * Thrown when one of the parameters is illegal.
	 *
	 */
	public static class InvalidParameter extends AEmployeeException {

		private static final long serialVersionUID = 6423454812287539804L;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.ganeralDialogTitle, null,
					GuiCommonDefs.invalidParamFailureMsg);
		}

	}
	
	
	/**
	 * Thrown when worker try to do operation before connecting
	 *
	 */
	public static class EmployeeNotConnected extends AEmployeeException {

		private static final long serialVersionUID = 736110846693773042L;	
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.workerOprDialofTitle, null, 
					GuiCommonDefs.workerNotConnected);
		}
	}

	/**
	 * Thrown when worker try to connect twice
	 * 
	 */
	public static class EmployeeAlreadyConnected extends AEmployeeException {

		private static final long serialVersionUID = -4880371025977064054L;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.workerOprDialofTitle, null, 
					GuiCommonDefs.workerIsAlreadyConnected);
		}
	}
	
	/**
	 * Thrown when worker not exist in database or password don't match
	 *
	 */
	public static class AuthenticationError extends AEmployeeException {

		private static final long serialVersionUID = -3035025128800993047L;
			
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.loginFailureDialogTitle, null,
					GuiCommonDefs.wrongUserNamePasswordFailureMsg);
		}
	}
	
	/**
	 * Thrown when requesting product that not exist in catalog
	 *
	 */
	public static class ProductNotExistInCatalog extends AEmployeeException {

		private static final long serialVersionUID = -1218770191837502028L;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productNotExistsInCatalogMsg);
		}
	}
	
	/**
	 * Thrown when requesting product already exist in catalog
	 *
	 */
	public static class ProductAlreadyExistInCatalog extends AEmployeeException {

		private static final long serialVersionUID = 231336810775816368L;
		
		@Override
		public void showInfoToUser() {	
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productAlreadyExistsInCatalogMsg);
		}
	}
	
	/**
	 * Thrown when requesting to remove product which still in stock for sale.
	 *
	 */
	public static class ProductStillForSale extends AEmployeeException {

		private static final long serialVersionUID = -6441045369608147333L;
		
		@Override
		public void showInfoToUser() {	
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productStillForSaleMsg);
		}
	}
	
	/**
	 * Thrown when requesting amount bigger than available.
	 *
	 */
	public static class AmountBiggerThanAvailable extends AEmployeeException {

		private static final long serialVersionUID = -6977996976396033467L;
			
		@Override
		public void showInfoToUser() {	
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productCapacityIsNotEnoughMsg);
		}
	}
	
	/**
	 * Thrown when product package does not exist.
	 *
	 */
	public static class ProductPackageDoesNotExist extends AEmployeeException {

		private static final long serialVersionUID = -1086760036316621806L;
		
		@Override
		public void showInfoToUser() {	
			DialogMessagesService.showErrorDialog(GuiCommonDefs.productOperationFailureTitle, null,
					GuiCommonDefs.productPackageDoesNotExistMsg);
		}

	}
	
	/**
	 * Thrown when failed to receive respond from server.
	 *
	 */
	public static class ConnectionFailure extends AEmployeeException {

		private static final long serialVersionUID = -7116035947026187101L;

		@Override
		public void showInfoToUser() {	
			DialogMessagesService.showErrorDialog(GuiCommonDefs.criticalErrorTitle, GuiCommonDefs.connectionFailureMsg,
					GuiCommonDefs.criticalErrorMsg);
		}

	}
	
	/**
	 * Thrown when trying to add worker which it's username already exists.
	 *
	 */
	public static class WorkerAlreadyExists extends AEmployeeException {

		private static final long serialVersionUID = 1110701675869392627L;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.managerOprDialofTitle, null, 
					GuiCommonDefs.workerIsAlreadyInSys);
		}
	}
	
	/**
	 * Thrown when trying to remove worker which it's username doesn't exist.
	 *
	 */
	public static class WorkerDoesNotExist extends AEmployeeException {

		private static final long serialVersionUID = -5920340047010353992L;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.managerOprDialofTitle, null, 
					GuiCommonDefs.emplIsNotInSys);
		}
	}
	
	/**
	 * Thrown when trying to add item which it's ID already exists.
	 *
	 */
	public static class ParamIDAlreadyExists extends AEmployeeException {

		private static final long serialVersionUID = -3169770231713956064L;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.managerOprDialofTitle, null, 
					GuiCommonDefs.itemIsInSys);
		}
	}
	
	/**
	 * Thrown when trying to add item which it's ID does not exist.
	 *
	 */
	public static class ParamIDDoesNotExist extends AEmployeeException {

		private static final long serialVersionUID = -4286591069692064061L;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.managerOprDialofTitle, null, 
					GuiCommonDefs.itemIsNotInSys);
		}
	}
	
	/**
	 * Thrown when trying to remove ingredient which still in use.
	 *
	 */
	public static class IngredientStillInUse extends AEmployeeException {

		private static final long serialVersionUID = -1228674600657566979L;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.managerOprDialofTitle, null, 
					GuiCommonDefs.ingIsInUse);
		}
	}
	
	/**
	 * Thrown when trying to remove manufacturer which still in use.
	 *
	 */
	public static class ManfacturerStillInUse extends AEmployeeException {

		private static final long serialVersionUID = 907342426645603127L;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.managerOprDialofTitle, null, 
					GuiCommonDefs.manuIsInUse);
		}
	}
}
