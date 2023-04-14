package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

import java.io.IOException;

import com.example.Icons.AddProcessButtonIcon;
import com.example.Icons.ButtonIcon;
import com.example.Icons.ContinueButtonIcon;
import com.example.Icons.PauseButtonIcon;
import com.example.Icons.StartButtonIcon;
import com.example.Icons.StopButtonIcon;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

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

    private void handleStartButtonPress(MouseEvent event) {
        startButton.setDisable(true);
        stopButton.setDisable(false);
        pauseButton.setDisable(false);

        // TODO
    }

    private void handleStopButtonPress(MouseEvent event) {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        pauseButton.setDisable(true);
        continueButton.setDisable(true);

        // TODO
    }

    private void handlePauseButtonPress(MouseEvent event) {
        pauseButton.setDisable(true);
        continueButton.setDisable(false);

        // TODO
    }

    private void handleContinueButtonPress(MouseEvent event) {
        pauseButton.setDisable(false);
        continueButton.setDisable(true);

        // TODO
    }

    private void handleAddProcessButtonPress(MouseEvent event) {

        // TODO
    }

    @Override
    public void start(Stage stage) throws IOException {
        VBox mainLayout = new VBox();

        startButtonIcon.paint(startButton);
        startButton.setOnMousePressed(this::handleStartButtonPress);

        stopButton.setDisable(true);
        stopButtonIcon.paint(stopButton);
        stopButton.setOnMousePressed(this::handleStopButtonPress);

        pauseButton.setDisable(true);
        pauseButtonIcon.paint(pauseButton);
        pauseButton.setOnMousePressed(this::handlePauseButtonPress);

        continueButton.setDisable(true);
        continueButtonIcon.paint(continueButton);
        continueButton.setOnMousePressed(this::handleContinueButtonPress);

        addProcessButton.setDisable(true);
        addProcessButtonIcon.paint(addProcessButton);
        addProcessButton.setOnMousePressed(this::handleAddProcessButtonPress);

        HBox.setMargin(startButton, new javafx.geometry.Insets(10, 5, 10, 10));
        HBox.setMargin(stopButton, new javafx.geometry.Insets(10, 10, 10, 5));
        HBox.setMargin(pauseButton, new javafx.geometry.Insets(10, 5, 10, 5));
        HBox.setMargin(continueButton, new javafx.geometry.Insets(10, 10, 10, 5));
        
        HBox schedulersControllers = new HBox();

        schedulersControllers.setAlignment(Pos.CENTER);
        
        schedulersControllers.getChildren().addAll(startButton, stopButton,pauseButton, continueButton, addProcessButton);

        mainLayout.getChildren().addAll(schedulersControllers);

        SchedulerProcessesGrapher schedulerProcessesGrapher = new SchedulerProcessesGrapher();
        schedulerProcessesGrapher.paint(mainLayout);

        scene = new Scene(mainLayout, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
