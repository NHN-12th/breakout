package nhn.breakoutt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShapeTest {

    @Test
    public void testCircleShapeCreation() {
        Circle circle = new Circle(new Point(100.0, 100.0), 30);

        Point position = circle.getPosition();
        assertEquals(100, position.getX(), 0.001, "Circle X 좌표가 잘못되었습니다");
        assertEquals(100, position.getY(), 0.001, "Circle Y 좌표가 잘못되었습니다");
        assertEquals(30, circle.getRadius(), 0.001, "Circle 반지름이 잘못되었습니다");

        // Shape로 타입 캐스팅 가능한지 확인
        Shape shape = circle;
        assertEquals("Circle", shape.getShapeType(), "Circle 타입이 올바르지 않습니다");
    }

    @Test
    public void testRectangleShapeCreation() {
        Shape rectangle = new Rectangle(new Point(50.0, 75.0), 100, 80);

        Point position = rectangle.getPosition();
        assertEquals(50, position.getX(), 0.001, "Rectangle X 좌표가 잘못되었습니다");
        assertEquals(75, position.getY(), 0.001, "Rectangle Y 좌표가 잘못되었습니다");
        assertEquals(100, rectangle.getWidth(), 0.001, "Rectangle 너비가 잘못되었습니다");
        assertEquals(80, rectangle.getHeight(), 0.001, "Rectangle 높이가 잘못되었습니다");

        assertEquals("Rectangle", rectangle.getShapeType(), "Rectangle 타입이 올바르지 않습니다");
    }

    @Test
    public void testCircleArea() {
        Circle circle = new Circle(new Point(0.0, 0.0), 10);
        double expectedArea = Math.PI * 10 * 10;

        assertEquals(expectedArea, circle.getArea(), 0.001, "Circle 면적 계산이 잘못되었습니다");
    }

    @Test
    public void testRectangleArea() {
        Rectangle rectangle = new Rectangle(new Point(0.0, 0.0), 20, 15);
        double expectedArea = 20 * 15;

        assertEquals(expectedArea, rectangle.getArea(), 0.001, "Rectangle 면적 계산이 잘못되었습니다");
    }

    @Test
    public void testCirclePerimeter() {
        Circle circle = new Circle(new Point(0.0, 0.0), 10);
        double expectedPerimeter = 2 * Math.PI * 10;

        assertEquals(expectedPerimeter, circle.getPerimeter(), 0.001, "Circle 둘레 계산이 잘못되었습니다");
    }

    @Test
    public void testRectanglePerimeter() {
        Rectangle rectangle = new Rectangle(new Point(0.0, 0.0), 20, 15);
        double expectedPerimeter = 2 * (20 + 15);

        assertEquals(expectedPerimeter, rectangle.getPerimeter(), 0.001, "Rectangle 둘레 계산이 잘못되었습니다");
    }

    @Test
    public void testAbstractShapeCannotBeInstantiated() {
        // 추상 클래스는 직접 인스턴스화할 수 없음을 확인
        // 이는 컴파일 타임에 확인되므로 런타임 테스트로는 검증하기 어려움
        // 대신 하위 클래스가 올바르게 추상 메서드를 구현하는지 확인

        Circle circle = new Circle(new Point(0.0, 0.0), 10);
        assertTrue(circle instanceof Shape, "Circle은 Shape의 인스턴스여야 합니다");

        Rectangle rectangle = new Rectangle(new Point(0.0, 0.0), 10, 10);
        assertTrue(rectangle instanceof Shape, "Rectangle은 Shape의 인스턴스여야 합니다");
    }

    @Test
    public void testPolymorphism() {
        Shape[] shapes = {
                new Circle(0, 0, 10),
                new Rectangle(0, 0, 20, 15)
        };

        // 다형성을 통해 다양한 타입의 도형을 동일하게 처리
        for (Shape shape : shapes) {
            assertTrue(shape.getArea() > 0, "모든 도형의 면적은 양수여야 합니다");
            assertTrue(shape.getPerimeter() > 0, "모든 도형의 둘레는 양수여야 합니다");
            assertNotNull(shape.getShapeType(), "도형 타입이 null이면 안됩니다");
        }
    }
}