package UtilsContracts;

/**
 * BarcodeScanEvent - handles the event from scanner
 * 
 * @author Shimon Azulay
 * @since  2017-02-02
 *
 */
public class BarcodeScanEvent {
	
	long barcode;
	
	public BarcodeScanEvent(long barcode) {
		this.barcode = barcode;
	}
	
	public long getBarcode() {
		return barcode;
	}

}
