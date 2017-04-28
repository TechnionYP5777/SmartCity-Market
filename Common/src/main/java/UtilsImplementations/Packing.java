package UtilsImplementations;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.*;

import org.apache.log4j.Logger;

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
				long filesize = file.length(); //size in bytes

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
}
