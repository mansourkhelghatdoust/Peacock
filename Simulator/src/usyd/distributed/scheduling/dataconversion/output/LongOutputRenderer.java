package usyd.distributed.scheduling.dataconversion.output;

import java.io.PrintWriter;
import java.util.List;
import usyd.distributed.scheduling.dataconversion.OutputRenderer;

public class LongOutputRenderer implements OutputRenderer<Long> {

	private String fullName;

	public LongOutputRenderer(String filePath, String fileName) {
		this.fullName = filePath + fileName;
	}

	@Override
	public void render(List<Long> list) {
		try {
			PrintWriter writer = new PrintWriter(fullName + ".txt", "UTF-8");
			writer.println(list.get(0));
			writer.close();
		} catch (Exception ex) {
			System.out.println("Error occured in BunchTextOutputRenderer " + ex.getMessage());
			System.exit(-1);
		}
	}

}
