package nhn.breakoutt;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleWorldTest {

    private SimpleWorld world;
    private static final double WORLD_WIDTH = 800;
    private static final double WORLD_HEIGHT = 600;

    @BeforeEach
    public void setUp() {
        world = new SimpleWorld(WORLD_WIDTH, WORLD_HEIGHT);
    }

    @Test
    public void testWorldCreation() {
        assertEquals(WORLD_WIDTH, world.getWidth(), 0.001);
        assertEquals(WORLD_HEIGHT, world.getHeight(), 0.001);
        assertEquals(0, world.getObjectCount());
        assertEquals(4, world.getBoundaries().size(), "경계가 4개 생성되어야 합니다");
    }

    @Test
    public void testWorldSizeValidation() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleWorld(-100, 100));
        assertThrows(IllegalArgumentException.class, () -> new SimpleWorld(100, -100));
        assertThrows(IllegalArgumentException.class, () -> new SimpleWorld(0, 100));
        assertThrows(IllegalArgumentException.class, () -> new SimpleWorld(100, 0));
    }

    @Test
    public void testAddAndRemoveObjects() {
        Ball ball = new Ball(100, 100, 20);
        Box box = new Box(new Point(200.0, 200.0), 50, 50);

        // 객체 추가
        world.addObject(ball);
        world.addObject(box);
        assertEquals(2, world.getObjectCount());

        // 객체 제거
        world.removeObject(ball);
        assertEquals(1, world.getObjectCount());

        // 모든 객체 제거
        world.clearObjects();
        assertEquals(0, world.getObjectCount());

        // null 객체 추가 시도
        world.addObject(null);
        assertEquals(0, world.getObjectCount(), "null 객체는 추가되지 않아야 합니다");
    }

    @Test
    public void testGetObjectsOfType() {
        Ball ball1 = new Ball(100, 100, 20);
        Ball ball2 = new Ball(200, 200, 30);
        Box box = new Box(new Point(300.0, 300.0), 50, 50);

        world.addObject(ball1);
        world.addObject(ball2);
        world.addObject(box);

        List<Ball> balls = world.getObjectsOfType(Ball.class);
        List<Box> boxes = world.getObjectsOfType(Box.class);
        List<Paintable> paintables = world.getObjectsOfType(Paintable.class);

        assertEquals(2, balls.size(), "Ball 객체가 2개여야 합니다");
        assertEquals(1, boxes.size(), "Box 객체가 1개여야 합니다");
        assertEquals(3, paintables.size(), "Paintable 객체가 3개여야 합니다");
    }

    @Test
    public void testMovableObjectUpdate() {
        Ball movableBall = new Ball(100, 100, 20, 50, 30, Color.RED, CollisionAction.BOUNCE);
        Ball staticBall = new Ball(200, 200, 20); // 속도가 0인 공

        world.addObject(movableBall);
        world.addObject(staticBall);

        double oldX = movableBall.getX();
        double oldY = movableBall.getY();

        world.update(1.0); // 1초 업데이트

        // 움직이는 공은 위치가 변경되어야 함
        assertEquals(oldX + 50, movableBall.getX(), 0.001);
        assertEquals(oldY + 30, movableBall.getY(), 0.001);

        // 정적인 공은 위치가 변경되지 않아야 함
        assertEquals(200, staticBall.getX(), 0.001);
        assertEquals(200, staticBall.getY(), 0.001);
    }

    @Test
    public void testDestroyedObjectRemoval() {
        Ball destroyableBall = new Ball(100, 100, 20, 0, 0, Color.RED, CollisionAction.DESTROY);
        Ball normalBall = new Ball(200, 200, 20);

        world.addObject(destroyableBall);
        world.addObject(normalBall);
        assertEquals(2, world.getObjectCount());

        // 공을 파괴 상태로 설정
        destroyableBall.setDestroyed(true);

        world.update(0.1);

        // 파괴된 공은 제거되어야 함
        assertEquals(1, world.getObjectCount());
        List<Ball> remainingBalls = world.getObjectsOfType(Ball.class);
        assertEquals(1, remainingBalls.size());
        assertSame(normalBall, remainingBalls.get(0));
    }

    @Test
    public void testBoundaryCollision() {
        // 왼쪽 경계에 충돌할 공
        Ball leftBall = new Ball(10, 300, 20, -50, 0, Color.RED, CollisionAction.BOUNCE);
        // 오른쪽 경계에 충돌할 공
        Ball rightBall = new Ball(WORLD_WIDTH - 10, 300, 20, 50, 0, Color.BLUE, CollisionAction.BOUNCE);

        world.addObject(leftBall);
        world.addObject(rightBall);

        world.update(1.0);

        // 경계와 충돌 후 속도가 반전되어야 함
        assertTrue(leftBall.getDx() > 0, "왼쪽 공의 X 속도가 양수로 변경되어야 합니다");
        assertTrue(rightBall.getDx() < 0, "오른쪽 공의 X 속도가 음수로 변경되어야 합니다");
    }

    @Test
    public void testObjectCollision() {
        // 충돌할 두 공 생성
        Ball ball1 = new Ball(100, 100, 20, 50, 0, Color.RED, CollisionAction.BOUNCE);
        Ball ball2 = new Ball(130, 100, 20, -50, 0, Color.BLUE, CollisionAction.BOUNCE);

        world.addObject(ball1);
        world.addObject(ball2);

        double ball1OriginalDx = ball1.getDx();
        double ball2OriginalDx = ball2.getDx();

        world.update(0.1);

        // 충돌 후 속도가 교환되어야 함
        assertEquals(ball2OriginalDx, ball1.getDx(), 0.001, "ball1의 속도가 ball2의 원래 속도와 같아야 합니다");
        assertEquals(ball1OriginalDx, ball2.getDx(), 0.001, "ball2의 속도가 ball1의 원래 속도와 같아야 합니다");
    }

    @Test
    public void testInterfaceBasedOperations() {
        Ball ball = new Ball(100, 100, 20, 10, 15, Color.GREEN, CollisionAction.BOUNCE);
        Box box = new Box(new Point(200.0, 200.0), 50, 50);

        world.addObject(ball);
        world.addObject(box);

        // instanceof를 통한 타입 확인 테스트
        List<Object> objects = world.getObjects();

        int paintableCount = 0;
        int movableCount = 0;
        int collidableCount = 0;

        for (Object obj : objects) {
            if (obj instanceof Paintable) paintableCount++;
            if (obj instanceof Movable) movableCount++;
            if (obj instanceof Collidable) collidableCount++;
        }

        assertEquals(2, paintableCount, "Paintable 객체가 2개여야 합니다");
        assertEquals(1, movableCount, "Movable 객체가 1개여야 합니다 (Ball만)");
        assertEquals(2, collidableCount, "Collidable 객체가 2개여야 합니다");
    }

    @Test
    public void testBoundariesSetup() {
        List<Box> boundaries = world.getBoundaries();
        assertEquals(4, boundaries.size(), "경계가 4개여야 합니다");

        for (Box boundary : boundaries) {
            assertEquals(CollisionAction.BOUNCE, boundary.getCollisionAction(),
                        "모든 경계는 BOUNCE 액션이어야 합니다");
        }

        // 경계들이 화면 밖에 위치하는지 확인
        boolean hasLeftBoundary = false;
        boolean hasRightBoundary = false;
        boolean hasTopBoundary = false;
        boolean hasBottomBoundary = false;

        for (Box boundary : boundaries) {
            Bounds bounds = boundary.getBounds();

            if (bounds.getMaxX() <= 0) hasLeftBoundary = true;
            if (bounds.getMinX() >= WORLD_WIDTH) hasRightBoundary = true;
            if (bounds.getMaxY() <= 0) hasTopBoundary = true;
            if (bounds.getMinY() >= WORLD_HEIGHT) hasBottomBoundary = true;
        }

        assertTrue(hasLeftBoundary, "왼쪽 경계가 있어야 합니다");
        assertTrue(hasRightBoundary, "오른쪽 경계가 있어야 합니다");
        assertTrue(hasTopBoundary, "위쪽 경계가 있어야 합니다");
        assertTrue(hasBottomBoundary, "아래쪽 경계가 있어야 합니다");
    }

    @Test
    public void testToString() {
        Ball ball = new Ball(100, 100, 20);
        Box box = new Box(new Point(200.0, 200.0), 50, 50);

        world.addObject(ball);
        world.addObject(box);

        String worldString = world.toString();
        assertTrue(worldString.contains("SimpleWorld"));
        assertTrue(worldString.contains("800x600"));
        assertTrue(worldString.contains("Objects: 2"));
        assertTrue(worldString.contains("Paintable: 2"));
        assertTrue(worldString.contains("Movable: 1"));
        assertTrue(worldString.contains("Collidable: 2"));
    }

    @Test
    public void testComplexScenario() {
        // 복잡한 시나리오: 여러 공과 박스가 상호작용
        Ball fastBall = new Ball(50, 50, 15, 100, 80, Color.RED, CollisionAction.BOUNCE);
        Ball slowBall = new Ball(150, 150, 25, 20, -30, Color.BLUE, CollisionAction.BOUNCE);
        Ball destroyBall = new Ball(250, 250, 20, 50, 50, Color.YELLOW, CollisionAction.DESTROY);
        Box staticBox = new Box(new Point(300.0, 300.0), 60, 60);

        world.addObject(fastBall);
        world.addObject(slowBall);
        world.addObject(destroyBall);
        world.addObject(staticBox);

        assertEquals(4, world.getObjectCount());

        // 여러 프레임 업데이트
        for (int i = 0; i < 10; i++) {
            world.update(0.1);
        }

        // 객체들이 여전히 존재하는지 확인 (경계 밖으로 나가지 않았다면)
        assertTrue(world.getObjectCount() >= 3, "최소 3개 객체가 남아있어야 합니다 (staticBox 포함)");

        // 모든 남은 Ball들이 경계 안에 있는지 확인
        List<Ball> remainingBalls = world.getObjectsOfType(Ball.class);
        for (Ball ball : remainingBalls) {
            assertTrue(ball.getX() >= 0 && ball.getX() <= WORLD_WIDTH,
                      "공이 가로 경계 안에 있어야 합니다");
            assertTrue(ball.getY() >= 0 && ball.getY() <= WORLD_HEIGHT,
                      "공이 세로 경계 안에 있어야 합니다");
        }
    }
}
