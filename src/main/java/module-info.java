module nhn.breakoutt {
    requires javafx.controls;
    requires javafx.fxml;


    opens nhn.breakoutt to javafx.fxml;
    exports nhn.breakoutt;
}