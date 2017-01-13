package UtilsContracts;

public class BarcodeScanEvent {
	
	String barcode;
	
	public BarcodeScanEvent(String barcode) {
		this.barcode = barcode;
	}
	
	public String getBarcode() {
		return barcode;
	}

}
