package com.schedulers_algorithms.Non_Preemptive_SJF;
import java.util.Comparator;
import com.schedulers_algorithms.Utils.Process;

public class SJFSProcessComparatorA implements Comparator<Process>{
    @Override
    public int compare(Process p1, Process p2) {
        // Compare by arrival time
        int compareByArrivalTime = Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
        if (compareByArrivalTime != 0) {
            return compareByArrivalTime;
        }

        // If arrival times are the same, compare by burst time
        return Integer.compare(p1.getBurstTime(), p2.getBurstTime());
    }
}
