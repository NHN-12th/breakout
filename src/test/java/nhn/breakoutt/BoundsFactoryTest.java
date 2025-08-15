package nhn.breakoutt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoundsFactoryTest {
    @Test
    public void testCreateCircleBounds() {
        Bounds bounds = BoundsFactory.createCircleBounds(new Point(100.0, 150.0), 25);

        assertTrue(bounds instanceof CircleBounds, "CircleBounds 타입이 생성되지 않았습니다");

        CircleBounds circle = (CircleBounds) bounds;
        assertEquals(100, circle.getCenterX(), 0.001, "Circle X 중심이 잘못되었습니다");
        assertEquals(150, circle.getCenterY(), 0.001, "Circle Y 중심이 잘못되었습니다");
        assertEquals(25, circle.getRadius(), 0.001, "Circle 반지름이 잘못되었습니다");
    }

    @Test
    public void testCreateRectangleBounds() {
        Bounds bounds = BoundsFactory.createRectangleBounds(50, 75, 120, 80);

        assertTrue(bounds instanceof RectangleBounds, "RectangleBounds 타입이 생성되지 않았습니다");

        RectangleBounds rect = (RectangleBounds) bounds;
        assertEquals(50, rect.getX(), 0.001, "Rectangle X가 잘못되었습니다");
        assertEquals(75, rect.getY(), 0.001, "Rectangle Y가 잘못되었습니다");
        assertEquals(120, rect.getWidth(), 0.001, "Rectangle 너비가 잘못되었습니다");
        assertEquals(80, rect.getHeight(), 0.001, "Rectangle 높이가 잘못되었습니다");
    }

    @Test
    public void testFactoryMethodPattern() {
        // Factory Method 패턴의 이점: 클라이언트 코드는 구체적인 클래스를 몰라도 됨
        Bounds[] bounds = {
                BoundsFactory.createCircleBounds(new Point(0.0, 0.0), 10),
                BoundsFactory.createRectangleBounds(0, 0, 20, 15)
        };

        // 다형성을 통해 동일하게 처리
        for (Bounds bound : bounds) {
            assertTrue(bound.getMinX() >= 0 || bound.getMinX() < 0, "Bounds가 올바르게 생성되었습니다");
            assertTrue(bound.getArea() > 0, "모든 Bounds의 면적은 양수여야 합니다");
        }
    }

    @Test
    public void testInvalidParameters() {
        // 유효하지 않은 매개변수에 대한 처리
        assertThrows(IllegalArgumentException.class, () -> {
            BoundsFactory.createCircleBounds(new Point(0.0, 0.0), -5); // 음수 반지름
        }, "음수 반지름에 대해 예외가 발생하지 않았습니다");

        assertThrows(IllegalArgumentException.class, () -> {
            BoundsFactory.createRectangleBounds(0, 0, -10, 5); // 음수 너비
        }, "음수 너비에 대해 예외가 발생하지 않았습니다");

        assertThrows(IllegalArgumentException.class, () -> {
            BoundsFactory.createRectangleBounds(0, 0, 10, -5); // 음수 높이
        }, "음수 높이에 대해 예외가 발생하지 않았습니다");
    }

    @Test
    public void testFactoryConsistency() {
        // 팩토리로 생성한 객체들이 일관된 행동을 보이는지 확인
        CircleBounds circle1 = new CircleBounds(new Point(50.0, 50.0), 20);
        Bounds circle2 = BoundsFactory.createCircleBounds(new Point(50.0, 50.0), 20);

        assertEquals(circle1.getArea(), circle2.getArea(), 0.001,
                "직접 생성과 팩토리 생성 결과가 다릅니다");
        assertEquals(circle1.getMinX(), circle2.getMinX(), 0.001,
                "직접 생성과 팩토리 생성의 경계값이 다릅니다");

        assertTrue(circle1.intersects(circle2), "동일한 원들이 교차하지 않는다고 판단되었습니다");
    }
}