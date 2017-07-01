package EmployeeGui;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.PropertyConfigurator;
import com.sun.javafx.application.LauncherImpl;

import CommonDI.CommonDiConfigurator;
import EmployeeCommon.EmployeeScreensParameterService;
import EmployeeCommon.IEmployeeScreensParameterService;
import EmployeeContracts.IWorker;
import EmployeeDI.EmployeeDiConfigurator;
import EmployeeDefs.WorkerDefs;
import EmployeeImplementations.Manager;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.StackTraceUtil;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * EmployeeApplicationScreen - This class is the stage GUI class for Employee
 * 
 * @author Shimon Azulay
 * @since 2016-12-26
 * 
 */
@SuppressWarnings("restriction")
public class EmployeeApplicationScreen extends AbstractApplicationScreen {

	static Boolean disableVideo;
	
	BarcodeEventHandler barcodeEventHandler;

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			InjectionFactory.createInjector(new EmployeeDiConfigurator(), new CommonDiConfigurator());

			IEmployeeScreensParameterService employeeScreensParameterService = InjectionFactory
					.getInstance(EmployeeScreensParameterService.class);

			employeeScreensParameterService.setNotShowMainScreenVideo(!disableVideo);
			barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);
			barcodeEventHandler.initializeHandler();
			barcodeEventHandler.startListening();
			setScene("/EmployeeMainScreen/EmployeeMainScreen.fxml");
			stage.setTitle("Smart Market Beta");
			stage.setMaximized(true);

			stage.setOnCloseRequest(event -> {
				try {
					IWorker worker = InjectionFactory.getInstance(Manager.class);
					if (worker.isLoggedIn())
						worker.logout();
					event.consume();
					Platform.exit();
					System.exit(0);
				} catch (SMException e) {
					log.fatal(e);
					log.debug(StackTraceUtil.stackTraceToStr(e));
					e.showInfoToUser();
					Platform.exit();
					System.exit(0);
				}
			});

			stage.show();

		} catch (Exception e) {
			log.fatal(e);
			log.debug(StackTraceUtil.stackTraceToStr(e));
		}
	}

	private static boolean parseArguments(String[] args) {
		WorkerDefs.port = 2000;
		
        Options options = new Options();

        Option portOption = new Option("p", "port", true, "Server port");
        options.addOption(portOption);
        
        Option disableVideoOption = new Option("d", "disableVideo", false, "Disable video on start");
        options.addOption(disableVideoOption);
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            return false;
        }

        if (cmd.getOptionValue("port") != null)
        	WorkerDefs.port = Integer.valueOf(cmd.getOptionValue("port"));
        
        disableVideo = cmd.getOptionValue("disableVideo") != null;
        
		return true;
	}
	
	public static void main(String[] args) {

		if (!parseArguments(args))
			return;
		
		/* Setting log properties */
		PropertyConfigurator.configure("../log4j.properties");

		/* lunching program with preloader */
		LauncherImpl.launchApplication(EmployeeApplicationScreen.class, EmployeeGuiPreloader.class, args);
	}

}
