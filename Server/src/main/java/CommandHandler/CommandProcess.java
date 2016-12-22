package CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import BasicCommonClasses.Login;
import BasicCommonClasses.SmartCode;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import ClientServerCommunication.ProcessRequest;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.AuthenticationError;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.ProductNotExistInCatalog;
import SQLDatabase.SQLDatabaseException.WorkerNotConnected;

public class CommandProcess implements ProcessRequest {

	public static final Logger LOGGER = Logger.getLogger(CommandProcess.class.getName());
	
	private CommandWrapper inCommandWrapper;
	private CommandWrapper outCommandWrapper;
	
	private void loginCommand() {		
		Login login;
		SQLDatabaseConnection sqlDatabaseConnection = new SQLDatabaseConnection();
		
		LOGGER.log(Level.FINE, "Login command called");
		
		login = new Gson().fromJson(inCommandWrapper.getData(), Login.class);
				
		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			outCommandWrapper.setSenderID((sqlDatabaseConnection.WorkerLogin(login.getUserName(), login.getPassword())));
			
			LOGGER.log(Level.FINE, "Login command succeded with sender ID " + outCommandWrapper.getSenderID());
		} catch (AuthenticationError e) {
			LOGGER.log(Level.FINE, "Login command failed, username dosen't exist or wrong password received");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD);			
		} catch (CriticalError e) {
			LOGGER.log(Level.SEVERE, "Login command failed, critical error occured from SQL Database connection");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			e.printStackTrace();
		}
		
		LOGGER.log(Level.FINE, "Login with User " + login.getUserName() + " finished");
	}
	
	private void logoutCommand() {
		String username;
		SQLDatabaseConnection sqlDatabaseConnection = new SQLDatabaseConnection();
		
		LOGGER.log(Level.FINE, "Logout command called");
		
		username = new Gson().fromJson(inCommandWrapper.getData(), String.class);
		
		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			sqlDatabaseConnection.WorkerLogout(inCommandWrapper.getSenderID(), username);
			
			LOGGER.log(Level.FINE, "Logout command succeded with sender ID " + inCommandWrapper.getSenderID());
		} catch (WorkerNotConnected e) {
			LOGGER.log(Level.FINE, "Logout command failed, username dosen't login to the system");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);			
		} catch (CriticalError e) {
			LOGGER.log(Level.SEVERE, "Logout command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			e.printStackTrace();
		}
		
		LOGGER.log(Level.FINE, "Logout with User " + username + " finished");
	}
	
	private void viewProductFromCatalogCommand() {
		SmartCode smartCode;
		SQLDatabaseConnection sqlDatabaseConnection = new SQLDatabaseConnection();
		
		LOGGER.log(Level.FINE, "View Product From Catalog command called");
		
		smartCode = new Gson().fromJson(inCommandWrapper.getData(), SmartCode.class);
		
		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, sqlDatabaseConnection.getProductFromCatalog(inCommandWrapper.getSenderID(), smartCode.getBarcode()));
			
			LOGGER.log(Level.FINE, "Get product from catalog command secceeded with barcode " + smartCode.getBarcode());
		} catch (ProductNotExistInCatalog e) {
			LOGGER.log(Level.FINE, "Get product from catalog command failed, product dosen't exist in the system");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
		} catch (WorkerNotConnected e) {
			LOGGER.log(Level.FINE, "Get product from catalog command failed, username dosen't login to the system");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (CriticalError e) {
			LOGGER.log(Level.SEVERE, "Get product from catalog command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			e.printStackTrace();
		}
		
		LOGGER.log(Level.FINE, "View Product From Catalog with product barcode  " + smartCode.getBarcode() + " finished");
	}
	
	private void interpretCommand(String command) {
		inCommandWrapper = CommandWrapper.deserialize(command);
		
		switch(inCommandWrapper.getCommandDescriptor()) {
		case LOGIN:
			loginCommand();

			break;
			
		case LOGOUT:
			logoutCommand();
			
			break;
			
		case VIEW_PRODUCT_FROM_CATALOG:
			viewProductFromCatalogCommand();
			
			break;
			
		default:
			try {
				/* Command not supported, returning invalid command */
				outCommandWrapper = (CommandWrapper)inCommandWrapper.clone();
				outCommandWrapper.setResultDescriptor(ResultDescriptor.SM_INVALID_CMD_DESCRIPTOR);
			} catch (CloneNotSupportedException e) {
				outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
				
				LOGGER.log(Level.SEVERE, "Failed to clone command");
				
				return;
			}
			break;
		}
	}
	
	@Override	
	public void process(Socket clientSocket) {	
        BufferedReader in = null;
        PrintWriter out = null;
    	String command = null;
        
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);

			/* waiting for client command */
			//TODO Aviad - Added timeout to for waiting time.
			while (command == null)
				command = in.readLine();
		} catch (IOException e1) {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			LOGGER.log(Level.SEVERE, "Failed to get string command");
			
			out.println(outCommandWrapper.serialize());
			
			return;
		}
		
		interpretCommand(command);
		
		out.println(outCommandWrapper.serialize());
	}

}
