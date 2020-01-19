package usyd.distributed.scheduling.analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class PJCTComparator {

	private static final String path = "D:\\Result-PJCT\\";

	private static final String outPath = "D:\\Result-PJCT\\Results\\";

	public static void main(String[] args) {

		PJCTComparator jct = new PJCTComparator();

		jct.doComparision("10000-5s-Full", "10000-10s-Full");

		jct.doComparision("10000-5s-Full", "10000-15s-Full");

		jct.doComparision("10000-5s-Full", "Eagle-10000");

		jct.doComparision("15000-5s-Full", "Sparrow-15000");

		jct.doComparision("15000-5s-Full", "Eagle-15000");

		jct.doComparision("10000-5s-Full", "Sparrow-10000");

		jct.doComparision("10000-15s-MisEstimation", "Sparrow-10000");

		jct.doComparision("10000-15s-Full", "10000-15s-WithoutAverageWorkload");

		jct.doComparision("10000-15s-Full", "10000-15s-WithoutMoving");

		jct.doComparision("10000-15s-Full", "10000-15s-WithoutReordering");

		jct.doComparision("10000-15s-Full", "10000-15s-WithoutTS");

		jct.doComparision("10000-15s-Full", "10000-15s-WithuotQueueSize");

		jct.doComparision("20000-5s-Full", "Eagle-20000");

		jct.doComparision("20000-5s-Full", "Sparrow-20000");

	}

	public void doComparision(String thisOne, String otherOne) {

		String thisDirectory = path + thisOne + "\\";

		String otherDirectory = path + otherOne + "\\";

		HashMap<String, String> directories = getDirectories(thisDirectory, otherDirectory);

		doComparision(thisOne, otherOne, directories);

		System.out.println(thisDirectory + "    " + otherDirectory);

	}

	private void doComparision(String thisOne, String otherOne, HashMap<String, String> directories) {

		String thisDirectory = path + thisOne + "\\";

		String otherDirectory = path + otherOne + "\\";

		for (String key : directories.keySet()) {
			String result = doComparision(thisDirectory, otherDirectory, key);
			print(thisOne, otherOne, key, result);
		}

	}

	private void print(String thisDirectory, String otherDirectory, String key, String result) {

		PrintWriter printWriter;
		try {

			printWriter = new PrintWriter(
					outPath + thisDirectory + ";" + otherDirectory + "\\" + key + "\\" + "result.txt");

			printWriter.println(result);

			printWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private String doComparision(String thisDirectory, String otherDirectory, String config) {

		HashMap<Long, Long> thisData = readFile(thisDirectory + config + "\\result.txt");
		HashMap<Long, Long> otherData = readFile(otherDirectory + config + "\\result.txt");

		if (thisData == null || otherData == null || thisData.isEmpty() || otherData.isEmpty())
			return null;

		double thisWon = 0, otherWon = 0, equal = 0;

		for (Long key : thisData.keySet()) {

			if (thisData.get(key) < otherData.get(key) + 1) {
				thisWon++;
			} else if (otherData.get(key) < thisData.get(key) +1) {
				otherWon++;
			} else {
				equal++;
			}
		}

		return Double.toString((thisWon / thisData.size()) * 100) + "       "
				+ Double.toString((otherWon / thisData.size()) * 100) + "        "
				+ Double.toString((equal / thisData.size()) * 100);

	}

	private HashMap<Long, Long> readFile(String file) {

		BufferedReader br = null;
		FileReader fr = null;
		HashMap<Long, Long> retVal = new HashMap<Long, Long>();

		try {

			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String currentLine = null;

			while ((currentLine = br.readLine()) != null)
				retVal.put(Long.parseLong(currentLine.split(":")[0]), Long.parseLong(currentLine.split(":")[1]));

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

		return retVal;
	}

	private HashMap<String, String> getDirectories(String thisDirectory, String otherDirectory) {

		HashMap<String, String> directories = new HashMap<String, String>();

		File file = new File(thisDirectory);

		String[] thisDirectories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		for (String key : thisDirectories)
			directories.put(key, null);

		file = new File(otherDirectory);

		String[] otherDirectories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		for (String value : otherDirectories) {
			if (directories.containsKey(value))
				directories.put(value, value);

		}

		return directories;
	}

}
