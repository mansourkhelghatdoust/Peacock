package usyd.distributed.scheduling.dataconversion;

public interface Function<T, U> {
	U apply(T t);
}
