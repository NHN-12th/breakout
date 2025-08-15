package nhn.breakoutt;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CollisionActionTest {

    @Test
    public void testBounceAction() {
        // Ball: 중심(50, 100), 반지름 15 -> 영역: 35-65, 85-115
        Ball ball = new Ball(50, 100, 15, Color.RED);
        ball.setDx(100);
        ball.setDy(50);
        ball.setCollisionAction(CollisionAction.BOUNCE);

        // Box: 시작점(60, 95), 크기 20x40 -> 영역: 60-80, 95-135
        // Ball과 Box가 겹치는 영역: 60-65, 95-115
        Box wall = new Box(60, 95, 20, 40, Color.GRAY);
        wall.setCollisionAction(CollisionAction.BOUNCE);

        // 충돌 전 속도 저장
        double oldDx = ball.getDx();
        double oldDy = ball.getDy();

        // 충돌 처리
        assertTrue(ball.isColliding(wall), "Ball과 Wall이 충돌해야 합니다");
        ball.handleCollision(wall);

        // BOUNCE 액션: 속도 반전
        assertNotEquals(oldDx, ball.getDx(), "BOUNCE 액션 후 X 속도가 변경되어야 합니다");
        // Y 속도는 수직 충돌이 아니면 변경되지 않을 수 있음
    }

    @Test
    public void testStopAction() {
        // Ball: 중심(100, 100), 반지름 20 -> 영역: 80-120, 80-120
        Ball ball = new Ball(100, 100, 20, Color.BLUE);
        ball.setDx(80);
        ball.setDy(60);
        ball.setCollisionAction(CollisionAction.STOP);

        // Box: 시작점(110, 110), 크기 30x30 -> 영역: 110-140, 110-140
        // Ball과 Box가 겹치는 영역: 110-120, 110-120
        Box obstacle = new Box(110, 110, 30, 30, Color.BROWN);

        // 충돌 처리
        assertTrue(ball.isColliding(obstacle), "Ball과 Obstacle이 충돌해야 합니다");
        ball.handleCollision(obstacle);

        // STOP 액션: 속도 0
        assertEquals(0, ball.getDx(), 0.001, "STOP 액션 후 X 속도가 0이어야 합니다");
        assertEquals(0, ball.getDy(), 0.001, "STOP 액션 후 Y 속도가 0이어야 합니다");
    }

    @Test
    public void testDestroyAction() {
        // Ball: 중심(100, 100), 반지름 20 -> 영역: 80-120, 80-120
        Ball ball = new Ball(100, 100, 20, Color.GREEN);
        ball.setCollisionAction(CollisionAction.DESTROY);

        // Box: 시작점(105, 105), 크기 40x40 -> 영역: 105-145, 105-145
        // Ball과 Box가 겹치는 영역: 105-120, 105-120
        Box destroyer = new Box(105, 105, 40, 40, Color.BLACK);

        // 충돌 처리
        assertTrue(ball.isColliding(destroyer), "Ball과 Destroyer가 충돌해야 합니다");
        ball.handleCollision(destroyer);

        // DESTROY 액션: 객체 제거 표시
        assertTrue(ball.isDestroyed(), "DESTROY 액션 후 객체가 제거 표시되어야 합니다");
    }

    @Test
    public void testPassAction() {
        // Ball: 중심(100, 100), 반지름 20 -> 영역: 80-120, 80-120
        Ball ball = new Ball(100, 100, 20, Color.YELLOW);
        ball.setDx(50);
        ball.setDy(30);
        ball.setCollisionAction(CollisionAction.PASS);

        // Box: 시작점(110, 110), 크기 30x30 -> 영역: 110-140, 110-140
        // Ball과 Box가 겹치는 영역: 110-120, 110-120
        Box passThrough = new Box(110, 110, 30, 30, Color.CYAN);

        // 충돌 전 속도 저장
        double oldDx = ball.getDx();
        double oldDy = ball.getDy();

        // 충돌 처리
        assertTrue(ball.isColliding(passThrough), "Ball과 PassThrough가 충돌해야 합니다");
        ball.handleCollision(passThrough);

        // PASS 액션: 속도 변화 없음
        assertEquals(oldDx, ball.getDx(), 0.001, "PASS 액션 후 X 속도가 변경되지 않아야 합니다");
        assertEquals(oldDy, ball.getDy(), 0.001, "PASS 액션 후 Y 속도가 변경되지 않아야 합니다");
    }

    @Test
    public void testCustomAction() {
        // ExplodingBall: 중심(100, 100), 반지름 25 -> 영역: 75-125, 75-125
        ExplodingBall explodingBall = new ExplodingBall(100, 100, 25, Color.ORANGE);
        explodingBall.setCollisionAction(CollisionAction.CUSTOM);

        // Box: 시작점(115, 115), 크기 20x20 -> 영역: 115-135, 115-135
        // ExplodingBall과 Box가 겹치는 영역: 115-125, 115-125
        Box trigger = new Box(115, 115, 20, 20, Color.RED);

        // 충돌 처리
        assertTrue(explodingBall.isColliding(trigger), "ExplodingBall과 Trigger가 충돌해야 합니다");
        explodingBall.handleCollision(trigger);

        // CUSTOM 액션: 폭발 (구현에 따라 다름)
        assertTrue(explodingBall.hasExploded(), "CUSTOM 액션 후 ExplodingBall이 폭발해야 합니다");
        assertTrue(explodingBall.isDestroyed(), "폭발 후 원본 객체는 제거되어야 합니다");
    }

    @Test
    public void testDynamicActionChange() {
        Ball ball = new Ball(100, 100, 20, Color.MAGENTA);

        // 런타임에 액션 변경 가능
        ball.setCollisionAction(CollisionAction.BOUNCE);
        assertEquals(CollisionAction.BOUNCE, ball.getCollisionAction(), "액션이 BOUNCE로 설정되지 않았습니다");

        ball.setCollisionAction(CollisionAction.STOP);
        assertEquals(CollisionAction.STOP, ball.getCollisionAction(), "액션이 STOP으로 변경되지 않았습니다");

        ball.setCollisionAction(CollisionAction.DESTROY);
        assertEquals(CollisionAction.DESTROY, ball.getCollisionAction(), "액션이 DESTROY로 변경되지 않았습니다");

        // 6장에서는 이런 유연성이 불가능했음!
        System.out.println("런타임 액션 변경 성공: 인터페이스의 유연성!");
    }
}