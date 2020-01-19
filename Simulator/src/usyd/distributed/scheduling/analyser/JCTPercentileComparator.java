package usyd.distributed.scheduling.analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class JCTPercentileComparator {

	private static final String path = "D:\\Result-JCTPercentile\\";

	private static final String outPath = "D:\\Result-JCTPercentile\\Results\\";

	public static void main(String[] args) {

		JCTPercentileComparator jct = new JCTPercentileComparator();

		jct.doComparision("10000-5s-Full", "10000-10s-Full");

		jct.doComparision("10000-5s-Full", "10000-15s-Full");

		jct.doComparision("10000-5s-Full", "Eagle-10000");

		jct.doComparision("10000-5s-Full", "Sparrow-10000");

		jct.doComparision("15000-5s-Full", "Sparrow-15000");

		jct.doComparision("15000-5s-Full", "Eagle-15000");

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
			HashMap<String, String> result = doComparision(thisDirectory, otherDirectory, key);
			print(thisOne, otherOne, key, result);
		}

	}

	private void print(String thisDirectory, String otherDirectory, String key, HashMap<String, String> result) {

		PrintWriter printWriter;
		try {

			printWriter = new PrintWriter(
					outPath + thisDirectory + ";" + otherDirectory + "\\" + key + "\\" + "result.txt");

			for (String val : result.keySet())
				printWriter.println(val + ":" + result.get(val));

			printWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private HashMap<String, String> doComparision(String thisDirectory, String otherDirectory, String config) {

		HashMap<String, String> result = new HashMap<String, String>();

		HashMap<String, String> thisData = readFile(thisDirectory + config + "\\result.txt");
		HashMap<String, String> otherData = readFile(otherDirectory + config + "\\result.txt");

		if (thisData == null || otherData == null || thisData.isEmpty() || otherData.isEmpty())
			return result;

		for (String key : thisData.keySet()) {

			double thisdata = Double.parseDouble(thisData.get(key));
			double otherdata = Double.parseDouble(otherData.get(key));

			result.put(key, Double.toString((thisdata / otherdata) * 100));
		}

		return result;
	}

	private HashMap<String, String> readFile(String file) {

		HashMap<String, String> result = new HashMap<String, String>();

		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(file));

			while ((sCurrentLine = br.readLine()) != null) {
				result.put(sCurrentLine.split(":")[0].trim(), sCurrentLine.split(":")[1].trim());
			}

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
		return result;

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
