public class Process {

	private int arrival = 0;
	private int burst = 0;
	private int priority = 0;
	private int waitingTime = 0;
	private int turnAroundTime = 0;
		
	public Process(int arrival, int burst, int priority){
		this.arrival = arrival;
		this.burst = burst;
		this.priority = priority;
	}
	public int getArrivalTime() {
		return this.arrival;
	}
	
	public int getBurstTime() {
		return this.burst;
	}
	
	public int getPriority() {
		return this.priority;
	}
	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
	public int getWaitingTime() {
		return this.waitingTime;
	}
	public void setTurnAroundTime(int turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}
	public int getTurnAroundTime() {
		return this.turnAroundTime;
	}
}
