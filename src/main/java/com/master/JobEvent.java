package com.master;

import org.apache.hadoop.yarn.event.AbstractEvent;

public class JobEvent extends AbstractEvent<JobEventType>{

	public JobEvent(JobEventType type) {
		super(type);
	}

}
