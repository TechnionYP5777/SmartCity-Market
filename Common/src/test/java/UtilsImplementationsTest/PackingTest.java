package UtilsImplementationsTest;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.zip.ZipFile;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import UtilsImplementations.Packing;

/**
 * @author idan atias
 *
 * @since Apr 28, 2017
 * 
 *        Class for testing packing module
 */
public class PackingTest {
	
	//pack test
	static File[] filesToZip;
	static String filesToZipPath = String.valueOf("src/test/java/UtilsImplementationsTest/testingDirForPackingModule/toPack");
	static String zipfileToPackPath = String.valueOf("src/test/java/UtilsImplementationsTest/testingDirForPackingModule/packed.zip");
	
	//unpack test
	static String zipfileToUnpackPath = String.valueOf("src/test/java/UtilsImplementationsTest/testingDirForPackingModule/toUnpack/packed.zip");
	static String unpackDirPath = String.valueOf("src/test/java/UtilsImplementationsTest/testingDirForPackingModule/unpacked/");

	static boolean DEBUG = true; //initialized as false

	@BeforeClass
	public static void setup() {
		File toPackFolder = new File(filesToZipPath);
		//System.out.println("Working Directory = " + System.getProperty("user.dir"));
		filesToZip = toPackFolder.listFiles();
	}

	@AfterClass
	public static void cleanup(){
		if (DEBUG)
			return;
		File zipfileToDelete = new File(zipfileToPackPath);
		zipfileToDelete.delete();
		File unpackedFolder = new File(unpackDirPath);
		for (File file : unpackedFolder.listFiles())
			file.delete();
	}

	@Test
	public void packTest() {
		ZipFile zf = Packing.pack(filesToZip, zipfileToPackPath);
		if (zf == null)
			fail();
	}

	@Test 
	public void unpackTest(){
		try{
			ZipFile zipfile = new ZipFile(zipfileToUnpackPath);
			Packing.unpack(zipfile, unpackDirPath);
		} catch(Exception e){
			fail();
		}
	}
}
