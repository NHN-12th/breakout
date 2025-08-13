package nhn.breakoutt;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InheritanceProblemTest {

    @Test
    public void testCodeDuplication() {
        // MovableBall과 MovableBox의 move 메서드가 동일한지 확인
        MovableBall ball = new MovableBall(new Point(100.0, 100.0), 20);
        MovableBox box = new MovableBox(new Point(200.0, 200.0), 40, 40);

        ball.setVelocity(new Vector2D(50, 50));
        box.setVelocity(new Vector2D(50, 50));

        ball.move(1.0);
        box.move(1.0);

        // 동일한 이동을 했지만 코드는 중복됨
        assertEquals(150, ball.getCenter().getX(), 0.001);
        assertEquals(250, box.getPosition().getX(), 0.001);
    }

    @Test
    public void testTypeCheckingComplexity() {
        List<Object> objects = new ArrayList<>();
        objects.add(new Ball(new Point(0.0, 0.0), 10));
        objects.add(new MovableBall(new Point(0.0, 0.0), 10));
        objects.add(new Box(new Point(0.0, 0.0), 20, 20));
        objects.add(new MovableBox(new Point(0.0, 0.0), 20, 20));

        int movableCount = 0;
        for (Object obj : objects) {
            // 각 타입을 개별적으로 체크해야 함
            if (obj instanceof MovableBall || obj instanceof MovableBox) {
                movableCount++;
            }
        }

        assertEquals(2, movableCount);
    }

    @Test
    public void testCollisionComplexity() {
        Ball ball = new Ball(new Point(50.0, 50.0), 20);
        Box box = new Box(new Point(60.0, 60.0), 40, 40);

        // Ball과 Box의 충돌을 처리하는 별도 로직 필요
        boolean collision = checkBallBoxCollision(ball, box);
        assertTrue(collision);
    }

    private boolean checkBallBoxCollision(Ball ball, Box box) {
        // Box를 Bounds로 변환하여 검사
        Point boxPos = box.getPosition();
        Bounds boxBounds = new RectangleBounds(
                boxPos.getX(), boxPos.getY(), box.getWidth(), box.getHeight()
        );

        Point ballCenter = ball.getCenter();
        double closestX = Math.max(boxBounds.getMinX(),
                Math.min(ballCenter.getX(), boxBounds.getMaxX()));
        double closestY = Math.max(boxBounds.getMinY(),
                Math.min(ballCenter.getY(), boxBounds.getMaxY()));

        double distanceX = ballCenter.getX() - closestX;
        double distanceY = ballCenter.getY() - closestY;
        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        return distanceSquared < (ball.getRadius() * ball.getRadius());
    }
}