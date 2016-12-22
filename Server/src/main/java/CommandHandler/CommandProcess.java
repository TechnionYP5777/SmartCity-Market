package CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

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

	static Logger log = Logger.getLogger(CommandProcess.class.getName());
	
	private CommandWrapper inCommandWrapper;
	private CommandWrapper outCommandWrapper;
	
	private void loginCommand() {		
		Login login;
		SQLDatabaseConnection sqlDatabaseConnection = new SQLDatabaseConnection();
		
		log.info("Login command called");
		
		login = new Gson().fromJson(inCommandWrapper.getData(), Login.class);
				
		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			outCommandWrapper.setSenderID((sqlDatabaseConnection.WorkerLogin(login.getUserName(), login.getPassword())));
			
			log.info("Login command succeded with sender ID " + outCommandWrapper.getSenderID());
		} catch (AuthenticationError e) {
			log.info("Login command failed, username dosen't exist or wrong password received");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_USERNAME_DOES_NOT_EXIST_WRONG_PASSWORD);			
		} catch (CriticalError e) {
			log.fatal("Login command failed, critical error occured from SQL Database connection");
			
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			e.printStackTrace();
		}
		
		log.info( "Login with User " + login.getUserName() + " finished");
	}
	
	private void logoutCommand() {
		String username;
		SQLDatabaseConnection sqlDatabaseConnection = new SQLDatabaseConnection();
		
		log.info("Logout command called");
		
		username = new Gson().fromJson(inCommandWrapper.getData(), String.class);
		
		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK);
			sqlDatabaseConnection.WorkerLogout(inCommandWrapper.getSenderID(), username);
			
			log.info("Logout command succeded with sender ID " + inCommandWrapper.getSenderID());
		} catch (WorkerNotConnected e) {
			log.info("Logout command failed, username dosen't login to the system");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);			
		} catch (CriticalError e) {
			log.fatal("Logout command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			e.printStackTrace();
		}
		
		log.info("Logout with User " + username + " finished");
	}
	
	private void viewProductFromCatalogCommand() {
		SmartCode smartCode;
		SQLDatabaseConnection sqlDatabaseConnection = new SQLDatabaseConnection();
		
		log.info("View Product From Catalog command called");
		
		smartCode = new Gson().fromJson(inCommandWrapper.getData(), SmartCode.class);
		
		try {
			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_OK, sqlDatabaseConnection.getProductFromCatalog(inCommandWrapper.getSenderID(), smartCode.getBarcode()));
			
			log.info("Get product from catalog command secceeded with barcode " + smartCode.getBarcode());
		} catch (ProductNotExistInCatalog e) {
			log.info("Get product from catalog command failed, product dosen't exist in the system");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_CATALOG_PRODUCT_DOES_NOT_EXIST);
		} catch (WorkerNotConnected e) {
			log.info("Get product from catalog command failed, username dosen't login to the system");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED);
		} catch (CriticalError e) {
			log.fatal("Get product from catalog command failed, critical error occured from SQL Database connection");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
			e.printStackTrace();
		}
		
		log.info("View Product From Catalog with product barcode  " + smartCode.getBarcode() + " finished");
	}
	
	private void interpretCommand(String command) {
		log.info("New command received: " + command);
		
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
				
				log.fatal("Failed to clone command");
				
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
			log.fatal("Failed to get string command");
			
			out.println(outCommandWrapper.serialize());
			
			return;
		}
		
		interpretCommand(command);
		
		out.println(outCommandWrapper.serialize());
	}

}
