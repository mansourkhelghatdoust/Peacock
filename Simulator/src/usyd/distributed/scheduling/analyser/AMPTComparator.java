package usyd.distributed.scheduling.analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class AMPTComparator {

	private static final String path = "D:\\Result-AMPT\\";

	private static final String outPath = "D:\\Result-AMPT\\Results\\";

	public static void main(String[] args) {

		AMPTComparator jct = new AMPTComparator();

		jct.doComparision("10000-10s-Full","10000-5s-Full");

		jct.doComparision("10000-15s-Full","10000-5s-Full");

		jct.doComparision("10000-15s-Full","10000-10s-Full");

		jct.doComparision("15000-15s-Full","10000-15s-Full");

		jct.doComparision("20000-15s-Full","15000-15s-Full");

		jct.doComparision("20000-15s-Full","10000-15s-Full");
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
			String result = doComparision(thisDirectory, otherDirectory, key, directories.get(key));
			print(thisOne, otherOne, key, result);
		}

	}

	private void print(String thisDirectory, String otherDirectory, String key, String result) {

		PrintWriter printWriter;
		try {

			printWriter = new PrintWriter(
					outPath + thisDirectory + ";" + otherDirectory + "\\" + key+ "\\" + "result.txt");

			printWriter.println(result);

			printWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private String doComparision(String thisDirectory, String otherDirectory, String configThis, String configOther) {

		String thisData = readFile(
				thisDirectory + thisDirectory.split("\\\\")[2].split("-")[0] + "-" + configThis + "\\result.txt");
		String otherData = readFile(
				otherDirectory + otherDirectory.split("\\\\")[2].split("-")[0] + "-" + configOther + "\\result.txt");

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
			directories.put(key.split("-")[1], null);

		file = new File(otherDirectory);

		String[] otherDirectories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		for (String value : otherDirectories)
			directories.put(value.split("-")[1], value.split("-")[1]);

		return directories;
	}

}
