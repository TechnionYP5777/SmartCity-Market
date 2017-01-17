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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * CartMainScreen - Controller for main screen which holds the main operations available for cart durring shopping.
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
	Button addProductButton;
	
	@FXML 
	Button removeProductButton;
		
	@FXML
	ListView<CartProduct> productsListView;
	
	@FXML
	Button logoutButton;
	
	@FXML
	Button buyButton;
	
	@FXML
	TextField productsNumberTextField;
	
	@FXML
	TextField totalSumTextField;
	
	SmartCode lastScannedSmartCode = null;
	
	Lock smartCodeLocker;
	
	
	ObservableList<CartProduct> productsObservableList;
	
	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(cartMainScreenPane);
		//defining behavior when stage/window is closed.
		cart = TempCartPassingData.cart;
		
		productsObservableList.addAll(cart.getCartProductCache().values());
		productsListView.setItems(productsObservableList);
		productsListView.setCellFactory(new Callback<ListView<CartProduct>, ListCell<CartProduct>>() {
		     @Override public ListCell<CartProduct> call(ListView<CartProduct> list) {
		         return new CartProductFormatCell();
		     }
		 });
		
		
		primeStage.setOnCloseRequest(event -> {
			try {
				cart.logout();
			} catch (SMException e){
				//todo: continue from here
			}
		});
	}
	
	@FXML
	public void removeProductButtonPressed(ActionEvent __) {
		waitForScannerEvent();
		SmartCode sc = lastScannedSmartCode;
		smartCodeLocker.unlock();
		//todo: continue from here

	}
	
	@FXML
	public void addProductButtonPressed(ActionEvent __) {
		waitForScannerEvent();
		SmartCode sc = lastScannedSmartCode;
		smartCodeLocker.unlock();
		//todo: continue from here

	}
	
	@FXML
	public void logoutButtonPressed(ActionEvent __) {
		try {
			cart.logout();
		} catch (CartNotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CriticalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//todo: continue from here

	}

	@FXML
	public void buyButtonPressed(ActionEvent __) {
		try {
			cart.checkOutGroceryList();
		} catch (CriticalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CartNotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//todo: continue from here

	}
	
	private void waitForScannerEvent() {
		// TODO Auto-generated method stub
		
	}
	
	@Subscribe
	public void barcodeScanned(BarcodeScanEvent ¢) {
		
		SmartCode sc = new SmartCode(1, LocalDate.now());/* = new SmartCode(¢.getBarcode(),  ¢.getExpirationDate*)*/;
		if (smartCodeLocker.tryLock()) {
			lastScannedSmartCode = sc;
		}
		//todo: continue from here

	}
}
