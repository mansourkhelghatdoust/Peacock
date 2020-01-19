package usyd.distributed.scheduling.analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

public class JCTPercentileCalculator {

	private static final String path = "D:\\Result\\";
	private static final String outPath = "D:\\Result-JCTPercentile\\";

	public static void main(String[] args) {

		JCTPercentileCalculator analyzer = new JCTPercentileCalculator();
		analyzer.calculate();
	}

	public void calculate() {

		String[] directories = getDirectories();

		for (String directory : directories) {

			String[] subDirectories = getSubDirectories(path + directory);

			for (String subDirectory : subDirectories) {

				File[] files = getFiles(path + directory + "\\" + subDirectory);

				HashMap<Integer, Integer> result = calculatePercentiles(files, 50, 70, 90);

				print(directory, subDirectory, result);

				System.out.println(directory + "  " + subDirectory);

			}

		}
	}

	private void print(String directory, String subDirectory, HashMap<Integer, Integer> result) {

		PrintWriter printWriter;
		try {

			printWriter = new PrintWriter(outPath + directory + "\\" + subDirectory + "\\" + "result.txt");
			for (Integer key : result.keySet())
				printWriter.println(key + ":" + result.get(key));

			printWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private HashMap<Integer, Integer> calculatePercentiles(File[] files, int... percentiles) {

		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();

		for (int percentile : percentiles)
			result.put(percentile, calculatePercentile(files, percentile));

		return result;
	}

	private Integer calculatePercentile(File[] files, int percentile) {

		Integer sum = 0;

		for (File file : files)
			sum += calculatePercentile(file, percentile);

		if (files.length == 0)
			return 0;
		return sum / files.length;
	}

	private Integer calculatePercentile(File file, int percentile) {

		int[] array = new int[173090];
		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(file.getAbsolutePath());
			br = new BufferedReader(fr);

			String sCurrentLine;

			int counter = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				array[counter] = Integer.parseInt(sCurrentLine.split(":")[1].trim());
				counter++;
			}

			Arrays.sort(array);

			return calculatePercentile(array, percentile);

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
		return -1;

	}

	private int calculatePercentile(int[] array, int percentile) {

		return array[Math.floorDiv(Math.multiplyExact(array.length, percentile), 100)];
	}

	private String[] getDirectories() {

		File file = new File(path);

		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		return directories;
	}

	private String[] getSubDirectories(String directory) {

		String[] subDirectories = new File(directory).list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		return subDirectories;
	}

	private File[] getFiles(String innerDirectory) {

		File[] files = new File(innerDirectory).listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().startsWith("pjct");
			}
		});

		return files;
	}
}