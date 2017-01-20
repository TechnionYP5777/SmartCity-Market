package CartGui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;

import com.google.common.eventbus.Subscribe;
import BasicCommonClasses.CartProduct;
import BasicCommonClasses.SmartCode;
import CartContracts.ACartExceptions.CartNotConnected;
import CartContracts.ACartExceptions.CriticalError;
import CartContracts.ICart;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import UtilsContracts.BarcodeScanEvent;
import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.SmartcodeScanEvent;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * CartMainScreen - Controller for main screen which holds the main operations
 * available for cart durring shopping.
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
	ListView<Integer> productsListView;

	@FXML
	TextField productsNumberTextField;

	@FXML
	TextField totalSumTextField;

	@FXML
	GridPane productInfoPane;
	Button removeAllItemsButton;

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

	SmartCode lastScannedSmartCode = null;

	Lock smartCodeLocker;

	IBarcodeEventHandler barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(cartMainScreenPane);
		barcodeEventHandler.register(this);

		// productInfoPane.setDisable(false);
		// productInfoPane.setVisible(false);

		cart = TempCartPassingData.cart;
		// productsObservableList.addAll(cart.getCartProductCache().values());

		// defining behavior when stage/window is closed.


		primeStage.setOnCloseRequest(event -> {
			try {
				cart.logout();
			} catch (SMException e) {
				// todo: continue from here
			}
		});
	}

	@FXML
	public void purchaseButtonPressed(ActionEvent __) {
		try {
			cart.checkOutGroceryList();
		} catch (CriticalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CartNotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// todo: continue from here
	}

	@FXML
	public void cancelButtonPressed(ActionEvent __) {
		// TODO Auto-generated method stub

	}

	public void addButtonPressed(ActionEvent __) {
		// todo: continue from here
	}

	public void removeButtonPressed(ActionEvent __) {
		// todo: continue from here
	}

	public void removeAllButtonPressed(ActionEvent __) {
		// todo: continue from here
	}

	@Subscribe
	public void smartcoseScanned(SmartcodeScanEvent ¢) {

		SmartCode smartcode = ¢.getSmarCode();
		// System.out.println("scanned Smart Code: " + smartcode);

		Alert alert = new Alert(AlertType.INFORMATION, "scanned Smart Code: " + smartcode);
		alert.showAndWait();
		// todo: from here

	}
}
