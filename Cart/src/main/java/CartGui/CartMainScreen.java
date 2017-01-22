package CartGui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.google.common.eventbus.Subscribe;
import BasicCommonClasses.CartProduct;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.SmartCode;
import CartContracts.ACartExceptions.AmountBiggerThanAvailable;
import CartContracts.ACartExceptions.CartNotConnected;
import CartContracts.ACartExceptions.CriticalError;
import CartContracts.ACartExceptions.ProductNotInCart;
import CartContracts.ACartExceptions.ProductPackageDoesNotExist;
import CartContracts.ICart;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.SmartcodeScanEvent;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * CartMainScreen - Controller for main screen which holds the main operations
 * available for cart during shopping.
 * 
 * @author Lior Ben Ami
 * @since 2017-01-11
 */
public class CartMainScreen implements Initializable {

	Stage primeStage = CartApplicationScreen.stage;

	ICart cart;

	// Main screen panes
	@FXML
	GridPane cartMainScreenPane;

	@FXML
	ListView<CartProduct> productsListView;

	@FXML
	TextField productsNumberTextField;

	@FXML
	TextField totalSumTextField;

	@FXML
	GridPane productInfoPane;

	@FXML
	Label productNameLabel;

	@FXML
	Label manufacturerLabel;

	@FXML
	Label priceLabel;

	@FXML
	Label amountLabel;

	@FXML
	TextArea descriptionTextArea;

	@FXML
	Button addButton;

	@FXML
	Button removeButton;

	@FXML
	Button removeAllButton;

	SmartCode scannedSmartCode = null;
	
	CatalogProduct catalogProduct;
	
	ObservableList<CartProduct> productsObservableList = FXCollections.<CartProduct>observableArrayList();

	// Lock smartCodeLocker;

	IBarcodeEventHandler barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);

	/**
	 * the productInfoPane has three visible mode: 
	 * 0 - unvisible (default) 
	 * 1 - SCANNED_PRODUCT: when the pane present scanned product info and actions 
	 * 2 - PRESSED_PRODUCT: when the pane present pressed (on the listView Cell) product info and actions
	 */
	private static enum ProductInfoPaneVisibleMode {
		SCANNED_PRODUCT, PRESSED_PRODUCT
	}

	private void addOrRemoveScannedProduct(CatalogProduct catalogProduct, Integer amount) {
		updateProductInfoPaine(catalogProduct, amount, ProductInfoPaneVisibleMode.SCANNED_PRODUCT);
	}

	private void updateProductInfoPaine(CatalogProduct catalogProduct, Integer amount,
			ProductInfoPaneVisibleMode mode) {
//		productNameLabel.setText(catalogProduct.getName());
//		manufacturerLabel.setText(catalogProduct.getManufacturer().getName());
//		priceLabel.setText(String.format("%1$.2f", catalogProduct.getPrice()));
//		amountLabel.setText(amount.toString());
		descriptionTextArea.setText(/*catalogProduct.getDescription()*/ "Test");
		switch (mode) {
		case SCANNED_PRODUCT: {
			removeAllButton.setDisable(true);
			removeAllButton.setVisible(false);
			addButton.setDisable(false);
			addButton.setVisible(true);
			removeButton.setDisable(false);
			removeButton.setVisible(true);
			break;
		}
		case PRESSED_PRODUCT: {
			removeAllButton.setDisable(false);
			removeAllButton.setVisible(true);
			addButton.setDisable(true);
			addButton.setVisible(false);
			removeButton.setDisable(true);
			removeButton.setVisible(false);
			break;
		}
		}
		setAbilityAndVisibilityOfProductInfoPane(true);
	}
	
	private void setAbilityAndVisibilityOfProductInfoPane(boolean visibilty) {
		productInfoPane.setDisable(!visibilty);
		productInfoPane.setVisible(visibilty);
	}

	private void updateCartProductsInfo() {
		updateProductsList();
		productsNumberTextField.setText(cart.getCartProductsNum().toString());
		totalSumTextField.setText(cart.getTotalSum().toString());
	}

	private void updateProductsList() {
		// TODO Auto-generated method stub
		
	}

	private void logoutAndExit() {
		try {
			cart.logout();
			Platform.exit();
			System.exit(0);
		} catch (SMException e) {
			Platform.exit();
			System.exit(0);
		}
		TempCartPassingData.cart =  null;
		AbstractApplicationScreen.setScene("/CartWelcomeScreen/CartWelcomeScreen.fxml");		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(cartMainScreenPane);
		barcodeEventHandler.register(this);

		
		cart = TempCartPassingData.cart;

		productsListView.setEditable(true);
		productsListView.setItems(productsObservableList);
		productsListView.setCellFactory(new Callback<ListView<CartProduct>, ListCell<CartProduct>>() {
			@Override
			public ListCell<CartProduct> call(ListView<CartProduct> list) {
				return new CartProductCellFormat();
			}
		});
		// TODO Enable this:
		//setAbilityOfProductInfoPane(false);
		descriptionTextArea.setEditable(false);
		productsNumberTextField.setEditable(false);
		totalSumTextField.setEditable(false);

		// defining behavior when stage/window is closed.
		primeStage.setOnCloseRequest(event -> 
			logoutAndExit());
	}

	@FXML
	public void purchaseButtonPressed(ActionEvent __) {
		try {
			cart.checkOutGroceryList();
		} catch (SMException e) {
			CartGuiExceptionsHandler.handle(e);	
			return;
		}
		logoutAndExit();
	}

	@FXML
	public void cancelButtonPressed(ActionEvent __) {
		logoutAndExit();
	}

	public void addButtonPressed(ActionEvent __) {
		try {
			cart.addProductToCart(scannedSmartCode, 1);
		} catch (SMException e) {
			CartGuiExceptionsHandler.handle(e);	
			return;
		}
		HashMap<Long, CartProduct> shoppingList = cart.getCartProductCache();
		productsObservableList.clear();
		shoppingList.forEach((key,value) -> {
			productsObservableList.add(value);
		});

		updateCartProductsInfo();
		setAbilityAndVisibilityOfProductInfoPane(false);
	}

	public void removeButtonPressed(ActionEvent __) {
		try {
			cart.returnProductToShelf(scannedSmartCode, 1);
		} catch (SMException e) {
			CartGuiExceptionsHandler.handle(e);	
			return;
		}
		updateCartProductsInfo();
		setAbilityAndVisibilityOfProductInfoPane(false);
	}

	public void removeAllButtonPressed(ActionEvent __) {
		try {
			cart.removeAllItemsOfCartProduct(scannedSmartCode);
		} catch (SMException e) {
			CartGuiExceptionsHandler.handle(e);	
			return;
		}
		updateCartProductsInfo();
		setAbilityAndVisibilityOfProductInfoPane(false);
	}

	private void smartcodeScannedHandler() {
		Integer amount;
		CartProduct cartPtoduct = cart.getCartProduct(scannedSmartCode);
		if (cartPtoduct == null) { // the product isn't in the cart
			try {
				catalogProduct = cart.viewCatalogProduct(scannedSmartCode);
			} catch (SMException e) {
				CartGuiExceptionsHandler.handle(e);	
				return;
			}
			amount = 0;
		} else { // the product is in the cart
			catalogProduct = cartPtoduct.getCatalogProduct();
			amount = cartPtoduct.getTotalAmount();
		}
		addOrRemoveScannedProduct(catalogProduct, amount);		
	}
	
	@Subscribe
	public void smartcodeScanned(SmartcodeScanEvent ¢) {
		SmartCode smartCode = ¢.getSmarCode();
		scannedSmartCode = smartCode;
		smartcodeScannedHandler();
	}
}
