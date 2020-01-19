package usyd.distributed.scheduling.dataconversion.input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import usyd.distributed.scheduling.dataconversion.InputScanner;
import usyd.distributed.scheduling.dataconversion.Utility;

public class BunchTextFileScanner implements InputScanner<BufferedReader> {

	BufferedReader br = null;
	boolean read = false;

	public BunchTextFileScanner(String fileName) {

		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			Utility.handleIOException(br, e);
		}

	}

	@Override
	public boolean hasNext() {

		if (!read) {
			read = true;
			return true;
		}
		return false;
		
	}

	@Override
	public BufferedReader next() {

		if (read)
			return br;
		return null;
		
	}

}
