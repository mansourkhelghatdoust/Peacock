package usyd.distributed.scheduling.util;

import java.io.File;

public class ProcessSynchronizer {

	private String path;

	public ProcessSynchronizer(String path) {
		this.path = path;
	}

	public void lock() {

		while (!new File(path + ".lock").mkdir()) {

		}

	}

	public void unlock() {

		while (!new File(path + ".lock").delete()) {

		}

	}

}
