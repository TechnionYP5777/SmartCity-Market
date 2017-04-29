package ClientServerCommunicationTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
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
				fail();
			}

			closeServerSocket();
		}
	}

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void ClientRequestHandlerLegalTimeoutTest() {
		new Thread(new ServerTest()).start();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			fail();
		}

		try {
			ClientRequestHandler clientRequestHandler = new ClientRequestHandler();
			clientRequestHandler.createSocket(portTest, SERVER_HOST_NAME, TIMEOUT);
			clientRequestHandler.finishRequest();
		} catch (UnknownHostException | RuntimeException e) {
			fail();
		}
	}

	@Test
	public void ClientRequestHandlerZeroTimeoutTest() {
		try {
			(new ClientRequestHandler()).createSocket(portTest, SERVER_HOST_NAME, 0);
		} catch (UnknownHostException e) {
			fail();
		} catch (RuntimeException e) {
			/* success */
		}
	}

	@Test
	public void ClientRequestHandlerNegativeTimeoutTest() {
		try {
			(new ClientRequestHandler()).createSocket(portTest, SERVER_HOST_NAME, -1);
		} catch (RuntimeException e) {
			/* success */
		} catch (UnknownHostException e) {
			fail();
		}
	}

	@Test
	public void ClientRequestHandlerUnknownHostTest() {
		try {
			ClientRequestHandler clientRequestHandler = new ClientRequestHandler();
			clientRequestHandler.createSocket(portTest, SERVER_UKNOWN_HOST_NAME, TIMEOUT);
			clientRequestHandler.finishRequest();
		} catch (UnknownHostException e) {
			/* success */
		} catch (RuntimeException e) {
			fail();
		}
	}
}
