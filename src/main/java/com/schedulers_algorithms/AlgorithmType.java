package com.schedulers_algorithms;
import com.schedulers_algorithms.Utils.Process;

public interface AlgorithmType {
    public void addProcessToReadyQueue(Process process);
    public Process getCPUHookedProcess();
    public boolean isCPUBuzy();
    public boolean isReadyQueueEmpty();
    public void executeProcess();
    public double getAverageWaitingTime();
    public double getAverageTurnaroundTime();
    public void checkFutureArrivalProcessesInReadyQueue();
}
