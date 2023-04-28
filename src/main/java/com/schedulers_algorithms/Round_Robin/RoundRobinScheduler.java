import java.util.LinkedList;
import java.util.Queue;

public class RoundRobinScheduler {

    private Queue<Process> queue1;
    private Queue<Process> queue2;
    private int timeQuantum;

    public RoundRobinScheduler(int timeQuantum) {
        this.queue1 = new LinkedList<>();
        this.queue2 = new LinkedList<>();
        this.timeQuantum = timeQuantum;
    }

    public void addProcess(Process process) {
        queue1.add(process);
    }

    public void run() {
        int time = 0;
        while (!queue1.isEmpty() || !queue2.isEmpty()) {
            Process currentProcess;
            if (!queue1.isEmpty()) {
                currentProcess = queue1.poll();
            } else {
                currentProcess = queue2.poll();
            }
            int runTime = Math.min(timeQuantum, currentProcess.getRemainingTime());
            time += runTime;
            currentProcess.run(runTime);
            if (currentProcess.isFinished()) {
                System.out.println("Process " + currentProcess.getId() + " finished at time " + time);
            } else {
                if (currentProcess.getLastQueue() == 1) {
                    currentProcess.setLastQueue(2);
                    queue2.add(currentProcess);
                } else {
                    currentProcess.setLastQueue(1);
                    queue1.add(currentProcess);
                }
            }
        }
    }

    public static void main(String[] args) {
        RoundRobinScheduler scheduler = new RoundRobinScheduler(2);
        scheduler.addProcess(new Process(1, 5));
        scheduler.addProcess(new Process(2, 3));
        scheduler.addProcess(new Process(3, 8));
        scheduler.run();
    }
}

class Process {

    private int id;
    private int remainingTime;
    private int lastQueue;

    public Process(int id, int executionTime) {
        this.id = id;
        this.remainingTime = executionTime;
        this.lastQueue = 1;
    }

    public int getId() {
        return id;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void run(int time) {
        remainingTime -= time;
    }

    public boolean isFinished() {
        return remainingTime <= 0;
    }

    public int getLastQueue() {
        return lastQueue;
    }

    public void setLastQueue(int lastQueue) {
        this.lastQueue = lastQueue;
    }
}