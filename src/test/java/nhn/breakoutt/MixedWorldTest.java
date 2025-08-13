package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class MixedWorldTest {

    private MixedWorld world;

    @BeforeEach
    public void setUp() {
        world = new MixedWorld(800, 600);
    }

    @Test
    public void testWorldCreation() {
        assertEquals(800, world.getWidth(), "World의 너비가 올바르게 설정되지 않았습니다");
        assertEquals(600, world.getHeight(), "World의 높이가 올바르게 설정되지 않았습니다");
        assertEquals(0, world.getBallCount(), "초기 Ball 수는 0이어야 합니다");
        assertEquals(0, world.getBoxCount(), "초기 Box 수는 0이어야 합니다");
    }

    @Test
    public void testAddMixedObjects() {
        Ball ball = new Ball(new Point(100.0, 100.0), 20);
        PaintableBall paintableBall = new PaintableBall(new Point(200.0, 200.0), 25, Color.BLUE);
        Box box = new Box(new Point(300.0, 300.0), 40, 50);
        PaintableBox paintableBox = new PaintableBox(new Point(400.0, 400.0), 30, 35, Color.GREEN);

        world.addBall(ball);
        world.addBall(paintableBall);
        world.addBox(box);
        world.addBox(paintableBox);

        assertEquals(2, world.getBallCount(), "Ball이 올바르게 추가되지 않았습니다");
        assertEquals(2, world.getBoxCount(), "Box가 올바르게 추가되지 않았습니다");
    }

    @Test
    public void testTypeCheckingComplexity() {
        // 다양한 타입의 객체들 추가
        world.addBall(new Ball(new Point(0.0, 0.0), 10));
        world.addBall(new PaintableBall(new Point(0.0, 0.0), 10, Color.RED));
        world.addBall(new MovableBall(new Point(0.0, 0.0), 10));
        world.addBall(new PaintableMovableBall(new Point(0.0, 0.0), 10, Color.BLUE));
        world.addBox(new Box(new Point(0.0, 0.0), 20, 20));
        world.addBox(new PaintableBox(new Point(0.0, 0.0), 20, 20, Color.GREEN));
        world.addBox(new MovableBox(new Point(0.0, 0.0), 20, 20));
        world.addBox(new PaintableMovableBox(new Point(0.0, 0.0), 20, 20, Color.YELLOW));

        // update 메서드에서 각 타입별로 instanceof 체크가 필요함
        world.update(1.0);

        // 이 테스트는 컴파일 오류 없이 실행되지만,
        // 실제 구현에서는 수많은 instanceof 체크가 필요함을 보여줍니다
        assertEquals(4, world.getBallCount(), "Ball 타입 객체들이 관리되고 있습니다");
        assertEquals(4, world.getBoxCount(), "Box 타입 객체들이 관리되고 있습니다");
    }

    @Test
    public void testRenderComplexity() {
        GraphicsContext gc = Mockito.mock(GraphicsContext.class);

        // 다양한 타입의 객체들 추가
        world.addBall(new PaintableBall(new Point(100.0, 100.0), 20, Color.RED));
        world.addBall(new MovableBall(new Point(200.0, 200.0), 25));
        world.addBox(new PaintableBox(new Point(300.0, 300.0), 40, 50, Color.BLUE));
        world.addBox(new MovableBox(new Point(400.0, 400.0), 30, 35));

        // 렌더링 수행 (각 타입별로 다른 처리 필요)
        assertDoesNotThrow(() -> {
            world.render(gc);
        }, "MixedWorld 렌더링 중 예외가 발생했습니다");

        // 실제로는 render 메서드 내부에서 다음과 같은 복잡한 타입 체크가 필요:
        // if (ball instanceof PaintableBall) { ... }
        // else if (ball instanceof MovableBall) { ... }
        // if (box instanceof PaintableBox) { ... }
        // else if (box instanceof MovableBox) { ... }
    }

    @Test
    public void testUpdateComplexity() {
        MovableBall movableBall = new MovableBall(new Point(100.0, 100.0), 20);
        movableBall.setVelocity(new Vector2D(50, 30));

        MovableBox movableBox = new MovableBox(new Point(200.0, 200.0), 40, 50);
        movableBox.setVelocity(new Vector2D(25, 35));

        world.addBall(movableBall);
        world.addBox(movableBox);

        // 업데이트 전 위치
        Point ballPos = movableBall.getCenter();
        double ballX = ballPos.getX();
        double ballY = ballPos.getY();
        Point boxPos = movableBox.getPosition();
        double boxX = boxPos.getX();
        double boxY = boxPos.getY();

        // 1초 업데이트
        world.update(1.0);

        // 움직이는 객체들만 이동했는지 확인
        // (update 메서드에서 instanceof MovableBall, instanceof MovableBox 체크 필요)
        Point newBallPos = movableBall.getCenter();
        Point newBoxPos = movableBox.getPosition();
        assertNotEquals(ballX, newBallPos.getX(), "MovableBall이 이동하지 않았습니다");
        assertNotEquals(ballY, newBallPos.getY(), "MovableBall이 이동하지 않았습니다");
        assertNotEquals(boxX, newBoxPos.getX(), "MovableBox가 이동하지 않았습니다");
        assertNotEquals(boxY, newBoxPos.getY(), "MovableBox가 이동하지 않았습니다");
    }
}