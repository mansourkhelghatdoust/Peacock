package usyd.distributed.scheduling.simulator.observers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FileResultPrinter extends ResultPrinterBase {

	private String fileName;

	public FileResultPrinter(String fileName) {
		this.fileName = fileName;
	}

	public FileResultPrinter(ResultPrinterBase base, String fileName) {
		super(base);
		this.fileName = fileName;
	}

	@Override
	public void print(List<String> list) {

		try {
			
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");

			list.stream().forEach(writer::println);

			writer.close();

		} catch (IOException e) {
			System.out.println("Error in writing the output " + e.getMessage());
		}

	}

}
