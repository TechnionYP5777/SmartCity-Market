package UtilsContracts;

import BasicCommonClasses.SmartCode;

/**
 * SmartcodeScanEvent - this class save and return the Smartcode which given from the event.
 * 
 * @author aviad
 * @since  2017-05-02
 * 
 */
public class SmartcodeScanEvent {

	SmartCode smartcode;
	
	public SmartcodeScanEvent(SmartCode smartcode) {
		this.smartcode = smartcode;
	}
	
	public SmartCode getSmarCode() {
		return smartcode;
	}
	
}
