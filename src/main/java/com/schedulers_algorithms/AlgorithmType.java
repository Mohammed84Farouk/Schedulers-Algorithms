package com.schedulers_algorithms;

public interface AlgorithmType {
    public void addProcessToReadyQueue(Process process);
    public Process getCPUHookedProcess();
    public boolean isCPUBuzy();
    public void runProcess();
}
