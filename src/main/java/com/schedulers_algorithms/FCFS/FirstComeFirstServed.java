package com.schedulers_algorithms.FCFS;

import java.util.Vector;

import com.schedulers_algorithms.AlgorithmType;
import com.schedulers_algorithms.CPU;
import com.schedulers_algorithms.CPU.CPUState;
import com.schedulers_algorithms.Utils.Process;

public class FirstComeFirstServed implements AlgorithmType {
    private CPU cpu;

    private Vector<Process> readyQueue;

    public FirstComeFirstServed() {
        cpu = new CPU();
        readyQueue = new Vector<Process>();
    }

    @Override
    public void addProcessToReadyQueue(Process process) {
        switch (cpu.getState()) {
            case IDLE:
                cpu.switchState(CPUState.BUZY);
                cpu.hookProcess(process);
                return;
            case BUZY:
                hookProcessOnCPUIfHigherPriority(process);
                return;
            default:
                return;
        }
    }

    private void hookProcessOnCPUIfHigherPriority(Process process) {
        
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
    public void runProcess() {
        if (cpu.getState() == CPUState.IDLE) {
            if (!hookProcessOnCPUFromReadyQueue())
                return;
        }

        int hookedProcessBurstTime = cpu.getHookedProcess().getBurstTime();
        cpu.getHookedProcess().setBurstTime(hookedProcessBurstTime - 1);

        if ((hookedProcessBurstTime - 1) == 0) {
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
            if (readyQueue.elementAt(i).getArrivalTime() < highestPriorityProcessValue) {
                highestPriorityProcessIndex = i;
                highestPriorityProcessValue = readyQueue.elementAt(i).getArrivalTime();
            }
        }

        if (highestPriorityProcessIndex != Integer.MAX_VALUE) {
            cpu.hookProcess(readyQueue.elementAt(highestPriorityProcessIndex));
            cpu.switchState(CPUState.BUZY);
            readyQueue.removeElementAt(highestPriorityProcessIndex);
        }

        return true;
    }
}