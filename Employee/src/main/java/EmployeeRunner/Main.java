package EmployeeRunner;

import java.io.*;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.inject.Guice;
import com.google.inject.Injector;

import BasicCommonClasses.CatalogProduct;
import EmployeeContracts.IWorker;
import EmployeeDI.WorkerDiConfigurator;
import EmployeeDefs.AEmployeeExceptions.AuthenticationError;
import EmployeeDefs.AEmployeeExceptions.CriticalError;
import EmployeeDefs.AEmployeeExceptions.InvalidParameter;
import EmployeeDefs.AEmployeeExceptions.ProductNotExistInCatalog;
import EmployeeDefs.AEmployeeExceptions.UnknownSenderID;
import EmployeeDefs.AEmployeeExceptions.WorkerAlreadyConnected;
import EmployeeDefs.AEmployeeExceptions.WorkerNotConnected;
import EmployeeImplementations.Worker;

/**
 * This is the employee main: This will temporarily replace GUI until it's
 * deployed to our project.
 *
 * @author idan atias
 *
 */
public class Main {

	static Logger log = Logger.getLogger(Main.class.getName());
	
	// Standard input:
	static final InputStreamReader cin = new InputStreamReader(System.in);

	// commands id's:
	static final int loginCmd = 1;

	static final int logoutCmd = 2;

	static final int viewCatalogProductCmd = 3;

	static final int endSession = 0;

	// default values:
	static final int barcodeLen = 16;

	static final int userNameLen = 10;

	static final int passwordLen = 10;

	// logger:
	static final Level verbosity = Level.ALL;

	public static void main(String args[]) throws IOException {		
		/* Setting  log properties */
		PropertyConfigurator.configure("../log4j.properties");
		log.setLevel(verbosity);

		Injector injector = Guice.createInjector(new WorkerDiConfigurator());
		Worker worker = injector.getInstance(Worker.class);

		try {
			printCommandsInfo();
			char cmdId;
			do {
				cmdId = (char) cin.read();
				try {
					switch (cmdId) {
					case '1':
						loginHandler(worker);
						printCommandsInfo();
						break;
					case '2':
						logoutHandler(worker);
						printCommandsInfo();
						break;
					case '3':
						viewCatalogProdcutHandler(worker);
						printCommandsInfo();
						break;
					case '0':
						endSessionHandler();
						break;
					case '\n':
						// for ignoring the enter key
						break;
					default:
						System.out.println("input=(" + cmdId + ") is not a valid cmd-id. Please retry: ");
						printCommandsInfo();
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					printCommandsInfo();
				}
			} while (cmdId != '0');
		} finally {
			if (cin != null)
				cin.close();
		}
	}

	private static void endSessionHandler() {
		System.out.println("Ending session...");
		System.out.println("Session Ended");
	}

	private static void logoutHandler(IWorker ¢) {
		try {
			¢.logout();
		} catch (InvalidParameter | UnknownSenderID | CriticalError | WorkerNotConnected e) {
			System.out.println("Failure occured, currentlly unsupported by worker.");
			e.printStackTrace();
		}
		System.out.println("Logged out successfully");
	}

	private static void viewCatalogProdcutHandler(IWorker w) throws IOException {
		System.out.println("Please enter product barcode (max barcode len is " + barcodeLen + "): ");
		String barcodeString = getLineFromStdin(barcodeLen);
		int barcode = Integer.parseInt(barcodeString);
		System.out.println("barcode inserted is: " + barcode);
		CatalogProduct catProd = null;
		try {
			catProd = w.viewProductFromCatalog(barcode);
		} catch (InvalidParameter | UnknownSenderID | CriticalError | WorkerNotConnected | ProductNotExistInCatalog e) {
			System.out.println("Failure occured, currentlly unsupported by worker.");
			e.printStackTrace();
		}
		System.out.println("Product name: " + catProd.getName());
		System.out.println("Product price: " + catProd.getPrice());
		System.out.println("Product description: " + catProd.getDescription());
	}

	private static void loginHandler(IWorker w) throws IOException {
		System.out.println("Enter your username: ");
		String userName = getLineFromStdin(userNameLen);
		System.out.println("Enter your password: ");
		try {
			w.login(userName, getLineFromStdin(passwordLen));
		} catch (InvalidParameter | CriticalError | WorkerAlreadyConnected | AuthenticationError e) {
			System.out.println("Failure occured, currentlly unsupported by worker.");
			e.printStackTrace();
		}
		System.out.println("Logged in successfully");
	}

	private static String getLineFromStdin(int len) throws IOException {
		InputStreamReader cin = new InputStreamReader(System.in);
		char[] line = new char[len];
		char curr;
		int idx = 0;
		do {
			if (idx >= len) {
				System.out.println("ERROR - input line is too long.");
				return null;
			}
			curr = (char) cin.read();
			line[idx] = curr;
			++idx;
		} while (curr != '\n');
		return convertCharArrToString(line, idx);
	}

	private static String convertCharArrToString(char[] line, int len) {
		String $ = "";
		for (int ¢ = 0; ¢ < len; ++¢)
			if (line[¢] != '\n')
				$ += line[¢];
		return $;
	}

	private static void printCommandsInfo() {
		System.out.println("*********************SmartMarket-CLI*****************************");
		System.out.println("Enter cmd id: login = 1; logout = 2; viewCatalogProduct = 3;");
		System.out.println("For terminating this session enter 0");
		System.out.println("*********************SmartMarket-CLI*****************************");
	}

}
