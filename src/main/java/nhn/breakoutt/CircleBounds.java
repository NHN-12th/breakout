package nhn.breakoutt;

/**
 * 원형 경계를 표현하는 Bounds 구현 클래스입니다.
 */
public class CircleBounds extends Bounds {
    private final double centerX;
    private final double centerY;
    private final double radius;

    /**
     * 원형 경계를 생성합니다.
     * @param centerX 중심 X 좌표
     * @param centerY 중심 Y 좌표
     * @param radius 반지름
     * @throws IllegalArgumentException 반지름이 음수인 경우
     */
    public CircleBounds(double centerX, double centerY, double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative: " + radius);
        }

        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    /**
     * 중심점과 반지름으로 원형 경계를 생성합니다.
     * @param center 중심점
     * @param radius 반지름
     * @throws IllegalArgumentException center가 null이거나 반지름이 음수인 경우
     */
    public CircleBounds(Point center, double radius) {
        if (center == null) {
            throw new IllegalArgumentException("Center point cannot be null");
        }
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative: " + radius);
        }

        this.centerX = center.getX();
        this.centerY = center.getY();
        this.radius = radius;
    }

    // Bounds 추상 메서드 구현 - 원의 경계 사각형을 반환

    @Override
    public double getMinX() {
        return centerX - radius;
    }

    @Override
    public double getMinY() {
        return centerY - radius;
    }

    @Override
    public double getMaxX() {
        return centerX + radius;
    }

    @Override
    public double getMaxY() {
        return centerY + radius;
    }

    // Circle 관련 getter 메서드들

    /**
     * 원의 중심 X 좌표를 반환합니다.
     * @return 중심 X 좌표
     */
    public double getCircleCenterX() {
        return centerX;
    }

    /**
     * 원의 중심 Y 좌표를 반환합니다.
     * @return 중심 Y 좌표
     */
    public double getCircleCenterY() {
        return centerY;
    }

    /**
     * 원의 중심점을 반환합니다.
     * @return 중심점
     */
    public Point getCircleCenter() {
        return new Point(centerX, centerY);
    }

    /**
     * 원의 반지름을 반환합니다.
     * @return 반지름
     */
    public double getRadius() {
        return radius;
    }

    /**
     * 원의 지름을 반환합니다.
     * @return 지름 (반지름 × 2)
     */
    public double getDiameter() {
        return radius * 2;
    }

    /**
     * 원의 둘레를 계산합니다.
     * @return 둘레 (2π × 반지름)
     */
    public double getCircumference() {
        return 2 * Math.PI * radius;
    }

    /**
     * 원의 넓이를 계산합니다.
     * @return 넓이 (π × 반지름²)
     */
    public double getCircleArea() {
        return Math.PI * radius * radius;
    }

    // 원형 경계 특화된 메서드들

    /**
     * 점이 원 내부에 있는지 확인합니다 (경계 사각형이 아닌 실제 원형 영역).
     * @param point 확인할 점
     * @return 원 내부에 있으면 true, 아니면 false
     */
    public boolean containsInCircle(Point point) {
        if (point == null) {
            return false;
        }
        return distanceFromCenter(point) <= radius;
    }

    /**
     * 좌표가 원 내부에 있는지 확인합니다.
     * @param x X 좌표
     * @param y Y 좌표
     * @return 원 내부에 있으면 true, 아니면 false
     */
    public boolean containsInCircle(double x, double y) {
        return distanceFromCenter(x, y) <= radius;
    }

    /**
     * 다른 원과 교차하는지 확인합니다.
     * @param other 다른 원형 경계
     * @return 교차하면 true, 아니면 false
     */
    public boolean intersectsWithCircle(CircleBounds other) {
        if (other == null) {
            return false;
        }
        double distanceBetweenCenters = distanceFromCenter(other.centerX, other.centerY);
        return distanceBetweenCenters <= (radius + other.radius);
    }

    /**
     * 중심점으로부터의 거리를 계산합니다.
     * @param point 거리를 계산할 점
     * @return 중심점으로부터의 거리
     */
    public double distanceFromCenter(Point point) {
        if (point == null) {
            throw new IllegalArgumentException("Point cannot be null");
        }
        return distanceFromCenter(point.getX(), point.getY());
    }

    /**
     * 중심점으로부터의 거리를 계산합니다.
     * @param x X 좌표
     * @param y Y 좌표
     * @return 중심점으로부터의 거리
     */
    public double distanceFromCenter(double x, double y) {
        double dx = x - centerX;
        double dy = y - centerY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 새로운 위치로 이동한 원형 경계를 반환합니다.
     * @param deltaX X 방향 이동량
     * @param deltaY Y 방향 이동량
     * @return 이동된 새로운 원형 경계
     */
    public CircleBounds translate(double deltaX, double deltaY) {
        return new CircleBounds(centerX + deltaX, centerY + deltaY, radius);
    }

    /**
     * 크기가 조정된 새로운 원형 경계를 반환합니다.
     * @param scaleFactor 스케일 팩터
     * @return 크기가 조정된 새로운 원형 경계
     * @throws IllegalArgumentException 스케일 팩터가 음수인 경우
     */
    public CircleBounds scale(double scaleFactor) {
        if (scaleFactor < 0) {
            throw new IllegalArgumentException("Scale factor cannot be negative: " + scaleFactor);
        }
        return new CircleBounds(centerX, centerY, radius * scaleFactor);
    }

    @Override
    public String toString() {
        return String.format("CircleBounds[centerX=%.2f, centerY=%.2f, radius=%.2f]",
                centerX, centerY, radius);
    }
}
