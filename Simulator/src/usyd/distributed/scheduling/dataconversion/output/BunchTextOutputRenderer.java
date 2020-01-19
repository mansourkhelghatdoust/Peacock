package usyd.distributed.scheduling.dataconversion.output;

import java.io.PrintWriter;
import java.util.List;
import usyd.distributed.scheduling.dataconversion.OutputRenderer;

public class BunchTextOutputRenderer implements OutputRenderer<List<String>> {

	private String fullName;

	public BunchTextOutputRenderer(String filePath, String fileName) {
		this.fullName = filePath + fileName;
	}

	@Override
	public void render(List<List<String>> list) {

		try {
			PrintWriter writer = new PrintWriter(fullName, "UTF-8");
			List<String> lst = list.get(0);
			for (String str : lst)
				writer.println(str);
			writer.close();
		} catch (Exception ex) {
			System.out.println("Error occured in BunchTextOutputRenderer " + ex.getMessage());
			System.exit(-1);
		}
		
	}

}
