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

public class BoundedWorldApp extends Application {
    private BoundedWorld world;
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
        // BoundedWorld 생성 (800×600)
        world = new BoundedWorld(800, 600);

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
        primaryStage.setTitle("Bounded World - Wall & Ball Collisions");
        primaryStage.setScene(scene);
        primaryStage.show();

        // 마우스 클릭으로 새 공 추가
        canvas.setOnMouseClicked(event -> {
            addBallAtPosition(event.getX(), event.getY());
        });

        // 초기 공들 생성
        createInitialBalls();

        // 초기 화면 그리기
        draw();

        // GameLoop 시작
        startGameLoop();
    }

    private void createInitialBalls() {
        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            // 랜덤 크기: 15~35 픽셀
            double radius = 15 + random.nextDouble() * 20;

            // 랜덤 위치: 가장자리에서 반지름만큼 떨어진 곳
            double x = radius + random.nextDouble() * (800 - 2 * radius);
            double y = radius + random.nextDouble() * (600 - 2 * radius);
            Point center = new Point(x, y);

            // 랜덤 색상: RGB 각각 0~255
            Color color = Color.rgb(
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
            );

            // 랜덤 속도: -150~150 pixels/second
            double velocityX = -150 + random.nextDouble() * 300;
            double velocityY = -150 + random.nextDouble() * 300;
            Vector2D velocity = new Vector2D(velocityX, velocityY);

            // BoundedBall 생성 및 추가 (BoundedWorld가 자동으로 경계 설정)
            BoundedBall ball = new BoundedBall(center, radius, color);
            ball.setVelocity(velocity);
            world.add(ball);
        }
    }

    private void addBallAtPosition(double x, double y) {
        Random random = new Random();

        // 클릭 위치에 새 공 추가
        double radius = 15 + random.nextDouble() * 15; // 15~30 크기
        Color color = Color.rgb(
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256)
        );

        // 경계 확인 후 위치 조정
        x = Math.max(radius, Math.min(800 - radius, x));
        y = Math.max(radius, Math.min(600 - radius, y));

        // 랜덤 속도
        double velocityX = -100 + random.nextDouble() * 200;
        double velocityY = -100 + random.nextDouble() * 200;
        Vector2D velocity = new Vector2D(velocityX, velocityY);

        BoundedBall ball = new BoundedBall(new Point(x, y), radius, color);
        ball.setVelocity(velocity);
        world.add(ball);
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // 델타 타임 계산 (초 단위)
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                // World 업데이트 (이동, 벽 충돌, 공 간 충돌 모두 처리)
                world.update(deltaTime);

                // 화면 그리기
                draw();

                // FPS 계산
                calculateFPS();
            }
        };
        gameLoop.start();
    }

    private void calculateFPS() {
        frameCount++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFpsTime >= 1000) { // 1초마다
            int fps = frameCount;
            frameCount = 0;
            lastFpsTime = currentTime;
            Platform.runLater(() -> fpsLabel.setText("FPS: " + fps + " | Balls: " + world.getBallCount()));
        }
    }

    private void draw() {
        // 화면 지우기
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // 경계선 그리기
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, 800, 600);

        // 모든 공 그리기
        for (Ball ball : world.getBalls()) {
            if (ball instanceof PaintableBall paintableBall) {
                paintableBall.paint(gc);
            }
        }
    }

    @Override
    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
}
