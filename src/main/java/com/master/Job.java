package com.master;

import java.util.EnumSet;
import org.apache.hadoop.yarn.event.EventHandler;
import org.apache.hadoop.yarn.state.MultipleArcTransition;
import org.apache.hadoop.yarn.state.SingleArcTransition;
import org.apache.hadoop.yarn.state.StateMachine;
import org.apache.hadoop.yarn.state.StateMachineFactory;

public class Job implements EventHandler<JobEvent> {

	private static final StateMachineFactory<Job, JobState, JobEventType, JobEvent> stateMachineFactory = new StateMachineFactory<Job, JobState, JobEventType, JobEvent>(
			JobState.NEW).addTransition(JobState.NEW, JobState.RUNNING, JobEventType.START, new StartTransation())
					.addTransition(JobState.RUNNING, EnumSet.of(JobState.FAILED, JobState.SUCCESSED),
							JobEventType.FINISH, new StopTransation())
					.installTopology();

	private final StateMachine<JobState, JobEventType, JobEvent> stateMachine;

	public Job() {
		this.stateMachine = stateMachineFactory.make(this);
	}

	public static void main(String[] args) {
		Job job = new Job();
		System.out.println("发送事件:" + JobEventType.START);
		job.handle(new JobEvent(JobEventType.START));
		System.out.println("StateMachine当前状态:" + job.stateMachine.getCurrentState());
		System.out.println("发送事件:" + JobEventType.FINISH);
		job.handle(new JobEvent(JobEventType.FINISH));
		System.out.println("StateMachine当前状态:" + job.stateMachine.getCurrentState());
	}

	@Override
	public void handle(JobEvent event) {
		this.stateMachine.doTransition(event.getType(), event);
	}

	private static class StartTransation implements SingleArcTransition<Job, JobEvent> {
		public void transition(Job job, JobEvent event) {
			System.out.println("StateMachine处理事件:" + event.getType());
		}
	}

	private static class StopTransation implements MultipleArcTransition<Job, JobEvent, JobState> {

		public JobState transition(Job job, JobEvent event) {
			System.out.println("StateMachine处理事件:" + event.getType());
			int i = 1;

			if (i / 1 == 3) {
				return JobState.FAILED;
			}
			// 默认返回SUCCESSED
			return JobState.SUCCESSED;
		}
	}

}
