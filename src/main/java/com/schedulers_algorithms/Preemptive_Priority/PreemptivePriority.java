package com.schedulers_algorithms.Preemptive_Priority;

import java.util.Vector;

import com.schedulers_algorithms.Preemptive_Priority.CPU.CPUState;
import com.schedulers_algorithms.Utils.Process;

/*
 * 
 * Range [0 - 7], with 0 as highest priority.
 * 
 */
public class PreemptivePriority {
    private CPU cpu;

    private Vector<Process> readyQueue;

    public PreemptivePriority() {
        cpu = new CPU();
        readyQueue = new Vector<Process>();
    }

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

    public Process getCPUHookedProcess() {
        return cpu.getHookedProcess();
    }

    public void runProcess() {
        if (cpu.getHookedProcess() == null) {
            if (!hookProcessOnCPUFromReadyQueue())
                return;
        }

        int hookedProcessBurstTime = cpu.getHookedProcess().getBurstTime();
        cpu.getHookedProcess().setBurstTime(hookedProcessBurstTime - 1);

        if ((hookedProcessBurstTime - 1) == 0) {
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
            readyQueue.removeElementAt(highestPriorityProcessIndex);
        }

        return true;
    }
}
