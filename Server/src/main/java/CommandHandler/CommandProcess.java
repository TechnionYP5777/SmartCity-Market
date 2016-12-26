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
		
		log.info("New command received: " + command);
		
		outCommandWrapper = new CommandExecuter(command).execute(new SQLDatabaseConnection());
		
		out.println(outCommandWrapper.serialize());
	}

}
