package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class InterfaceDesignTest {

    @Test
    public void testPaintableInterface() {
        // Paintable 인터페이스 구현 확인
        Ball ball = new Ball(100, 100, 20);
        Box box = new Box(new Point(200.0, 200.0), 50, 40);
        Triangle triangle = new Triangle(300, 300, 30);

        // 모든 객체가 Paintable을 구현하는지 확인
        assertTrue(ball instanceof Paintable, "Ball이 Paintable을 구현하지 않았습니다");
        assertTrue(box instanceof Paintable, "Box가 Paintable을 구현하지 않았습니다");
        assertTrue(triangle instanceof Paintable, "Triangle이 Paintable을 구현하지 않았습니다");

        // paint 메서드 호출 가능한지 확인 (예외 발생하지 않아야 함)
        GraphicsContext gc = mock(GraphicsContext.class);
        assertDoesNotThrow(() -> ball.paint(gc), "Ball.paint() 호출 시 예외 발생");
        assertDoesNotThrow(() -> box.paint(gc), "Box.paint() 호출 시 예외 발생");
        assertDoesNotThrow(() -> triangle.paint(gc), "Triangle.paint() 호출 시 예외 발생");
    }

    @Test
    public void testMovableInterface() {
        Ball ball = new Ball(100, 100, 20);
        MovableBox movableBox = new MovableBox(new Point(200.0, 200.0), 50, 40);
        Triangle triangle = new Triangle(300, 300, 30);

        // Movable 인터페이스 구현 확인
        assertTrue(ball instanceof Movable, "Ball이 Movable을 구현하지 않았습니다");
        assertTrue(movableBox instanceof Movable, "MovableBox가 Movable을 구현하지 않았습니다");
        assertTrue(triangle instanceof Movable, "Triangle이 Movable을 구현하지 않았습니다");

        // Movable 메서드들 작동 확인
        ball.setDx(50);
        ball.setDy(30);
        assertEquals(50, ball.getDx(), 0.001, "Ball getDx() 작동하지 않음");
        assertEquals(30, ball.getDy(), 0.001, "Ball getDy() 작동하지 않음");

        // move 메서드 호출 확인
        double oldX = ball.getX();
        double oldY = ball.getY();
        ball.move(1.0);
        assertNotEquals(oldX, ball.getX(), "Ball이 이동하지 않았습니다");
        assertNotEquals(oldY, ball.getY(), "Ball이 이동하지 않았습니다");
    }

    @Test
    public void testCollidableInterface() {
        Ball ball = new Ball(100, 100, 20);
        Box box = new Box(new Point(110.0, 110.0), 40, 30);

        // Collidable 인터페이스 구현 확인
        assertTrue(ball instanceof Collidable, "Ball이 Collidable을 구현하지 않았습니다");
        assertTrue(box instanceof Collidable, "Box가 Collidable을 구현하지 않았습니다");

        // getBounds 메서드 확인
        Bounds ballBounds = ball.getBounds();
        Bounds boxBounds = box.getBounds();
        assertNotNull(ballBounds, "Ball getBounds()가 null을 반환했습니다");
        assertNotNull(boxBounds, "Box getBounds()가 null을 반환했습니다");

        // isColliding 메서드 확인
        assertTrue(ball.isColliding(box), "충돌하는 객체들이 충돌하지 않는다고 판단되었습니다");

        // handleCollision 메서드 호출 확인 (예외 발생하지 않아야 함)
        assertDoesNotThrow(() -> ball.handleCollision(box), "handleCollision 호출 시 예외 발생");
    }

    @Test
    public void testBoundableInterface() {
        Ball ball = new Ball(100, 100, 20);
        Box box = new Box(200, 200, 50, 40);

        // Boundable은 Collidable의 부모 인터페이스
        assertTrue(ball instanceof Boundable, "Ball이 Boundable을 구현하지 않았습니다");
        assertTrue(box instanceof Boundable, "Box가 Boundable을 구현하지 않았습니다");

        // getBounds 메서드 확인
        Bounds ballBounds = ball.getBounds();
        assertTrue(ballBounds instanceof CircleBounds, "Ball은 CircleBounds를 반환해야 합니다");

        Bounds boxBounds = box.getBounds();
        assertTrue(boxBounds instanceof RectangleBounds, "Box는 RectangleBounds를 반환해야 합니다");
    }
}