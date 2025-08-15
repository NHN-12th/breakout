package nhn.breakoutt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WallCollisionTest {
    @Test
    public void testWallCollisionDetection() {
        BoundedBall ball = new BoundedBall(new Point(50.0, 300.0), 20);

        CollisionDetector.WallCollision collision = CollisionDetector.checkWallCollision(
                ball, 0, 0, 800, 600
        );

        assertNull(collision, "충돌이 없을 때는 null이 반환되어야 합니다");

        ball.moveTo(new Point(15.0, 300.0)); // 왼쪽 벽에 닿음
        collision = CollisionDetector.checkWallCollision(
                ball, 0, 0, 800, 600
        );

        assertEquals(CollisionDetector.Wall.LEFT, collision.getWall());
        assertEquals(5, collision.getPenetration(), 0.001);
    }
}