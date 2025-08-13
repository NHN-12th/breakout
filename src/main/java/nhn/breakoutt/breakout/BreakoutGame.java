package nhn.breakoutt.breakout;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nhn.breakoutt.*;

public class BreakoutGame extends Application {
    private static final int GAME_WIDTH = 800;
    private static final int GAME_HEIGHT = 600;
    private static final int INITIAL_LIVES = 3;
    private static final double BALL_SPEED = 200;

    private BreakoutWorld world;
    private Canvas canvas;
    private GraphicsContext gc;
    private AnimationTimer gameLoop;

    private GameState gameState;
    private int lives;
    private long lastTime;

    private Label scoreLabel;
    private Label livesLabel;
    private Label levelLabel;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        setupGame();
        setupUI(primaryStage);
        startGame();
    }

    private void setupGame() {
        world = new BreakoutWorld(GAME_WIDTH, GAME_HEIGHT);
        canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        gameState = GameState.MENU;
        lives = INITIAL_LIVES;
        lastTime = System.nanoTime();
    }

    private void setupUI(Stage primaryStage) {
        scoreLabel = new Label("점수: 0");
        livesLabel = new Label("생명: " + lives);
        levelLabel = new Label("레벨: 1");
        statusLabel = new Label("마우스를 움직여 패들을 조작하고, 클릭으로 게임을 시작하세요!");

        HBox infoPanel = new HBox(20);
        infoPanel.getChildren().addAll(scoreLabel, livesLabel, levelLabel);
        infoPanel.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        // 하단 상태 패널
        statusLabel.setStyle("-fx-padding: 10; -fx-background-color: #e0e0e0;");

        // 메인 레이아웃
        BorderPane root = new BorderPane();
        root.setTop(infoPanel);
        root.setCenter(canvas);
        root.setBottom(statusLabel);

        // 이벤트 핸들러 설정
        setupEventHandlers(canvas);

        // Scene 및 Stage 설정
        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT + 60);
        primaryStage.setTitle("벽돌 깨기 게임");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // 캔버스 포커스 설정
        canvas.requestFocus();
    }

    private void setupEventHandlers(Canvas canvas) {
        // 마우스 이동: 패들 제어
        canvas.setOnMouseMoved(this::handleMouseMove);

        // 마우스 클릭: 게임 시작/공 발사
        canvas.setOnMouseClicked(this::handleMouseClick);

        // 키보드 입력
        canvas.setOnKeyPressed(this::handleKeyPress);

        // 포커스 가능하도록 설정
        canvas.setFocusTraversable(true);
    }

    private void handleMouseMove(MouseEvent event) {
        if (gameState == GameState.PLAYING || gameState == GameState.BALL_LOST) {
            world.getPaddle().setTargetX(event.getX());
        }
    }

    private void handleMouseClick(MouseEvent event) {
        switch (gameState) {
            case MENU:
                startNewGame();
                break;

            case BALL_LOST:
                if (lives > 0) {
                    launchBall();
                    gameState = GameState.PLAYING;
                }
                break;

            case PLAYING:
                // 끈끈한 패들에서 공 발사
                if (world.getPaddle().isStickyMode()) {
                    world.getPaddle().releaseBall();
                }
                break;

            case GAME_OVER:
            case GAME_WON:
                restartGame();
                break;
        }
    }

    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
                if (gameState == GameState.PLAYING) {
                    gameState = GameState.PAUSED;
                } else if (gameState == GameState.PAUSED) {
                    gameState = GameState.PLAYING;
                }
                break;

            case R:
                restartGame();
                break;

            case ESCAPE:
                System.exit(0);
                break;
        }
    }

    private void startGame() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                update(deltaTime);
                render();
            }
        };
        gameLoop.start();
    }

    private void update(double deltaTime) {
        if (gameState == GameState.PLAYING) {
            world.update(deltaTime);
            checkGameState();
        }
        updateUI();
    }

    private void checkGameState() {
        // 레벨 완료 확인
        if (world.isLevelComplete()) {
            gameState = GameState.LEVEL_COMPLETE;
            nextLevel();
        }

        // 공을 모두 잃었는지 확인
        if (world.areAllBallsLost()) {
            lives--;
            if (lives > 0) {
                gameState = GameState.BALL_LOST;
            } else {
                gameState = GameState.GAME_OVER;
            }
        }
    }

    private void nextLevel() {
        world.createLevel(world.getLevel() + 1);
        launchBall();
        gameState = GameState.PLAYING;

        // 공 속도 증가
        for (Ball ball : world.getBalls()) {
            double speedMultiplier = 1.0 + (world.getLevel() - 1) * 0.1;
            ball.setDx(ball.getDx() * speedMultiplier);
            ball.setDy(ball.getDy() * speedMultiplier);
        }
    }

    private void startNewGame() {
        world.createLevel(1);
        launchBall();
        gameState = GameState.PLAYING;
        lives = INITIAL_LIVES;
    }

    private void launchBall() {
        // 기존 공들 제거
        world.getBalls().clear();

        // 새 공 생성
        BreakoutPaddle paddle = world.getPaddle();
        double ballX = paddle.getX() + paddle.getWidth() / 2;
        double ballY = paddle.getY() - 15;

        Ball ball = new Ball(ballX, ballY, 10, BALL_SPEED * 0.5, -BALL_SPEED, Color.WHITE, CollisionAction.BOUNCE);

        world.addBall(ball);
    }

    private void restartGame() {
        world = new BreakoutWorld(GAME_WIDTH, GAME_HEIGHT);
        startNewGame();
    }

    private void render() {
        // 화면 지우기
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        // 게임 월드 렌더링
        world.render(gc);

        // 게임 상태별 오버레이
        renderGameStateOverlay();
    }

    private void renderGameStateOverlay() {
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font(24));

        String message = "";
        switch (gameState) {
            case MENU:
                message = "벽돌 깨기 게임\\n클릭하여 시작!";
                break;
            case PAUSED:
                message = "일시정지\\nSpace키로 계속";
                break;
            case BALL_LOST:
                message = "공을 잃었습니다!\\n클릭하여 다시 시작";
                break;
            case LEVEL_COMPLETE:
                message = "레벨 완료!\\n다음 레벨로...";
                break;
            case GAME_OVER:
                message = "게임 오버\\n최종 점수: " + world.getScore() + "\\n클릭하여 재시작";
                break;
            case GAME_WON:
                message = "축하합니다!\\n모든 레벨 클리어!\\n최종 점수: " + world.getScore();
                break;
        }

        if (!message.isEmpty()) {
            // 반투명 배경
            gc.setGlobalAlpha(0.8);
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
            gc.setGlobalAlpha(1.0);

            // 메시지 표시
            gc.setFill(Color.WHITE);
            String[] lines = message.split("\\\\n");
            for (int i = 0; i < lines.length; i++) {
                double textWidth = lines[i].length() * 12; // 대략적인 텍스트 너비
                double x = (GAME_WIDTH - textWidth) / 2;
                double y = GAME_HEIGHT / 2.0 - (lines.length - 1) * 15 + i * 30;
                gc.fillText(lines[i], x, y);
            }
        }
    }

    private void updateUI() {
        scoreLabel.setText("점수: " + world.getScore());
        livesLabel.setText("생명: " + lives);
        levelLabel.setText("레벨: " + world.getLevel());

        switch (gameState) {
            case PLAYING:
                statusLabel.setText("게임 진행 중 - Space: 일시정지, R: 재시작, ESC: 종료");
                break;
            case PAUSED:
                statusLabel.setText("일시정지 - Space키로 계속");
                break;
            case BALL_LOST:
                statusLabel.setText("공을 잃었습니다! 클릭하여 다시 시작");
                break;
            case GAME_OVER:
                statusLabel.setText("게임 오버! 클릭하여 재시작 또는 R키");
                break;
            default:
                statusLabel.setText("벽돌 깨기 게임");
                break;
        }
    }

    @Override
    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
