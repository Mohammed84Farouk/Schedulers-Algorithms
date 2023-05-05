package com.schedulers_algorithms.FCFS;

import java.util.Vector;

import com.schedulers_algorithms.AlgorithmType;
import com.schedulers_algorithms.CPU;
import com.schedulers_algorithms.CPU.CPUState;
import com.schedulers_algorithms.Utils.Process;

public class FirstComeFirstServed implements AlgorithmType {
    private CPU cpu;

    private Vector<Process> readyQueue;
    private Vector<Process> queue1;
    private Vector<Process> queue2;
    public FirstComeFirstServed() {
        cpu = new CPU();
        queue1= new Vector<Process>();
        queue2= new Vector<Process>();
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
    public void executeProcess() {
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
    
    @Override
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
    public void checkFutureArrivalProcessesInReadyQueue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkFutureArrivalProcessesInReadyQueue'");
    }
    

    // @Override
    // public double getAverageWaitingTime() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getAverageWaitingTime'");
    // }

    // @Override
    // public double getAverageTurnaroundTime() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getAverageTurnaroundTime'");
    // }
}
