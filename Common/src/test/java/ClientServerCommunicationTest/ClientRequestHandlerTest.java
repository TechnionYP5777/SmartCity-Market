package ClientServerCommunicationTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import org.junit.Test;

import ClientServerCommunication.ClientRequestHandler;

/**
 * @author Aviad Cohen
 * @since 2016-12-10
 */

public class ClientRequestHandlerTest {

	private static final int SERVER_PORT = 9001;
	private static final String SERVER_HOST_NAME = "localhost";
	private static final String SERVER_UKNOWN_HOST_NAME = "unknown12345";
	private static final int TIMEOUT = 500;
	
	private ServerSocket serverSocket;
	private int portTest = SERVER_PORT;
	
	private class ServerTest implements Runnable {
		
		private void closeServerSocket() {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
			
			serverSocket = null;
			++portTest;
		}
		
		@Override
		public void run() {
			serverSocket = null;
			
			try {
				serverSocket = new ServerSocket(portTest);
				serverSocket.accept();
			} catch (IOException e1) {
				e1.printStackTrace();
				fail();
			}
			
			closeServerSocket();
		}
	}
	
	@Test public void ClientRequestHandlerLegalTimeoutTest () {
		new Thread(new ServerTest()).start();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			fail();
		}
		
		try {
			new ClientRequestHandler(portTest, SERVER_HOST_NAME, TIMEOUT).finishRequest();
		} catch (UnknownHostException | RuntimeException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test public void ClientRequestHandlerZeroTimeoutTest () {
		try {
			new ClientRequestHandler(portTest, SERVER_HOST_NAME, 0);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			fail();
		} catch (RuntimeException e) {
			/* success */
		} 
	}

	@Test public void ClientRequestHandlerNegativeTimeoutTest () {	
		try {
			new ClientRequestHandler(portTest, SERVER_HOST_NAME, -1);
		} catch (RuntimeException e) {
			/* success */
		} catch (UnknownHostException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test public void ClientRequestHandlerUnknownHostTest () {				
		try {
			new ClientRequestHandler(portTest, SERVER_UKNOWN_HOST_NAME, TIMEOUT).finishRequest();
		} catch (UnknownHostException e) {
			/* success */
		} catch (RuntimeException e) {
			e.printStackTrace();
			fail();
		}
	}
}
