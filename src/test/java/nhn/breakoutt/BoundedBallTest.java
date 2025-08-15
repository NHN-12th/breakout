package nhn.breakoutt;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoundedBallTest {

    private BoundedBall ball;
    private BoundedWorld world;

    @BeforeEach
    public void setUp() {
        ball = new BoundedBall(new Point(100.0, 100.0), 20, Color.RED);
        world = new BoundedWorld(800, 600);
        world.add(ball);
    }

    @Test
    public void testBoundedBallCreation() {
        // 부모 클래스 속성 확인
        Point center = ball.getCenter();
        assertEquals(100, center.getX(), 0.001, "X 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(100, center.getY(), 0.001, "Y 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(20, ball.getRadius(), 0.001, "반지름이 올바르게 설정되지 않았습니다");
        assertEquals(Color.RED, ball.getColor(), "색상이 올바르게 설정되지 않았습니다");
    }

    @Test
    public void testInitialBounds() {
        // 생성 시 경계 초기값 확인 (경계 없음 상태)
        BoundedBall unboundedBall = new BoundedBall(new Point(50.0, 50.0), 10, Color.BLUE);

        // 경계가 설정되지 않은 상태에서는 자유롭게 이동 가능
        unboundedBall.setVelocity(new Vector2D(100, 100));
        unboundedBall.move(1.0);

        // 경계가 없으므로 위치가 그대로 변경됨
        Point newCenter = unboundedBall.getCenter();
        assertEquals(150, newCenter.getX(), 0.001, "경계가 없을 때 X 이동이 제한되면 안됩니다");
        assertEquals(150, newCenter.getY(), 0.001, "경계가 없을 때 Y 이동이 제한되면 안됩니다");
    }

    // 반발 계수는 다음 장에서 구현
    /*
    @Test
    public void testRestitutionSetterGetter() {
        ball.setRestitution(0.9);
        assertEquals(0.9, ball.getRestitution(), 0.001, "반발 계수 설정이 올바르지 않습니다");

        // 유효하지 않은 반발 계수
        assertThrows(IllegalArgumentException.class, () -> {
            ball.setRestitution(-0.1); // 음수
        }, "음수 반발 계수에 대해 예외가 발생하지 않았습니다");

        assertThrows(IllegalArgumentException.class, () -> {
            ball.setRestitution(1.1); // 1보다 큰 값
        }, "1보다 큰 반발 계수에 대해 예외가 발생하지 않았습니다");
    }
    */


    @Test
    public void testWallCollisionLeft() {
        ball.moveTo(new Point(10.0, ball.getCenter().getY())); // 왼쪽 벽 근처
        ball.setVelocity(new Vector2D(-50, 0)); // 왼쪽으로 이동

        Vector2D initialVelocity = ball.getVelocity();
        ball.move(0.1); // 충돌 발생

        assertTrue(ball.getVelocity().getX() > 0, "왼쪽 벽 충돌 후 X 속도가 양수가 되어야 합니다");
    }

    @Test
    public void testWallCollisionRight() {
        ball.moveTo(new Point(780.0, ball.getCenter().getY())); // 오른쪽 벽 근처 (800 - 20)
        ball.setVelocity(new Vector2D(50, 0)); // 오른쪽으로 이동

        ball.move(0.1); // 충돌 발생

        assertTrue(ball.getVelocity().getX() < 0, "오른쪽 벽 충돌 후 X 속도가 음수가 되어야 합니다");
    }

    @Test
    public void testWallCollisionTop() {
        ball.moveTo(new Point(ball.getCenter().getX(), 10.0)); // 위쪽 벽 근처
        ball.setVelocity(new Vector2D(0, -50)); // 위쪽으로 이동

        ball.move(0.1); // 충돌 발생

        assertTrue(ball.getVelocity().getY() > 0, "위쪽 벽 충돌 후 Y 속도가 양수가 되어야 합니다");
    }

    @Test
    public void testWallCollisionBottom() {
        ball.moveTo(new Point(ball.getCenter().getX(), 580.0)); // 아래쪽 벽 근처 (600 - 20)
        ball.setVelocity(new Vector2D(0, 50)); // 아래쪽으로 이동

        ball.move(0.1); // 충돌 발생

        assertTrue(ball.getVelocity().getY() < 0, "아래쪽 벽 충돌 후 Y 속도가 음수가 되어야 합니다");
    }

    @Test
    public void testInheritance() {
        // BoundedBall이 MovableBall을 상속받는지 확인
        assertTrue(ball instanceof MovableBall, "BoundedBall은 MovableBall을 상속받아야 합니다");
        assertTrue(ball instanceof PaintableBall, "BoundedBall은 PaintableBall을 상속받아야 합니다");
        assertTrue(ball instanceof Ball, "BoundedBall은 Ball을 상속받아야 합니다");

        // 부모 클래스의 메서드 사용 가능한지 확인
        ball.setVelocity(new Vector2D(100, 75));
        Vector2D velocity = ball.getVelocity();
        assertEquals(100, velocity.getX(), 0.001, "상속받은 속도 설정이 작동하지 않습니다");
        assertEquals(75, velocity.getY(), 0.001, "상속받은 속도 설정이 작동하지 않습니다");

        Point oldCenter = ball.getCenter();
        ball.move(0.1);
        Point newCenter = ball.getCenter();
        assertEquals(oldCenter.getX() + 10, newCenter.getX(), 0.001, "상속받은 move 메서드가 작동하지 않습니다");
        assertEquals(oldCenter.getY() + 7.5, newCenter.getY(), 0.001, "상속받은 move 메서드가 작동하지 않습니다");
    }
}