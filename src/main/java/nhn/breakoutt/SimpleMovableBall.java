package nhn.breakoutt;

/**
 * AbstractBall을 상속받아 움직이는 공을 구현한 클래스입니다.
 * Vector2D를 사용하여 속도를 관리합니다.
 */
public class SimpleMovableBall extends AbstractBall {
    private Vector2D velocity;  // 속도 벡터 (상대적 이동량)

    /**
     * 움직이는 공을 생성합니다.
     * @param center 중심점
     * @param radius 반지름
     * @param velocity 초기 속도 벡터
     */
    public SimpleMovableBall(Point center, double radius, Vector2D velocity) {
        super(center, radius);
        this.velocity = velocity != null ? velocity : new Vector2D(0, 0);
    }

    /**
     * 정지 상태의 움직이는 공을 생성합니다.
     * @param center 중심점
     * @param radius 반지름
     */
    public SimpleMovableBall(Point center, double radius) {
        this(center, radius, new Vector2D(0, 0));
    }

    /**
     * 좌표와 속도로 움직이는 공을 생성합니다.
     * @param x X 좌표
     * @param y Y 좌표
     * @param radius 반지름
     * @param vx X 방향 속도
     * @param vy Y 방향 속도
     */
    public SimpleMovableBall(double x, double y, double radius, double vx, double vy) {
        this(new Point(x, y), radius, new Vector2D(vx, vy));
    }

    /**
     * 정지 상태의 움직이는 공을 좌표로 생성합니다.
     * @param x X 좌표
     * @param y Y 좌표
     * @param radius 반지름
     */
    public SimpleMovableBall(double x, double y, double radius) {
        this(new Point(x, y), radius, new Vector2D(0, 0));
    }

    // 속도 관련 메서드들

    /**
     * 공의 속도 벡터를 반환합니다.
     * @return 속도 벡터
     */
    public Vector2D getVelocity() {
        return velocity;
    }

    /**
     * 공의 속도 벡터를 설정합니다.
     * @param velocity 새로운 속도 벡터
     */
    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity != null ? velocity : new Vector2D(0, 0);
    }

    /**
     * 공의 속도를 개별 성분으로 설정합니다.
     * @param vx X 방향 속도
     * @param vy Y 방향 속도
     */
    public void setVelocity(double vx, double vy) {
        this.velocity = new Vector2D(vx, vy);
    }

    /**
     * 공의 속력(속도의 크기)을 반환합니다.
     * @return 속력
     */
    public double getSpeed() {
        return velocity.magnitude();
    }

    /**
     * 공이 움직이고 있는지 확인합니다.
     * @return 움직이고 있으면 true, 정지 상태면 false
     */
    public boolean isMoving() {
        return getSpeed() > 0;
    }

    /**
     * 공을 정지시킵니다.
     */
    public void stop() {
        this.velocity = new Vector2D(0, 0);
    }

    /**
     * 속도에 가속도를 적용합니다.
     * @param acceleration 가속도 벡터
     * @param deltaTime 경과 시간
     */
    public void accelerate(Vector2D acceleration, double deltaTime) {
        if (acceleration != null) {
            // v = v₀ + a × t
            Vector2D deltaVelocity = acceleration.multiply(deltaTime);
            this.velocity = this.velocity.add(deltaVelocity);
        }
    }

    /**
     * 개별 성분으로 가속도를 적용합니다.
     * @param ax X 방향 가속도
     * @param ay Y 방향 가속도
     * @param deltaTime 경과 시간
     */
    public void accelerate(double ax, double ay, double deltaTime) {
        accelerate(new Vector2D(ax, ay), deltaTime);
    }

    // Template Method 패턴: 핵심 업데이트 로직 구현

    /**
     * 속도에 따른 위치 업데이트를 수행합니다.
     * 현재 위치(Point) + 속도(Vector2D) × 시간 = 새 위치(Point)
     * @param deltaTime 경과 시간
     */
    @Override
    protected void performUpdate(double deltaTime) {
        if (isMoving()) {
            // 이동량 계산: displacement = velocity × time
            Vector2D displacement = velocity.multiply(deltaTime);

            // 새로운 위치 계산: newPosition = currentPosition + displacement
            Point newPosition = center.add(displacement);

            // 위치 업데이트 (updateBounds()는 moveTo()에서 자동 호출됨)
            moveTo(newPosition);
        }
    }

    /**
     * 공의 운동 에너지를 계산합니다.
     * 질량을 반지름의 세제곱에 비례한다고 가정합니다.
     * @return 운동 에너지 (1/2 × m × v²)
     */
    public double getKineticEnergy() {
        double mass = getMass();
        double speedSquared = velocity.magnitude() * velocity.magnitude();
        return 0.5 * mass * speedSquared;
    }

    /**
     * 공의 질량을 반환합니다.
     * 반지름의 세제곱에 비례한다고 가정합니다.
     * @return 질량
     */
    public double getMass() {
        return radius * radius * radius;
    }

    /**
     * 공의 운동량을 계산합니다.
     * @return 운동량 벡터 (m × v)
     */
    public Vector2D getMomentum() {
        return velocity.multiply(getMass());
    }

    /**
     * 다음 프레임에서의 예상 위치를 계산합니다.
     * 실제 위치를 변경하지 않고 예측만 수행합니다.
     * @param deltaTime 경과 시간
     * @return 예상 위치
     */
    public Point predictNextPosition(double deltaTime) {
        Vector2D displacement = velocity.multiply(deltaTime);
        return center.add(displacement);
    }

    /**
     * 특정 시간 후의 예상 위치를 계산합니다.
     * @param time 예측할 시간
     * @return 예상 위치
     */
    public Point getPositionAfter(double time) {
        return predictNextPosition(time);
    }

    @Override
    public String toString() {
        return String.format("SimpleMovableBall(center=%.2f,%.2f, radius=%.2f, velocity=%.2f,%.2f, speed=%.2f)",
                center.getX(), center.getY(), radius,
                velocity.getX(), velocity.getY(), getSpeed());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof SimpleMovableBall)) return false;
        if (!super.equals(obj)) return false;

        SimpleMovableBall other = (SimpleMovableBall) obj;
        return velocity.equals(other.velocity);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), velocity);
    }
}
