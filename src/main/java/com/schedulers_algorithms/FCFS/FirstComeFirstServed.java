package com.schedulers_algorithms.FCFS;

import java.util.Vector;

import com.schedulers_algorithms.AlgorithmType;
import com.schedulers_algorithms.App;
import com.schedulers_algorithms.CPU;
import com.schedulers_algorithms.CPU.CPUState;
import com.schedulers_algorithms.Utils.Process;

public class FirstComeFirstServed implements AlgorithmType {
    private CPU cpu;

    private Vector<Process> readyQueue;
    private int currentTime = 0;

    public CPU getCpu() {
        return cpu;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public Vector<Process> getReadyQueue() {
        return readyQueue;
    }

    public Vector<Process> getQueue1() {
        return queue1;
    }

    public Vector<Process> getQueue2() {
        return queue2;
    }

    private Vector<Process> queue1;
    private Vector<Process> queue2;

    public FirstComeFirstServed() {
        cpu = new CPU();
        queue1 = new Vector<Process>();
        queue2 = new Vector<Process>();
        readyQueue = new Vector<Process>();
    }

    @Override
    public void addProcessToReadyQueue(Process process) {
        // currentTime = App.getCurrentTime();
        switch (cpu.getState()) {
            case IDLE:
                if (process.getArrivalTime() <= currentTime) {
                    cpu.hookProcess(process);
                    cpu.switchState(CPUState.BUZY);
                } else {
                    hookProcessOnReadyQueue(process);
                }
                return;
            case BUZY:
                hookProcessOnReadyQueue(process);
                return;
            default:
                return;
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

        cpu.getHookedProcess().runProcess(1);
        cpu.getHookedProcess().setWaitingTime(cpu.getHookedProcess().getWaitingTime() - 1);

        if (cpu.getHookedProcess().isFinished()) {
            cpu.getHookedProcess().setTurnAroundTime(currentTime - cpu.getHookedProcess().getArrivalTime() + 1);
            // totalTurnaroundTime += currentTime - cpu.getHookedProcess().getArrivalTime()
            // + 1;
            // totalWaitingTime += cpu.getHookedProcess().getTurnAroundTime() +
            // cpu.getHookedProcess().getWaitingTime();
            cpu.switchState(CPUState.IDLE);
            cpu.unHookProcess();
            hookProcessOnCPUFromReadyQueue();
        }
    }

    private boolean hookProcessOnCPUFromReadyQueue() {
        currentTime=App.getCurrentTime();
        if (readyQueue.size() == 0)
            return false;

        int arrivalValue = Integer.MAX_VALUE;
        int processIndex = -1;

        for (int i = 0; i < readyQueue.size(); i++) {
            if (readyQueue.elementAt(i).getArrivalTime() <= currentTime
                    && readyQueue.elementAt(i).getArrivalTime() < arrivalValue) {
                arrivalValue = readyQueue.elementAt(i).getArrivalTime();   
                processIndex = i;
            }
        }

        if (arrivalValue != Integer.MAX_VALUE) {
            cpu.hookProcess(readyQueue.elementAt(processIndex));
            cpu.switchState(CPUState.BUZY);
            readyQueue.removeElementAt(processIndex);
            return true;
        }

        return false;
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
        currentTime = App.getCurrentTime(); // Comment this line before running tests
        // System.out.println("before size(): "+readyQueue.size());

        if (cpu.isBuzy())
            return;

        int processIndex = -1;
        for (int i = 0; i < readyQueue.size(); i++) {
            if (readyQueue.elementAt(i).getArrivalTime() == currentTime) {
                processIndex = i;
                break;
            }
        }

        if (processIndex != -1) {
            Process futureProcess = readyQueue.elementAt(processIndex);
            readyQueue.removeElementAt(processIndex);

            switch (cpu.getState()) {
                case IDLE:
                    cpu.hookProcess(futureProcess);
                    cpu.switchState(CPUState.BUZY);
                    break;
                case BUZY:
                    hookProcessOnReadyQueue(futureProcess);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void rearrangeProcesses() {

    }

    @Override
    public boolean isReadyQueueEmpty() {
        return readyQueue.isEmpty();
    }

    // @Override
    // public double getAverageWaitingTime() {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'getAverageWaitingTime'");
    // }

    // @Override
    // public double getAverageTurnaroundTime() {
    // // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method
    // 'getAverageTurnaroundTime'");
    // }
}
