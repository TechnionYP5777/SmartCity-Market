package EmployeeGui;

import org.apache.log4j.PropertyConfigurator;

import GuiUtils.AbstractApplicationScreen;
import UtilsContracts.BarcodeEventHandlerDiConfigurator;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.stage.Stage;

/**
 * EmployeeApplicationScreen - This class is the stage GUI class for Employee
 * 
 * @author Shimon Azulay
 * @since 2016-12-26
 */

public class EmployeeApplicationScreen extends AbstractApplicationScreen {

	BarcodeEventHandler barcodeEventHandler;

	@Override
	public void start(Stage primaryStage) {
		try {


			stage = primaryStage;
			barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class,
					new BarcodeEventHandlerDiConfigurator());
			// barcodeEventHandler.initializeHandler();
			// barcodeEventHandler.startListening();			
			setScene("/EmployeeMainScreen/EmployeeMainScreen.fxml");
			stage.setTitle("Smart Market Beta");
			stage.setMaximized(true);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/* Setting log properties */
		PropertyConfigurator.configure("../log4j.properties");

		launch(args);
	}

}
