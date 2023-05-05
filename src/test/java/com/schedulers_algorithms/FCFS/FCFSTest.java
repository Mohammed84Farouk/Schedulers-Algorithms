package com.schedulers_algorithms.FCFS;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.schedulers_algorithms.CPU.CPUState;
import com.schedulers_algorithms.Utils.Process;

@TestInstance(Lifecycle.PER_CLASS)
public class FCFSTest {

    private FirstComeFirstServed fcfs;

    @BeforeAll
    public void init() {
        fcfs = new FirstComeFirstServed();
    }

    private void clearCPU() {
        if (fcfs.isCPUBuzy()){ 
            fcfs.getCpu().switchState(CPUState.IDLE);
            fcfs.getCpu().unHookProcess();
        }
    }

    private void clearReadyQueue() {
        fcfs.getReadyQueue().clear();
    }

    @Test
    public void testExecuteProcess_initialProcesses_noFutureProcesses() {
        clearCPU();
        clearReadyQueue();

        fcfs.setCurrentTime(0);

        ArrayList<Process> processes = new ArrayList<Process>();
        processes.add(new Process(0, 0, 3));
        processes.add(new Process(1, 0, 5));
        processes.add(new Process(2, 0, 2));
        processes.add(new Process(3, 0, 7));
        processes.add(new Process(4, 0, 1));
        
        for (int i = 0 ; i < processes.size() ; i++) {
            fcfs.addProcessToReadyQueue(processes.get(i));
        }

        ArrayList<Integer> ids = new ArrayList<Integer>();
        ids.add(0);
        ids.add(1);
        ids.add(2);
        ids.add(3);
        ids.add(4);

        int index = 0;

        Assertions.assertEquals(processes.size() - 1, fcfs.getReadyQueue().size());
        Assertions.assertEquals(true, fcfs.getCpu().isBuzy());
        Assertions.assertEquals(ids.get(index), fcfs.getCPUHookedProcess().getId());

        boolean canCheck = false;
        index++;

        /*
         * Run for 100 seconds
         * 
         */
        for (int i = 0 ; i < 100 ; i++, fcfs.setCurrentTime(i)) {
            if (!fcfs.getCpu().isBuzy()) continue;

            if (fcfs.getCPUHookedProcess().getBurstTime() == 1) canCheck = true;

            fcfs.executeProcess();

            if (!fcfs.getCpu().isBuzy()) {
                canCheck = false;
                continue;
            }

            if (canCheck == true) {
                Assertions.assertEquals(ids.get(index), fcfs.getCPUHookedProcess().getId());
                canCheck = false;
                index++;
            }
        }

    }
}
