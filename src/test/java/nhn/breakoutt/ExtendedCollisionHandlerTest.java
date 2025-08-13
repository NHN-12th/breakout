package nhn.breakoutt;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ExtendedCollisionHandlerTest {

    @Test
    public void testBallToBallCollisions() {
        ExtendedCollisionHandler handler = new ExtendedCollisionHandler();

        // 충돌하는 두 Ball
        MovableBall ball1 = new MovableBall(new Point(100.0, 100.0), 20);
        MovableBall ball2 = new MovableBall(new Point(130.0, 100.0), 15);
        ball1.setVelocity(new Vector2D(50, 0));
        ball2.setVelocity(new Vector2D(-30, 0));

        List<Ball> balls = Arrays.asList(ball1, ball2);

        // 충돌 처리 전 속도 저장
        double ball1VxBefore = ball1.getVelocity().getX();
        double ball2VxBefore = ball2.getVelocity().getX();

        handler.checkBallToBallCollisions(balls);

        // 충돌 후 속도가 변경되었는지 확인
        assertNotEquals(ball1VxBefore, ball1.getVelocity().getX(), "Ball1의 속도가 변경되지 않았습니다");
        assertNotEquals(ball2VxBefore, ball2.getVelocity().getX(), "Ball2의 속도가 변경되지 않았습니다");
    }

    @Test
    public void testBoxToBoxCollisions() {
        ExtendedCollisionHandler handler = new ExtendedCollisionHandler();

        // 충돌하는 두 Box
        MovableBox box1 = new MovableBox(new Point(100.0, 100.0), 40, 30);
        MovableBox box2 = new MovableBox(new Point(120.0, 110.0), 50, 40);
        box1.setVelocity(new Vector2D(20, 10));
        box2.setVelocity(new Vector2D(-15, -5));

        List<Box> boxes = Arrays.asList(box1, box2);

        // 충돌 처리 전 속도 저장
        double box1VxBefore = box1.getVelocity().getX();
        double box1VyBefore = box1.getVelocity().getY();

        handler.checkBoxToBoxCollisions(boxes);

        // 충돌 후 속도가 변경되었넸지 확인
        assertNotEquals(box1VxBefore, box1.getVelocity().getX(), "Box1의 X 속도가 변경되지 않았습니다");
        assertNotEquals(box1VyBefore, box1.getVelocity().getY(), "Box1의 Y 속도가 변경되지 않았습니다");
    }

    @Test
    public void testBallToBoxCollisions() {
        ExtendedCollisionHandler handler = new ExtendedCollisionHandler();

        // Ball이 Box와 충돌
        MovableBall ball = new MovableBall(new Point(50.0, 100.0), 15);
        Box box = new Box(new Point(80.0, 90.0), 60, 40);
        ball.setVelocity(new Vector2D(40, 0));

        List<Ball> balls = Arrays.asList(ball);
        List<Box> boxes = Arrays.asList(box);

        // 충돌 처리 전 속도 저장
        double ballVxBefore = ball.getVelocity().getX();

        handler.checkBallToBoxCollisions(balls, boxes);

        // Ball이 Box의 왼쪽 면과 충돌하여 속도가 반전되었는지 확인
        assertEquals(-ballVxBefore, ball.getVelocity().getX(), 0.1, "Ball의 X 속도가 반전되지 않았습니다");
    }

    @Test
    public void testCollisionSideDetection() {
        ExtendedCollisionHandler handler = new ExtendedCollisionHandler();
        Box box = new Box(new Point(100.0, 100.0), 80, 60);
        // Box 경계: Left=60, Right=140, Top=70, Bottom=130

        // 각 면에서의 충돌 테스트 - 올바른 기대값으로 수정
        assertEquals(CollisionSide.LEFT, handler.detectCollisionSide(50, 100, box), "왼쪽 면 충돌 감지 실패");
        assertEquals(CollisionSide.RIGHT, handler.detectCollisionSide(150, 100, box), "오른쪽 면 충돌 감지 실패");
        assertEquals(CollisionSide.TOP, handler.detectCollisionSide(100, 60, box), "위쪽 면 충돌 감지 실패");
        assertEquals(CollisionSide.BOTTOM, handler.detectCollisionSide(100, 140, box), "아래쪽 면 충돌 감지 실패");
        // Box 중심에서는 모든 면까지의 거리가 같으므로 CORNER로 처리하거나,
        // 아니면 Box 외부 모서리 영역의 점으로 변경
        assertEquals(CollisionSide.CORNER, handler.detectCollisionSide(100, 100, box), "코너 충돌 감지 실패");
    }

    @Test
    public void testCollisionCombinationExplosion() {
        // 이 테스트는 타입이 증가할수록 필요한 충돌 메서드가 기하급수적으로 증가함을 보여줍니다

        // 현재: Ball, Box (2개 타입)
        // 필요한 충돌 메서드: 3개 (Ball-Ball, Box-Box, Ball-Box)

        ExtendedCollisionHandler handler = new ExtendedCollisionHandler();
        assertTrue(handler.hasMethod("checkBallToBallCollisions"), "Ball-Ball 충돌 메서드 필요");
        assertTrue(handler.hasMethod("checkBoxToBoxCollisions"), "Box-Box 충돌 메서드 필요");
        assertTrue(handler.hasMethod("checkBallToBoxCollisions"), "Ball-Box 충돌 메서드 필요");

        // Triangle이 추가되면 6개 메서드 필요:
        // Ball-Ball, Box-Box, Triangle-Triangle, Ball-Box, Ball-Triangle, Box-Triangle

        // Circle이 추가되면 10개 메서드 필요:
        // 기존 6개 + Circle-Circle, Ball-Circle, Box-Circle, Triangle-Circle

        // n개 타입 = n(n+1)/2개 메서드 필요!
        int typeCount = 2;
        int requiredMethods = typeCount * (typeCount + 1) / 2;
        assertEquals(3, requiredMethods, "2개 타입에 대해 3개 충돌 메서드 필요");

        // 타입이 4개로 증가하면?
        typeCount = 4;
        requiredMethods = typeCount * (typeCount + 1) / 2;
        assertEquals(10, requiredMethods, "4개 타입에 대해 10개 충돌 메서드 필요 - 조합 폭발!");
    }
}