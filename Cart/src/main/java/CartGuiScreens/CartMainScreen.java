package CartGuiScreens;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;


import com.google.common.eventbus.Subscribe;
import BasicCommonClasses.CartProduct;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.SmartCode;
import CartContracts.ICart;
import CartGuiHelpers.CartGuiExceptionsHandler;
import CartGuiHelpers.CartProductCellFormat;
import CartGuiHelpers.TempCartPassingData;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.SmartcodeScanEvent;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

	@FXML
	ImageView productInfoImage;
	
	SmartCode scannedSmartCode;
	
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
	private enum ProductInfoPaneVisibleMode {
		SCANNED_PRODUCT, PRESSED_PRODUCT
	}

	private void addOrRemoveScannedProduct(CatalogProduct p, Integer amount) {
		
		updateProductInfoPaine(p, amount, ProductInfoPaneVisibleMode.SCANNED_PRODUCT);
	}

	private void updateProductInfoPaine(CatalogProduct p, Integer amount,
			ProductInfoPaneVisibleMode m) {
		updateProductInfoTexts(p, amount);
		switch (m) {
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
	
	private void updateProductInfoTexts(CatalogProduct p, Integer amount) {
		productNameLabel.setText(p.getName());
		manufacturerLabel.setText(p.getManufacturer().getName());
		priceLabel.setText(String.format("%1$.2f", p.getPrice()));
		amountLabel.setText((amount + ""));
		descriptionTextArea.setText(p.getDescription());	
		URL imageUrl = null;
		try {
			imageUrl = new File("../Common/src/main/resources/ProductsPictures/" + p.getBarcode() + ".jpg").toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		productInfoImage.setImage((new Image(imageUrl + "", 290, 230, true, false)));
	}

	private void setAbilityAndVisibilityOfProductInfoPane(boolean visibilty) {
		productInfoPane.setDisable(!visibilty);
		productInfoPane.setVisible(visibilty);
	}

	private void updateCartProductsInfo() {
		syncListViewWithCart();
		productsNumberTextField.setText((cart.getCartProductsNum() + ""));
		totalSumTextField.setText((cart.getTotalSum() + ""));
	}
	
	private void syncListViewWithCart() {
		HashMap<Long, CartProduct> shoppingList = cart.getCartProductCache();
		productsObservableList.clear();
		shoppingList.forEach((key,value) -> {
			productsObservableList.add(value);
		});
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
			public ListCell<CartProduct> call(ListView<CartProduct> __) {
				return new CartProductCellFormat();
			}
		});
		
		productsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CartProduct>() {
		   
			@Override
			public void changed(ObservableValue<? extends CartProduct> __, CartProduct oldValue,
					CartProduct newValue) {
		    	updateProductInfoPaine(newValue.getCatalogProduct(), newValue.getTotalAmount(), ProductInfoPaneVisibleMode.PRESSED_PRODUCT);
				
			}
		});
		setAbilityAndVisibilityOfProductInfoPane(false);
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
		if (cartPtoduct != null) {
			catalogProduct = cartPtoduct.getCatalogProduct();
			amount = cartPtoduct.getTotalAmount();
		} else {
			try {
				catalogProduct = cart.viewCatalogProduct(scannedSmartCode);
			} catch (SMException e) {
				CartGuiExceptionsHandler.handle(e);
				return;
			}
			amount = 0;
		}
		
		
		Platform.runLater(new Runnable() {	
			@Override
			public void run() {
				addOrRemoveScannedProduct(catalogProduct, amount);	
			}
		});
	}
	
	@Subscribe
	public void smartcodeScanned(SmartcodeScanEvent ¢) {
		SmartCode smartCode = ¢.getSmarCode();
		scannedSmartCode = smartCode;
		smartcodeScannedHandler();
	}
}
