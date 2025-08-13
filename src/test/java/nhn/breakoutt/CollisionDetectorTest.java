package nhn.breakoutt;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CollisionDetectorTest {

    @Test
    public void testWallCollisionDetection() {
        BoundedBall ball = new BoundedBall(new Point(15.0, 100.0), 20, Color.RED);

        // 왼쪽 벽 충돌 (x - radius < 0)
        CollisionDetector.WallCollision collision = CollisionDetector.checkWallCollision(ball, 0, 0, 800, 600);
        assertNotNull(collision, "벽 충돌이 감지되지 않았습니다");
        assertEquals(CollisionDetector.Wall.LEFT, collision.getWall(), "잘못된 벽이 감지되었습니다");
        assertEquals(5, collision.getPenetration(), 0.001, "침투 깊이가 잘못 계산되었습니다");

        // 오른쪽 벽 충돌
        ball.moveTo(new Point(785.0, 100.0)); // x + radius > 800
        collision = CollisionDetector.checkWallCollision(ball, 0, 0, 800, 600);
        assertEquals(CollisionDetector.Wall.RIGHT, collision.getWall(), "오른쪽 벽 충돌이 감지되지 않았습니다");
        assertEquals(5, collision.getPenetration(), 0.001, "오른쪽 벽 침투 깊이가 잘못되었습니다");

        // 위쪽 벽 충돌
        ball.moveTo(new Point(100.0, 15.0)); // y - radius < 0
        collision = CollisionDetector.checkWallCollision(ball, 0, 0, 800, 600);
        assertEquals(CollisionDetector.Wall.TOP, collision.getWall(), "위쪽 벽 충돌이 감지되지 않았습니다");

        // 아래쪽 벽 충돌
        ball.moveTo(new Point(100.0, 585.0)); // y + radius > 600
        collision = CollisionDetector.checkWallCollision(ball, 0, 0, 800, 600);
        assertEquals(CollisionDetector.Wall.BOTTOM, collision.getWall(), "아래쪽 벽 충돌이 감지되지 않았습니다");

        // 충돌하지 않는 경우
        ball.moveTo(new Point(400.0, 300.0));
        collision = CollisionDetector.checkWallCollision(ball, 0, 0, 800, 600);
        assertNull(collision, "벽과 충돌하지 않는데 충돌이 감지되었습니다");
    }

    // 벽 충돌 후 속도 반전 테스트
    @Test
    public void testWallCollisionResponse() {
        BoundedBall ball = new BoundedBall(new Point(10.0, 300.0), 20, Color.RED);
        ball.setVelocity(new Vector2D(-50, 0)); // 왼쪽으로 이동

        // 왼쪽 벽 충돌 감지
        CollisionDetector.WallCollision collision = CollisionDetector.checkWallCollision(ball, 0, 0, 800, 600);
        assertEquals(CollisionDetector.Wall.LEFT, collision.getWall(), "왼쪽 벽 충돌이 감지되지 않았습니다");

        // 벽 충돌 해결
        CollisionDetector.resolveWallCollision(ball, collision);

        // 속도가 반전되었는지 확인
        assertTrue(ball.getVelocity().getX() > 0, "왼쪽 벽 충돌 후 X 속도가 양수가 되어야 합니다");
    }
}