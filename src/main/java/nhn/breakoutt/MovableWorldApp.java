package nhn.breakoutt;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class MovableWorldApp extends Application {
    private MovableWorld world;
    private Canvas canvas;
    private GraphicsContext gc;
    private Label fpsLabel;
    private AnimationTimer gameLoop;

    // FPS 계산용 변수들
    private int frameCount = 0;
    private long lastFpsTime = System.currentTimeMillis();
    private long lastTime = System.nanoTime();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // World 생성 (800×600)
        world = new MovableWorld(800, 600);

        // Canvas 생성
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();

        // FPS 라벨 생성
        fpsLabel = new Label("FPS: 0");
        fpsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // 레이아웃 구성
        VBox root = new VBox();
        root.setStyle("-fx-background-color: black;");
        root.getChildren().addAll(fpsLabel, canvas);

        // Scene 구성
        Scene scene = new Scene(root, 800, 630);
        primaryStage.setTitle("Movable World");
        primaryStage.setScene(scene);
        primaryStage.show();

        // 랜덤한 속도로 움직이는 공 10개 생성
        createMovingBalls();

        // 초기 화면 그리기
        draw();

        // GameLoop 시작
        startGameLoop();
    }

    /**
     * 랜덤한 속도로 움직이는 공들을 생성합니다.
     */
    private void createMovingBalls() {
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            // 랜덤 위치: 가장자리에서 반지름만큼 떨어진 곳
            double radius = 10 + random.nextDouble() * 20; // 10~30 픽셀
            double x = radius + random.nextDouble() * (800 - 2 * radius);
            double y = radius + random.nextDouble() * (600 - 2 * radius);

            // 랜덤 색상: RGB 각각 0~255
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            Color color = Color.rgb(r, g, b);

            // 랜덤 속도: -100~100 pixels/second
            double dx = (random.nextDouble() - 0.5) * 200; // -100~100
            double dy = (random.nextDouble() - 0.5) * 200; // -100~100

            // MovableBall 생성 및 추가
            MovableBall ball = new MovableBall(new Point(x, y), radius, color);
            ball.setVelocity(new Vector2D(dx, dy));
            world.add(ball);
        }
    }

    /**
     * 게임 루프를 시작합니다.
     */
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // 델타 타임 계산 (초 단위)
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                // World 업데이트
                world.move(deltaTime);

                // 화면 그리기
                draw();

                // FPS 계산
                calculateFPS();
            }
        };
        gameLoop.start();
    }

    /**
     * FPS를 계산하고 표시합니다.
     */
    private void calculateFPS() {
        frameCount++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFpsTime >= 1000) { // 1초마다
            int fps = frameCount;
            frameCount = 0;
            lastFpsTime = currentTime;
            Platform.runLater(() -> fpsLabel.setText("FPS: " + fps));
        }
    }

    /**
     * 화면을 그립니다.
     */
    private void draw() {
        // 배경 지우기
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // World의 모든 객체 그리기
        world.draw(gc);
    }

    @Override
    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
}