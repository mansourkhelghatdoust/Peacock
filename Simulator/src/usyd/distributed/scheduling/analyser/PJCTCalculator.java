package usyd.distributed.scheduling.analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class PJCTCalculator {

	private static final String path = "D:\\Result\\";
	private static final String outPath = "D:\\Result-PJCT\\";

	public static void main(String[] args) {

		PJCTCalculator analyzer = new PJCTCalculator();
		analyzer.calculate();
	}

	public void calculate() {

		String[] directories = getDirectories();

		for (String directory : directories) {

			String[] subDirectories = getSubDirectories(path + directory);

			for (String subDirectory : subDirectories) {

				File[] files = getFiles(path + directory + "\\" + subDirectory);

				HashMap<Long, Long> result = calculateAverageCompletionTimePerJob(files);

				print(directory, subDirectory, result);

				System.out.println(directory + "  " + subDirectory);

			}
		}

	}

	private void print(String directory, String subDirectory, HashMap<Long, Long> result) {

		PrintWriter printWriter;
		try {

			printWriter = new PrintWriter(outPath + directory + "\\" + subDirectory + "\\" + "result.txt");
			for (Long key : result.keySet())
				printWriter.println(key + ":" + result.get(key));

			printWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private HashMap<Long, Long> calculateAverageCompletionTimePerJob(File[] files) {

		HashMap<Long, Long> result = new HashMap<Long, Long>();

		for (File file : files) {
			HashMap<Long, Long> current = calculateAverageCompletionTimePerJob(file);
			for (Long key : current.keySet())
				result.put(key, result.get(key) != null ? result.get(key) : 0 + current.get(key));
		}

		for (Long key : result.keySet())
			result.put(key, result.get(key) / files.length);

		return result;
	}

	private HashMap<Long, Long> calculateAverageCompletionTimePerJob(File file) {

		BufferedReader br = null;
		FileReader fr = null;

		HashMap<Long, Long> retVal = new HashMap<Long, Long>();

		try {

			fr = new FileReader(file.getAbsolutePath());
			br = new BufferedReader(fr);

			String sCurrentLine = null;
			while ((sCurrentLine = br.readLine()) != null) {
					retVal.put(Long.parseLong(sCurrentLine.split(":")[0].trim()),
							Long.parseLong(sCurrentLine.split(":")[1].trim()));
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
		return retVal;

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
