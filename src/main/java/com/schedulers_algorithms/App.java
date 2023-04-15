package com.schedulers_algorithms;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;

import com.schedulers_algorithms.Icons.AddProcessButtonIcon;
import com.schedulers_algorithms.Icons.ButtonIcon;
import com.schedulers_algorithms.Icons.ContinueButtonIcon;
import com.schedulers_algorithms.Icons.PauseButtonIcon;
import com.schedulers_algorithms.Icons.StartButtonIcon;
import com.schedulers_algorithms.Icons.StopButtonIcon;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    TableView<String[]> table = new TableView<>();
    ObservableList<String[]> data = FXCollections.observableArrayList(
            new String[] { "John", "Doe", "30" },
            new String[] { "Jane", "Smith", "25" });

    Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(500), event -> {
                // Perform timer operations here
                data.add(new String[] { "farouk", "saif", "33" });
            }));

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

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

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

        // data.add(new String[] { "John", "Doe", "30" });

        // ObservableList<String[]> data = table.getItems();

        // String[] newRow = new String[] { "farouk", "Jones", "22" };

        // int lastIndex = data.size() - 1;
        // String[] lastRow = data.get(lastIndex);
        // lastRow[0] = newRow[0];
        // lastRow[1] = newRow[1];
        // lastRow[2] = newRow[2];

        // data.add(newRow);
        // table.refresh();

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

        // addProcessButton.setDisable(true);
        addProcessButtonIcon.paint(addProcessButton);
        addProcessButton.setOnMousePressed(this::handleAddProcessButtonPress);

        HBox.setMargin(startButton, new javafx.geometry.Insets(10, 5, 10, 10));
        HBox.setMargin(stopButton, new javafx.geometry.Insets(10, 10, 10, 5));
        HBox.setMargin(pauseButton, new javafx.geometry.Insets(10, 5, 10, 5));
        HBox.setMargin(continueButton, new javafx.geometry.Insets(10, 10, 10, 5));

        HBox schedulersControllers = new HBox();

        schedulersControllers.setAlignment(Pos.CENTER);

        schedulersControllers.getChildren().addAll(startButton, stopButton, pauseButton, continueButton,
                addProcessButton);

        mainLayout.getChildren().addAll(schedulersControllers);

        ////

        TableColumn<String[], String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[0]));

        TableColumn<String[], String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[1]));

        TableColumn<String[], String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[2]));

        table.setItems(data);
        table.getColumns().addAll(firstNameCol, lastNameCol, ageCol);

        mainLayout.getChildren().add(table);

        ////

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
