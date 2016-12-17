package ServerRunner;

import java.net.UnknownHostException;

import ClientServerCommunication.ThreadPooledServer;
import CommandHandler.CommandProcess;

import org.apache.commons.cli.*;

/**
 * This class runs the server from initialization to shutdown.
 * 
 * @param args - arguments for the server:
 * 1. Server Port.
 * 2. Server IP.
 * 3. Number of Threads.
 */
public class Main {
	
	private static final Integer DEFAULT_NUMBER_OF_THREADS = 5;
	private static final Integer DEFAULT_TIME_OF_TIMEOUT_IN_SECONDS = 120;
	
	private static Integer port;
	private static String serverIP;
	private static Integer numOfThreads;
	private static Integer timeout;
	
	private static boolean parseArguments(String[] args) {
		/* setting default timeout to 120 seconds */
		numOfThreads = DEFAULT_NUMBER_OF_THREADS;
		timeout      = DEFAULT_TIME_OF_TIMEOUT_IN_SECONDS;
		
		//TODO Aviad - add verbose flag to turn on logger prints
        Options options = new Options();

        Option portOption = new Option("p", "port", true, "Server port");
        portOption.setRequired(true);
        options.addOption(portOption);

        Option serverIPOption = new Option("i", "ip", true, "Server ip");
        serverIPOption.setRequired(true);
        options.addOption(serverIPOption);

        Option threadsOption = new Option("n", "nthreads", true, "Number of server threads");
        options.addOption(threadsOption);
        
        Option timeoutOption = new Option("t", "timeout", true, "Time pf timeout in seconds");
        options.addOption(timeoutOption);
        
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

        port         = Integer.valueOf(cmd.getOptionValue("port"));
        serverIP     = cmd.getOptionValue("ip");
        numOfThreads = Integer.valueOf(cmd.getOptionValue("nthreads"));
        timeout      = Integer.valueOf(cmd.getOptionValue("timeout"));
        
		return true;
	}
	
	public static void main(String[] args) {
		if (!parseArguments(args))
			return;
		
		CommandProcess commandProcess = new CommandProcess();
		ThreadPooledServer server = null;
		try {
			server = new ThreadPooledServer(port, numOfThreads, commandProcess, serverIP);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();

			System.out.println("Server IP address leads to unknown host, server won't start.");
			return;
		}
		
        System.out.println("Starting Server.");
		new Thread(server).start();

	    try
        {
	    	Thread.sleep(1000 * timeout);
        }
	    catch (InterruptedException e) 
        {
	    	/* Get interrupt to stop server */
            System.out.println("Server Got interrupted.");
        }
	    
        System.out.println("Stopping server.");
		
		server.stop();
	}
}
