package ServerRunner;

import java.net.UnknownHostException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import ClientServerCommunication.ServerWorkerRunnable;
import ClientServerCommunication.ThreadPooledServer;
import CommandHandler.CommandProcess;

import org.apache.commons.cli.*;

/**
 * This class runs the server from initialization to shutdown.
 * 
 * Please run ServerMain to see usage.
 */
public class Main {
	
	private static final Integer DEFAULT_NUMBER_OF_THREADS = 5;
	private static final Integer DEFAULT_TIME_OF_TIMEOUT_IN_SECONDS = 120;
	
	private static Integer port;
	private static String serverIP;
	private static Integer numOfThreads;
	private static Integer timeout;
	private static Level verbosity;
	
	private static boolean parseArguments(String[] args) {
		/* setting default timeout to 120 seconds */
		numOfThreads = DEFAULT_NUMBER_OF_THREADS;
		timeout      = DEFAULT_TIME_OF_TIMEOUT_IN_SECONDS;
		verbosity    = Level.SEVERE;
		
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
        
        Option verbosityOption = new Option("v", "verbose", true, "Select the level of verbosity: SEVERE / FINE / FINER.");
        options.addOption(verbosityOption);
        
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

        port     = Integer.valueOf(cmd.getOptionValue("port"));
        serverIP = cmd.getOptionValue("ip");
        
        if (cmd.getOptionValue("nthreads") != null)
        	numOfThreads = Integer.valueOf(cmd.getOptionValue("nthreads"));
        
        if (cmd.getOptionValue("timeout") != null)
        	timeout = Integer.valueOf(cmd.getOptionValue("timeout"));
        
        if (cmd.getOptionValue("verbose") != null)
			verbosity = Level.parse(cmd.getOptionValue("verbose"));
        
		return true;
	}
	
	private static void setLoggerVerbosity() {
		//TODO - Noam - please add SQL logger when it's ready (see issue #86)
	    ConsoleHandler handler = new ConsoleHandler();
	    handler.setFormatter(new SimpleFormatter());
	    handler.setLevel(verbosity);
	    
		Logger.getLogger(ThreadPooledServer.class.getName()).setLevel(verbosity);
		Logger.getLogger(ServerWorkerRunnable.class.getName()).setLevel(verbosity);
		Logger.getLogger(CommandProcess.class.getName()).setLevel(verbosity);
	    
	    Logger.getLogger(ThreadPooledServer.class.getName()).addHandler(handler);
	    Logger.getLogger(ServerWorkerRunnable.class.getName()).addHandler(handler);
	    Logger.getLogger(CommandProcess.class.getName()).addHandler(handler);
	}
	
	public static void main(String[] args) {
		if (!parseArguments(args))
			return;
		
		setLoggerVerbosity();
		
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
