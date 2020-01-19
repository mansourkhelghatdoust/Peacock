package usyd.distributed.scheduling.dataconversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Utility {

	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.equals(""))
			return true;
		else
			return false;
	}

	public static void handleIOException(BufferedReader br, Exception ex) {
		try {
			br.close();
			exit(ex);
		} catch (IOException e1) {
			exit(e1);
		}
	}

	public static void handleIOException(BufferedWriter bw) {
		try {
			bw.close();
		} catch (IOException e1) {
			exit(e1);
		}
	}

	public static void handleIOException(BufferedWriter bw, Exception ex) {
		try {
			bw.close();
			exit(ex);
		} catch (IOException e1) {
			exit(e1);
		}
	}

	public static void exit(Exception ex) {
		System.out.println(ex.getMessage());
		System.exit(-1);
	}

}
