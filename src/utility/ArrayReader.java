package utility;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArrayReader {

		// private static String[][] result;
		// The name of the file to open.
		String fileName = "tiles.txt";

		public ArrayReader(String fileName) {
			this.fileName = fileName;
		}

		public int[][] read() {
			// This will reference one line at a time
			String line = null;
			int[][] result;
			List<String[]> lines = new ArrayList<>();
			try {
				// FileReader reads text files in the default encoding.
				FileReader fileReader = new FileReader(fileName);

				// Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = new BufferedReader(fileReader);

				while ((line = bufferedReader.readLine()) != null) {
					lines.add(line.split(" "));

				}

				// Always close files.
				bufferedReader.close();
			} catch (FileNotFoundException ex) {
				System.out.println("Unable to open file '" + fileName + "'");
			} catch (IOException ex) {
				System.out.println("Error reading file '" + fileName + "'");
				// Or we could just do this:
				// ex.printStackTrace();
			}
			result = new int[lines.size()][];
			for (int i = 0; i < lines.size(); i++) {
				String[] next = lines.get(i);
				result[i] = new int[next.length];
				for (int q = 0; q < next.length; q++) {
					result[i][q] = Integer.parseInt(next[q]);
				}
			}
			return result;

		}
	}