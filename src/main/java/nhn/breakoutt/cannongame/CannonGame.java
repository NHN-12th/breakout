package nhn.breakoutt.cannongame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CannonGame extends Application {
    private CannonGameWorld world;
    private Stage primaryStage;
    private Timeline gameLoop;
    private Canvas canvas;
    private GraphicsContext gc;
    private static final double CANVAS_WIDTH = 800;
    private static final double CANVAS_HEIGHT = 600;
    private long lastTime = 0;
    private boolean isPaused = false;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Simple Cannon Game");
        showMainMenu();
    }

    public void showMainMenu() {
        VBox menuLayout = new VBox(20);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: lightblue; -fx-padding: 50;");

        // 게임 제목
        Label titleLabel = new Label("SIMPLE CANNON GAME");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        // 메뉴 버튼들
        Button startButton = new Button("게임 시작");
        Button exitButton = new Button("종료");

        // 버튼 스타일링
        String buttonStyle = "-fx-font-size: 16px; -fx-min-width: 150px; -fx-min-height: 40px;";
        startButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        // 이벤트 핸들러 연결
        startButton.setOnAction(e -> startGame());
        exitButton.setOnAction(e -> Platform.exit());

        menuLayout.getChildren().addAll(titleLabel, startButton, exitButton);

        Scene menuScene = new Scene(menuLayout, CANVAS_WIDTH, CANVAS_HEIGHT);
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public void startGame() {
        // CannonGameWorld 객체 생성
        world = new CannonGameWorld(CANVAS_WIDTH, CANVAS_HEIGHT);

        // 레이아웃 구성
        BorderPane gameLayout = new BorderPane();

        // Canvas 생성 및 중앙 배치
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        gameLayout.setCenter(canvas);

        // 간단한 컨트롤 패널을 하단에 배치
        HBox controlPanel = createSimpleControlPanel();
        gameLayout.setBottom(controlPanel);

        Scene gameScene = new Scene(gameLayout, CANVAS_WIDTH, CANVAS_HEIGHT + 60);

        // 입력 이벤트 핸들러 등록
        setupInputHandlers(gameScene);

        primaryStage.setScene(gameScene);

        // 게임 루프 시작
        startGameLoop();
    }

    private HBox createSimpleControlPanel() {
        HBox controlPanel = new HBox(15);
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setStyle("-fx-background-color: lightgray; -fx-padding: 10;");

        // 파워 조절 슬라이더
        Label powerLabel = new Label("파워:");
        Slider powerSlider = new Slider(100, 1000, 500);
        powerSlider.setShowTickLabels(true);
        powerSlider.setShowTickMarks(true);
        powerSlider.setMajorTickUnit(200);

        // 발사 버튼
        Button fireButton = new Button("발사!");
        fireButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");

        // 메뉴 버튼
        Button menuButton = new Button("메뉴");

        // 이벤트 리스너 연결
        powerSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (world != null) {
                world.getCannon().setPower(newVal.doubleValue());
            }
        });

        fireButton.setOnAction(e -> {
            if (world != null) {
                world.fire();
            }
        });

        menuButton.setOnAction(e -> {
            stopGameLoop();
            showMainMenu();
        });

        controlPanel.getChildren().addAll(powerLabel, powerSlider, fireButton, menuButton);

        return controlPanel;
    }

    private void setupInputHandlers(Scene scene) {
        // 마우스 이벤트 처리
        canvas.setOnMouseMoved(e -> {
            if (world != null) {
                world.getCannon().aimAt(e.getX(), e.getY());
            }
        });

        canvas.setOnMouseClicked(e -> {
            if (world != null) {
                world.fire();
            }
        });

        // 키보드 이벤트 처리
        scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));

        // Canvas에 포커스 설정
        canvas.setFocusTraversable(true);
        canvas.requestFocus();
    }

    private void handleKeyPress(KeyCode code) {
        if (world == null) return;

        switch (code) {
            case UP:
                world.getCannon().adjustAngle(-Math.toRadians(5));
                break;
            case DOWN:
                world.getCannon().adjustAngle(Math.toRadians(5));
                break;
            case LEFT:
                world.getCannon().setPower(world.getCannon().getPower() - 25);
                break;
            case RIGHT:
                world.getCannon().setPower(world.getCannon().getPower() + 25);
                break;
            case SPACE:
                world.fire();
                break;
            case ESCAPE:
                stopGameLoop();
                showMainMenu();
                break;
            case R:
                world.resetGame();
                break;
            case N:
                if (world.isGameWon()) {
                    world.nextLevel();
                }
                break;
        }
    }

    private void startGameLoop() {
        lastTime = System.nanoTime();

        gameLoop = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            if (!isPaused) {
                long currentTime = System.nanoTime();
                double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
                lastTime = currentTime;

                // 게임 업데이트
                world.update(deltaTime);

                // 화면 렌더링
                world.paint(gc);
                if (world.isGameWon() || world.isGameLost()) {
                    // 게임 종료 메시지는 world.paint()에서 표시됨
                }
            }
        }));

        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
