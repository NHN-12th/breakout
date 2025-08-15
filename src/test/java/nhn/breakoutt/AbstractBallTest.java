package nhn.breakoutt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// AbstractBall을 상속받는 테스트용 구현체
class TestBall extends AbstractBall {
    private Vector2D velocity = new Vector2D(0, 0);
    private boolean updateCalled = false;
    private double lastDeltaTime;

    public TestBall(Point center, double radius) {
        super(center.getX(), center.getY(), radius);
    }

    @Override
    protected void performUpdate(double deltaTime) {
        this.updateCalled = true;
        this.lastDeltaTime = deltaTime;
        // 간단한 이동 로직
        setX(getX() + getVelocity().getX() * deltaTime);
        setY(getY() + getVelocity().getY() * deltaTime);
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }


    public boolean isUpdateCalled() {
        return updateCalled;
    }

    public double getLastDeltaTime() {
        return lastDeltaTime;
    }

    public void resetUpdateFlag() {
        updateCalled = false;
    }
}

public class AbstractBallTest {

    private TestBall ball;

    @BeforeEach
    public void setUp() {
        ball = new TestBall(new Point(100.0, 100.0), 20);
    }

    @Test
    public void testAbstractBallCreation() {
        TestBall ball = new TestBall(new Point(100.0, 100.0), 20);
        Point center = ball.getCenter();
        assertEquals(100, center.getX(), 0.001, "AbstractBall X 좌표가 잘못되었습니다");
        assertEquals(100, center.getY(), 0.001, "AbstractBall Y 좌표가 잘못되었습니다");
        assertEquals(20, ball.getRadius(), 0.001, "AbstractBall 반지름이 잘못되었습니다");

        // SimpleMovableBall의 속도 기본값 확인
        SimpleMovableBall movableBall = new SimpleMovableBall(new Point(50.0, 50.0), 10);
        Vector2D velocity = movableBall.getVelocity();
        assertEquals(0.0, velocity.getX(), 0.001, "초기 X 속도가 0이 아닙니다");
        assertEquals(0.0, velocity.getY(), 0.001, "초기 Y 속도가 0이 아닙니다");
    }

    @Test
    public void testTemplateMethodPattern() {
        ball.setVelocity(new Vector2D(50, 30));

        // Template Method인 update 호출
        ball.update(0.1);

        // performUpdate가 호출되었는지 확인
        assertTrue(ball.isUpdateCalled(), "performUpdate 메서드가 호출되지 않았습니다");
        assertEquals(0.1, ball.getLastDeltaTime(), 0.001, "deltaTime이 올바르게 전달되지 않았습니다");

        // 이동이 올바르게 수행되었는지 확인
        assertEquals(105, ball.getX(), 0.001, "Template Method를 통한 X 이동이 잘못되었습니다");
        assertEquals(103, ball.getY(), 0.001, "Template Method를 통한 Y 이동이 잘못되었습니다");

        // 후처리 확인 (충돌 체크, 경계 처리 등이 여기서 수행될 수 있음)
        // 이는 AbstractBall의 update 메서드에서 performUpdate 호출 후 수행
    }

    @Test
    public void testVelocityMethods() {
        ball.setVelocity(new Vector2D(75, 50));

        assertEquals(75, ball.getVelocity().getX(), 0.001, "벡터 속도 설정 X가 잘못되었습니다");
        assertEquals(50, ball.getVelocity().getY(), 0.001, "벡터 속도 설정 Y가 잘못되었습니다");

        Vector2D velocity = ball.getVelocity();
        assertEquals(75, velocity.getX(), 0.001, "벡터 속도 조회 X가 잘못되었습니다");
        assertEquals(50, velocity.getY(), 0.001, "벡터 속도 조회 Y가 잘못되었습니다");
    }

    @Test
    public void testBoundsIntegration() {
        Bounds bounds = ball.getBounds();

        assertTrue(bounds instanceof CircleBounds, "AbstractBall의 bounds는 CircleBounds여야 합니다");

        CircleBounds circleBounds = (CircleBounds) bounds;
        assertEquals(100, circleBounds.getCenterX(), 0.001, "Bounds 중심 X가 잘못되었습니다");
        assertEquals(100, circleBounds.getCenterY(), 0.001, "Bounds 중심 Y가 잘못되었습니다");
        assertEquals(20, circleBounds.getRadius(), 0.001, "Bounds 반지름이 잘못되었습니다");
    }

    @Test
    public void testMultipleUpdates() {
        ball.setVelocity(new Vector2D(10, 5));

        // 여러 번 업데이트 수행
        for (int i = 0; i < 5; i++) {
            ball.resetUpdateFlag();
            ball.update(0.1);
            assertTrue(ball.isUpdateCalled(), "매번 performUpdate가 호출되어야 합니다");
        }

        // 최종 위치 확인
        assertEquals(105, ball.getX(), 0.001, "5번 업데이트 후 X 위치가 잘못되었습니다");
        assertEquals(102.5, ball.getY(), 0.001, "5번 업데이트 후 Y 위치가 잘못되었습니다");
    }

    @Test
    public void testHookMethods() {
        // Template Method 패턴에서 hook 메서드들이 있다면 테스트
        // 예: beforeUpdate, afterUpdate 등이 AbstractBall에 정의되어 있다면


        ball.update(0.1);

        // Hook 메서드들이 올바른 순서로 호출되는지 확인
        // 이는 실제 AbstractBall 구현에 따라 달라질 수 있음
        assertTrue(ball.isUpdateCalled(), "핵심 업데이트 로직이 호출되었습니다");
    }
}