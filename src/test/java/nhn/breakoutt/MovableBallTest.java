package nhn.breakoutt;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class MovableBallTest {

    private MovableBall ball;

    @BeforeEach
    public void setUp() {
        ball = new MovableBall(new Point(100.0, 100.0), 20, Color.RED);
    }

    @Test
    public void testMovableBallCreation() {
        MovableBall newBall = new MovableBall(new Point(100.0, 100.0), 20, Color.RED);

        // 부모 클래스 속성 확인
        Point center = newBall.getCenter();
        assertEquals(100, center.getX(), 0.001, "X 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(100, center.getY(), 0.001, "Y 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(20, newBall.getRadius(), 0.001, "반지름이 올바르게 설정되지 않았습니다");
        assertEquals(Color.RED, newBall.getColor(), "색상이 올바르게 설정되지 않았습니다");

        // 초기 속도는 0이어야 함
        Vector2D velocity = newBall.getVelocity();
        assertEquals(0.0, velocity.getX(), 0.001, "초기 X 속도는 0이어야 합니다");
        assertEquals(0.0, velocity.getY(), 0.001, "초기 Y 속도는 0이어야 합니다");
    }

    @Test
    public void testVelocitySetterAndGetter() {
        Vector2D newVelocity = new Vector2D(50.0, 30.0);
        ball.setVelocity(newVelocity);

        Vector2D velocity = ball.getVelocity();
        assertEquals(50.0, velocity.getX(), 0.001, "X 속도 설정이 올바르지 않습니다");
        assertEquals(30.0, velocity.getY(), 0.001, "Y 속도 설정이 올바르지 않습니다");
    }

    @Test
    public void testVelocityImmutability() {
        Vector2D originalVelocity = new Vector2D(100.0, 75.0);
        ball.setVelocity(originalVelocity);

        // 원본 벡터를 변경해도 ball의 속도는 영향받지 않아야 함
        // (Vector2D가 불변 객체이므로)
        Vector2D retrievedVelocity = ball.getVelocity();
        assertEquals(100.0, retrievedVelocity.getX(), 0.001, "벡터 속도 X가 올바르지 않습니다");
        assertEquals(75.0, retrievedVelocity.getY(), 0.001, "벡터 속도 Y가 올바르지 않습니다");
    }

    @Test
    public void testMove() {
        ball.setVelocity(new Vector2D(60.0, 80.0)); // 60, 80 pixels/second

        double deltaTime = 0.5; // 0.5초
        ball.move(deltaTime);

        // 0.5초 동안 이동한 거리 계산
        Point newCenter = ball.getCenter();
        assertEquals(130.0, newCenter.getX(), 0.001, "X 방향 이동이 올바르지 않습니다"); // 100 + 60*0.5
        assertEquals(140.0, newCenter.getY(), 0.001, "Y 방향 이동이 올바르지 않습니다"); // 100 + 80*0.5
    }

    @Test
    public void testMoveWithZeroVelocity() {
        MovableBall zeroVelBall = new MovableBall(new Point(50.0, 50.0), 10, Color.GREEN);
        // 기본 속도는 영벡터

        Point originalCenter = zeroVelBall.getCenter();
        zeroVelBall.move(1.0); // 1초 경과

        Point newCenter = zeroVelBall.getCenter();
        assertEquals(originalCenter.getX(), newCenter.getX(), 0.001, "속도가 0일 때 X 위치가 변경되었습니다");
        assertEquals(originalCenter.getY(), newCenter.getY(), 0.001, "속도가 0일 때 Y 위치가 변경되었습니다");
    }

    @Test
    public void testMoveWithNegativeVelocity() {
        ball.setVelocity(new Vector2D(-40.0, -30.0));

        ball.move(1.0);

        Point newCenter = ball.getCenter();
        assertEquals(60.0, newCenter.getX(), 0.001, "음수 X 속도 이동이 올바르지 않습니다");
        assertEquals(70.0, newCenter.getY(), 0.001, "음수 Y 속도 이동이 올바르지 않습니다");
    }

    @Test
    public void testInheritance() {
        // MovableBall이 PaintableBall을 상속받는지 확인
        assertTrue(ball instanceof PaintableBall, "MovableBall은 PaintableBall을 상속받아야 합니다");
        assertTrue(ball instanceof Ball, "MovableBall은 Ball을 상속받아야 합니다");

        // 부모 클래스의 메서드 사용 가능한지 확인
        ball.setColor(Color.BLUE);
        assertEquals(Color.BLUE, ball.getColor(), "상속받은 색상 변경이 작동하지 않습니다");

        assertTrue(ball.contains(100, 100), "상속받은 contains 메서드가 작동하지 않습니다");
    }

    @Test
    public void testVelocityMagnitudeCalculation() {
        ball.setVelocity(new Vector2D(30.0, 40.0));

        double magnitude = ball.getVelocity().magnitude();
        assertEquals(50.0, magnitude, 0.001, "속력 계산이 올바르지 않습니다"); // √(30² + 40²) = 50
    }

    @Test
    public void testVelocityDirectionCalculation() {
        ball.setVelocity(new Vector2D(10.0, 10.0));

        Vector2D vel = ball.getVelocity();
        double direction = Math.atan2(vel.getY(), vel.getX());
        assertEquals(Math.PI / 4, direction, 0.001, "방향 계산이 올바르지 않습니다"); // 45도 = π/4 라디안
    }
}