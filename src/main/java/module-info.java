module nhn.breakoutt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens nhn.breakoutt to javafx.fxml;
    opens nhn.breakoutt.breakout to javafx.fxml;

    exports nhn.breakoutt;
    exports nhn.breakoutt.breakout;
}