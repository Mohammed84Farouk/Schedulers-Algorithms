package com.schedulers_algorithms.Non_Preemptive_SJF;
import java.util.Comparator;
import com.schedulers_algorithms.Utils.Process;

public class SJFSProcessComparatorB implements Comparator<Process>{
    @Override
    public int compare(Process p1, Process p2) {
        // Compare by arrival time
        int compareByBurstTime = Integer.compare(p1.getBurstTime(), p2.getBurstTime());
        if (compareByBurstTime != 0) {
            return compareByBurstTime;
        }

        // If arrival times are the same, compare by burst time
        return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
    }
}
