package usyd.distributed.scheduling.simulator.events.eagle;

import java.util.HashMap;

public class SuccinctStateSharing {

	public double timeStamp;
	public HashMap<Integer, Integer> states;

	public SuccinctStateSharing(double timeStamp, HashMap<Integer, Integer> states) {
		super();
		this.timeStamp = timeStamp;
		this.states = states;
	}

}
