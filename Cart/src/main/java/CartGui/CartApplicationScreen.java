package CartGui;

import org.apache.log4j.PropertyConfigurator;

import BasicCommonClasses.CartProduct;
import CartDI.CartDiConfigurator;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * CartApplicationScreen - This class is the stage GUI class for Cart
 * 
 * @author Lior Ben Ami
 * @since 2017-01-13 */
public class CartApplicationScreen extends AbstractApplicationScreen {

	BarcodeEventHandler barcodeEventHandler;

	public class IntCellFormat  extends ListCell<Integer>
	{
		@Override
		public void updateItem(Integer item, boolean empty)
		{
			super.updateItem(item, empty);

			this.setText("int: " + Integer.toString(item));
			setGraphic(null);
		}
	}

	
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			InjectionFactory.createInjector(new CartDiConfigurator());
			
			barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);
			barcodeEventHandler.initializeHandler();
			barcodeEventHandler.startListening();

			setScene("/CartWelcomeScreen/CartWelcomeScreen.fxml");
			stage.setTitle("Smart Market Beta");
			stage.setMaximized(true);
			stage.show();
			
			ObservableList<Integer> productsObservableList = FXCollections.<Integer>observableArrayList(new Integer(0), new Integer(1));
			
			ListView<Integer> cartList = new ListView<Integer>();

			cartList.setItems(productsObservableList);
			cartList.setCellFactory(new Callback<ListView<Integer>, ListCell<Integer>>() {
			     @Override public ListCell<Integer> call(ListView<Integer> list) {
			         return new IntCellFormat();
			     }
			 });

		} catch (Exception ¢) {
			¢.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/* Setting log properties */
		PropertyConfigurator.configure("../log4j.properties");
		
		launch(args);
	}
}
