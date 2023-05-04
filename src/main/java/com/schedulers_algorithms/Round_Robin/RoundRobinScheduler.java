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
    private int timeQuantum;
    private CPU cpu;
    int time = 0;
    private double totalturnaroundtime, totalwaitingtime;
    private int numofprocess;

    public RoundRobinScheduler(int timeQuantum) {
        this.queue1 = new LinkedList<>();
        this.timeQuantum = timeQuantum;
        time = 0; 
    }

    @Override
    public void addProcessToReadyQueue(Process process) {
        queue1.add(process);
        numofprocess++;
    }
    public void setQuantum(int quantum){
        this.timeQuantum = quantum;
        System.out.println(this.timeQuantum);
    }
    @Override
    public void executeProcess() {
        if (!queue1.isEmpty()) {
            Process currentProcess;
            currentProcess = queue1.poll();
            System.out.println( " burst:" + currentProcess.getBurstTime()+ " currentTime:" + App.getCurrentTime()+ " arrival:" + currentProcess.getArrivalTime());
            if(currentProcess.getArrivalTime()>App.getCurrentTime()) return;
            int runTime = Math.min(timeQuantum, currentProcess.getBurstTime());
            time += runTime;
            currentProcess.runProcess(runTime);
            System.out.println("Arrival:" + currentProcess.getArrivalTime() + " App Current:"+App.getCurrentTime() + " Process ID:" + currentProcess.getId() + " burst:" + currentProcess.getBurstTime());
            if (currentProcess.isFinished()) {
                currentProcess.setTurnAroundTime(time - currentProcess.getArrivalTime());
                totalturnaroundtime += currentProcess.getTurnAroundTime();
                currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getBurstTime());
                totalwaitingtime += currentProcess.getWaitingTime();
                System.out.println("Process " + currentProcess.getId() + " finished at time " + App.getCurrentTime() +  " burst:" + currentProcess.getBurstTime());
            } else {
                queue1.add(currentProcess);
                System.out.println("Process " + currentProcess.getId() + " is executing at time " + App.getCurrentTime() + " burst:" + currentProcess.getBurstTime());
            }
        }
    }
    @Override
    public double getAverageWaitingTime() {
        return (double) totalwaitingtime / numofprocess;
    }
    @Override 
    public double getAverageTurnaroundTime() {
        return (double) totalturnaroundtime / numofprocess;
    }
    @Override
    public Process getCPUHookedProcess() {
        Process currentProcess = null;
        if (!queue1.isEmpty()) {
            currentProcess = queue1.peek();
        }
        assert currentProcess != null;
        System.out.println("getCPUHookedProcess() "+currentProcess);
        return currentProcess;
    }

    @Override
    public boolean isCPUBuzy() {
        if (!queue1.isEmpty()) {
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
