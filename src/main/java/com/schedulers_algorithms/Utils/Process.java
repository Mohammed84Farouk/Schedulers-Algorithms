package com.schedulers_algorithms.Utils;

import javafx.scene.paint.Color;

public class Process {

	private int id;
	private int arrival;
	private int burst;
	private int priority;
	private int waitingTime;
	private int turnAroundTime;
	private Color color;

	public Process(int id, int arrival, int burst, int priority, Color color) {
		this.id = id;
		this.arrival = arrival;
		this.burst = burst;
		this.priority = priority;
		this.color = color;
	}

	public int getId() {
		return this.id;
	}

	public int getArrivalTime() {
		return this.arrival;
	}

	public void setBurstTime(int burst) {
		this.burst = burst;
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

	public Color getColor() {
		return color;
	}
}
