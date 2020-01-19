package usyd.distributed.scheduling.simulator.observers;

import usyd.distributed.scheduling.simulator.Job;

public interface Visitor {
	public void visit(Job job);
}