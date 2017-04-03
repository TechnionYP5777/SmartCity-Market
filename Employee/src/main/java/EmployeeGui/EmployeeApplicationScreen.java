package EmployeeGui;

import org.apache.log4j.PropertyConfigurator;
import com.sun.javafx.application.LauncherImpl;

import CommonDI.CommonDiConfigurator;
import EmployeeCommon.EmployeeScreensParameterService;
import EmployeeCommon.IEmployeeScreensParameterService;
import EmployeeDI.EmployeeDiConfigurator;
import GuiUtils.AbstractApplicationScreen;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import javafx.application.Platform;
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
	
	static boolean show;

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			InjectionFactory.createInjector(new EmployeeDiConfigurator(), new CommonDiConfigurator());
			
			IEmployeeScreensParameterService employeeScreensParameterService = InjectionFactory
					.getInstance(EmployeeScreensParameterService.class);
			
			employeeScreensParameterService.setNotShowMainScreenVideo(show);
			barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);
			barcodeEventHandler.initializeHandler();
			barcodeEventHandler.startListening();
			setScene("/EmployeeMainScreen/EmployeeMainScreen.fxml");
			stage.setTitle("Smart Market Beta");
			stage.setMaximized(true);
				
			stage.show();

			stage.setOnCloseRequest(event -> {
				Platform.exit();
				System.exit(0);
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
			
		if (args.length == 1 && args[0].equals("show")) {
			show = true;
		} else if (args.length == 1 && args[0].equals("notShow")) {
			show = false;
		} else {
			throw new RuntimeException("Wrong parameters! please "
					+ "choose: show or notShow");
		}

		/* Setting log properties */
		PropertyConfigurator.configure("../log4j.properties");

		/* lunching program with preloader */
		LauncherImpl.launchApplication(EmployeeApplicationScreen.class, EmployeeGuiPreloader.class, args);
	}

}
