package nhn.breakoutt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Lab 5-1: Bounds 시스템 구현 테스트
 */
public class BoundsSystemTest {

    @BeforeEach
    public void setUp() {
        // 필드 변수들을 지역 변수로 변경하여 경고 제거
    }

    @Test
    public void testBoundsIntersection() {
        // 교차하지 않는 경우
        Bounds rect = new RectangleBounds(0, 0, 100, 100);
        Bounds circle = new CircleBounds(new Point(150.0, 50.0), 30);

        assertFalse(rect.intersects(circle), "멀리 떨어진 경계들은 교차하지 않아야 합니다");

        // 교차하는 경우
        circle = new CircleBounds(new Point(80.0, 50.0), 30);
        assertTrue(rect.intersects(circle), "겹치는 경계들은 교차해야 합니다");
    }

    @Test
    public void testRectangleBounds() {
        RectangleBounds rect = new RectangleBounds(10, 20, 50, 30);

        assertEquals(10, rect.getMinX(), "최소 X 좌표가 올바르지 않습니다");
        assertEquals(20, rect.getMinY(), "최소 Y 좌표가 올바르지 않습니다");
        assertEquals(60, rect.getMaxX(), "최대 X 좌표가 올바르지 않습니다");
        assertEquals(50, rect.getMaxY(), "최대 Y 좌표가 올바르지 않습니다");
        assertEquals(50, rect.getWidth(), "너비가 올바르지 않습니다");
        assertEquals(30, rect.getHeight(), "높이가 올바르지 않습니다");

        // 점 포함 테스트
        assertTrue(rect.contains(new Point(35.0, 35.0)), "경계 내부의 점을 포함해야 합니다");
        assertFalse(rect.contains(new Point(5.0, 5.0)), "경계 외부의 점을 포함하지 않아야 합니다");

        // 유효성 검사 테스트
        assertThrows(IllegalArgumentException.class, () -> new RectangleBounds(0, 0, -10, 10),
                "음수 너비에 대해 예외가 발생해야 합니다");
    }

    @Test
    public void testCircleBounds() {
        CircleBounds circle = new CircleBounds(50, 50, 25);

        assertEquals(25, circle.getMinX(), "최소 X 좌표가 올바르지 않습니다");
        assertEquals(25, circle.getMinY(), "최소 Y 좌표가 올바르지 않습니다");
        assertEquals(75, circle.getMaxX(), "최대 X 좌표가 올바르지 않습니다");
        assertEquals(75, circle.getMaxY(), "최대 Y 좌표가 올바르지 않습니다");
        assertEquals(50, circle.getWidth(), "너비가 올바르지 않습니다");
        assertEquals(50, circle.getHeight(), "높이가 올바르지 않습니다");

        // 원형 영역 포함 테스트
        assertTrue(circle.containsInCircle(new Point(50.0, 50.0)), "중심점을 포함해야 합니다");
        assertTrue(circle.containsInCircle(new Point(65.0, 50.0)), "반지름 내부의 점을 포함해야 합니다");
        assertFalse(circle.containsInCircle(new Point(80.0, 50.0)), "반지름 외부의 점을 포함하지 않아야 합니다");

        // 원과 원 교차 테스트
        CircleBounds other = new CircleBounds(70, 50, 25);
        assertTrue(circle.intersectsWithCircle(other), "겹치는 원들은 교차해야 합니다");

        other = new CircleBounds(110, 50, 25);
        assertFalse(circle.intersectsWithCircle(other), "멀리 떨어진 원들은 교차하지 않아야 합니다");
    }

    @Test
    public void testCompositeBounds() {
        CompositeBounds composite = new CompositeBounds();
        assertTrue(composite.isEmptyBounds(), "빈 복합 경계는 비어있어야 합니다");

        // 경계 추가
        RectangleBounds rect1 = new RectangleBounds(0, 0, 50, 50);
        RectangleBounds rect2 = new RectangleBounds(100, 100, 50, 50);
        CircleBounds circle = new CircleBounds(200, 200, 25);

        composite.addBounds(rect1).addBounds(rect2).addBounds(circle);

        assertEquals(3, composite.getBoundsCount(), "3개의 경계가 추가되어야 합니다");
        assertFalse(composite.isEmptyBounds(), "경계가 추가된 후에는 비어있지 않아야 합니다");

        // 전체 경계 확인
        assertEquals(0, composite.getMinX(), "최소 X 좌표가 올바르지 않습니다");
        assertEquals(0, composite.getMinY(), "최소 Y 좌표가 올바르지 않습니다");
        assertEquals(225, composite.getMaxX(), "최대 X 좌표가 올바르지 않습니다"); // 200 + 25
        assertEquals(225, composite.getMaxY(), "최대 Y 좌표가 올바르지 않습니다"); // 200 + 25

        // 점 포함 테스트
        assertTrue(composite.containsInAnyBounds(new Point(25.0, 25.0)), "첫 번째 사각형 내의 점을 포함해야 합니다");
        assertTrue(composite.containsInAnyBounds(new Point(125.0, 125.0)), "두 번째 사각형 내의 점을 포함해야 합니다");
        assertTrue(composite.containsInAnyBounds(new Point(200.0, 200.0)), "원 내의 점을 포함해야 합니다");
        assertFalse(composite.containsInAnyBounds(new Point(300.0, 300.0)), "모든 경계 밖의 점을 포함하지 않아야 합니다");

        // 경계 제거
        assertTrue(composite.removeBounds(rect1), "존재하는 경계를 제거할 수 있어야 합니다");
        assertEquals(2, composite.getBoundsCount(), "제거 후 경계 개수가 줄어야 합니다");

        // 모든 경계 제거
        composite.clearBounds();
        assertTrue(composite.isEmptyBounds(), "모든 경계 제거 후 비어있어야 합니다");
    }

    @Test
    public void testBoundsContainment() {
        RectangleBounds outerRect = new RectangleBounds(0, 0, 100, 100);
        RectangleBounds innerRect = new RectangleBounds(25, 25, 50, 50);
        CircleBounds innerCircle = new CircleBounds(50, 50, 20);
        CircleBounds outerCircle = new CircleBounds(50, 50, 80);

        // 사각형이 다른 사각형을 포함
        assertTrue(outerRect.contains(innerRect), "큰 사각형이 작은 사각형을 포함해야 합니다");
        assertFalse(innerRect.contains(outerRect), "작은 사각형이 큰 사각형을 포함하지 않아야 합니다");

        // 사각형이 원을 포함 (경계 사각형 기준)
        assertTrue(outerRect.contains(innerCircle), "사각형이 원의 경계 사각형을 포함해야 합니다");

        // 원이 원을 포함 (경계 사각형 기준)
        assertTrue(outerCircle.contains(innerCircle), "큰 원이 작은 원을 포함해야 합니다");
    }

    @Test
    public void testComplexIntersections() {
        // 여러 경계의 복잡한 교차 시나리오
        RectangleBounds rect1 = new RectangleBounds(0, 0, 100, 100);
        RectangleBounds rect2 = new RectangleBounds(50, 50, 100, 100);
        CircleBounds circle1 = new CircleBounds(75, 75, 30);
        CircleBounds circle2 = new CircleBounds(200, 200, 25);

        // 사각형들 교차
        assertTrue(rect1.intersects(rect2), "겹치는 사각형들이 교차해야 합니다");

        // 사각형과 원 교차
        assertTrue(rect1.intersects(circle1), "겹치는 사각형과 원이 교차해야 합니다");
        assertFalse(rect1.intersects(circle2), "멀리 떨어진 사각형과 원이 교차하지 않아야 합니다");

        // 복합 경계 교차
        CompositeBounds composite = new CompositeBounds(rect1, circle2);
        assertTrue(composite.intersects(rect2), "복합 경계가 교차하는 경계와 교차해야 합니다");
        assertTrue(composite.intersectsWithAnyBounds(rect2), "복합 경계의 일부가 교차해야 합니다");
    }

    @Test
    public void testBoundsOperations() {
        RectangleBounds rect1 = new RectangleBounds(0, 0, 50, 50);
        RectangleBounds rect2 = new RectangleBounds(25, 25, 50, 50);

        // 교집합 테스트
        Bounds intersection = rect1.intersection(rect2);
        assertNotNull(intersection, "교차하는 경계들의 교집합이 존재해야 합니다");
        assertEquals(25, intersection.getMinX(), "교집합의 최소 X가 올바르지 않습니다");
        assertEquals(25, intersection.getMinY(), "교집합의 최소 Y가 올바르지 않습니다");
        assertEquals(50, intersection.getMaxX(), "교집합의 최대 X가 올바르지 않습니다");
        assertEquals(50, intersection.getMaxY(), "교집합의 최대 Y가 올바르지 않습니다");

        // 합집합 테스트
        Bounds union = rect1.union(rect2);
        assertNotNull(union, "경계들의 합집합이 존재해야 합니다");
        assertEquals(0, union.getMinX(), "합집합의 최소 X가 올바르지 않습니다");
        assertEquals(0, union.getMinY(), "합집합의 최소 Y가 올바르지 않습니다");
        assertEquals(75, union.getMaxX(), "합집합의 최대 X가 올바르지 않습니다");
        assertEquals(75, union.getMaxY(), "합집합의 최대 Y가 올바르지 않습니다");
    }

    @Test
    public void testAbstractBallAndSimpleMovableBall() {
        // SimpleMovableBall 테스트
        SimpleMovableBall ball = new SimpleMovableBall(new Point(50.0, 50.0), 10, new Vector2D(5, 3));

        assertEquals(50, ball.getCenter().getX(), "초기 X 위치가 올바르지 않습니다");
        assertEquals(50, ball.getCenter().getY(), "초기 Y 위치가 올바르지 않습니다");
        assertEquals(10, ball.getRadius(), "반지름이 올바르지 않습니다");

        Vector2D velocity = ball.getVelocity();
        assertEquals(5, velocity.getX(), "X 속도가 올바르지 않습니다");
        assertEquals(3, velocity.getY(), "Y 속도가 올바르지 않습니다");

        // Template Method 패턴으로 업데이트
        ball.update(1.0); // 1초 후

        assertEquals(55, ball.getCenter().getX(), 0.001, "업데이트 후 X 위치가 올바르지 않습니다");
        assertEquals(53, ball.getCenter().getY(), 0.001, "업데이트 후 Y 위치가 올바르지 않습니다");

        // 경계 확인
        Bounds bounds = ball.getBounds();
        assertInstanceOf(CircleBounds.class, bounds, "공의 경계는 CircleBounds여야 합니다");
        assertEquals(45, bounds.getMinX(), 0.001, "경계의 최소 X가 올바르지 않습니다");
        assertEquals(43, bounds.getMinY(), 0.001, "경계의 최소 Y가 올바르지 않습니다");
    }
}
