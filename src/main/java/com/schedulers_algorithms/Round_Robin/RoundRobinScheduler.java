package com.schedulers_algorithms.Round_Robin;
import java.util.LinkedList;
import java.util.Queue;
import com.schedulers_algorithms.CPU;
import com.schedulers_algorithms.AlgorithmType;
import com.schedulers_algorithms.App;
import com.schedulers_algorithms.Utils.Process;
import javafx.scene.paint.Color;

public class RoundRobinScheduler implements AlgorithmType{

    private final Queue<Process> queue1;
    private int timeQuantum;
    private CPU cpu;
    int time;
    private double totalTurnAroundTime, totalWaitingTime;
    private int numOfProcesses;

    public RoundRobinScheduler(int timeQuantum) {
        this.queue1 = new LinkedList<>();
        this.timeQuantum = timeQuantum;
        time = 0; 
    }

    @Override
    public void addProcessToReadyQueue(Process process) {
        queue1.add(process);
        numOfProcesses++;
    }
    public void setQuantum(int quantum){
        this.timeQuantum = quantum;
        System.out.println(this.timeQuantum);
    }
    @Override
    public void executeProcess() {
        if (!queue1.isEmpty()) {
            Process currentProcess = null;
            boolean flag=false;
            Queue<Process> queue2 = new LinkedList<>();
            Queue<Process> queue3 = new LinkedList<>();
            while(!queue1.isEmpty()) {
                if (queue1.peek().getArrivalTime() <= App.getCurrentTime()) {
                    if(!flag) {
                        currentProcess = queue1.peek();
                        flag=true;
                    }
                    queue2.add(queue1.poll());
                }
                else queue3.add(queue1.poll());
            }
            while(!queue2.isEmpty()){
                if(queue2.peek()!=currentProcess)  queue1.add(queue2.poll());
                else queue2.poll();
            }
            assert currentProcess != null;
            System.out.println( " burst:" + currentProcess.getBurstTime()+ " currentTime:" + App.getCurrentTime()+ " arrival:" + currentProcess.getArrivalTime());
            if(currentProcess.getArrivalTime()>App.getCurrentTime()){
                while(!queue3.isEmpty()) queue1.add(queue3.poll());
                return;
            }
            int runTime = Math.min(timeQuantum, currentProcess.getBurstTime());
            time += runTime;
            currentProcess.runProcess(runTime);
            System.out.println("Arrival:" + currentProcess.getArrivalTime() + " App Current:"+App.getCurrentTime() + " Process ID:" + currentProcess.getId() + " burst:" + currentProcess.getBurstTime());
            if (currentProcess.isFinished()) {
                currentProcess.setTurnAroundTime(time - currentProcess.getArrivalTime());
                totalTurnAroundTime += currentProcess.getTurnAroundTime();
                currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getBurstTime());
                totalWaitingTime += currentProcess.getWaitingTime();
                System.out.println("Process " + currentProcess.getId() + " finished at time " + App.getCurrentTime() +  " burst:" + currentProcess.getBurstTime());
            } else {
                queue1.add(currentProcess);
                System.out.println("Process " + currentProcess.getId() + " is executing at time " + App.getCurrentTime() + " burst:" + currentProcess.getBurstTime());
            }
            while(!queue3.isEmpty()) queue1.add(queue3.poll());
        }
    }
    @Override
    public double getAverageWaitingTime() {
        return totalWaitingTime / (double) numOfProcesses;
    }
    @Override 
    public double getAverageTurnaroundTime() {
        return totalTurnAroundTime / (double) numOfProcesses;
    }
    @Override
    public Process getCPUHookedProcess() {
        Process currentProcess = null;
        Queue<Process> queue2 = new LinkedList<>();
        boolean flag=false;
        while(!queue1.isEmpty()) {
            if (!flag && queue1.peek().getArrivalTime() <= App.getCurrentTime()) {
                flag = true;
                currentProcess = queue1.peek();
                queue2.add(queue1.poll());
            }
            else queue2.add(queue1.poll());
        }
        assert currentProcess != null;
        System.out.println("getCPUHookedProcess() "+currentProcess);
        while(!queue2.isEmpty()){
            queue1.add(queue2.poll());
        }
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
