package UtilsImplementations;

/**
 * BarcodeEventHandler - This class is represents client that listing to barcode scanning
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

import BasicCommonClasses.SmartCode;
import SmartcodeParser.SmartcodeParser;
import UtilsContracts.BarcodeScanEvent;
import UtilsContracts.IBarcodeEventHandler;
import UtilsContracts.SmartcodeScanEvent;

@Singleton
public class BarcodeEventHandler implements IBarcodeEventHandler {
	
	public static final Integer BARCODE_SCANNER_DEFAULT_PORT = 4000;
	
	private EventBus eventBus;
	
	private static final int backlog = 50;
	
	private ServerSocket scannerSocket;
	
	private boolean stopListening;
		
	@Override
	public void initializeHandler() throws IOException  {
		this.scannerSocket = new ServerSocket(BARCODE_SCANNER_DEFAULT_PORT, backlog);
		eventBus = new EventBus();
	}
	
	@Override
	public void startListening()  {
		new Thread(new BarcodeListener()).start();	
	}
	
	public class BarcodeListener implements Runnable {

        @Override
		public void run() {
        	for (String code = ""; !stopListening;)
				try {
					Thread.sleep(5000);
					code = new BufferedReader(new InputStreamReader(scannerSocket.accept().getInputStream()))
							.readLine();
					SmartCode smartcode = SmartcodeParser.formCode(code);
					eventBus.post(smartcode.getExpirationDate() != null ? new SmartcodeScanEvent(smartcode)
							: new BarcodeScanEvent(smartcode.getBarcode()));
				} catch (IOException | InterruptedException e) {
					/* Drop scan silently */
				}
      
        }
	}
	
	@Override
	public void stopListening() {
		stopListening = true;
	}
	
	@Override
	public void register(Object listener) {
		eventBus.register(listener);
	}
	
	@Override
	public void unregister(Object listener) {
		eventBus.unregister(listener);
	}

	@Override
	public void publishEvent(SmartCode ¢) {
		eventBus.post(new SmartcodeScanEvent(¢));
		
	}
}
