package com.schedulers_algorithms;

import com.schedulers_algorithms.Add_Process_Dialog.AddProcessDialog;
import com.schedulers_algorithms.Dropdown_Button.DropdownButton;
import com.schedulers_algorithms.GanttChart.GanttChart;
import com.schedulers_algorithms.HardwareStatusBar.HardwareStatusBar;
import com.schedulers_algorithms.Icons.*;
import com.schedulers_algorithms.FCFS.FirstComeFirstServed;
import com.schedulers_algorithms.Round_Robin.RoundRobinScheduler;
import com.schedulers_algorithms.Non_Preemptive_SJF.SJFS;
import com.schedulers_algorithms.Preemptive_Priority.PreemptivePriority;
import com.schedulers_algorithms.ProcessDetailsTable.ProcessDetailsTable;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

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

    private final DropdownButton dropdownButton = new DropdownButton();

    private final ProcessDetailsTable processDetailsTable = new ProcessDetailsTable();

    HardwareStatusBar hardwareStatusBar = new HardwareStatusBar();

    GanttChart ganttChart = new GanttChart();

    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), this::handleTimelineEvent));

    public static int getCurrentTime(){
        return accumulativeSeconds;
    }
    /*
     * 
     * Timer function.
     */
    int tempX=50, lastProcess=-1, lastTime=0;
    Color lastColor;
    private void handleTimelineEvent(ActionEvent event) {
        if (algorithmType.isCPUBuzy()) {
            Label label = new Label("P"+lastProcess);
            if(lastProcess==-1) {
                lastProcess = algorithmType.getCPUHookedProcess().getId();
                lastTime = accumulativeSeconds;
                lastColor = algorithmType.getCPUHookedProcess().getColor();
            }
            else if(lastProcess==algorithmType.getCPUHookedProcess().getId()) tempX += 50;
            else {
                createRectangle(label);
                tempX = 50;
                lastProcess = algorithmType.getCPUHookedProcess().getId();
                lastTime=accumulativeSeconds;
                lastColor=algorithmType.getCPUHookedProcess().getColor();
            }
            lastProcess=algorithmType.getCPUHookedProcess().getId();

            ganttChart.adjustView(); // TODO fix here
        }
        else if(lastProcess!=-1){
            Label label = new Label("P"+lastProcess);
            createRectangle(label);
            lastProcess=-1;
            tempX=50;
        }
        else{           // ready queue is empty and we're still counting
            Rectangle rectangle = new Rectangle(50, 50);
            rectangle.setFill(Color.TRANSPARENT);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(rectangle);
            ganttChart.getChildren().add(stackPane);
        }

        algorithmType.executeProcess();

        accumulativeSeconds++;
        String hoursStr = String.format("%02d", (accumulativeSeconds / 3600));
        String minutesStr = String.format("%02d", ((accumulativeSeconds / 60) % 60));
        String secondsStr = String.format("%02d", (accumulativeSeconds % 60));

        timer.setText(hoursStr+':'+minutesStr+':'+secondsStr);
    }

    private void createRectangle(Label label) {
        Rectangle rectangle = new Rectangle(tempX, 50);
        rectangle.setFill(lastColor);
        HBox hbox = new HBox();
        hbox.setSpacing(0);                                // spacing between each box carrying the lines and label2
        Line line = new Line(0, 0, 0, 30);
        line.setStrokeWidth(1);                            // thickness
        hbox.getChildren().add(line);
        Label label2 = new Label(Integer.toString(lastTime));
        label2.setTranslateY(35);
        hbox.getChildren().add(label2);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rectangle, label, hbox);
        ganttChart.getChildren().add(stackPane);
    }

    private void updateLook() {
        switch (currentSchedulerState) {
            case INITIALIZATION:
                startButton.setDisable(false);
                stopButton.setDisable(true);
                pauseButton.setDisable(true);
                continueButton.setDisable(true);
                addProcessButton.setDisable(false);
                break;
            case PAUSED:
                startButton.setDisable(true);
                stopButton.setDisable(false);
                pauseButton.setDisable(true);
                continueButton.setDisable(false);
                addProcessButton.setDisable(false);
                break;
            case RUNNING:
                startButton.setDisable(true);
                stopButton.setDisable(false);
                pauseButton.setDisable(false);
                continueButton.setDisable(true);
                addProcessButton.setDisable(false);
                break;
            case INVALID:
                startButton.setDisable(true);
                stopButton.setDisable(true);
                pauseButton.setDisable(true);
                continueButton.setDisable(true);
                addProcessButton.setDisable(true);
                break;
            default:
                break;
        }
    }

    private void handleStartButtonPress(MouseEvent event) {
        currentSchedulerState = SchedulerState.RUNNING;
        updateLook();

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // TODO
    }

    private void handleStopButtonPress(MouseEvent event) {
        currentSchedulerState = SchedulerState.INITIALIZATION;
        updateLook();

        timeline.stop();

        // TODO
    }

    private void handlePauseButtonPress(MouseEvent event) {
        currentSchedulerState = SchedulerState.PAUSED;
        updateLook();

        timeline.pause();

        // TODO
    }

    private void handleContinueButtonPress(MouseEvent event) {
        currentSchedulerState = SchedulerState.RUNNING;
        updateLook();

        timeline.play();

        // TODO
    }

    private void handleAddProcessButtonPress(MouseEvent event) {
        if (currentSchedulerState == SchedulerState.RUNNING) timeline.pause();

        StringBuilder processPriority = new StringBuilder();
        StringBuilder processBurst = new StringBuilder();
        ProcessColor processColor = new ProcessColor(Color.RED);
        AddProcessDialog addProcessDialog = new AddProcessDialog(processPriority, processBurst, processColor);
        addProcessDialog.showDialog();

        Process process = new Process(
            processesIdTracker++, 
            accumulativeSeconds,
            Integer.parseInt(processBurst.toString()),
            Integer.parseInt(processPriority.toString()),
            processColor.getColor());

        processDetailsTable.addProcess(SchedulerAlgorithm.PREEMPTIVE_PRIORITY, process);

        algorithmType.addProcessToReadyQueue(process);

        if (currentSchedulerState == SchedulerState.RUNNING) timeline.play();
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
                algorithmType = null;
                currentSchedulerState = SchedulerState.INVALID;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.NONE);
                break;
            case FCFS:
                algorithmType = new FirstComeFirstServed();
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.FCFS);
                break;
            case NON_PREEMPTIVE_PRIORITY:
                algorithmType = new PreemptivePriority(false);
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.NON_PREEMPTIVE_PRIORITY);
                break;
            case NON_PREEMPTIVE_SJF:
                algorithmType = new SJFS(false);
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.NON_PREEMPTIVE_SJF);
                break;
            case PREEMPTIVE_PRIORITY:
                algorithmType = new PreemptivePriority(true);
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.PREEMPTIVE_PRIORITY);
                break;
            case PREEMPTIVE_SJF:
                algorithmType = new SJFS(true);
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.NON_PREEMPTIVE_SJF);
                break;
            case RR:
                algorithmType = new RoundRobinScheduler(2);
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.RR);
                break;
            default:
                break;
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

        schedulersControllers.setSpacing(10);

        mainLayout.getChildren().add(schedulersControllers);

        dropdownButton.setOnAction(this::dropdownOnAction);
        dropdownButton.place(mainLayout);

        processDetailsTable.place(mainLayout);

        hardwareStatusBar.place(mainLayout);

        ganttChart.place(mainLayout);

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
