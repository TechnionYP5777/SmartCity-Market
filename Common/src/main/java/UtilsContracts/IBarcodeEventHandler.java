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

	/**
	 * Initialize the barcode event handler
	 * 
	 * @throws IOException
	 */
	void initializeHandler() throws IOException;

	/**
	 * set the handler to start listen to the barcode scanner
	 */
	void startListening();

	/**
	 * set the handler to stop listen to the barcode scanner
	 */
	void stopListening();

	/**
	 * register the handler to the register
	 * 
	 * @param listener - the listener to register to.
	 */
	void register(Object listener);

	/**
	 * unregister the handler to the register
	 * 
	 * @param listener - the listener to register to.
	 */
	void unregister(Object listener);
	
	/**
	 * send the event to the listener
	 * 
	 * @param smartcode to send to the listener
	 */
	void publishEvent(SmartCode c);
}
