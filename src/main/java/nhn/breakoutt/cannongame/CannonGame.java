package nhn.breakoutt.cannongame;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CannonGame extends Application {
    private CannonGameWorld world;
    private Stage primaryStage;
    private Timeline gameLoop;
    private Canvas canvas;
    private GraphicsContext gc;

    private Label gameStartLabel;
    private Label levelSelectLabel;
    private Label settingLabel;
    private Label exitLabel;

    @Override
    public void start(Stage stage){
        showMainMenu();
    }

    public void showMainMenu(){
        gameStartLabel = new Label("게임 시작");
        levelSelectLabel = new Label("레벨 선택");
        settingLabel = new Label("설정");
        exitLabel = new Label("종료");

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(gameStartLabel, levelSelectLabel, settingLabel, exitLabel);

        Scene scene = new Scene(vBox, 800, 600);
    }
}
