include 'types.thrift'

namespace java usyd.distributed.scheduler.peacock.thrift

	service SchedulerServices {
		
		  void submitJob(1: types.TSchedulingRequest request) throws (1: types.TIncompleteRequestException e);
			
		  # Sends a Task Status change message to be delivered to the front end. Statuses are 
		  # 0 : Task request has been sent to the node monitor 
		  # 1 : Task execution has just been started.
		  # 2 : Task execution has been finished.
		  void notifyTaskStatusChange(1: types.TLightProbe probe, 2: binary message, 3: i32 status);
		
		  bool registerNodeMonitor(1: string nodeMonitorAddress);
		  
		  bool unRegisterNodeMonitor(1: string nodeMonitorAddress);
		  
		  bool broadCastGlobalState(1: types.TGlobalStateUpdateRequest request);
		  
		  void registerScheduler(1: string schedulerAddress);
		  
		  void unRegisterScheduler(1: string schedulerAddress);
	  	    	  
	}
	
	service SchedulerGetTaskService {
	
	    types.TTaskLaunchSpec getTask(1: string jobId, 2: string taskId) 
	}
		
	service NodeMonitorServices {
	
	    bool registerBackend(1: string listenSocket);
		
	    bool enqueueTaskProbes(1: types.TEnqueueProbeRequest request);
	 
	}
	
	service ProbesRotationServices {
	
	    void rotateProbes(1: types.TRotateProbesRequest request);  		
	 
	}
	
	
	service BackendServices {

	    types.TTaskExecutionResult launchTask(1: binary message, 2: types.TLightProbe probe);

	}

	service FrontendServices {
	
	   void notifyTaskStatusChange(1: types.TLightProbe probe, 2: binary message, 3: i32 status);
	
	}