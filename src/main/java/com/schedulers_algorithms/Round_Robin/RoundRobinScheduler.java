package com.schedulers_algorithms.Round_Robin;
import java.util.LinkedList;
import java.util.Queue;

import com.schedulers_algorithms.AlgorithmType;
import com.schedulers_algorithms.Utils.Process;
import javafx.scene.paint.Color;

public class RoundRobinScheduler implements AlgorithmType{

    private Queue<Process> queue1;
    private Queue<Process> queue2;
    private int timeQuantum;

    public RoundRobinScheduler(int timeQuantum) {
        this.queue1 = new LinkedList<>();
        this.queue2 = new LinkedList<>();
        this.timeQuantum = timeQuantum;
    }

    public void addProcessToReadyQueue(Process process) {
        queue1.add(process);
    }
    public void runProcess() {
        int time = 0;
        while (!queue1.isEmpty() || !queue2.isEmpty()) {
            Process currentProcess;
            if (!queue1.isEmpty()) {
                currentProcess = queue1.poll();
            } else {
                currentProcess = queue2.poll();
            }
            int runTime = Math.min(timeQuantum, currentProcess.getBurstTime());
            time += runTime;
            currentProcess.runProcess(runTime);
            if (currentProcess.isFinished()) {
                currentProcess.setTurnAroundTime(time - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getBurstTime());
                System.out.println("Process " + currentProcess.getId() + " finished at time " + time);
            } else {
                if (currentProcess.getLastQueue() == 1) {
                    currentProcess.setLastQueue(2);
                    queue2.add(currentProcess);
                } else {
                    currentProcess.setLastQueue(1);
                    queue1.add(currentProcess);
                }
                System.out.println("Process " + currentProcess.getId() + " is executing at time " + time);
            }
        }
    }
    @Override
    public Process getCPUHookedProcess() {
        Process currentProcess;
        if (!queue1.isEmpty()) {
            currentProcess = queue1.poll();
        } else {
            currentProcess = queue2.poll();
        }
        return currentProcess;
    }

    @Override
    public boolean isCPUBuzy() {
        Process currentProcess;
        if (!queue1.isEmpty()) {
            currentProcess = queue1.poll();
        } else {
            currentProcess = queue2.poll();
        }
        return currentProcess != null;
    }

    public static void main(String[] args) throws InterruptedException {
        RoundRobinScheduler scheduler = new RoundRobinScheduler(2);
        Process p1 = new Process(1, 0, 5, 1, Color.RED);
        Process p2 = new Process(2, 1, 3, 2, Color.GREEN);
        Process p3 = new Process(3, 2, 8, 3, Color.BLUE);
        scheduler.addProcessToReadyQueue(p1);
        scheduler.addProcessToReadyQueue(p2);
        scheduler.addProcessToReadyQueue(p3);
        scheduler.runProcess();
    }
}