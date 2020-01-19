package usyd.distributed.scheduling.simulator.events.hawk;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

public class HawkQueue<E> extends ConcurrentSkipListSet<E> {

	private static final long serialVersionUID = 1L;

	private boolean addMode = true;

	public HawkQueue(Comparator<E> comparator) {
		super(comparator);
	}

	@Override
	public E first() {
		return super.first();
	}

	@Override
	public boolean add(E e) {

		addMode = true;
		boolean retVal = super.add(e);
		addMode = false;
		return retVal;

	}

	@Override
	public E pollFirst() {

		addMode = false;
		E retVal = super.pollFirst();
		addMode = true;
		return retVal;

	}

	@Override
	public boolean remove(Object o) {

		addMode = false;
		super.remove(o);
		addMode = true;
		return true;

	}

	public boolean isAddMode() {
		return addMode;
	}

	public void setAddMode(boolean addMode) {
		this.addMode = addMode;
	}

}
