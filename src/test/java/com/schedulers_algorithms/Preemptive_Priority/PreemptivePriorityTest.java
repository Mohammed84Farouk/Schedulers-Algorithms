package com.schedulers_algorithms.Preemptive_Priority;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.schedulers_algorithms.CPU.CPUState;
import com.schedulers_algorithms.Utils.Process;

@TestInstance(Lifecycle.PER_CLASS)
public class PreemptivePriorityTest {

    private PreemptivePriority preemptivePriority;

    @BeforeAll
    public void init() {
        preemptivePriority = new PreemptivePriority(true);
        preemptivePriority.setAgingEnabled(false);
    }

    private void clearCPU() {
        if (preemptivePriority.isCPUBuzy()){ 
            preemptivePriority.getCpu().switchState(CPUState.IDLE);
            preemptivePriority.getCpu().unHookProcess();
        }
    }

    private void clearReadyQueue() {
        preemptivePriority.getReadyQueue().clear();
    }

    @Test
    public void testAddProcessToReadyQueue_emptyQueue_hookOnCPU() {
        Process process = new Process(0, 0, 3, 3);

        preemptivePriority.addProcessToReadyQueue(process);

        Assertions.assertEquals(0, preemptivePriority.getReadyQueue().size());
        Assertions.assertEquals(true, preemptivePriority.getCpu().isBuzy());
    }

    @Test
    public void testAddProcessToReadyQueue_nonEmptyQueue_hookOnCPU() {
        clearCPU();

        Process process = new Process(0, 0, 3, 3);
        preemptivePriority.getReadyQueue().add(process);

        Process process1 = new Process(1, 0, 3, 2);

        preemptivePriority.addProcessToReadyQueue(process1);

        Assertions.assertEquals(1, preemptivePriority.getReadyQueue().size());
        Assertions.assertEquals(true, preemptivePriority.getCpu().isBuzy());
    }

    @Test
    public void testAddProcessToReadyQueue_highPriority() {
        clearCPU();
        clearReadyQueue();

        Process process = new Process(0, 0, 3,3);
        preemptivePriority.addProcessToReadyQueue(process);

        Assertions.assertEquals(0, preemptivePriority.getReadyQueue().size());
        Assertions.assertEquals(true, preemptivePriority.getCpu().isBuzy());

        Process process1 = new Process(1, 0, 3,2);
        preemptivePriority.addProcessToReadyQueue(process1);

        Assertions.assertEquals(1, preemptivePriority.getReadyQueue().size());
        Assertions.assertEquals(true, preemptivePriority.getCpu().isBuzy());
        Assertions.assertEquals(1, preemptivePriority.getCPUHookedProcess().getId());
    }

    @Test
    public void testAddProcessToReadyQueue_highPriority_futureProcess() {
        clearCPU();
        clearReadyQueue();

        preemptivePriority.setCurrentTime(0);

        Process process = new Process(0, 0, 3,3);
        preemptivePriority.addProcessToReadyQueue(process);

        Assertions.assertEquals(0, preemptivePriority.getReadyQueue().size());
        Assertions.assertEquals(true, preemptivePriority.getCpu().isBuzy());

        Process process1 = new Process(1, 5, 3,2);
        preemptivePriority.addProcessToReadyQueue(process1);

        Assertions.assertEquals(1, preemptivePriority.getReadyQueue().size());
        Assertions.assertEquals(true, preemptivePriority.getCpu().isBuzy());
        Assertions.assertEquals(0, preemptivePriority.getCPUHookedProcess().getId());
    }

    @Test
    public void testExecuteProcess_initialProcesses_noFutureProcesses() {
        clearCPU();
        clearReadyQueue();

        preemptivePriority.setCurrentTime(0);

        ArrayList<Process> processes = new ArrayList<Process>();
        processes.add(new Process(0, 0, 3,5));
        processes.add(new Process(1, 0, 5,3));
        processes.add(new Process(2, 0, 2,2));
        processes.add(new Process(3, 0, 7,0));
        processes.add(new Process(4, 0, 1,1));
        
        for (int i = 0 ; i < processes.size() ; i++) {
            preemptivePriority.addProcessToReadyQueue(processes.get(i));
        }
        
        int [] priorities = {0, 1, 2 , 3 , 5};
        int [] ids = {3, 4, 2, 1 ,0};
        int index = 0;

        Assertions.assertEquals(processes.size() - 1, preemptivePriority.getReadyQueue().size());
        Assertions.assertEquals(true, preemptivePriority.getCpu().isBuzy());
        Assertions.assertEquals(ids[index], preemptivePriority.getCPUHookedProcess().getId());
        Assertions.assertEquals(priorities[index], preemptivePriority.getCPUHookedProcess().getPriority()); // 3 is the highest priority process

        boolean canCheck = false;
        index++;

        /*
         * Run for 100 seconds
         * 
         */
        for (int i = 1 ; i < 100 ; i++) {
            if (!preemptivePriority.getCpu().isBuzy()) continue;

            if (preemptivePriority.getCPUHookedProcess().getBurstTime() == 1) canCheck = true;

            preemptivePriority.executeProcess();

            if (!preemptivePriority.getCpu().isBuzy()) continue;

            if (canCheck == true) {
                Assertions.assertEquals(ids[index], preemptivePriority.getCPUHookedProcess().getId());
                Assertions.assertEquals(priorities[index], preemptivePriority.getCPUHookedProcess().getPriority());
                canCheck = false;
                index++;
            }

            int currentTime = preemptivePriority.getCurrentTime() + 1;
            preemptivePriority.setCurrentTime(currentTime);
        }

    }
    
}
