package com.schedulers_algorithms.Preemptive_Priority;

import java.util.Vector;

import com.schedulers_algorithms.AlgorithmType;
import com.schedulers_algorithms.App;
import com.schedulers_algorithms.CPU;
import com.schedulers_algorithms.CPU.CPUState;
import com.schedulers_algorithms.Utils.Process;

/*
 * 
 * Range [0 - 7], with 0 as highest priority.
 * 
 */
public class PreemptivePriority implements AlgorithmType {
    private CPU cpu;
    private boolean isPreemptive;
    private Vector<Process> readyQueue;

    private double totalWaitingTime = 0.0;
    private double totalTurnaroundTime = 0.0;
    private int processesCount = 0;

    private int agingRoundTime = 0;

    public PreemptivePriority(boolean isPreemptive) {
        this.isPreemptive = isPreemptive;
        cpu = new CPU();
        readyQueue = new Vector<Process>();
    }

 @Override
    public void addProcessToReadyQueue(Process process) {
        processesCount++;
        switch (cpu.getState()) {
            case IDLE:
                cpu.switchState(CPUState.BUZY);
                cpu.hookProcess(process);
                return;
            case BUZY:
                if(isPreemptive){
                    hookProcessOnCPUIfHigherPriority(process);
                    return;
                }
                else
                {
                    hookProcessOnReadyQueue(process);
                    return;
                }
            default:
                return;
        }
    }

    private void hookProcessOnCPUIfHigherPriority(Process process) {
        /*
         * 
         * Here we will apply FCFS for processes with same priorities.
         * For processes with lower priorities, just add them to the ready queue.
         * 
         */
        if (process.getPriority() < cpu.getHookedProcessPriority()) {
            hookProcessOnReadyQueue(cpu.getHookedProcess());
            cpu.hookProcess(process);
        } else {
            hookProcessOnReadyQueue(process);
        }
    }

    private void hookProcessOnReadyQueue(Process process) {
        readyQueue.add(process);
    }

    @Override
    public Process getCPUHookedProcess() {
        return cpu.getHookedProcess();
    }

    @Override
    public boolean isCPUBuzy() {
        return cpu.isBuzy();
    }

    @Override
    public void executeProcess() {
        int currentTime = App.getCurrentTime();
        agingRoundTime++;
        if (agingRoundTime > 4) {
            ageProcesses();
            agingRoundTime = 0;
        }

        if (cpu.getState() == CPUState.IDLE) {
            if (!hookProcessOnCPUFromReadyQueue())
                return;
            else {
                totalWaitingTime+=currentTime;
            }    
        }

        cpu.getHookedProcess().runProcess(1);

        if (cpu.getHookedProcess().isFinished()) {
            totalTurnaroundTime+=currentTime-cpu.getHookedProcess().getArrivalTime();
            cpu.switchState(CPUState.IDLE);
            cpu.unHookProcess();
            hookProcessOnCPUFromReadyQueue();
        }
    }

    private boolean hookProcessOnCPUFromReadyQueue() {
        if (readyQueue.size() == 0)
            return false;

        int highestPriorityProcessValue = Integer.MAX_VALUE;
        int highestPriorityProcessIndex = Integer.MAX_VALUE;
        for (int i = 0; i < readyQueue.size(); i++) {
            if (readyQueue.elementAt(i).getPriority() < highestPriorityProcessValue) {
                highestPriorityProcessIndex = i;
                highestPriorityProcessValue = readyQueue.elementAt(i).getPriority();
            }
        }

        if (highestPriorityProcessIndex != Integer.MAX_VALUE) {
            cpu.hookProcess(readyQueue.elementAt(highestPriorityProcessIndex));
            cpu.switchState(CPUState.BUZY);
            readyQueue.removeElementAt(highestPriorityProcessIndex);
        }

        return true;
    }

    private void ageProcesses() {
        for (int i = 0 ; i < readyQueue.size() ; i++) {
            readyQueue.elementAt(i).age();
        }
    }

    public double getAverageWaitingTime() {
        return totalWaitingTime / processesCount;
    }

    @Override
    public double getAverageTurnaroundTime() {
        return totalTurnaroundTime / processesCount;
    }
}
