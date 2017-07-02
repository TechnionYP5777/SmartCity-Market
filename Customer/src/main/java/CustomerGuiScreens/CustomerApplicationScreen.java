package CustomerGuiScreens;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.PropertyConfigurator;

import CustomerContracts.ICustomer;
import CustomerDI.CustomerDiConfigurator;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import GuiUtils.AbstractApplicationScreen;
import SMExceptions.SMException;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
import UtilsImplementations.StackTraceUtil;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * CustomerApplicationScreen - This class is the stage GUI class for Customer
 * 
 * @author Lior Ben Ami
 * @since 2017-01-13 
 */
public class CustomerApplicationScreen extends AbstractApplicationScreen {

	BarcodeEventHandler barcodeEventHandler;

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			InjectionFactory.createInjector(new CustomerDiConfigurator());
			
			barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class);
			barcodeEventHandler.initializeHandler();
			barcodeEventHandler.startListening();

			setScene("/CustomerWelcomeScreen/CustomerWelcomeScreen.fxml");
			stage.setTitle("Smart Market Beta");
			stage.setMaximized(true);
			
			stage.setOnCloseRequest(event -> { 
				try {
					ICustomer customer = InjectionFactory.getInstance(Customer.class);
					customer.logout();
					Platform.exit();
					System.exit(0);
				} catch (SMException e) {
					log.fatal(e);
					log.debug(StackTraceUtil.stackTraceToStr(e));
					e.showInfoToUser();
					Platform.exit();
					System.exit(0);
				} catch (Exception e) {
					log.fatal(e);
					log.debug(StackTraceUtil.stackTraceToStr(e));
					Platform.exit();
					System.exit(0);
				}
			});
			
			stage.show();

			
		} catch (Exception ¢) {
			throw new RuntimeException();
		}
	}

	private static boolean parseArguments(String[] args) {
		CustomerDefs.port = 2000;
		CustomerDefs.host = "127.0.0.1";
		CustomerDefs.disableVid = false;
		
        Options options = new Options();

        Option portOption = new Option("p", "port", true, "Server port");
        options.addOption(portOption);
        
        Option ipOption = new Option("i", "serverIP", true, "The server ip (default = local host)");
        options.addOption(ipOption);
        
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
        	CustomerDefs.port = Integer.valueOf(cmd.getOptionValue("port"));
        
        if (cmd.getOptionValue("serverIP") != null)
			CustomerDefs.host = cmd.getOptionValue("serverIP");
        
        CustomerDefs.disableVid = cmd.getOptionValue("disableVideo") != null;
        
		return true;
	}
	
	public static void main(String[] args) {
		
		/* Allowing to change IP and port from command line */
		if (!parseArguments(args))
			return;
		
		/* Setting log properties */
		PropertyConfigurator.configure("../log4j.properties");
				
		launch(args);
	}
}
