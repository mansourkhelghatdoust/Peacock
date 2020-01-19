namespace java usyd.distributed.scheduler.peacock.thrift

struct TSchedulingRequest {
  1: map<string,binary> tasks;
  2: i32 estimatedDuration;
  3: string jobId;
  4: string frontendAddress;
  5: i64 maximumWaitingTime;
  6: i64 enterTime;
}

struct TFullProbe {
  1: string taskIds;    
  2: string jobId; 
  3: i64 estimatedDuration;
  4: i64 maximumWaitingTime;
  5: i64 enterTime;
  6: THostPort schedulerAddress; 
  7: bool immediateExecution;
}

struct TLocalFullProbe {
  1: TFullProbe fullProbe;
  2: i64 localEnterTime;
  3: i64 localWaitingTime;    
}

struct TLightProbe {
  1: string taskId;    
  2: string jobId; 
}

struct THostPort {
  1: string host;
  2: i32 port;
}

struct TTaskLaunchSpec {
  1: string taskId;
  2: string jobId; 
  3: binary message;
  4: TGlobalStateInfo globalInfo;
}

struct TGlobalStateUpdateRequest {
  1: i32 numTasks;
  2: i64 avgExecutionTime;
  3: bool added;
}

struct TGlobalStateInfo {
  1: i32 queueSize;
  2: i64 workLoad;
  3: i64 time
}


struct TEnqueueProbeRequest {
  1: TFullProbe probe;
  2: TGlobalStateInfo globalInfo;
}

struct TRotateProbesRequest {
  1: list<TFullProbe> fullProbeIds;
  2: TGlobalStateInfo globalInfo;
}

struct TTaskExecutionResult {
  1: i32 status;
  2: binary message;
}

exception TIncompleteRequestException {
  1: string message;
}
