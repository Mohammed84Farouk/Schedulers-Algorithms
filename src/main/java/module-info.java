module com.schedulers_algorithms {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.schedulers_algorithms to javafx.fxml;
    exports com.schedulers_algorithms;
}
