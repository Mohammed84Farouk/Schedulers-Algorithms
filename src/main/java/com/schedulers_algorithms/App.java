package com.schedulers_algorithms;

import com.schedulers_algorithms.Non_Preemptive_SJF.SJFS;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import com.schedulers_algorithms.Utils.Process;
import com.schedulers_algorithms.Utils.ProcessColor;
import com.schedulers_algorithms.Add_Process_Dialog.AddProcessDialog;
import com.schedulers_algorithms.Dropdown_Button.DropdownButton;
import com.schedulers_algorithms.GanttChart.GanttChart;
import com.schedulers_algorithms.ProcessDetailsTable.ProcessDetailsTable;
import com.schedulers_algorithms.Timer.Timer;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.ScrollPane;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;

import javafx.event.ActionEvent;

import com.schedulers_algorithms.Icons.AddProcessButtonIcon;
import com.schedulers_algorithms.Icons.ButtonIcon;
import com.schedulers_algorithms.Icons.ContinueButtonIcon;
import com.schedulers_algorithms.Icons.PauseButtonIcon;
import com.schedulers_algorithms.Icons.StartButtonIcon;
import com.schedulers_algorithms.Icons.StopButtonIcon;
import com.schedulers_algorithms.Preemptive_Priority.PreemptivePriority;

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

    private static Scene scene;

    AlgorithmType algorithmType;


    Timer timer = new Timer("00:00:00");

    private Button startButton = new Button();
    private ButtonIcon startButtonIcon = new StartButtonIcon();

    private Button stopButton = new Button();
    private ButtonIcon stopButtonIcon = new StopButtonIcon();

    private Button pauseButton = new Button();
    private ButtonIcon pauseButtonIcon = new PauseButtonIcon();

    private Button continueButton = new Button();
    private ButtonIcon continueButtonIcon = new ContinueButtonIcon();

    private Button addProcessButton = new Button();
    private ButtonIcon addProcessButtonIcon = new AddProcessButtonIcon();

    private DropdownButton dropdownButton = new DropdownButton();

    private ProcessDetailsTable processDetailsTable = new ProcessDetailsTable();

    GanttChart ganttChart = new GanttChart();

    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), this::handleTimelimeEvent));

    public static int getCurrentTime(){
        return accumulativeSeconds;
    }
    /*
     * 
     * Timer function.
     */
    private void handleTimelimeEvent(ActionEvent event) {
        if (algorithmType.isCPUBuzy()) {
            Rectangle rectangle = new Rectangle(50, 50);
            rectangle.setFill(algorithmType.getCPUHookedProcess().getColor());
            Label label = new Label("P"+Integer.toString(algorithmType.getCPUHookedProcess().getId()));
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(rectangle, label);
            ganttChart.getChildren().add(stackPane);

            ganttChart.adjustView(); // TODO fix here
        }

        algorithmType.runProcess();

        accumulativeSeconds++;
        String hoursStr = String.format("%02d", (accumulativeSeconds / 3600));
        String minutesStr = String.format("%02d", ((accumulativeSeconds / 60) % 60));
        String secondsStr = String.format("%02d", (accumulativeSeconds % 60));

        timer.setText(hoursStr+':'+minutesStr+':'+secondsStr);
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
        ComboBox<SchedulerAlgorithm> source = (ComboBox<SchedulerAlgorithm>) event.getSource();
        SchedulerAlgorithm selectedValue = source.getSelectionModel().getSelectedItem();
        switch (selectedValue) {
            case NONE:
                timeline.stop();
                accumulativeSeconds = 0;
                timer.reset();
                algorithmType = null;
                currentSchedulerState = SchedulerState.INVALID;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.NONE);
                break;
            case FCFS:
                break;
            case NON_PREEMPTIVE_PRIORITY:
                break;
            case NON_PREEMPTIVE_SJF:
                algorithmType = new SJFS(false);
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.NON_PREEMPTIVE_SJF);
                break;
            case PREEMPTIVE_PRIORITY:
                algorithmType = new PreemptivePriority();
                currentSchedulerState = SchedulerState.INITIALIZATION;
                updateLook();
                processDetailsTable.switchAlgorithm(SchedulerAlgorithm.PREEMPTIVE_PRIORITY);
                break;
            case PREEMPTIVE_SJF:
                break;
            case RR:
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

        ganttChart.place(mainLayout);

        mainLayout.setAlignment(Pos.CENTER);

        scene = new Scene(mainLayout, 640, 480);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
