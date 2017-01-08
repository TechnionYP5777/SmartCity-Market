package UtilsImplementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class BarcodeScanner {
	
	public static final Integer BARCODE_SCANNER_DEFAULT_PORT = 2345;
	
	private static final int backlog = 50;
	private ServerSocket scannerSocket;

	public BarcodeScanner() throws IOException {
		this.scannerSocket = new ServerSocket(BARCODE_SCANNER_DEFAULT_PORT, backlog);
	}
	
	public BarcodeScanner(int port) throws IOException {
		this.scannerSocket = new ServerSocket(port, backlog);
	}
	
	public String getBarcodeFromScanner() throws IOException {
		return new BufferedReader(new InputStreamReader(this.scannerSocket.accept().getInputStream())).readLine();
	}
}
