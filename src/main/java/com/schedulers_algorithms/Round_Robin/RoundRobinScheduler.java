package com.schedulers_algorithms.Round_Robin;
import java.util.LinkedList;
import java.util.Queue;
import com.schedulers_algorithms.CPU;
import com.schedulers_algorithms.CPU.CPUState;
import com.schedulers_algorithms.AlgorithmType;
import com.schedulers_algorithms.App;
import com.schedulers_algorithms.Utils.Process;
import javafx.scene.paint.Color;

public class RoundRobinScheduler implements AlgorithmType{

    private Queue<Process> queue1;
    private Queue<Process> queue2;
    private int timeQuantum;
    private CPU cpu;
    int time = 0;

    public RoundRobinScheduler(int timeQuantum) {
        this.queue1 = new LinkedList<>();
        this.queue2 = new LinkedList<>();
        this.timeQuantum = timeQuantum;
        time = 0; 
    }

    @Override
    public void addProcessToReadyQueue(Process process) {
        queue1.add(process);
    }
    public void setQuantum(int quantum){
        this.timeQuantum = quantum;
    }
    @Override
    public void executeProcess() {
        if (!queue1.isEmpty() || !queue2.isEmpty()) {
            Process currentProcess;
            if (!queue1.isEmpty() && (queue1.peek().getArrivalTime() <= App.getCurrentTime())) {
                currentProcess = queue1.poll();
            } else if(!queue2.isEmpty() && (queue2.peek().getArrivalTime() <= App.getCurrentTime())){
                currentProcess = queue2.poll();
            }
            else{
                if (!queue1.isEmpty() && !(queue1.peek().getArrivalTime() <= App.getCurrentTime())) {
                    queue2.add(queue1.poll());
                } else if(!queue2.isEmpty() && !(queue2.peek().getArrivalTime() <= App.getCurrentTime())){
                    queue1.add(queue2.poll());
                }
                return;
            }
            int runTime = Math.min(timeQuantum, currentProcess.getBurstTime());
            time += runTime;
            currentProcess.runProcess(runTime);
            System.out.println("Arrivial:" + currentProcess.getArrivalTime() + " App Current:"+App.getCurrentTime());
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
            currentProcess = queue1.peek();
        } else {
            currentProcess = queue2.peek();
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
        RoundRobinScheduler scheduler = new RoundRobinScheduler(2);
        Process p1 = new Process(1, 0, 5, 1, Color.RED);
        Process p2 = new Process(2, 1, 3, 2, Color.GREEN);
        Process p3 = new Process(3, 2, 8, 3, Color.BLUE);
        scheduler.addProcessToReadyQueue(p1);
        scheduler.addProcessToReadyQueue(p2);
        scheduler.addProcessToReadyQueue(p3);
        scheduler.executeProcess();
    }
}
