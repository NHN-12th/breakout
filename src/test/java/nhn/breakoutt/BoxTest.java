package nhn.breakoutt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoxTest {

    @Test
    public void testBoxCreation() {
        Box box = new Box(new Point(100.0, 200.0), 50, 30);
        Point position = box.getPosition();
        assertEquals(100, position.getX(), 0.001, "X 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(200, position.getY(), 0.001, "Y 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(50, box.getWidth(), 0.001, "너비가 올바르게 설정되지 않았습니다");
        assertEquals(30, box.getHeight(), 0.001, "높이가 올바르게 설정되지 않았습니다");
    }

    @Test
    public void testBoxPosition() {
        Box box = new Box(new Point(50.0, 75.0), 40, 60);

        // setter 테스트
        box.setPosition(new Point(150.0, 175.0));
        box.setWidth(80);
        box.setHeight(120);

        Point position = box.getPosition();
        assertEquals(150, position.getX(), 0.001, "setPosition X가 올바르게 작동하지 않습니다");
        assertEquals(175, position.getY(), 0.001, "setPosition Y가 올바르게 작동하지 않습니다");
        assertEquals(80, box.getWidth(), 0.001, "setWidth가 올바르게 작동하지 않습니다");
        assertEquals(120, box.getHeight(), 0.001, "setHeight가 올바르게 작동하지 않습니다");
    }

    @Test
    public void testSizeValidation() {
        // 유효하지 않은 크기 테스트
        assertThrows(IllegalArgumentException.class, () -> {
            new Box(new Point(0.0, 0.0), -10, 20); // 음수 너비는 예외 발생해야 함
        }, "음수 너비에 대해 예외가 발생하지 않았습니다");

        assertThrows(IllegalArgumentException.class, () -> {
            new Box(new Point(0.0, 0.0), 20, -10); // 음수 높이는 예외 발생해야 함
        }, "음수 높이에 대해 예외가 발생하지 않았습니다");

        assertThrows(IllegalArgumentException.class, () -> {
            new Box(new Point(0.0, 0.0), 0, 10); // 0 너비도 예외 발생해야 함
        }, "0 너비에 대해 예외가 발생하지 않았습니다");

        assertThrows(IllegalArgumentException.class, () -> {
            new Box(new Point(0.0, 0.0), 10, 0); // 0 높이도 예외 발생해야 함
        }, "0 높이에 대해 예외가 발생하지 않았습니다");
    }

    @Test
    public void testContains() {
        Box box = new Box(new Point(100.0, 100.0), 80, 60);

        // Box 내부의 점들
        assertTrue(box.contains(new Point(100.0, 100.0)), "왼쪽 상단 모서리가 포함되지 않았습니다");
        assertTrue(box.contains(new Point(140.0, 130.0)), "Box 내부 점이 포함되지 않았습니다");
        assertTrue(box.contains(new Point(180.0, 160.0)), "오른쪽 하단 모서리가 포함되지 않았습니다");
        assertTrue(box.contains(new Point(180.0, 130.0)), "오른쪽 경계선 위의 점이 포함되지 않았습니다");
        assertTrue(box.contains(new Point(140.0, 160.0)), "아래쪽 경계선 위의 점이 포함되지 않았습니다");

        // Box 외부의 점들
        assertFalse(box.contains(new Point(99.0, 100.0)), "왼쪽 경계 밖 점이 포함되었습니다");
        assertFalse(box.contains(new Point(100.0, 99.0)), "위쪽 경계 밖 점이 포함되었습니다");
        assertFalse(box.contains(new Point(181.0, 130.0)), "오른쪽 경계 밖 점이 포함되었습니다");
        assertFalse(box.contains(new Point(140.0, 161.0)), "아래쪽 경계 밖 점이 포함되었습니다");
        assertFalse(box.contains(new Point(50.0, 50.0)), "Box 완전 외부 점이 포함되었습니다");
        assertFalse(box.contains(new Point(200.0, 200.0)), "Box 완전 외부 점이 포함되었습니다");
    }

    @Test
    public void testGetBounds() {
        Box box = new Box(new Point(50.0, 75.0), 100, 80);
        Bounds bounds = box.getBounds();

        assertEquals(50, bounds.getMinX(), 0.001, "경계의 최소 X가 올바르지 않습니다");
        assertEquals(75, bounds.getMinY(), 0.001, "경계의 최소 Y가 올바르지 않습니다");
        assertEquals(150, bounds.getMaxX(), 0.001, "경계의 최대 X가 올바르지 않습니다");
        assertEquals(155, bounds.getMaxY(), 0.001, "경계의 최대 Y가 올바르지 않습니다");
    }
}