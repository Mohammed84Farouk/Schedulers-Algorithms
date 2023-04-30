package com.schedulers_algorithms.Utils;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Process {

	private final int id;
    private final int arrivalTime;
    private int burstTime;
    private int priority;
    private int waitingTime;
    private int turnAroundTime;
    private int lastQueue;
	private final Color color;

    public Process(int id, int arrivalTime, int burstTime, int priority, Color color) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.lastQueue = 1;
		this.color = color;
    }

    public Process(int id, int arrivalTime, int burstTime, Color color) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = -1;
        this.lastQueue = 1;
		this.color = color;
    }

    public int getId() {
        return this.id;
    }

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getBurstTime() {
        return this.burstTime;
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
        return this.color;
    }
    public int getLastQueue() {
        return this.lastQueue;
    }
    public void setLastQueue(int lastQueue) {
        this.lastQueue = lastQueue;
    }
    public boolean isFinished() {
        return burstTime == 0;
    }
    public void runProcess(int time) {
        burstTime -= time;
    }
    public void age() {
        if (priority == 0) return;
        priority--;
    }
}
