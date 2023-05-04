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
    private int time;

    public RoundRobinScheduler(int timeQuantum) {
        this.queue1 = new LinkedList<>();
        this.queue2 = new LinkedList<>();
        this.timeQuantum = timeQuantum;
        this.time = 0;
    }

    @Override
    public void addProcessToReadyQueue(Process process) {
        queue1.add(process);
    }

    @Override
    public void executeProcess() {
        Process currentProcess;
        if (!queue1.isEmpty()) {
            currentProcess = queue1.poll();
        } else if (!queue2.isEmpty()) {
            currentProcess = queue2.poll();
        } else {
            return; // nothing to execute
        }
        int runTime = Math.min(timeQuantum, currentProcess.getBurstTime());
        time += runTime;
        currentProcess.runProcess(timeQuantum);
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
    public double getAverageWaitingTime() {
        int totalWaitingTime = 0;
        int numProcesses = 0;
        for (Process p : queue1) {
            totalWaitingTime += p.getWaitingTime();
            numProcesses++;
        }
        for (Process p : queue2) {
            totalWaitingTime += p.getWaitingTime();
            numProcesses++;
        }
        return (double) totalWaitingTime / numProcesses;
    }
    
    public double getAverageTurnaroundTime() {
        int totalTurnaroundTime = 0;
        int numProcesses = 0;
        for (Process p : queue1) {
            totalTurnaroundTime += p.getTurnAroundTime();
            numProcesses++;
        }
        for (Process p : queue2) {
            totalTurnaroundTime += p.getTurnAroundTime();
            numProcesses++;
        }
        return (double) totalTurnaroundTime / numProcesses;
    }
    @Override
    public Process getCPUHookedProcess() {
        Process currentProcess;
        if (!queue1.isEmpty()) {
            currentProcess = queue1.poll();
        } else {
            currentProcess = queue2.poll();
        }
        System.out.println("getCPUHookedProcess() "+currentProcess);
        return currentProcess;
    }

    @Override
    public boolean isCPUBuzy() {
        if (!queue1.isEmpty() || !queue2.isEmpty()) {
            System.out.println("isCPUBuzy() true");
            return true;
        } else {
            System.out.println("isCPUBuzy() false");
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
    }
}
