package sgf;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SGFReaderTest {

	private static String sgfFile = SGFReader.class.getResource("").getPath();

	@Test
	public void readSGF() {
		SGFReader reader = new SGFReader(loadFile(sgfFile));
	}

	private String loadFile(String filepath) {
		InputStreamReader is = null;
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();

		try {
			is = new FileReader(filepath);
			reader = new BufferedReader(is);

			String currentLine = null;
			while ((currentLine = reader.readLine()) != null) {
				builder.append(currentLine);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if ( is != null )
					is.close();

				if ( reader != null )
					reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return builder.toString();
	}
}