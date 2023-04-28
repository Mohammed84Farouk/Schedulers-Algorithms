package com.schedulers_algorithms.Non_Preemptive_SJF;




import com.schedulers_algorithms.AlgorithmType;

import java.util.TreeSet;
import java.util.Vector;
import com.schedulers_algorithms.Utils.Process;

import static com.schedulers_algorithms.App.getCurrentTime;

public class SJFS implements AlgorithmType {
    private TreeSet<Process> processesB = new TreeSet<>(new SJFSProcessComparatorB());
    private TreeSet<Process> processesA = new TreeSet<>(new SJFSProcessComparatorA());
    Vector<Process> readyQueue;
    private boolean isPreemptive = false;
    private double AverageWaitingTime = 0;
    private double AverageTurnAroundTime = 0;
    private Process currentProcess;
    public SJFS(boolean isPreemptive) {
        this.isPreemptive = isPreemptive;
        this.currentProcess = null;
    }

    @Override
    public void addProcessToReadyQueue(Process process){
        processesA.add(process);
    }



    @Override
    public Process getCPUHookedProcess() {
        return this.currentProcess;
    }

    @Override
    public boolean isCPUBuzy() {
        return currentProcess != null;
    }

    public void runProcess(){
        if(currentProcess != null)currentProcess.setBurstTime(currentProcess.getBurstTime()-1);
        if(currentProcess == null || currentProcess.getBurstTime() == 0){
            if(!getProcessFromReadyQueue()){
                currentProcess = null;
            }
        }

    }
    private boolean getProcessFromReadyQueue(){
        int currentTime = getCurrentTime();
        System.out.println(currentTime);
        while(!processesA.isEmpty() && currentTime >= processesA.first().getArrivalTime()){
            processesB.add(processesA.first());
            processesA.remove(processesA.first());
        }
        if(processesB.isEmpty())  {
            return false;
        }
        else currentProcess = processesB.first();
        currentProcess.setWaitingTime(currentTime-currentProcess.getArrivalTime());
        currentProcess.setTurnAroundTime(currentTime - currentProcess.getArrivalTime());
        this.AverageWaitingTime+= currentProcess.getWaitingTime();
        this.AverageTurnAroundTime+=(currentTime - currentProcess.getArrivalTime());
        processesB.remove(currentProcess);
        return true;
    }


}
