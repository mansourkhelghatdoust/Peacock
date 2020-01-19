package usyd.distributed.scheduling.dataconversion;

public interface InputScanner<T> {

	public boolean hasNext();

	public T next();
}
