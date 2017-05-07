package PicturesHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.zip.ZipFile;

import CommonDefs.GuiCommonDefs;
import SMExceptions.SMException;
import UtilsImplementations.Packing;

public class PictureManager {
	public static boolean checkIfMostUpdate(LocalDate d) throws IOException {
		ObjectInputStream objectinputstream = null;
		LocalDate updatedDate = null;
		
		try {
			FileInputStream streamIn = new FileInputStream(GuiCommonDefs.productsPicturesPathLastUpdate);
		    objectinputstream = new ObjectInputStream(streamIn);
		    updatedDate = (LocalDate) objectinputstream.readObject();
		} catch (Exception e) {
			throw new IOException();
		} finally {
		    if(objectinputstream != null)
				objectinputstream.close(); 
		}
		
		return updatedDate != null && d.compareTo(updatedDate) >= 0;
	}
	
	public static void updateDate(LocalDate d) throws IOException {
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		
		try {
			File file = new File(GuiCommonDefs.productsPicturesPathLastUpdate);
			fout = new FileOutputStream(file, false);
		    oos = new ObjectOutputStream(fout);
		    oos.writeObject(d);
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new IOException();
		} finally {
		    if(oos  != null)
				oos.close(); 
		}
	}
	
	public static LocalDate getCurrentDate() throws IOException {
		ObjectInputStream objectinputstream = null;
		LocalDate updatedDate = null;
		
		try {
			FileInputStream streamIn = new FileInputStream(GuiCommonDefs.productsPicturesPathLastUpdate);
		    objectinputstream = new ObjectInputStream(streamIn);
		    updatedDate = (LocalDate) objectinputstream.readObject();
		} catch (Exception e) {
			throw new IOException();
		} finally {
		    if(objectinputstream != null)
				objectinputstream.close(); 
		}
		
		return updatedDate;
	}
	
	public static void doPicturesExchange(String encodedPicturesZipFile) throws IOException, SMException{
		File decodedZipFile = Packing.decode(encodedPicturesZipFile, GuiCommonDefs.productsCustomerPicturesFolderZipFile);
		if (decodedZipFile == null)
			throw new RuntimeException();
		ZipFile productPicturesZip = new ZipFile(decodedZipFile);
		Packing.unpack(productPicturesZip, GuiCommonDefs.productsPicturesFolderPath);
		decodedZipFile.delete();
	}
}
