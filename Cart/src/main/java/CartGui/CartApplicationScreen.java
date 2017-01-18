package CartGui;

import org.apache.log4j.PropertyConfigurator;

import CartDI.CartDiConfigurator;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.stage.Stage;

/**
 * CartApplicationScreen - This class is the stage GUI class for Cart
 * 
 * @author Lior Ben Ami
 * @since 2017-01-13 */
public class CartApplicationScreen extends AbstractApplicationScreen {

	BarcodeEventHandler barcodeEventHandler;

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
