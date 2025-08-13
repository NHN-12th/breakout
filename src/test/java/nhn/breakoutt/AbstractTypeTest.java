package nhn.breakoutt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lab 5-3: 디자인 패턴 적용 테스트
 * Vector, Bounds, AbstractBall 등 추상 타입들의 동작을 검증합니다.
 */
public class AbstractTypeTest {

    private BallFactory ballFactory;
    private Bounds testArea;

    @BeforeEach
    public void setUp() {
        ballFactory = new SimpleMovableBallFactory();
        testArea = new RectangleBounds(0, 0, 400, 300);
    }

    @Test
    public void testVector2DOperations() {
        Vector2D v1 = new Vector2D(3, 4);
        assertEquals(5.0, v1.magnitude(), 0.001, "벡터 (3,4)의 크기는 5여야 합니다");

        Vector2D v2 = v1.normalize();
        assertEquals(1.0, v2.magnitude(), 0.001, "정규화된 벡터의 크기는 1이어야 합니다");
        assertEquals(0.6, v2.getX(), 0.001, "정규화된 벡터의 X 성분이 올바르지 않습니다");
        assertEquals(0.8, v2.getY(), 0.001, "정규화된 벡터의 Y 성분이 올바르지 않습니다");

        // 추가 벡터 연산 테스트
        Vector2D v3 = new Vector2D(1, 2);
        Vector2D sum = v1.add(v3);
        assertEquals(4.0, sum.getX(), 0.001, "벡터 덧셈 X 성분이 올바르지 않습니다");
        assertEquals(6.0, sum.getY(), 0.001, "벡터 덧셈 Y 성분이 올바르지 않습니다");

        double dotProduct = v1.dot(v3);
        assertEquals(11.0, dotProduct, 0.001, "내적 계산이 올바르지 않습니다"); // 3*1 + 4*2 = 11

        Vector2D scaled = v1.multiply(2.0);
        assertEquals(6.0, scaled.getX(), 0.001, "스칼라 곱 X 성분이 올바르지 않습니다");
        assertEquals(8.0, scaled.getY(), 0.001, "스칼라 곱 Y 성분이 올바르지 않습니다");
    }

    @Test
    public void testBoundsContainment() {
        Bounds outer = new RectangleBounds(0, 0, 200, 200);
        Bounds inner = new CircleBounds(100, 100, 50);

        assertTrue(outer.contains(inner), "큰 경계가 작은 경계를 포함해야 합니다");
        assertFalse(inner.contains(outer), "작은 경계가 큰 경계를 포함하지 않아야 합니다");

        assertTrue(outer.contains(100, 100), "외부 경계가 중심점을 포함해야 합니다");
        assertTrue(inner.contains(100, 100), "내부 경계가 중심점을 포함해야 합니다");

        // 경계 교차 테스트
        assertTrue(outer.intersects(inner), "겹치는 경계들은 교차해야 합니다");

        // 경계 밖의 점 테스트
        assertFalse(outer.contains(250, 250), "경계 밖의 점을 포함하지 않아야 합니다");

        // 경계 정보 테스트
        assertEquals(200.0, outer.getWidth(), 0.001, "외부 경계의 너비가 올바르지 않습니다");
        assertEquals(200.0, outer.getHeight(), 0.001, "외부 경계의 높이가 올바르지 않습니다");
        assertEquals(100.0, inner.getWidth(), 0.001, "내부 경계의 너비가 올바르지 않습니다");
    }

    @Test
    public void testAbstractBallUpdate() {
        SimpleMovableBall ball = new SimpleMovableBall(new Point(100.0, 100.0), 20);
        ball.setVelocity(new Vector2D(50, 30));

        Point oldCenter = ball.getCenter();
        double oldX = oldCenter.getX();
        double oldY = oldCenter.getY();

        // Template Method 패턴으로 업데이트
        ball.update(1.0);

        Point newCenter = ball.getCenter();
        assertEquals(oldX + 50, newCenter.getX(), 0.001, "X 위치 업데이트가 올바르지 않습니다");
        assertEquals(oldY + 30, newCenter.getY(), 0.001, "Y 위치 업데이트가 올바르지 않습니다");

        // Bounds도 업데이트되었는지 확인
        assertEquals(newCenter.getX(), ball.getBounds().getCenterX(), 0.001, "경계의 중심 X가 업데이트되지 않았습니다");
        assertEquals(newCenter.getY(), ball.getBounds().getCenterY(), 0.001, "경계의 중심 Y가 업데이트되지 않았습니다");

        // 경계가 CircleBounds인지 확인
        assertInstanceOf(CircleBounds.class, ball.getBounds(), "공의 경계는 CircleBounds여야 합니다");
    }

    @Test
    public void testFactoryMethodPattern() {
        // Factory Method 패턴 테스트
        AbstractBall ball1 = ballFactory.createBall(new Point(50.0, 50.0), 15);
        AbstractBall ball2 = ballFactory.createBall(100, 100, 20);

        assertNotNull(ball1, "팩토리로 생성된 공이 null이 아니어야 합니다");
        assertNotNull(ball2, "팩토리로 생성된 공이 null이 아니어야 합니다");

        assertEquals(50, ball1.getCenter().getX(), 0.001, "생성된 공의 X 위치가 올바르지 않습니다");
        assertEquals(50, ball1.getCenter().getY(), 0.001, "생성된 공의 Y 위치가 올바르지 않습니다");
        assertEquals(15, ball1.getRadius(), 0.001, "생성된 공의 반지름이 올바르지 않습니다");

        assertTrue(ball1 instanceof SimpleMovableBall, "팩토리가 올바른 타입의 공을 생성해야 합니다");
        assertTrue(ball2 instanceof SimpleMovableBall, "팩토리가 올바른 타입의 공을 생성해야 합니다");
    }

    @Test
    public void testRandomBallsCreation() {
        // 랜덤 공 생성 테스트
        List<AbstractBall> balls = ballFactory.createRandomBalls(5, testArea);

        assertEquals(5, balls.size(), "요청한 수만큼 공이 생성되어야 합니다");

        for (AbstractBall ball : balls) {
            assertNotNull(ball, "생성된 공이 null이 아니어야 합니다");

            // 공이 지정된 영역 내에 있는지 확인
            Point center = ball.getCenter();
            double radius = ball.getRadius();

            assertTrue(center.getX() - radius >= testArea.getMinX(), "공이 영역의 왼쪽 경계를 벗어났습니다");
            assertTrue(center.getX() + radius <= testArea.getMaxX(), "공이 영역의 오른쪽 경계를 벗어났습니다");
            assertTrue(center.getY() - radius >= testArea.getMinY(), "공이 영역의 위쪽 경계를 벗어났습니다");
            assertTrue(center.getY() + radius <= testArea.getMaxY(), "공이 영역의 아래쪽 경계를 벗어났습니다");

            // 반지름이 예상 범위 내에 있는지 확인
            assertTrue(radius >= BallFactory.getMinRadius(), "반지름이 최소값보다 작습니다");
            assertTrue(radius <= BallFactory.getMaxRadius(), "반지름이 최대값보다 큽니다");
        }
    }

    @Test
    public void testGridBallsCreation() {
        // 격자 형태 공 생성 테스트
        List<AbstractBall> gridBalls = ballFactory.createGridBalls(3, 4, testArea, 15);

        assertEquals(12, gridBalls.size(), "3x4 격자는 12개의 공을 생성해야 합니다");

        for (AbstractBall ball : gridBalls) {
            assertEquals(15, ball.getRadius(), 0.001, "격자 공의 반지름이 지정값과 다릅니다");
            assertTrue(testArea.contains(ball.getCenter()), "격자 공이 지정된 영역 내에 있어야 합니다");
        }
    }

    @Test
    public void testCompositeBounds() {
        // 복합 경계 테스트
        CompositeBounds composite = new CompositeBounds();

        RectangleBounds rect1 = new RectangleBounds(0, 0, 100, 100);
        RectangleBounds rect2 = new RectangleBounds(150, 150, 100, 100);
        CircleBounds circle = new CircleBounds(300, 300, 50);

        composite.addBounds(rect1).addBounds(rect2).addBounds(circle);

        assertEquals(3, composite.getBoundsCount(), "복합 경계에 3개의 경계가 추가되어야 합니다");

        // 전체 경계 영역 확인
        assertEquals(0, composite.getMinX(), 0.001, "복합 경계의 최소 X가 올바르지 않습니다");
        assertEquals(0, composite.getMinY(), 0.001, "복합 경계의 최소 Y가 올바르지 않습니다");
        assertEquals(350, composite.getMaxX(), 0.001, "복합 경계의 최대 X가 올바르지 않습니다"); // 300 + 50
        assertEquals(350, composite.getMaxY(), 0.001, "복합 경계의 최대 Y가 올바르지 않습니다"); // 300 + 50

        // 점 포함 테스트
        assertTrue(composite.containsInAnyBounds(new Point(50.0, 50.0)), "첫 번째 사각형 내의 점을 포함해야 합니다");
        assertTrue(composite.containsInAnyBounds(new Point(200.0, 200.0)), "두 번째 사각형 내의 점을 포함해야 합니다");
        assertFalse(composite.containsInAnyBounds(new Point(500.0, 500.0)), "모든 경계 밖의 점을 포함하지 않아야 합니다");
    }

    @Test
    public void testBallCollisionDetection() {
        // 공 충돌 감지 테스트
        AbstractBall ball1 = ballFactory.createBall(new Point(100.0, 100.0), 20);
        AbstractBall ball2 = ballFactory.createBall(new Point(130.0, 100.0), 20);
        AbstractBall ball3 = ballFactory.createBall(new Point(200.0, 100.0), 20);

        assertTrue(ball1.isColliding(ball2), "겹치는 공들은 충돌해야 합니다");
        assertFalse(ball1.isColliding(ball3), "멀리 떨어진 공들은 충돌하지 않아야 합니다");

        // 경계를 통한 충돌 감지
        assertTrue(ball1.getBounds().intersects(ball2.getBounds()), "겹치는 공의 경계들은 교차해야 합니다");
        assertFalse(ball1.getBounds().intersects(ball3.getBounds()), "멀리 떨어진 공의 경계들은 교차하지 않아야 합니다");
    }

    @Test
    public void testTemplateMethodPattern() {
        // Template Method 패턴 테스트
        SimpleMovableBall ball = new SimpleMovableBall(new Point(0.0, 0.0), 10, new Vector2D(10, 5));

        // 업데이트 전 상태 저장
        Point initialCenter = ball.getCenter();
        Vector2D initialVelocity = ball.getVelocity();

        // Template Method를 통한 업데이트
        ball.update(2.0); // 2초 업데이트

        // 위치가 velocity * time만큼 변경되었는지 확인
        Point expectedCenter = initialCenter.add(initialVelocity.multiply(2.0));
        assertEquals(expectedCenter.getX(), ball.getCenter().getX(), 0.001, "Template Method 업데이트 X 위치가 올바르지 않습니다");
        assertEquals(expectedCenter.getY(), ball.getCenter().getY(), 0.001, "Template Method 업데이트 Y 위치가 올바르지 않습니다");

        // 속도는 변경되지 않아야 함
        assertEquals(initialVelocity.getX(), ball.getVelocity().getX(), 0.001, "속도가 변경되지 않아야 합니다");
        assertEquals(initialVelocity.getY(), ball.getVelocity().getY(), 0.001, "속도가 변경되지 않아야 합니다");
    }

    @Test
    public void testFactoryExceptionHandling() {
        // 팩토리 예외 처리 테스트
        assertThrows(IllegalArgumentException.class, () ->
            ballFactory.createRandomBalls(-1, testArea), "음수 개수에 대해 예외가 발생해야 합니다");

        assertThrows(IllegalArgumentException.class, () ->
            ballFactory.createRandomBalls(5, null), "null 영역에 대해 예외가 발생해야 합니다");

        // 너무 작은 영역에 대한 예외
        Bounds tinyArea = new RectangleBounds(0, 0, 10, 10);
        assertThrows(IllegalArgumentException.class, () ->
            ballFactory.createRandomBall(tinyArea), "작은 영역에 대해 예외가 발생해야 합니다");
    }
}
