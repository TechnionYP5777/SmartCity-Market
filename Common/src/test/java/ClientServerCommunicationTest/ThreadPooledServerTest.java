package ClientServerCommunicationTest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import ClientServerCommunication.ClientRequestHandler;
import ClientServerCommunication.ProcessRequest;
import ClientServerCommunication.ThreadPooledServer;

/**
 * @author Aviad Cohen
 * @since 2016-12-10 */

public class ThreadPooledServerTest {

	private static final int SERVER_PORT = 9002;
	private static final String SERVER_HOST_NAME = "localhost";
	private static final int TIMEOUT = 1000;
	private static final String CLIENT_MSG_HELLO = "Hello";
	private static final String SERVER_MSG_WORLD = "World!";
	
	private class ProcessRequestTester implements ProcessRequest {

		@Override
		public void process(Socket clientSocket) {	
            BufferedReader in = null;
            PrintWriter out = null;
        	String $ = null;
            
			try {
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				out.println(SERVER_MSG_WORLD);
				while ($ == null)
					$ = in.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
				fail();
			}
		}
	}
	
	private class ClientRunner implements Runnable  {
		
		@Override
		public void run() {
			ClientRequestHandler clientRequestManager = null;
			try {
				clientRequestManager = new ClientRequestHandler(SERVER_PORT, SERVER_HOST_NAME, TIMEOUT);
			} catch (UnknownHostException | RuntimeException e1) {
				e1.printStackTrace();
				fail();
			}
			
			/* sending client message and waiting for respond */
			try {
				assertEquals(clientRequestManager.sendRequestWithRespond(CLIENT_MSG_HELLO), SERVER_MSG_WORLD);
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
			
			clientRequestManager.finishRequest();			
		}
	}
	
	@Test public void ServerWorkerRunnableHelloWorldTest () {
		ProcessRequestTester processRequestTester = new ProcessRequestTester();
		ThreadPooledServer server = new ThreadPooledServer(SERVER_PORT, 1, processRequestTester);
		Thread client;
		new Thread(server).start();
		
		/* waiting for server to run */
		while (server.isStopped());
		
		client = new Thread(new ClientRunner());
		client.start();
		
		try {
			client.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		
		server.stop();
	}
}
