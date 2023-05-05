package com.schedulers_algorithms;

import com.schedulers_algorithms.Add_Process_Dialog.AddProcessDialog;
import com.schedulers_algorithms.Dropdown_Button.DropdownButton;
import com.schedulers_algorithms.GanttChart.GanttChart;
import com.schedulers_algorithms.Icons.*;
import com.schedulers_algorithms.FCFS.FirstComeFirstServed;
import com.schedulers_algorithms.Round_Robin.RoundRobinScheduler;
import com.schedulers_algorithms.StatusBar.StatusBar;
import com.schedulers_algorithms.Non_Preemptive_SJF.SJFS;
import com.schedulers_algorithms.Preemptive_Priority.PreemptivePriority;
import com.schedulers_algorithms.ProcessDetailsTable.ProcessDetailsTable;
import com.schedulers_algorithms.RRQuantumSpinBox.RRQuantumSpinBox;
import com.schedulers_algorithms.Timer.Timer;
import com.schedulers_algorithms.Utils.Process;
import com.schedulers_algorithms.Utils.ProcessColor;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JavaFX App
 */
public class App extends Application {

    final String TITLE = "CPU Processes Scheduler";

    public enum SchedulerAlgorithm {
        NONE,
        FCFS,
        NON_PREEMPTIVE_PRIORITY,
        PREEMPTIVE_PRIORITY,
        NON_PREEMPTIVE_SJF,
        PREEMPTIVE_SJF,
        RR
    }

    public enum SchedulerState {
        INITIALIZATION,
        RUNNING,
        PAUSED,
        INVALID
    }

    private SchedulerAlgorithm currentSchedulerAlgorithm = SchedulerAlgorithm.NONE;
    private SchedulerState currentSchedulerState = SchedulerState.INVALID;

    /*
     * 
     * This variable keeps adding 1 each second.
     * 
     */
    private static int accumulativeSeconds = 0;

    /*
     * 
     * This variable gives id to every added process.
     */
    private int processesIdTracker = 0;

    AlgorithmType algorithmType;

    Timer timer = new Timer("00:00:00");

    private final Button startButton = new Button();
    private final ButtonIcon startButtonIcon = new StartButtonIcon();

    private final Button stopButton = new Button();
    private final ButtonIcon stopButtonIcon = new StopButtonIcon();

    private final Button pauseButton = new Button();
    private final ButtonIcon pauseButtonIcon = new PauseButtonIcon();

    private final Button continueButton = new Button();
    private final ButtonIcon continueButtonIcon = new ContinueButtonIcon();

    private final Button addProcessButton = new Button();
    private final ButtonIcon addProcessButtonIcon = new AddProcessButtonIcon();

    private final RRQuantumSpinBox rrQuantumSpinBox = new RRQuantumSpinBox();

    private final DropdownButton dropdownButton = new DropdownButton();

    private final ProcessDetailsTable processDetailsTable = new ProcessDetailsTable();

    StatusBar statusBar = new StatusBar();

    GanttChart ganttChart = new GanttChart();

    private final Button generateAverageWaitingTimeButton = new Button("Generate Average Waiting Time");

    private final Button generateAverageTurnaroundTimeButton = new Button("Generate Average Turnaround Time");

    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), this::handleTimelineEvent));

    public static int getCurrentTime() {
        return accumulativeSeconds;
    }

    enum ViewingPhase {
        DRAWING_PROCESS,
        DRAWING_EMPTY,
        CALCULATING
    };

    private ViewingPhase currentViewingPhase = ViewingPhase.DRAWING_EMPTY;

    int tempX = 50, lastProcess = -1, lastTime = 0, tempRR = 0;
    Color lastColor;

    private void handleTimelineEvent(ActionEvent event) {

        if (algorithmType instanceof PreemptivePriority || algorithmType instanceof FirstComeFirstServed) {
            algorithmType.checkFutureArrivalProcessesInReadyQueue();
        }

        if (algorithmType.isCPUBuzy() && algorithmType.getCPUHookedProcess().getArrivalTime() <= accumulativeSeconds) {
            if (tempRR > 0) {
                tempX += 50;
                tempRR--;
            } else if (lastProcess == -1) {
                if (algorithmType instanceof RoundRobinScheduler)
                    tempRR = Math.min(rrQuantumSpinBox.getValue(), algorithmType.getCPUHookedProcess().getBurstTime())
                            - 1;
                lastProcess = algorithmType.getCPUHookedProcess().getId();
                lastTime = accumulativeSeconds + (algorithmType instanceof SJFS ? -1 : 0);
                lastColor = algorithmType.getCPUHookedProcess().getColor();
            } else if (lastProcess == algorithmType.getCPUHookedProcess().getId()) {
                if (algorithmType instanceof RoundRobinScheduler)
                    tempRR = Math.min(rrQuantumSpinBox.getValue(), algorithmType.getCPUHookedProcess().getBurstTime()) - 1;
                tempX += 50;
            } else {
                Label label = new Label("P" + lastProcess);
                createRectangle(label);
                tempX = 50;
                lastProcess = algorithmType.getCPUHookedProcess().getId();
                if (algorithmType instanceof RoundRobinScheduler)
                    tempRR = Math.min(rrQuantumSpinBox.getValue(), algorithmType.getCPUHookedProcess().getBurstTime()) - 1;
                lastTime = accumulativeSeconds + (algorithmType instanceof SJFS ? -1 : 0);
                lastColor = algorithmType.getCPUHookedProcess().getColor();
            }
            lastProcess = algorithmType.getCPUHookedProcess().getId();

            ganttChart.adjustView(); // TODO fix here
        } else if (lastProcess != -1) {
            Label label = new Label("P" + lastProcess);
            createRectangle(label);
            lastProcess = -1;
            tempX = 50;
        } else { // ready queue is empty and we're still counting
            if (!(algorithmType instanceof SJFS) || accumulativeSeconds >= 1) {
                Rectangle rectangle = new Rectangle(50, 50);
                rectangle.setFill(Color.TRANSPARENT);
                Circle circle = new Circle(3);
                circle.setFill(Color.BLACK);
                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(rectangle, circle);
                ganttChart.getChildren().add(stackPane);
            }
        }

        if (tempRR == 0)
            algorithmType.executeProcess();
        
        System.out.println("currentTime from app: " + accumulativeSeconds);

        accumulativeSeconds++;
        String hoursStr = String.format("%02d", (accumulativeSeconds / 3600));
        String minutesStr = String.format("%02d", ((accumulativeSeconds / 60) % 60));
        String secondsStr = String.format("%02d", (accumulativeSeconds % 60));

        timer.setText(hoursStr + ':' + minutesStr + ':' + secondsStr);
    }

    private void createRectangle(Label label) {
        Rectangle rectangle = new Rectangle(tempX, 50);
        rectangle.setFill(lastColor);
        HBox hbox = new HBox();
        hbox.setSpacing(0); // spacing between each box carrying the lines and label2
        Line line = new Line(0, 0, 0, 30);
        line.setStrokeWidth(1); // thickness
        hbox.getChildren().add(line);
        Label label2 = new Label(Integer.toString(lastTime));
        label2.setTranslateY(35);
        hbox.getChildren().add(label2);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rectangle, label, hbox);
        ganttChart.getChildren().add(stackPane);

        System.out.println("tempX: " + tempX);
    }

    private void updateLook() {
        switch (currentSchedulerState) {
            case INITIALIZATION:
                startButton.setDisable(false);
                stopButton.setDisable(true);
                pauseButton.setDisable(true);
                continueButton.setDisable(true);
                addProcessButton.setDisable(false);
                rrQuantumSpinBox.setDisable(currentSchedulerAlgorithm != SchedulerAlgorithm.RR);
                generateAverageWaitingTimeButton.setDisable(true);
                generateAverageTurnaroundTimeButton.setDisable(true);
                break;
            case PAUSED:
                startButton.setDisable(true);
                stopButton.setDisable(false);
                pauseButton.setDisable(true);
                continueButton.setDisable(false);
                addProcessButton.setDisable(false);
                rrQuantumSpinBox.setDisable(currentSchedulerAlgorithm != SchedulerAlgorithm.RR);
                generateAverageWaitingTimeButton.setDisable(false);
                generateAverageTurnaroundTimeButton.setDisable(false);
                break;
            case RUNNING:
                startButton.setDisable(true);
                stopButton.setDisable(false);
                pauseButton.setDisable(false);
                continueButton.setDisable(true);
                addProcessButton.setDisable(false);
                rrQuantumSpinBox.setDisable(true);
                generateAverageWaitingTimeButton.setDisable(true);
                generateAverageTurnaroundTimeButton.setDisable(true);
                break;
            case INVALID:
                startButton.setDisable(true);
                stopButton.setDisable(true);
                pauseButton.setDisable(true);
                continueButton.setDisable(true);
                addProcessButton.setDisable(true);
                rrQuantumSpinBox.setDisable(true);
                generateAverageWaitingTimeButton.setDisable(true);
                generateAverageTurnaroundTimeButton.setDisable(true);
                break;
            default:
                startButton.setDisable(true);
                stopButton.setDisable(true);
                pauseButton.setDisable(true);
                continueButton.setDisable(true);
                addProcessButton.setDisable(true);
                rrQuantumSpinBox.setDisable(true);
                generateAverageWaitingTimeButton.setDisable(true);
                generateAverageTurnaroundTimeButton.setDisable(true);
                break;
        }
    }

    private void handleStartButtonPress(MouseEvent event) {
        currentSchedulerState = SchedulerState.RUNNING;
        updateLook();

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void handleStopButtonPress(MouseEvent event) {
        currentSchedulerState = SchedulerState.INITIALIZATION;
        updateLook();

        timeline.stop();
    }

    private void handlePauseButtonPress(MouseEvent event) {
        currentSchedulerState = SchedulerState.PAUSED;
        updateLook();

        timeline.pause();
    }

    private void handleContinueButtonPress(MouseEvent event) {
        currentSchedulerState = SchedulerState.RUNNING;
        updateLook();

        timeline.play();
    }

    private void handleAddProcessButtonPress(MouseEvent event) {
        if (currentSchedulerState == SchedulerState.RUNNING)
            timeline.pause();

        BooleanWrapper isSaved = new BooleanWrapper(false);

        StringWrapper processPriority = new StringWrapper();
        StringWrapper processBurst = new StringWrapper();
        ProcessColor processColor = new ProcessColor(Color.RED);
        BooleanWrapper isFutureProcess = new BooleanWrapper(false);
        StringWrapper processArrival = new StringWrapper();
        AddProcessDialog addProcessDialog = new AddProcessDialog(currentSchedulerAlgorithm, isSaved, processPriority,
                processBurst, processColor,
                isFutureProcess, processArrival);

        addProcessDialog.showDialog();

        if (!(isSaved.value)) {
            return;
        }

        int priority, burst;

        burst = Integer.parseInt(processBurst.value);

        Process process;

        if (currentSchedulerAlgorithm == SchedulerAlgorithm.PREEMPTIVE_PRIORITY
                || currentSchedulerAlgorithm == SchedulerAlgorithm.NON_PREEMPTIVE_PRIORITY) {

            priority = Integer.parseInt(processPriority.value);

            process = new Process(
                    ++processesIdTracker,
                    (isFutureProcess.value) ? Integer.parseInt(processArrival.value) : accumulativeSeconds,
                    burst,
                    priority,
                    processColor.getColor());
        } else {
            process = new Process(
                    ++processesIdTracker,
                    (isFutureProcess.value) ? Integer.parseInt(processArrival.value) : accumulativeSeconds,
                    burst,
                    processColor.getColor());
        }

        processDetailsTable.addProcess(currentSchedulerAlgorithm, process);

        algorithmType.addProcessToReadyQueue(process);

        // processDetailsTable.addProcess(currentSchedulerAlgorithm, new Process(0, 0, 3, Color.rgb(135, 206, 250)));
        // processDetailsTable.addProcess(currentSchedulerAlgorithm, new Process(1, 4, 5, Color.rgb(135, 206, 250)));
        // algorithmType.addProcessToReadyQueue(new Process(0, 0, 3,Color.rgb(135, 206, 250)));
        // algorithmType.addProcessToReadyQueue(new Process(1, 4, 5,Color.rgb(135, 206, 250)));

        // processDetailsTable.addProcess(currentSchedulerAlgorithm, new Process(0, 5, 5, Color.rgb(135, 206, 250)));
        // processDetailsTable.addProcess(currentSchedulerAlgorithm, new Process(1, 6, 4, Color.rgb(135, 206, 250)));
        // processDetailsTable.addProcess(currentSchedulerAlgorithm, new Process(2, 0, 3 , Color.rgb(135, 206, 250)));
        // processDetailsTable.addProcess(currentSchedulerAlgorithm, new Process(3, 6, 2,Color.rgb(135, 206, 250)));
        // processDetailsTable.addProcess(currentSchedulerAlgorithm, new Process(4, 5, 4,Color.rgb(135, 206, 250)));

        // algorithmType.addProcessToReadyQueue(new Process(0, 4, 5,Color.rgb(135, 206, 250)));
        // algorithmType.addProcessToReadyQueue(new Process(1, 6, 4,Color.rgb(135, 206, 250)));
        // algorithmType.addProcessToReadyQueue(new Process(2, 0, 3,Color.rgb(135, 206, 250)));
        // algorithmType.addProcessToReadyQueue(new Process(3, 6, 2,Color.rgb(135, 206, 250)));
        // algorithmType.addProcessToReadyQueue(new Process(4, 5, 4,Color.rgb(135, 206, 250)));

        if (currentSchedulerState == SchedulerState.RUNNING)
            timeline.play();
    }

    private void dropdownOnAction(ActionEvent event) {
        ganttChart.Clear();
        timeline.stop();
        accumulativeSeconds = 0;
        timer.reset();
        ComboBox<SchedulerAlgorithm> source = (ComboBox<SchedulerAlgorithm>) event.getSource();
        SchedulerAlgorithm selectedValue = source.getSelectionModel().getSelectedItem();
        switch (selectedValue) {
            case NONE:
                processesIdTracker = 0;
                algorithmType = null;
                currentSchedulerAlgorithm = SchedulerAlgorithm.NONE;
                currentSchedulerState = SchedulerState.INVALID;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.NONE);
                break;
            case FCFS:
                processesIdTracker = 0;
                algorithmType = new FirstComeFirstServed();
                currentSchedulerAlgorithm = SchedulerAlgorithm.FCFS;
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.FCFS);
                break;
            case NON_PREEMPTIVE_PRIORITY:
                processesIdTracker = 0;
                algorithmType = new PreemptivePriority(false);
                currentSchedulerAlgorithm = SchedulerAlgorithm.NON_PREEMPTIVE_PRIORITY;
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.NON_PREEMPTIVE_PRIORITY);
                break;
            case NON_PREEMPTIVE_SJF:
                processesIdTracker = 0;
                algorithmType = new SJFS(false);
                currentSchedulerAlgorithm = SchedulerAlgorithm.NON_PREEMPTIVE_SJF;
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.NON_PREEMPTIVE_SJF);
                break;
            case PREEMPTIVE_PRIORITY:
                processesIdTracker = 0;
                algorithmType = new PreemptivePriority(true);
                currentSchedulerAlgorithm = SchedulerAlgorithm.PREEMPTIVE_PRIORITY;
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.PREEMPTIVE_PRIORITY);
                break;
            case PREEMPTIVE_SJF:
                processesIdTracker = 0;
                algorithmType = new SJFS(true);
                currentSchedulerAlgorithm = SchedulerAlgorithm.PREEMPTIVE_SJF;
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.NON_PREEMPTIVE_SJF);
                break;
            case RR:
                processesIdTracker = 0;
                algorithmType = new RoundRobinScheduler(2);
                currentSchedulerAlgorithm = SchedulerAlgorithm.RR;
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.RR);
                break;
            default:
                break;
        }
    }

    private void handleGenerateAverageWaitingTimeButtonPress(MouseEvent event) {
        double averageWaitingTime = algorithmType.getAverageWaitingTime();
        statusBar.updateAverageWaitingTime(averageWaitingTime);
    }

    private void handleGenerateAverageTurnaroundTimeButtonPress(MouseEvent event) {
        double averageTurnaroundTime = algorithmType.getAverageTurnaroundTime();
        statusBar.updateAverageTurnaroundTime(averageTurnaroundTime);
    }

    public static class StringWrapper {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class BooleanWrapper {
        private boolean value;

        public BooleanWrapper(boolean value) {
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        VBox mainLayout = new VBox();
        mainLayout.setSpacing(10);

        timer.place(mainLayout);

        updateLook();

        startButtonIcon.paint(startButton);
        startButton.setOnMousePressed(this::handleStartButtonPress);

        stopButtonIcon.paint(stopButton);
        stopButton.setOnMousePressed(this::handleStopButtonPress);

        pauseButtonIcon.paint(pauseButton);
        pauseButton.setOnMousePressed(this::handlePauseButtonPress);

        continueButtonIcon.paint(continueButton);
        continueButton.setOnMousePressed(this::handleContinueButtonPress);

        addProcessButtonIcon.paint(addProcessButton);
        addProcessButton.setOnMousePressed(this::handleAddProcessButtonPress);

        HBox schedulersControllers = new HBox();

        schedulersControllers.setAlignment(Pos.CENTER);

        schedulersControllers.getChildren().addAll(startButton, stopButton, pauseButton, continueButton,
                addProcessButton);

        rrQuantumSpinBox.place(schedulersControllers);

        rrQuantumSpinBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            ((RoundRobinScheduler) algorithmType).setQuantum(rrQuantumSpinBox.getValue());
        });

        schedulersControllers.setSpacing(10);

        mainLayout.getChildren().add(schedulersControllers);

        dropdownButton.setOnAction(this::dropdownOnAction);
        dropdownButton.place(mainLayout);

        processDetailsTable.place(mainLayout);

        statusBar.place(mainLayout);

        ganttChart.place(mainLayout);

        generateAverageWaitingTimeButton.setOnMousePressed(this::handleGenerateAverageWaitingTimeButtonPress);
        generateAverageTurnaroundTimeButton.setOnMousePressed(this::handleGenerateAverageTurnaroundTimeButtonPress);

        HBox schedulersControllers2 = new HBox();

        schedulersControllers2.setSpacing(20);

        VBox.setMargin(schedulersControllers2, new javafx.geometry.Insets(0, 50, 20, 50));

        schedulersControllers2.setAlignment(Pos.CENTER);

        schedulersControllers2.getChildren().addAll(generateAverageWaitingTimeButton,
                generateAverageTurnaroundTimeButton);

        mainLayout.getChildren().add(schedulersControllers2);

        mainLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainLayout, 640, 480);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
