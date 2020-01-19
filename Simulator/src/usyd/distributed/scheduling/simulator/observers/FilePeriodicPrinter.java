package usyd.distributed.scheduling.simulator.observers;

public class FilePeriodicPrinter extends PeriodicPrinterBase {

	private String fileName = null;

	public FilePeriodicPrinter() {

	}

	public FilePeriodicPrinter(PeriodicPrinter base, String fileName) {
		super(base);
		this.setFileName(fileName);
	}

	public FilePeriodicPrinter(PeriodicPrinter base, int jobCompletedCountInterval, String fileName) {
		super(base, jobCompletedCountInterval);
		this.setFileName(fileName);
	}

	@Override
	public void print() {
		base.print();
		// To Do
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
