package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class MultipleInterfaceTest {

    @Test
    public void testBallMultipleInterfaces() {
        Ball ball = new Ball(100, 100, 20, Color.RED);

        // Ball이 모든 필요한 인터페이스를 구현하는지 확인
        assertTrue(ball instanceof Paintable, "Ball이 Paintable을 구현하지 않았습니다");
        assertTrue(ball instanceof Movable, "Ball이 Movable을 구현하지 않았습니다");
        assertTrue(ball instanceof Collidable, "Ball이 Collidable을 구현하지 않았습니다");
        assertTrue(ball instanceof Boundable, "Ball이 Boundable을 구현하지 않았습니다");

        // 모든 인터페이스의 메서드들이 작동하는지 확인
        GraphicsContext gc = mock(GraphicsContext.class);
        ball.paint(gc); // Paintable

        ball.setDx(50);
        ball.setDy(30);
        ball.move(1.0); // Movable

        Bounds bounds = ball.getBounds(); // Boundable
        assertNotNull(bounds, "getBounds()가 null을 반환했습니다");

        Box wall = new Box(ball.getX() + 15, ball.getY(), 20, 20);
        if (ball.isColliding(wall)) {
            ball.handleCollision(wall); // Collidable
        }
    }

    @Test
    public void testBoxMultipleInterfaces() {
        Box box = new Box(100, 100, 50, 40, Color.BLUE);

        // Box의 인터페이스 구현 확인
        assertTrue(box instanceof Paintable, "Box가 Paintable을 구현하지 않았습니다");
        assertTrue(box instanceof Collidable, "Box가 Collidable을 구현하지 않았습니다");
        assertTrue(box instanceof Boundable, "Box가 Boundable을 구현하지 않았습니다");

        // Box는 기본적으로 Movable을 구현하지 않음
        assertFalse(box instanceof Movable, "일반 Box는 Movable을 구현하면 안됩니다");
    }

    @Test
    public void testMovableBoxMultipleInterfaces() {
        MovableBox movableBox = new MovableBox(100, 100, 50, 40, Color.GREEN);

        // MovableBox는 Box의 모든 인터페이스 + Movable 추가
        assertTrue(movableBox instanceof Paintable, "MovableBox가 Paintable을 구현하지 않았습니다");
        assertTrue(movableBox instanceof Movable, "MovableBox가 Movable을 구현하지 않았습니다");
        assertTrue(movableBox instanceof Collidable, "MovableBox가 Collidable을 구현하지 않았습니다");
        assertTrue(movableBox instanceof Boundable, "MovableBox가 Boundable을 구현하지 않았습니다");
        assertTrue(movableBox instanceof Box, "MovableBox가 Box를 상속받지 않았습니다");

        // 모든 기능이 작동하는지 확인
        movableBox.setDx(25);
        movableBox.setDy(35);
        double oldX = movableBox.getX();
        movableBox.move(1.0);
        assertNotEquals(oldX, movableBox.getX(), "MovableBox가 이동하지 않았습니다");
    }

    @Test
    public void testTriangleMultipleInterfaces() {
        Triangle triangle = new Triangle(100, 100, 30);

        // Triangle의 인터페이스 구현 확인
        assertTrue(triangle instanceof Paintable, "Triangle이 Paintable을 구현하지 않았습니다");
        assertTrue(triangle instanceof Movable, "Triangle이 Movable을 구현하지 않았습니다");
        assertTrue(triangle instanceof Collidable, "Triangle이 Collidable을 구현하지 않았습니다");
        assertTrue(triangle instanceof Boundable, "Triangle이 Boundable을 구현하지 않았습니다");

        // Triangle 고유 기능 확인
        triangle.setDx(40);
        triangle.setDy(60);
        double oldX = triangle.getCenter().getX();
        double oldY = triangle.getCenter().getY();
        triangle.move(0.5);
        assertEquals(oldX + 20, triangle.getCenter().getX(), 0.001, "Triangle X 이동이 올바르지 않습니다");
        assertEquals(oldY + 30, triangle.getCenter().getY(), 0.001, "Triangle Y 이동이 올바르지 않습니다");
    }
}