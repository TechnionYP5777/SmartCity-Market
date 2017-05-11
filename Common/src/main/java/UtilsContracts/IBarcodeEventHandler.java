package UtilsContracts;

import java.io.IOException;

import BasicCommonClasses.SmartCode;

/**
 * IBarcodeEventHandler - interface which represents barcode event handler
 * 
 * @author Shimon Azulay
 * @since  2017-05-02
 *
 */
public interface IBarcodeEventHandler {

	void initializeHandler() throws IOException;

	void startListening();

	void stopListening();

	void register(Object listener);

	void unregister(Object listener);
	
	void publishEvent(SmartCode c);
}
