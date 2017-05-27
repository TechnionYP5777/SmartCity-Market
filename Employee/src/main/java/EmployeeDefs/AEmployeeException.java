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
	
	

	private static final long serialVersionUID = 0x783AC6652B1DA10CL;

	/**
	 * Thrown when invalid (unsupported) command was sent by worker.
	 *
	 */
	public static class InvalidCommandDescriptor extends AEmployeeException {

		private static final long serialVersionUID = -0x53755B7DC71D222BL;
	}
	
	/**
	 * Thrown when one of the parameters is illegal.
	 *
	 */
	public static class InvalidParameter extends AEmployeeException {

		private static final long serialVersionUID = 0x5924B21E6D2C1A5CL;
		
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

		private static final long serialVersionUID = 0xA3730EA3C7E8EF2L;	
		
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

		private static final long serialVersionUID = -0x43BA8F973F78BA76L;
		
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

		private static final long serialVersionUID = -0x2A1E93463E913F17L;
			
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

		private static final long serialVersionUID = -0x10E9F1083B4F1A4CL;
		
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

		private static final long serialVersionUID = 0x335DF9A8DC3A8B0L;
		
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

		private static final long serialVersionUID = -0x596330A335AB1185L;
		
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

		private static final long serialVersionUID = -0x60D6D3476DD5DDBBL;
			
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

		private static final long serialVersionUID = -0xF14F27EC0BE2BEEL;
		
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

		private static final long serialVersionUID = -0x62C13CFDCF68035DL;

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

		private static final long serialVersionUID = 0xF6A014A19A04EF3L;
		
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

		private static final long serialVersionUID = -0x522945E7CB925348L;
		
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

		private static final long serialVersionUID = -0x2BFD493B48EB00E0L;
		
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

		private static final long serialVersionUID = -0x3B7D07DDD65B713DL;
		
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

		private static final long serialVersionUID = -0x110D210A1CBD3D03L;
		
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

		private static final long serialVersionUID = 0xC97872135DA2337L;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.managerOprDialofTitle, null, 
					GuiCommonDefs.manuIsInUse);
		}
	}

	@Override
	public void showInfoToUser() {
		/* empty print */
	}
}
