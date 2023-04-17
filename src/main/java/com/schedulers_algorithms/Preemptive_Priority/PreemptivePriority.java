package com.schedulers_algorithms.Preemptive_Priority;

import java.util.Vector;
import com.schedulers_algorithms.Utils.Process;

import javafx.scene.paint.Color;

/*
 * 
 * Range [0 - 7], with 0 as highest priority.
 * 
 */
public class PreemptivePriority {
    private Vector<Process> readyQueue;
    private Process currentProcess;

    public PreemptivePriority() {
        readyQueue = new Vector<Process>();
        currentProcess = null;
    }

    public void addProcessToReadyQueue(Process process) {
        /*
         * 
         * If the CPU is IDLE
         * 
         */
        if (currentProcess == null)
            currentProcess = process;

        /*
         * 
         * If the new process has higher priority than current running process.
         * 
         */
        if (process.getPriority() < currentProcess.getPriority()) {
            readyQueue.add(currentProcess);
            currentProcess = process;
        }
        /*
         * 
         * Here we will apply FCFS for processes with same priorities.
         * For processes with lower priorities, just add them to the ready queue.
         * 
         */
        else {
            readyQueue.add(process);
        }
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public void runProcess() {
        if (currentProcess == null) return;
        currentProcess.setBurstTime(currentProcess.getBurstTime()-1);
        if (currentProcess.getBurstTime() <= 1) {
            if (!getProcessFromReadyQueue()) {
                currentProcess = null;
            }
        }
    }

    private boolean getProcessFromReadyQueue() {
        int highestPriorityProcessIndex = Integer.MAX_VALUE;
        for (int i = 0; i < readyQueue.size(); i++) {
            if (readyQueue.get(i).getPriority() < highestPriorityProcessIndex)
                highestPriorityProcessIndex = i;
        }

        if (highestPriorityProcessIndex != Integer.MAX_VALUE) {
            currentProcess = readyQueue.get(highestPriorityProcessIndex);
            readyQueue.removeElementAt(highestPriorityProcessIndex);
            return true;
        } else
            return false;
    }
}
