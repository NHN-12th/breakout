package nhn.breakoutt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BallCollisionTest {

    @Test
    public void testBallCollisionDetection() {
        Ball ball1 = new Ball(new Point(100.0, 100.0), 30);
        Ball ball2 = new Ball(new Point(150.0, 100.0), 30);

        assertTrue(BallCollision.areColliding(ball1, ball2));

        ball2.moveTo(new Point(200.0, 100.0));
        assertFalse(BallCollision.areColliding(ball1, ball2));
    }

    @Test
    public void testElasticCollision() {
        MovableBall ball1 = new MovableBall(new Point(100.0, 100.0), 20);
        ball1.setVelocity(new Vector2D(100, 0));

        MovableBall ball2 = new MovableBall(new Point(140.0, 100.0), 20);
        ball2.setVelocity(new Vector2D(-100, 0));

        // 충돌 전 총 운동량
        double totalMomentumBefore =
                ball1.getRadius() * ball1.getVelocity().getX() +
                        ball2.getRadius() * ball2.getVelocity().getX();

        BallCollision.resolveElasticCollision(ball1, ball2);

        // 충돌 후 총 운동량
        double totalMomentumAfter =
                ball1.getRadius() * ball1.getVelocity().getX() +
                        ball2.getRadius() * ball2.getVelocity().getX();

        // 운동량 보존 확인
        assertEquals(totalMomentumBefore, totalMomentumAfter, 0.001);
    }

    @Test
    public void testBallSeparation() {
        MovableBall ball1 = new MovableBall(new Point(100.0, 100.0), 20);
        MovableBall ball2 = new MovableBall(new Point(125.0, 100.0), 20);

        // 겹친 상태 (거리: 25, 반지름 합: 40, 겹침: 15)
        assertTrue(BallCollision.areColliding(ball1, ball2), "공들이 겹쳐있어야 합니다");

        BallCollision.separateBalls(ball1, ball2);

        // 분리 후 더 이상 겹치지 않아야 함
        assertFalse(BallCollision.areColliding(ball1, ball2),
                "분리 후에도 공들이 겹쳐있습니다");

        // 공들 사이의 거리가 반지름 합과 같거나 커야 함
        Point center1 = ball1.getCenter();
        Point center2 = ball2.getCenter();
        double distance = Math.sqrt(Math.pow(center2.getX() - center1.getX(), 2) +
                Math.pow(center2.getY() - center1.getY(), 2));
        assertTrue(distance >= 40, "분리 후 거리가 반지름 합보다 작습니다");
    }
}