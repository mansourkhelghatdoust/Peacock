package usyd.distributed.scheduling.dataconversion;

import java.io.File;

public class DirectoryScanner implements InputScanner<String> {

	private File[] listOfFiles;
	private int idx = 0;

	public DirectoryScanner(String directoryFullName) {
		listOfFiles = new File(directoryFullName).listFiles();
	}

	@Override
	public boolean hasNext() {
		if (idx < listOfFiles.length)
			return true;
		return false;
	}

	@Override
	public String next() {
		String retVal = listOfFiles[idx].getName();
		idx++;
		return retVal;
	}

}
