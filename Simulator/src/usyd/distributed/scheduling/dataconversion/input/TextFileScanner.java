package usyd.distributed.scheduling.dataconversion.input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import usyd.distributed.scheduling.dataconversion.InputScanner;
import usyd.distributed.scheduling.dataconversion.Utility;

public class TextFileScanner implements InputScanner<String> {

	BufferedReader br = null;
	String currentLine = null;
	boolean hasRead = false;

	public TextFileScanner(String fileName) {

		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			Utility.handleIOException(br, e);
		}
	}

	@Override
	public boolean hasNext() {

		if (hasRead)
			return true;
		try {
			currentLine = br.readLine();
			if (currentLine != null) {
				hasRead = true;
				return true;
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public String next() {

		if (hasRead) {
			hasRead = false;
			return currentLine;
		}
		return null;

	}
}
