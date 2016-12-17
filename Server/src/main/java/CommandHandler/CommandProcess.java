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

public class CommandProcess implements ProcessRequest {

	public static final Logger LOGGER = Logger.getLogger(CommandProcess.class.getName());
	
	private CommandWrapper inCommandWrapper;
	private CommandWrapper outCommandWrapper;
	
	private void loginCommand() {		
		Login login;
		
		LOGGER.log(Level.FINE, "Login command called");
		
		login = new Gson().fromJson(inCommandWrapper.getData(), Login.class);
		
		//TODO Noam - call SQL Login here and insert result to outCommandWrapper using try-catch
		
		LOGGER.log(Level.FINE, "Login with User " + login.getUserName() + " finished");
	}
	
	private void logoutCommand() {
		String username;
		
		LOGGER.log(Level.FINE, "Logout command called");
		
		username = new Gson().fromJson(inCommandWrapper.getData(), String.class);
		
		//TODO Noam - call SQL Logout here and insert result to outCommandWrapper using try-catch
		
		LOGGER.log(Level.FINE, "Logout with User " + username + " finished");
	}
	
	private void viewProductFromCatalogCommand() {
		SmartCode smartCode;
		
		LOGGER.log(Level.FINE, "View Product From Catalog command called");
		
		smartCode = new Gson().fromJson(inCommandWrapper.getData(), SmartCode.class);
		
		//TODO Noam - call SQL View Product From Catalog here and insert result to outCommandWrapper using try-catch
		
		LOGGER.log(Level.FINE, "View Product From Catalog with product barcode  " + smartCode.getBarcode() + " finished");
	}
	
	private void interpretCommand(String command) {
		inCommandWrapper = CommandWrapper.fromGson(command);
		
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
			
			out.println(outCommandWrapper.toGson());
			
			return;
		}
		
		interpretCommand(command);
		
		out.println(outCommandWrapper.toGson());
	}

}
