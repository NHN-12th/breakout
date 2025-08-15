package nhn.breakoutt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class BallWorld extends Application {
    private World world;
    private Canvas canvas;
    private GraphicsContext gc;
    private Random random;

    @Override
    public void start(Stage stage) throws Exception {
        world = new World(800, 600);
        canvas = new Canvas(800, 600); // 1. 도화지를 생성한다.
        gc = canvas.getGraphicsContext2D(); // 2. 그림 도구를 생성한다.
        random = new Random();

        // 5개의 랜덤한 공 생성
        createRandomBalls();

        // 마우스 클릭 이벤트 설정
        canvas.setOnMouseClicked(this::handleMouseClick);

        // Scene 구성
        Pane root = new Pane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Ball World");
        stage.setScene(scene);
        stage.show();

        // 초기 화면 그리기
        draw();
    }

    /**
     * 랜덤한 위치, 크기, 색상의 공을 생성합니다.
     */
    private void createRandomBalls() {
        for(int i = 0; i < 5; i++) {
            // 랜덤 위치 (공이 화면 밖으로 나가지 않도록 반지름 고려)
            double radius = 20 + random.nextDouble() * 30; // 20~50 크기
            double x = radius + random.nextDouble() * (world.getWidth() - 2 * radius);
            double y = radius + random.nextDouble() * (world.getHeight() - 2 * radius);

            // 랜덤 색상
            Color color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());

            PaintableBall ball = new PaintableBall(x, y, radius, color);
            world.add(ball);
        }
    }

    /**
     * 마우스 클릭 이벤트를 처리합니다.
     */
    private void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        double radius = 15 + random.nextDouble() * 25; // 15~40 크기
        Color color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());

        // 공이 화면 경계를 벗어나지 않도록 위치 조정
        x = Math.max(radius, Math.min(world.getWidth() - radius, x));
        y = Math.max(radius, Math.min(world.getHeight() - radius, y));

        world.add(new PaintableBall(x, y, radius, color));

        // 화면 다시 그리기
        draw();
    }

    /**
     * 화면 전체를 다시 그립니다.
     */
    private void draw() {
        world.draw(gc);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
