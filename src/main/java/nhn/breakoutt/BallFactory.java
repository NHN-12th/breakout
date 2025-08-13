package nhn.breakoutt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BallFactory {
    private static final Random random = new Random();
    private static final double MIN_RADIUS = 10.0;
    private static final double MAX_RADIUS = 40.0;

    /**
     * 구체적인 Ball을 생성합니다 (Factory Method).
     * 하위 클래스에서 구체적인 Ball 타입을 결정합니다.
     * @param center 중심점
     * @param radius 반지름
     * @return 생성된 Ball 인스턴스
     */
    public abstract AbstractBall createBall(Point center, double radius);

    /**
     * 구체적인 Ball을 좌표로 생성합니다 (Factory Method).
     * @param x X 좌표
     * @param y Y 좌표
     * @param radius 반지름
     * @return 생성된 Ball 인스턴스
     */
    public abstract AbstractBall createBall(double x, double y, double radius);

    // Template Method 패턴: 구체 메서드들

    /**
     * 주어진 영역 내에 랜덤한 공들을 여러 개 생성합니다.
     * @param count 생성할 공의 개수
     * @param area 공을 생성할 영역
     * @return 생성된 공들의 리스트
     * @throws IllegalArgumentException count가 0 이하이거나 area가 null인 경우
     */
    public List<AbstractBall> createRandomBalls(int count, Bounds area) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive: " + count);
        }
        if (area == null) {
            throw new IllegalArgumentException("Area cannot be null");
        }

        List<AbstractBall> balls = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            AbstractBall ball = createRandomBall(area);
            balls.add(ball);
        }

        return balls;
    }

    /**
     * 주어진 영역 내에 랜덤한 공을 하나 생성합니다.
     * @param area 공을 생성할 영역
     * @return 생성된 공
     */
    public AbstractBall createRandomBall(Bounds area) {
        if (area == null) {
            throw new IllegalArgumentException("Area cannot be null");
        }

        // 랜덤 반지름 계산 (10~40)
        double radius = MIN_RADIUS + random.nextDouble() * (MAX_RADIUS - MIN_RADIUS);

        // 영역의 경계 계산 (공이 완전히 영역 안에 있도록)
        double minX = area.getMinX() + radius;
        double minY = area.getMinY() + radius;
        double maxX = area.getMaxX() - radius;
        double maxY = area.getMaxY() - radius;

        // 유효한 영역이 있는지 확인
        if (minX >= maxX || minY >= maxY) {
            throw new IllegalArgumentException("Area is too small for the ball size");
        }

        // 랜덤 위치 계산
        double x = minX + random.nextDouble() * (maxX - minX);
        double y = minY + random.nextDouble() * (maxY - minY);

        return createBall(x, y, radius);
    }

    /**
     * 정사각형 영역 내에 랜덤한 공들을 생성합니다.
     * @param count 생성할 공의 개수
     * @param width 영역의 너비
     * @param height 영역의 높이
     * @return 생성된 공들의 리스트
     */
    public List<AbstractBall> createRandomBalls(int count, double width, double height) {
        RectangleBounds area = new RectangleBounds(0, 0, width, height);
        return createRandomBalls(count, area);
    }

    /**
     * 원형 영역 내에 랜덤한 공들을 생성합니다.
     * @param count 생성할 공의 개수
     * @param center 원형 영역의 중심
     * @param areaRadius 원형 영역의 반지름
     * @return 생성된 공들의 리스트
     */
    public List<AbstractBall> createRandomBallsInCircle(int count, Point center, double areaRadius) {
        CircleBounds area = new CircleBounds(center, areaRadius);
        return createRandomBalls(count, area);
    }

    /**
     * 격자 형태로 공들을 생성합니다.
     * @param rows 행 개수
     * @param cols 열 개수
     * @param area 전체 영역
     * @param radius 공의 반지름 (모든 공 동일)
     * @return 생성된 공들의 리스트
     */
    public List<AbstractBall> createGridBalls(int rows, int cols, Bounds area, double radius) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Rows and cols must be positive");
        }
        if (area == null) {
            throw new IllegalArgumentException("Area cannot be null");
        }
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }

        List<AbstractBall> balls = new ArrayList<>();

        double areaWidth = area.getWidth() - 2 * radius;
        double areaHeight = area.getHeight() - 2 * radius;

        double spacingX = cols > 1 ? areaWidth / (cols - 1) : 0;
        double spacingY = rows > 1 ? areaHeight / (rows - 1) : 0;

        double startX = area.getMinX() + radius;
        double startY = area.getMinY() + radius;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = startX + col * spacingX;
                double y = startY + row * spacingY;

                AbstractBall ball = createBall(x, y, radius);
                balls.add(ball);
            }
        }

        return balls;
    }

    public static double getMinRadius() {
        return MIN_RADIUS;
    }

    public static double getMaxRadius() {
        return MAX_RADIUS;
    }
}
