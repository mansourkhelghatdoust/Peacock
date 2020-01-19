package usyd.distributed.scheduling.dataconversion;

import java.util.List;

public interface OutputRenderer<T> {
	void render(List<T> list);
}
