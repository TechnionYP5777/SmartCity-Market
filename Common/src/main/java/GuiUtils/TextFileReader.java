package GuiUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TextFileReader - Class for reading a regular text files
 * @author Lior Ben Ami
 * @since 2017-05-11
 */
public class TextFileReader {
	public List<String> read(File file) throws IOException {
		List<String> lines = new ArrayList<String>();
		String line;
		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((line = br.readLine()) != null)
			lines.add(line);
		br.close();
		return lines;
	}
}
