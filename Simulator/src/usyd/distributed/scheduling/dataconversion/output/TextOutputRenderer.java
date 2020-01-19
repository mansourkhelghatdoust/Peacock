package usyd.distributed.scheduling.dataconversion.output;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import usyd.distributed.scheduling.dataconversion.OutputRenderer;
import usyd.distributed.scheduling.dataconversion.Utility;

public class TextOutputRenderer implements OutputRenderer<String> {

	private String fullName;
	BufferedReader br = null;

	public TextOutputRenderer(String filePath, String fileName) {
		this.fullName = filePath + fileName;
	}

	@Override
	public void render(List<String> list) {

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fullName, true));
			for (String str : list) {
				writer.write(str);
				writer.newLine();
			}
			writer.flush();

		} catch (Exception ex) {
			Utility.handleIOException(writer, ex);
		} finally {
			Utility.handleIOException(writer);
		}
	}
}
