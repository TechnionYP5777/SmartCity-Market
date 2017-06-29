package CommonDefs;

/**
 * GuiCommonDefs - definition of gui common constants.
 *  
 * @author Aviad Cohen
 * @since 2017-02-03 
 *
 */
public class GuiCommonDefs {
	
	public static final String smartCodePrompt = "Type Or Scan Product SmartCode...";
	
	public static final String barcodeCodePrompt = "Type Or Scan Product Barcode...";
	
	public static final String typeBarcode = "Insert valid barcode and press Enter key";
	
	public static final String typeSmartCode = "Insert valid smartCode and press Enter key";
	
	public static final String notEnoughAmountInWarehouse = "There is not enough amount in warehouse";
	
	public static final String notEnoughAmountInStore = "There is not enough amount in store";
	
	public static final String productDoesNotExistInDatabase = "The product doesn't exist in the Market database, please contact the supermarket manager";
	
	public static final String loginFailureDialogTitle = "Login failue!";
	
	public static final String ganeralDialogTitle = "Failue!";
	
	public static final String workerOprDialofTitle = "Worker opeartion failure!";
	
	public static final String managerOprDialofTitle = "Manager opeartion failure!";

	public static final String productOperationFailureTitle = "Product operation failure!";
	
	public static final String customerFailure = "Customer failure!";
	
	public static final String criticalErrorTitle = "INTERNAL ERROR! Please contact a the supermarket management";
	
	public static final String scanFailureDialogTitle = "Scan failure!";
	
	public static final String purchaseOperationFailureTitle = "Purchse operation failure!";
	
	public static final String registrationFieldFailureTitle = "Registartion field failure!";
	//GUI dialog messages
	
	//final public static String userAlreadyConnectedFailureMessage = "The user is already connected.";
	//TODO : (by idan to shimon) i think this case is for critical error, what do you say?
	
	public static final String userIsNotConnected = "The user is not connected.";
	
	public static final String invalidParamFailureMsg = "One or more inserted parameters are invalid.";
	
	public static final String wrongUserNamePasswordFailureMsg = "Wrong user name or password, please try again.";
		
	public static final String productNotExistsInCatalogMsg = "Could not find this barcode in the product catalog, please check your input.";
	
	public static final String productAlreadyExistsInCatalogMsg = "This Barcode is already in the catalog, please check your input.";
	
	public static final String productStillForSaleMsg = "This product is still for sale!";
	
	public static final String productCapacityIsNotEnoughMsg = "There are not enough items of this product, please validate the inserted amount.";
	
	public static final String connectionFailureMsg = "Failed to send respond to server";
	
	public static final String unexpectedFailureMsg = "Unexpected error occurred.";
	
	public static final String productPackageDoesNotExistMsg = "There is no such product package, please check the inserted SmartCode.";
	
	public static final String productNotLeftInStore = "There is no items of these product left in store, please check the scanned SmartCode.";
	
	public static final String productNotInCartMsg = "There is no such product in the cart!";
	
	public static final String groceryListIseEmptyMsg = "The cart is empty.";
	
	public static final String criticalErrorMsg = "Please contact SmartMarket support at smart.market.cs@gmail.com";

	public static final String registrationWrongRepeatedPass = "Wrong password. Repeted password doesn't match original password!";

	public static final String registrationUsedUserName = "This user name already in used. Please try another one.";
	
	public static final String workerNotConnected = "The user is not connected.";
	
	public static final String workerIsAlreadyConnected = "The user is already connected.";
	
	public static final String workerIsAlreadyInSys = "Worker is already in the system, Choose other username.";
	
	public static final String emplIsNotInSys = "The choosen employee is not in system";
	
	public static final String itemIsInSys = "The item is already in system.";
	
	public static final String itemIsNotInSys = "The item is not in the system.";
	
	public static final String itemStillInUse = "The item is still in use.";
	
	public static final String ingIsInUse = "The ingredient is still in use.";
	
	public static final String manuIsInUse = "The manufacturer is still in use.";
	
	public static final String usernameDoesNotExist = "The username doesn't exist in the system.";
	
	public static final String wrongAnswerGiven = "The answer is wrong, please try again.";
	
	public static final String emptyAllergensSet = "No allergens to remove.";
	
	public static final String logFilePermissionsHeader = "File Error";
	
	public static final String logFilePermissions = "Could not delete old log file. Please delete it manually and restart the application";
	
	public static String productsPicturesFolderPath = String.valueOf("../Common/src/main/resources/ProductsPictures");
	
	public static String productsPicturesPathLastUpdate = String.valueOf("../Common/src/main/resources/ProductsPictures/LastUpdate.txt");
	
	public static String productsPicturesFolderZipFile = String.valueOf("../Common/src/main/resources/PicturesZipFile/pictures.zip");
	
	public static String productsCustomerPicturesFolderZipFile = String.valueOf("../Common/src/main/resources/PicturesZipFile/customer_pictures.zip");
	
	public static String storeMapPicPath = String.valueOf("https://github.com/TechnionYP5777/SmartCity-Market/blob/master/Common/src/main/resources/storeMap.png");
}
