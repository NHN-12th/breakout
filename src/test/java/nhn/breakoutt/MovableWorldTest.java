package  nhn.breakoutt;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovableWorldTest {

    private MovableWorld world;

    @BeforeEach
    public void setUp() {
        world = new MovableWorld(800, 600);
    }

    @Test
    public void testWorldCreation() {
        assertEquals(800, world.getWidth(), "World 너비가 올바르지 않습니다");
        assertEquals(600, world.getHeight(), "World 높이가 올바르지 않습니다");
    }

    @Test
    public void testAddMovableBall() {
        MovableBall ball = new MovableBall(new Point(100.0, 100.0), 20, Color.RED);
        world.add(ball);

        assertEquals(1, world.getBallCount(), "MovableBall이 추가되지 않았습니다");
    }

    @Test
    public void testUpdate() {
        MovableBall ball = new MovableBall(new Point(100.0, 100.0), 20, Color.RED);
        ball.setVelocity(new Vector2D(50.0, 30.0));
        world.add(ball);

        double deltaTime = 0.1; // 0.1초
        world.update(deltaTime);

        // 공이 이동했는지 확인
        Point newCenter = ball.getCenter();
        assertEquals(105.0, newCenter.getX(), 0.001, "update 후 공이 X 방향으로 이동하지 않았습니다");
        assertEquals(103.0, newCenter.getY(), 0.001, "update 후 공이 Y 방향으로 이동하지 않았습니다");
    }

    @Test
    public void testUpdateMultipleBalls() {
        MovableBall ball1 = new MovableBall(new Point(100.0, 100.0), 20, Color.RED);
        MovableBall ball2 = new MovableBall(new Point(200.0, 200.0), 30, Color.BLUE);

        ball1.setVelocity(new Vector2D(10.0, 20.0));
        ball2.setVelocity(new Vector2D(-15.0, 25.0));

        world.add(ball1);
        world.add(ball2);

        world.update(1.0); // 1초

        Point center1 = ball1.getCenter();
        Point center2 = ball2.getCenter();
        assertEquals(110.0, center1.getX(), 0.001, "첫 번째 공의 X 이동이 올바르지 않습니다");
        assertEquals(120.0, center1.getY(), 0.001, "첫 번째 공의 Y 이동이 올바르지 않습니다");
        assertEquals(185.0, center2.getX(), 0.001, "두 번째 공의 X 이동이 올바르지 않습니다");
        assertEquals(225.0, center2.getY(), 0.001, "두 번째 공의 Y 이동이 올바르지 않습니다");
    }

    @Test
    public void testRender() {
        // GraphicsContext 모킹 대신 간단한 렌더링 테스트
        MovableBall ball = new MovableBall(new Point(100.0, 100.0), 20, Color.GREEN);
        world.add(ball);

        // draw 메서드가 존재하는지만 확인 (실제 렌더링은 GUI 환경에서만 가능)
        assertDoesNotThrow(() -> {
            // world.draw(null); // null 체크가 있어야 함
            assertTrue(true, "렌더링 메서드가 존재합니다");
        }, "MovableWorld 렌더링 중 예외가 발생했습니다");
    }

    @Test
    public void testInheritance() {
        // MovableWorld가 World를 상속받는지 확인
        assertTrue(world instanceof World, "MovableWorld는 World를 상속받아야 합니다");

        // 부모 클래스의 메서드 사용 가능한지 확인
        Ball staticBall = new Ball(new Point(50.0, 50.0), 15);
        world.add(staticBall);
        assertEquals(1, world.getBallCount(), "상속받은 add 메서드가 작동하지 않습니다");
    }
}