package usyd.distributed.scheduling.analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;

public class AJCTCalculator {

	private static final String path = "D:\\Result\\";
	private static final String outPath = "D:\\Result-AJCT\\";

	public static void main(String[] args) {

		AJCTCalculator analyzer = new AJCTCalculator();
		analyzer.calculate();
	}

	public void calculate() {

		String[] directories = getDirectories();

		for (String directory : directories) {

			String[] subDirectories = getSubDirectories(path + directory);

			for (String subDirectory : subDirectories) {

				File[] files = getFiles(path + directory + "\\" + subDirectory);

				Integer result = calculateAverageJobCompletionTime(files);

				print(directory, subDirectory, result);

				System.out.println(directory + "  " + subDirectory);

			}
		}

	}

	private void print(String directory, String subDirectory, Integer result) {

		PrintWriter printWriter;
		try {

			printWriter = new PrintWriter(outPath + directory + "\\" + subDirectory + "\\" + "result.txt");
			printWriter.println(result);

			printWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Integer calculateAverageJobCompletionTime(File[] files) {

		Integer sum = 0;

		for (File file : files)
			sum += calculateAverageJobCompletionTime(file);

		return sum / files.length;
	}

	private Integer calculateAverageJobCompletionTime(File file) {

		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(file.getAbsolutePath());
			br = new BufferedReader(fr);

			String sCurrentLine = br.readLine();
			if (sCurrentLine != null)
				return Integer.parseInt(sCurrentLine.split(":")[1].trim());
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
		return 0;

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
				return pathname.getName().startsWith("ajct");
			}
		});

		return files;
	}
}