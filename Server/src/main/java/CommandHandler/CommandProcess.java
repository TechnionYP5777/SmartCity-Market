package CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import ClientServerCommunication.ProcessRequest;
import SQLDatabase.SQLDatabaseConnection;


/**
 * CommandProcess - This structure will process the command from the clients.
 * 
 * @author Aviad Cohen
 * @since 2016-12-14
 */

public class CommandProcess implements ProcessRequest {

	static Logger log = Logger.getLogger(CommandProcess.class.getName());
		
	@Override	
	public void process(Socket clientSocket) {	
		CommandWrapper outCommandWrapper;
        BufferedReader in = null;
        PrintWriter out = null;
    	String command = null;
    	        
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			for (long startTime = System.currentTimeMillis(); command == null
					&& System.currentTimeMillis() - startTime < 1000;)
				command = in.readLine();
		} catch (IOException e1) {
			log.fatal("Failed to get string command");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);			
			out.println(outCommandWrapper.serialize());			
			return;
		}
		
		if (command == null) {
			/* Timeout failure */
			log.fatal("Failed to get string command due to timeout");

			outCommandWrapper = new CommandWrapper(ResultDescriptor.SM_ERR);
		} else {
			log.info("New command received: " + command);
			
			outCommandWrapper = new CommandExecuter(command).execute(new SQLDatabaseConnection());
		}
		
		out.println(outCommandWrapper.serialize());
	}

}
