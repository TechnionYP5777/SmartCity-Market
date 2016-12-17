package CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import ClientServerCommunication.ProcessRequest;

public class CommandProcess implements ProcessRequest {

	public static final Logger LOGGER = Logger.getLogger(CommandProcess.class.getName());
	
	private CommandWrapper inCommandWrapper;
	private CommandWrapper outCommandWrapper;
	
	private void interpretCommand(String command) {
		inCommandWrapper = CommandWrapper.fromGson(command);
		
		switch(inCommandWrapper.getCommandDescriptor()) {
		case LOGIN:
			//TODO Aviad - Added support to Login command
			LOGGER.log(Level.SEVERE, "Login command currectly unsupported");
			outCommandWrapper.setResultDescriptor(ResultDescriptor.SM_INVALID_CMD_DESCRIPTOR);

			break;
			
		case LOGOUT:
			//TODO Aviad - Added support to Logout command
			LOGGER.log(Level.SEVERE, "Logout command currectly unsupported");
			outCommandWrapper.setResultDescriptor(ResultDescriptor.SM_INVALID_CMD_DESCRIPTOR);
			
			break;
			
		case VIEW_PRODUCT_FROM_CATALOG:
			//TODO Aviad - Added support to VIEW_PRODUCT_FROM_CATALOG command
			LOGGER.log(Level.SEVERE, "VIEW PRODUCT FROM CATALOG command currectly unsupported");
			outCommandWrapper.setResultDescriptor(ResultDescriptor.SM_INVALID_CMD_DESCRIPTOR);
			
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
