package usyd.distributed.scheduling.analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class AJCTComparator {

	private static final String path = "D:\\Result-AJCT\\";

	private static final String outPath = "D:\\Result-AJCT\\Results\\";

	public static void main(String[] args) {

		AJCTComparator jct = new AJCTComparator();

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

		String thisData = readFile(thisDirectory + config + "\\result.txt");
		String otherData = readFile(otherDirectory + config + "\\result.txt");

		if (thisData == null || otherData == null || thisData.isEmpty() || otherData.isEmpty())
			return null;

		double thisdata = Double.parseDouble(thisData);
		double otherdata = Double.parseDouble(otherData);

		return Double.toString((thisdata / otherdata) * 100);

	}

	private String readFile(String file) {

		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(file);
			br = new BufferedReader(fr);

			br = new BufferedReader(new FileReader(file));

			return br.readLine().trim();

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

		return "0";
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
