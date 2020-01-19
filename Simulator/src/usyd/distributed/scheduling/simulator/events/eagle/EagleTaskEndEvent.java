package usyd.distributed.scheduling.simulator.events.eagle;

import usyd.distributed.scheduling.simulator.Worker;
import usyd.distributed.scheduling.simulator.Worker.JobTaskInfo;
import usyd.distributed.scheduling.simulator.events.TaskEndEvent;

public class EagleTaskEndEvent extends TaskEndEvent {

	public EagleTaskEndEvent(int time, Worker worker, JobTaskInfo jobTaskInfo) {
		super(time, worker, jobTaskInfo);
	}

	@Override
	public void run() {

		if (jobTaskInfo.getJob().getAvgExecutionTime() > 2000)
			EagleCentralizedScheduler.setOff(worker.getId());

		super.run();

	}
}
