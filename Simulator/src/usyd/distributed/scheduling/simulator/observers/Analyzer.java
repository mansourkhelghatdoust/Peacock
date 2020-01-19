package usyd.distributed.scheduling.simulator.observers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;

import usyd.distributed.scheduling.util.DataHelper;

public class Analyzer {

	public void calculateAveragePerJob() {

		try {
			File directory = new File(DataHelper.getDefaultOutputPath());
			// get all the files from a directory
			File[] algorithmsFld = directory.listFiles();

			for (File algorithm : algorithmsFld) {

				if (algorithm.isDirectory()) {

					File[] configurations = algorithm.listFiles();

					for (File configuration : configurations) {

						HashMap<Long, Long> aggregationOfExecutions = new HashMap<Long, Long>();
						int counter = 0;

						if (configuration.isDirectory()) {

							File[] executions = configuration.listFiles();

							for (File execution : executions) {

								if (execution.getName().startsWith("pjct")) {
									BufferedReader br = new BufferedReader(new FileReader(execution.getAbsolutePath()));
									while (true) {
										String currentLine = br.readLine();
										if (currentLine == null)
											break;
										String[] slices = currentLine.split(":");
										if (!aggregationOfExecutions.containsKey(Long.parseLong(slices[0].trim()))) {
											aggregationOfExecutions.put(Long.parseLong(slices[0].trim()),
													Long.parseLong(slices[1].trim()));
										} else {
											aggregationOfExecutions.put(Long.parseLong(slices[0].trim()),
													aggregationOfExecutions.get(Long.parseLong(slices[0].trim()))
															+ Long.parseLong(slices[1].trim()));
										}
									}
									br.close();
									counter++;
									// Save the average

								}
							}
							PrintWriter writer = new PrintWriter(
									configuration.getAbsolutePath() + "-" + algorithm.getName() + "-AvgPerJob.txt",
									"UTF-8");
							for (Entry<Long, Long> entry : aggregationOfExecutions.entrySet())
								writer.println(entry.getKey() + ":" + entry.getValue() / counter);
							writer.close();
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void calculatePercentagePerJobCompletionTime() {

		try {
			int first = 0, second = 0, draw = 0, count = 0;
			String firstFileName = null, secondFileName = null;
			boolean firstRead = false;
			HashMap<Long, Long> firstData = new HashMap<Long, Long>();
			HashMap<Long, Long> secondData = new HashMap<Long, Long>();

			File directory = new File(DataHelper.getDefaultOutputPath());
			// get all the files from a directory
			File[] folders = directory.getParentFile().listFiles();
			for (File folder : folders) {
				if (folder.getName().startsWith("Container")) {
					File[] files = folder.listFiles();
					for (File file : files) {

						BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
						while (true) {
							String currentLine = br.readLine();
							if (currentLine == null)
								break;
							String[] slices = currentLine.split(":");
							if (!firstRead) {
								firstFileName = file.getName();
								if (!firstData.containsKey(Long.parseLong(slices[0].trim()))) {
									firstData.put(Long.parseLong(slices[0].trim()), Long.parseLong(slices[1].trim()));
								} else {
									firstData.put(Long.parseLong(slices[0].trim()),
											firstData.get(Long.parseLong(slices[0].trim()))
													+ Long.parseLong(slices[1].trim()));
								}
							} else {
								secondFileName = file.getName();
								if (!secondData.containsKey(Long.parseLong(slices[0].trim()))) {
									secondData.put(Long.parseLong(slices[0].trim()), Long.parseLong(slices[1].trim()));
								} else {
									secondData.put(Long.parseLong(slices[0].trim()),
											secondData.get(Long.parseLong(slices[0].trim()))
													+ Long.parseLong(slices[1].trim()));
								}
							}
						}
						br.close();
						firstRead = true;
					}
				}
			}

			for (Entry<Long, Long> entry : firstData.entrySet()) {
				if (firstData.get(entry.getKey()) < secondData.get(entry.getKey()))
					first++;
				else if (firstData.get(entry.getKey()) > secondData.get(entry.getKey()))
					second++;
				else
					draw++;
				count++;
			}

			System.out.println(firstFileName + "  " + (double) first * 100 / count);
			System.out.println(secondFileName + "  " + (double) second * 100 / count);
			System.out.println("draw" + "  " + (double) draw * 100 / count);

		} catch (Exception ex) {

		}
	}

	public void calculateMove() {

		try {
			int moveCount = 0, taskCount = 0;
			File directory = new File(DataHelper.getDefaultOutputPath());
			// get all the files from a directory
			File[] folders = directory.listFiles();
			for (File folder : folders) {
				if (folder.getName().startsWith("move")) {
					BufferedReader br = new BufferedReader(new FileReader(folder.getAbsolutePath()));
					while (true) {
						String currentLine = br.readLine();
						if (currentLine == null)
							break;
						String[] slices = currentLine.split(":");
						taskCount += Long.parseLong(slices[1]);
						moveCount += Long.parseLong(slices[2]);
					}
					br.close();
				}
			}

			System.out.println((double) moveCount / taskCount);

		} catch (Exception ex) {

		}
	}

	public static void main(String[] args) {
		Analyzer analyzer = new Analyzer();
		analyzer.calculateMove();
	}
}