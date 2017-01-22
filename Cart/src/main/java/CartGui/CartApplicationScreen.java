package CartGui;

import org.apache.log4j.PropertyConfigurator;

import CartDI.CartDiConfigurator;
import CartImplemantations.CartDefs;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.application.Platform;
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
			
			stage.setOnCloseRequest(event -> 
				{
					Platform.exit();
					System.exit(0);
				}
			);
			
		} catch (Exception ¢) {
			¢.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/* Setting log properties */
		PropertyConfigurator.configure("../log4j.properties");
		
		/* Allowing to change IP and port from command line */
		if (args.length >= 1)
			CartDefs.host = args[0];
		
		if (args.length >= 2)
			CartDefs.port = Integer.parseInt(args[1]);
		
		launch(args);
	}
}
