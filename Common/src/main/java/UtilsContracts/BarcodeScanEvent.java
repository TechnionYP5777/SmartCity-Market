package UtilsContracts;

public class BarcodeScanEvent {
	
	long barcode;
	
	public BarcodeScanEvent(long barcode) {
		this.barcode = barcode;
	}
	
	public long getBarcode() {
		return barcode;
	}

}
