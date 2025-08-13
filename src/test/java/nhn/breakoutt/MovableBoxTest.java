package nhn.breakoutt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovableBoxTest {

    @Test
    public void testMovableBoxCreation() {
        MovableBox box = new MovableBox(new Point(100.0, 100.0), 50, 40);

        Point position = box.getPosition();
        assertEquals(100, position.getX(), 0.001, "X 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(100, position.getY(), 0.001, "Y 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(50, box.getWidth(), 0.001, "너비가 올바르게 설정되지 않았습니다");
        assertEquals(40, box.getHeight(), 0.001, "높이가 올바르게 설정되지 않았습니다");
        Vector2D velocity = box.getVelocity();
        assertEquals(0, velocity.getX(), 0.001, "초기 X 속도는 0이어야 합니다");
        assertEquals(0, velocity.getY(), 0.001, "초기 Y 속도는 0이어야 합니다");
    }

    @Test
    public void testMovement() {
        MovableBox box = new MovableBox(new Point(100.0, 100.0), 50, 40);
        box.setVelocity(new Vector2D(50, 30));

        // 1초 동안 이동
        box.move(1.0);

        Point position = box.getPosition();
        assertEquals(150, position.getX(), 0.001, "X 좌표 이동이 올바르지 않습니다");
        assertEquals(130, position.getY(), 0.001, "Y 좌표 이동이 올바르지 않습니다");
    }

    @Test
    public void testVelocityHandling() {
        MovableBox box = new MovableBox(new Point(0.0, 0.0), 50, 40);

        // 속도 설정
        box.setVelocity(new Vector2D(100, -50));

        Vector2D velocity = box.getVelocity();
        assertEquals(100, velocity.getX(), 0.001, "X 속도 설정이 올바르지 않습니다");
        assertEquals(-50, velocity.getY(), 0.001, "Y 속도 설정이 올바르지 않습니다");

        // 작은 시간 간격으로 이동
        box.move(0.1);

        Point position = box.getPosition();
        assertEquals(10, position.getX(), 0.001, "작은 시간 간격 이동이 올바르지 않습니다");
        assertEquals(-5, position.getY(), 0.001, "작은 시간 간격 이동이 올바르지 않습니다");
    }

    @Test
    public void testCodeDuplicationWithMovableBall() {
        // 이 테스트는 MovableBox와 MovableBall의 move 로직이
        // 동일하다는 것을 보여줍니다 (코드 중복 문제)
        MovableBox box = new MovableBox(new Point(0.0, 0.0), 50, 40);
        MovableBall ball = new MovableBall(new Point(0.0, 0.0), 25);

        // 동일한 속도 설정
        box.setVelocity(new Vector2D(60, 40));
        ball.setVelocity(new Vector2D(60, 40));

        // 동일한 시간만큼 이동
        box.move(2.0);
        ball.move(2.0);

        // 이동 결과가 동일해야 함 (move 로직이 동일하기 때문)
        Point boxPos = box.getPosition();
        Point ballCenter = ball.getCenter();
        assertEquals(120, boxPos.getX(), 0.001, "Box 이동 결과");
        assertEquals(80, boxPos.getY(), 0.001, "Box 이동 결과");
        assertEquals(120, ballCenter.getX(), 0.001, "Ball 이동 결과");
        assertEquals(80, ballCenter.getY(), 0.001, "Ball 이동 결과");
    }
}