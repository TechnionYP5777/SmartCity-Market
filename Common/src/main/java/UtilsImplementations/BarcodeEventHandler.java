package UtilsImplementations;

/**
 * EmployeeMainScreen - This class is the controller for the employee main screen
 * all action of this scene should be here.
 * 
 * @author Aviad Cohen
 * @author Shimon Azulay
 * @since 2017-01-06 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

import javax.inject.Singleton;

import com.google.common.eventbus.EventBus;

import UtilsContracts.BarcodeScanEvent;
import UtilsContracts.IBarcodeEventHandler;

@Singleton
public class BarcodeEventHandler implements IBarcodeEventHandler {
	
	public static final Integer BARCODE_SCANNER_DEFAULT_PORT = 2345;
	
	private EventBus eventBus;
	
	private static final int backlog = 50;
	
	private ServerSocket scannerSocket;
	
	private boolean stopListening;
		
	public void initializeHandler() throws IOException  {
		this.scannerSocket = new ServerSocket(BARCODE_SCANNER_DEFAULT_PORT, backlog);
		eventBus = new EventBus();
	}
	
	public void startListening()  {
		new Thread(new BarcodeListener()).start();	
	}
	
	public class BarcodeListener implements Runnable {

        public void run() {
        	for (String barcode = ""; !stopListening;)
				try {
					Thread.sleep(5000);
					barcode = new BufferedReader(new InputStreamReader(scannerSocket.accept().getInputStream()))
							.readLine();
					eventBus.post(new BarcodeScanEvent(barcode));
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
      
        }
	}
	
	public void stopListening() {
		stopListening = true;
	}
	
	public void register(Object listener) {
		eventBus.register(listener);
	}
	
	public void unregister(Object listener) {
		eventBus.unregister(listener);
	}
}
