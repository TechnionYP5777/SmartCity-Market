package UtilsContracts;

import BasicCommonClasses.SmartCode;

public class SmartcodeScanEvent {

	SmartCode smartcode;
	
	public SmartcodeScanEvent(SmartCode smartcode) {
		this.smartcode = smartcode;
	}
	
	public SmartCode getSmarCode() {
		return smartcode;
	}
	
}
