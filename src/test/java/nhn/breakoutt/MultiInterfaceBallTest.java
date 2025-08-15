package nhn.breakoutt;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 여러 인터페이스를 구현하는 Ball 클래스의 종합 테스트
 */
public class MultiInterfaceBallTest {

    @Test
    public void testBallImplementsMultipleInterfaces() {
        Ball ball = new Ball(100, 100, 20, 50, 30, Color.BLUE, CollisionAction.BOUNCE);

        // 인터페이스 구현 확인
        assertTrue(ball instanceof Paintable, "Ball이 Paintable을 구현하지 않았습니다");
        assertTrue(ball instanceof Movable, "Ball이 Movable을 구현하지 않았습니다");
        assertTrue(ball instanceof Collidable, "Ball이 Collidable을 구현하지 않았습니다");
        assertTrue(ball instanceof Boundable, "Ball이 Boundable을 구현하지 않았습니다");
    }

    @Test
    public void testMovableImplementation() {
        Ball ball = new Ball(100, 100, 20, 50, 30, Color.RED, CollisionAction.BOUNCE);

        // 초기 속도 확인
        assertEquals(50, ball.getDx(), 0.001);
        assertEquals(30, ball.getDy(), 0.001);

        // 위치 업데이트
        double oldX = ball.getX();
        double oldY = ball.getY();

        ball.move(1.0); // 1초 이동

        assertEquals(oldX + 50, ball.getX(), 0.001, "X 위치가 올바르게 업데이트되지 않았습니다");
        assertEquals(oldY + 30, ball.getY(), 0.001, "Y 위치가 올바르게 업데이트되지 않았습니다");

        // 속도 변경
        ball.setDx(100);
        ball.setDy(-50);
        assertEquals(100, ball.getDx(), 0.001);
        assertEquals(-50, ball.getDy(), 0.001);
    }

    @Test
    public void testCollidableImplementation() {
        Ball ball1 = new Ball(100, 100, 20, 50, 30, Color.RED, CollisionAction.BOUNCE);
        Ball ball2 = new Ball(130, 100, 20, -30, 0, Color.BLUE, CollisionAction.BOUNCE);

        // 충돌 검사
        assertTrue(ball1.isColliding(ball2), "충돌하는 공들이 충돌 감지되지 않았습니다");

        // 경계 확인
        Bounds bounds = ball1.getBounds();
        assertNotNull(bounds, "Bounds가 null입니다");
        assertEquals(100, bounds.getCenterX(), 0.001);
        assertEquals(100, bounds.getCenterY(), 0.001);

        // 충돌 액션 확인
        assertEquals(CollisionAction.BOUNCE, ball1.getCollisionAction());
    }

    @Test
    public void testCollisionActions() {
        // BOUNCE 액션 테스트
        Ball bounceBall = new Ball(100, 100, 20, 50, 30, Color.RED, CollisionAction.BOUNCE);
        Ball otherBall = new Ball(130, 100, 20, -30, 0, Color.BLUE, CollisionAction.BOUNCE);

        double oldDx = bounceBall.getDx();
        double oldDy = bounceBall.getDy();

        bounceBall.handleCollision(otherBall);

        // BOUNCE: 속도가 반전되어야 함
        assertEquals(-oldDx, bounceBall.getDx(), 0.001, "BOUNCE 액션에서 dx가 반전되지 않았습니다");
        assertEquals(-oldDy, bounceBall.getDy(), 0.001, "BOUNCE 액션에서 dy가 반전되지 않았습니다");

        // DESTROY 액션 테스트
        Ball destroyBall = new Ball(100, 100, 20, 50, 30, Color.RED, CollisionAction.DESTROY);
        assertFalse(destroyBall.isDestroyed(), "초기에 공이 파괴되어 있습니다");

        destroyBall.handleCollision(otherBall);
        assertTrue(destroyBall.isDestroyed(), "DESTROY 액션에서 공이 파괴되지 않았습니다");

        // STOP 액션 테스트
        Ball stopBall = new Ball(100, 100, 20, 50, 30, Color.RED, CollisionAction.STOP);
        stopBall.handleCollision(otherBall);
        assertEquals(0, stopBall.getDx(), 0.001, "STOP 액션에서 dx가 0이 되지 않았습니다");
        assertEquals(0, stopBall.getDy(), 0.001, "STOP 액션에서 dy가 0이 되지 않았습니다");

        // PASS 액션 테스트 (아무것도 변하지 않아야 함)
        Ball passBall = new Ball(100, 100, 20, 50, 30, Color.RED, CollisionAction.PASS);
        double passDx = passBall.getDx();
        double passDy = passBall.getDy();

        passBall.handleCollision(otherBall);
        assertEquals(passDx, passBall.getDx(), 0.001, "PASS 액션에서 속도가 변경되었습니다");
        assertEquals(passDy, passBall.getDy(), 0.001, "PASS 액션에서 속도가 변경되었습니다");
    }

    @Test
    public void testColorAndProperties() {
        Ball ball = new Ball(100, 100, 20, 50, 30, Color.GREEN, CollisionAction.CUSTOM);

        // 색상 확인
        assertEquals(Color.GREEN, ball.getColor());

        // 색상 변경
        ball.setColor(Color.YELLOW);
        assertEquals(Color.YELLOW, ball.getColor());

        // 충돌 액션 변경
        ball.setCollisionAction(CollisionAction.STOP);
        assertEquals(CollisionAction.STOP, ball.getCollisionAction());

        // null 유효성 검사
        assertThrows(IllegalArgumentException.class, () -> ball.setColor(null));
        assertThrows(IllegalArgumentException.class, () -> ball.setCollisionAction(null));
    }

    @Test
    public void testConstructors() {
        // 기본 생성자 (x, y, radius)
        Ball simpleBall = new Ball(50, 75, 15);
        assertEquals(50, simpleBall.getX(), 0.001);
        assertEquals(75, simpleBall.getY(), 0.001);
        assertEquals(15, simpleBall.getRadius(), 0.001);
        assertEquals(0, simpleBall.getDx(), 0.001);
        assertEquals(0, simpleBall.getDy(), 0.001);
        assertEquals(Color.RED, simpleBall.getColor());
        assertEquals(CollisionAction.BOUNCE, simpleBall.getCollisionAction());

        // Point 생성자
        Point center = new Point(100.0, 200.0);
        Ball pointBall = new Ball(center, 25);
        assertEquals(100.0, pointBall.getX(), 0.001);
        assertEquals(200.0, pointBall.getY(), 0.001);
        assertEquals(25.0, pointBall.getRadius(), 0.001);

        // 완전 생성자
        Ball fullBall = new Ball(150, 250, 30, 40, -20, Color.PURPLE, CollisionAction.DESTROY);
        assertEquals(150.0, fullBall.getX(), 0.001);
        assertEquals(250.0, fullBall.getY(), 0.001);
        assertEquals(30.0, fullBall.getRadius(), 0.001);
        assertEquals(40.0, fullBall.getDx(), 0.001);
        assertEquals(-20.0, fullBall.getDy(), 0.001);
        assertEquals(Color.PURPLE, fullBall.getColor());
        assertEquals(CollisionAction.DESTROY, fullBall.getCollisionAction());
    }

    @Test
    public void testValidation() {
        // 반지름 유효성 검사
        assertThrows(IllegalArgumentException.class, () ->
            new Ball(100, 100, -5, 0, 0, Color.RED, CollisionAction.BOUNCE));
        assertThrows(IllegalArgumentException.class, () ->
            new Ball(100, 100, 0, 0, 0, Color.RED, CollisionAction.BOUNCE));

        // null 색상 검사
        assertThrows(IllegalArgumentException.class, () ->
            new Ball(100, 100, 20, 0, 0, null, CollisionAction.BOUNCE));

        // null 충돌 액션 검사
        assertThrows(IllegalArgumentException.class, () ->
            new Ball(100, 100, 20, 0, 0, Color.RED, null));
    }

    @Test
    public void testBallInteraction() {
        // 여러 Ball들 간의 상호작용 테스트
        Ball ball1 = new Ball(100, 100, 20, 50, 30, Color.RED, CollisionAction.BOUNCE);
        Ball ball2 = new Ball(200, 200, 15, -30, -40, Color.BLUE, CollisionAction.DESTROY);
        Ball ball3 = new Ball(300, 100, 25, 0, 50, Color.GREEN, CollisionAction.STOP);

        // 각 Ball이 독립적으로 동작하는지 확인
        ball1.move(1.0);
        ball2.move(1.0);
        ball3.move(1.0);

        assertEquals(150, ball1.getX(), 0.001);
        assertEquals(130, ball1.getY(), 0.001);
        assertEquals(170, ball2.getX(), 0.001);
        assertEquals(160, ball2.getY(), 0.001);
        assertEquals(300, ball3.getX(), 0.001);
        assertEquals(150, ball3.getY(), 0.001);

        // 충돌 처리
        ball2.handleCollision(ball1);
        assertTrue(ball2.isDestroyed(), "ball2가 파괴되지 않았습니다");
        assertFalse(ball1.isDestroyed(), "ball1이 파괴되었습니다");
    }
}
