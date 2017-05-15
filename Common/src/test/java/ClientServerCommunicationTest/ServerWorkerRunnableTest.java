package ClientServerCommunicationTest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import ClientServerCommunication.ClientRequestHandler;
import ClientServerCommunication.ProcessRequest;
import ClientServerCommunication.ServerWorkerRunnable;

/**
 * @author Aviad Cohen
 * @since 2016-12-10 */

public class ServerWorkerRunnableTest {

	private static final int SERVER_PORT = 9000;
	private static final String SERVER_HOST_NAME = "localhost";
	private static final int TIMEOUT = 200;
	private static final String CLIENT_MSG_HELLO = "Hello";
	private static final String SERVER_MSG_WORLD = "World!";
		
	private class ClientHelloWorld implements Runnable  {
		
		@Override
		public void run() {
			ClientRequestHandler clientRequestManager = null;

			try {
				clientRequestManager = new ClientRequestHandler();
				clientRequestManager.createSocket(SERVER_PORT, SERVER_HOST_NAME, TIMEOUT);
			} catch (UnknownHostException | RuntimeException e1) {
				fail();
			}
			
			/* sending client message and waiting for respond */
			try {
				assertEquals(clientRequestManager.sendRequestWithRespond(CLIENT_MSG_HELLO), SERVER_MSG_WORLD);
			} catch (IOException e) {
				fail();
			}
			
			clientRequestManager.finishRequest();			
		}
	}
	
	private class ServerProcessRequestHelloWorld implements ProcessRequest {

		@Override
		public void process(Socket s) {
            BufferedReader in = null;
            PrintWriter out = null;
        	String $ = null;
            
			try {
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			} catch (IOException e1) {
				fail();
			}
			try {
				out = new PrintWriter(s.getOutputStream(), true);
			} catch (IOException e1) {
				fail();
			}
            
			try {
				while ($ == null)
					$ = in.readLine();
				
				assert($.equals(CLIENT_MSG_HELLO));
				
				out.println(SERVER_MSG_WORLD);
			} catch (IOException e) {
				fail();
			}		
		}
		
	}
	
	private class ServerHelloWorld implements Runnable {
		
		@Override
		public void run() {
			ServerSocket serverSocket = null;
			Socket clientSocket = null;
			
			try {
				serverSocket = new ServerSocket(SERVER_PORT);
				while (clientSocket == null)
					clientSocket = serverSocket.accept();
			} catch (IOException e1) {
				fail();
			}
			ServerWorkerRunnable workerRunnable = new ServerWorkerRunnable(clientSocket, new ServerProcessRequestHelloWorld());
			
			/* waiting for respond and sending server message */
			workerRunnable.run();
			
			try {
				serverSocket.close();
			} catch (IOException e) {
				fail();
			}
		}
	}
	
	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test public void ServerWorkerRunnableHelloWorldTest () {
		new Thread(new ServerHelloWorld()).start();
		new Thread(new ClientHelloWorld()).start();
		
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e) {
			assert(false);
		}
	}
	
	@Test
	@SuppressWarnings("unused") public void ServerWorkerRunnableNullArguments () {
		boolean success = false;
		
		try {
			new ServerWorkerRunnable(new Socket(), null);
		} catch (NullPointerException e) {
			success = true;
		}
		
		assert success;
		success = false;
		
		try {
			new ServerWorkerRunnable(null, new ServerProcessRequestHelloWorld());
		} catch (NullPointerException e) {
			success = true;
		}
			
		assert success;
	}
}
