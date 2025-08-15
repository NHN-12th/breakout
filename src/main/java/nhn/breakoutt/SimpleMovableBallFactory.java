package nhn.breakoutt;

import java.util.Random;

public class SimpleMovableBallFactory extends BallFactory {
    private static final Random random = new Random();

    @Override
    public AbstractBall createBall(Point center, double radius) {
        return new SimpleMovableBall(center, radius);
    }

    @Override
    public AbstractBall createBall(double x, double y, double radius) {
        return new SimpleMovableBall(x, y, radius);
    }

    /**
     * 랜덤한 속도를 가진 움직이는 공을 생성합니다.
     * @param center 중심점
     * @param radius 반지름
     * @param minSpeed 최소 속력
     * @param maxSpeed 최대 속력
     * @return 랜덤 속도를 가진 공
     */
    public SimpleMovableBall createMovingBall(Point center, double radius, double minSpeed, double maxSpeed) {
        // 랜덤 방향 (0~2π)
        double angle = random.nextDouble() * 2 * Math.PI;
        // 랜덤 속력
        double speed = minSpeed + random.nextDouble() * (maxSpeed - minSpeed);

        // 극좌표를 직교좌표로 변환
        double vx = speed * Math.cos(angle);
        double vy = speed * Math.sin(angle);

        return new SimpleMovableBall(center, radius, new Vector2D(vx, vy));
    }

    /**
     * 정해진 속도를 가진 움직이는 공을 생성합니다.
     * @param center 중심점
     * @param radius 반지름
     * @param velocity 속도 벡터
     * @return 지정된 속도를 가진 공
     */
    public SimpleMovableBall createMovingBall(Point center, double radius, Vector2D velocity) {
        return new SimpleMovableBall(center, radius, velocity);
    }
}
