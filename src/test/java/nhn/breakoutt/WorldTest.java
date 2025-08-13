package nhn.breakoutt;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {

    private World world;

    @BeforeEach
    public void setUp() {
        world = new World(800, 600);
    }

    @Test
    public void testWorldCreation() {
        assertEquals(800, world.getWidth(), "World의 너비가 올바르게 설정되지 않았습니다");
        assertEquals(600, world.getHeight(), "World의 높이가 올바르게 설정되지 않았습니다");
    }

    @Test
    public void testAddBall() {
        Ball ball = new Ball(100, 100, 20);
        world.add(ball);

        assertEquals(1, world.getBalls().size(), "Ball이 추가되지 않았습니다");
    }

    @Test
    public void testAddMultipleBalls() {
        Ball ball1 = new Ball(new Point(100.0, 100.0), 20);
        Ball ball2 = new Ball(new Point(200.0, 200.0), 30);
        PaintableBall ball3 = new PaintableBall(new Point(300.0, 300.0), 25, Color.RED);

        world.add(ball1);
        world.add(ball2);
        world.add(ball3);

        assertEquals(3, world.getBalls().size(), "여러 Ball이 올바르게 추가되지 않았습니다");
    }

    @Test
    public void testBoundaryCheck() {
        // 경계 내부의 공 - 추가 성공
        Ball validBall = new Ball(new Point(400.0, 300.0), 50);
        world.add(validBall);
        assertEquals(1, world.getBalls().size(), "경계 내부의 공이 추가되어야 합니다");

        // 경계 외부의 공 - 추가 실패 (예외 발생)
        Ball invalidBall = new Ball(new Point(50.0, 50.0), 100); // 왼쪽 상단 밖으로 나감
        assertThrows(IllegalArgumentException.class, () -> {
            world.add(invalidBall);
        }, "경계 외부의 공은 추가되면 안됩니다");
    }

    @Test
    public void testDefensiveCopy() {
        PaintableBall ball = new PaintableBall(new Point(400.0, 300.0), 25, Color.BLUE);
        world.add(ball);

        List<Ball> balls = world.getBalls();
        balls.clear(); // 반환된 리스트를 수정

        // 원본 리스트는 영향받지 않아야 함
        assertEquals(1, world.getBalls().size(), "방어적 복사가 제대로 되지 않았습니다");
    }

    @Test
    public void testRemoveBall() {
        Ball ball = new Ball(new Point(100.0, 100.0), 20);
        world.add(ball);
        assertEquals(1, world.getBallCount(), "Ball 추가 확인");

        world.remove(ball);
        assertEquals(0, world.getBallCount(), "Ball이 제거되지 않았습니다");

        // 존재하지 않는 공 제거 시 예외 발생
        assertThrows(NoSuchElementException.class, () -> {
            world.remove(ball);
        }, "존재하지 않는 공 제거 시 예외가 발생해야 합니다");
    }

    @Test
    public void testDraw() {
        // GraphicsContext mock 대신 실제 Canvas와 GraphicsContext 사용
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // PaintableBall들 추가
        world.add(new PaintableBall(new Point(100.0, 100.0), 20, Color.RED));
        world.add(new PaintableBall(new Point(200.0, 200.0), 30, Color.GREEN));

        // 그리기가 예외 없이 실행되는지 테스트
        assertDoesNotThrow(() -> {
            world.draw(gc);
        }, "draw 메서드 실행 중 예외가 발생했습니다");

        // World에 공이 2개 추가되었는지 확인
        assertEquals(2, world.getBallCount(), "World에 2개의 공이 있어야 합니다");
    }
}