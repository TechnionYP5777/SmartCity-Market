package UtilsContracts;

import java.io.IOException;

import BasicCommonClasses.SmartCode;

public interface IBarcodeEventHandler {

	void initializeHandler() throws IOException;

	void startListening();

	void stopListening();

	void register(Object listener);

	void unregister(Object listener);
	
	void publishEvent(SmartCode s);
}
