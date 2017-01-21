package EmployeeGui;

import org.apache.log4j.PropertyConfigurator;
import com.sun.javafx.application.LauncherImpl;
import EmployeeDI.EmployeeDiConfigurator;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.stage.Stage;

/**
 * EmployeeApplicationScreen - This class is the stage GUI class for Employee
 * 
 * @author Shimon Azulay
 * @since 2016-12-26
 */

@SuppressWarnings("restriction")
public class EmployeeApplicationScreen extends AbstractApplicationScreen {

	BarcodeEventHandler barcodeEventHandler;

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			InjectionFactory.createInjector(new EmployeeDiConfigurator());
			barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);
			barcodeEventHandler.initializeHandler();
			barcodeEventHandler.startListening();		
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
		
		/* lunching program with preloader */
        LauncherImpl.launchApplication(EmployeeApplicationScreen.class, EmployeeGuiPreloader.class, args);
	}

}
