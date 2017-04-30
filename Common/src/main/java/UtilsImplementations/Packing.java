package UtilsImplementations;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.*;
import java.util.Base64;

import org.apache.log4j.Logger;

import SMExceptions.SMException;

/**
 * @author idan atias
 *
 * @since Apr 28, 2017
 * 
 *        This is a common class used for packing files. Usually used when
 *        communicating between client and server.
 */
public class Packing {

	protected static Logger log = Logger.getLogger(Packing.class.getName());
	static final int bufferSize = 2048; // chunks of 2KB

	public static ZipFile pack(File[] fs, String zipFilePath) {
		if (fs == null || fs.length <= 0 || zipFilePath == null || zipFilePath.isEmpty())
			return null;
		try {
			int totEntries = 0;
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(zipFilePath);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
			byte data[] = new byte[bufferSize];

			for (File file : fs) {
				if (!file.isFile())
					continue;

				String filename = file.getName();
				String filepath = file.getAbsolutePath();
				long filesize = file.length(); // size in bytes

				log.info("Zipping and adding: " + filename + " into " + zipFilePath);

				FileInputStream fins = new FileInputStream(filepath);
				origin = new BufferedInputStream(fins, bufferSize);
				ZipEntry entry = new ZipEntry(filename);
				out.putNextEntry(entry);

				int count;
				for (long bytesLeftToWrite = filesize; bytesLeftToWrite > 0; bytesLeftToWrite -= count) {
					count = origin.read(data, 0, bufferSize);
					out.write(data, 0, count);
				}

				origin.close();
				++totEntries;
			}

			out.close();
			if (totEntries == 0)
				log.info("Created zip " + zipFilePath + "has no entries.");

			log.info("Successfully created zip " + zipFilePath);
			return new ZipFile(zipFilePath);

		} catch (Exception e) {
			log.error("Failed to pack files into " + zipFilePath);
			log.error(e + "");
			return null;
		}
	}

	public static void unpack(ZipFile zfile, String unpackPath) throws SMException {
		if (zfile == null || unpackPath == null || unpackPath.isEmpty()) {
			log.fatal("unpacking failed due to invalid parameter.");
			throw new SMException();
		}

		File unpackDir;
		try {
			unpackDir = new File(unpackPath);
		} catch (Exception e) {
			log.error(e + "");
			throw new SMException();
		}

		if (!unpackDir.isDirectory()) {
			log.error("Unpacking failed: Unpack path must be a directory");
			throw new SMException();
		}

		try {
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			for (Enumeration<? extends ZipEntry> e = zfile.entries(); e.hasMoreElements();) {
				entry = e.nextElement();
				log.info("Extracting: " + entry);
				is = new BufferedInputStream(zfile.getInputStream(entry));
				int count;
				byte data[] = new byte[bufferSize];

				FileOutputStream fos = new FileOutputStream(unpackDir.getAbsolutePath() + '/' + entry.getName());
				dest = new BufferedOutputStream(fos, bufferSize);

				while ((count = is.read(data, 0, bufferSize)) != -1)
					dest.write(data, 0, count);

				dest.flush();
				dest.close();
				is.close();
			}
		} catch (Exception e) {
			log.error(e + "");
			log.error("Failed extracting zipfile " + zfile + " into " + unpackDir.getAbsolutePath());
		}
	}

	public static String encode(File f) {
		String encodedfile = null;
		try {
			FileInputStream fileInputStreamReader = new FileInputStream(f);
			byte[] bytes = new byte[(int) f.length()];
			fileInputStreamReader.read(bytes);
			encodedfile = Base64.getEncoder().encodeToString(bytes) + "";
			fileInputStreamReader.close();
		} catch (Exception e) {
			log.error(e + "");
			log.error("Failed encoding file " + f);
			return null;
		}
		return encodedfile;
	}
	
	public static File decode(String s, String resPath){
		try {	

			byte[] sBytes = Base64.getDecoder().decode(s);
			int len = sBytes.length;
	
			FileOutputStream fos = new FileOutputStream(resPath);
			BufferedOutputStream dest = new BufferedOutputStream(fos, bufferSize);				

			dest.write(sBytes, 0, len);

			dest.flush();
			dest.close();
			fos.close();
			
			return new File(resPath);

		} catch (Exception e) {
			log.error(e + "");
			log.error("Failed decoding string into " + resPath);
			return null;
		}

	}

}
