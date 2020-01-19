package usyd.peacock.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.Joiner;

public class LogUtil {

	private final static Logger LOG = Logger.getLogger(LogUtil.class);

	private static Joiner paramJoiner = Joiner.on(",").useForNull("null");

	public static void logFunctionCall(Object... params) {

		String name = Thread.currentThread().getStackTrace()[2].getMethodName();

		LOG.info("Calling Function : " + name + " : [ " + paramJoiner.join(params) + " ]");

	}

	public static void logInfo(String text, String fileName) {

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			File file = new File(System.getProperty("user.dir") + "//" + fileName);

			if (!file.exists())
				file.createNewFile();

			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			bw.write(text);
			bw.newLine();

			bw.flush();
			fw.flush();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

	}

	public static void logInfo(List<String> list, String fileName) {

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			File file = new File(System.getProperty("user.dir") + "//" + fileName);

			if (!file.exists())
				file.createNewFile();

			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			for (String text : list) {
				bw.write(text);
				bw.newLine();
			}

			bw.flush();
			fw.flush();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

	}

}
