package nhn.breakoutt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Canvas 설정
        Canvas canvas = new Canvas(800, 600); // 1. 도화지를 생성한다.
        GraphicsContext gc = canvas.getGraphicsContext2D(); // 2. 그림 도구를 생성한다.

        // 그림 그리기
        gc.setFill(Color.RED); // 1. 색을 정한다.
        gc.fillOval(350, 250, 100, 100); // 2. Oval(타원)을 그린다.

        // 레이아웃 설정
        StackPane root = new StackPane();

        // 레이아웃(StackPane)에 도화지(Canvas) 추가
        root.getChildren().add(canvas);

        // 화면(Scene) 생성 - 화면에 레이아웃과 도화지를 포함시킨다.
        Scene scene = new Scene(root, 800, 600);

        // javafx 프로그램이 실행될 윈도우 창(Stage) 설정
        stage.setTitle("My First JavaFX Game"); // 윈도우 창 제목
        stage.setScene(scene); // 윈도우 창의 화면을 설정
        stage.show(); // 윈도우 창 표시하기
    }
}
