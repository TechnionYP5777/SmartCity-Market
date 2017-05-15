package UtilsImplementationsTest;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.zip.ZipFile;

import org.apache.log4j.PropertyConfigurator;
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

	static boolean CLEAN_ALL; //init as false
	static String testDir = String.valueOf("src/test/java/UtilsImplementationsTest/testingDirForPackingModule/");

	// pack test
	static File[] filesToZip;
	static String filesToZipPath = String.valueOf(testDir + "toPack");
	static String zipfileToPackPath = String.valueOf(testDir + "packed.zip");

	// unpack test
	static String zipfileToUnpackPath = String.valueOf(testDir + "toUnpack/packed.zip");
	static String unpackDirPath = String.valueOf(testDir + "unpacked/");

	
	@BeforeClass
	public static void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		filesToZip = (new File(filesToZipPath)).listFiles();
	}

	@AfterClass
	public static void cleanup() {
		if (CLEAN_ALL)
			for (File file : (new File(unpackDirPath)).listFiles())
				file.delete();
		else {
			File zipfileToDelete = new File(zipfileToPackPath);
			zipfileToDelete.delete();
		}
	}

	@Test
	public void packTest() {
		ZipFile zf = Packing.pack(filesToZip, zipfileToPackPath);
		if (zf == null)
			fail();
	}

	@Test
	public void unpackTest() {
		try {
			ZipFile zipfile = new ZipFile(zipfileToUnpackPath);
			Packing.unpack(zipfile, unpackDirPath);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void endcodeAndDecodeTest() {
		File orgFile = new File(zipfileToUnpackPath);
		String encodedFile = Packing.encode(orgFile);
		if (encodedFile == null)
			fail();
		File decodedFile = Packing.decode(encodedFile, testDir + "decoded.zip");
		if (decodedFile == null)
			fail();
	}
}
