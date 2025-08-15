package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class InterfaceTest {

    @Test
    public void testMultipleInterfaces() {
        Ball ball = new Ball(100, 100, 20);

        // Ball은 여러 인터페이스를 구현
        assertTrue(ball instanceof Paintable);
        assertTrue(ball instanceof Movable);
        assertTrue(ball instanceof Collidable);
    }

    @Test
    public void testPolymorphism() {
        List<Paintable> paintables = new ArrayList<>();
        paintables.add(new Ball(0, 0, 10));
        paintables.add(new Box(new Point(0.0, 0.0), 20, 20));

        // 모든 Paintable을 동일하게 처리
        GraphicsContext gc = mock(GraphicsContext.class);
        for (Paintable p : paintables) {
            p.paint(gc);
        }
    }

    @Test
    public void testCollisionActions() {
        Ball ball = new Ball(50, 50, 10);
        ball.setDx(100);
        ball.setCollisionAction(CollisionAction.BOUNCE);

        Box wall = new Box(new Point(55.0, 0.0), 10, 100);

        assertTrue(ball.isColliding(wall));

        double oldDx = ball.getDx();
        ball.handleCollision(wall);

        // 반사 후 방향이 바뀜
        assertEquals(-oldDx, ball.getDx(), 0.001);
    }
}