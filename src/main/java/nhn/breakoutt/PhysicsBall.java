package nhn.breakoutt;

import javafx.scene.paint.Color;

public class PhysicsBall extends MovableBall {
    private static final double GRAVITY = 980.0; // 중력 가속도 (pixels/s²)
    private static final double AIR_RESISTANCE = 0.99; // 공기저항 계수

    protected double mass; // 질량
    protected double restitution; // 반발계수 (0-1)
    protected boolean affectedByGravity; // 중력 영향 여부

    public PhysicsBall(Point center, double radius, Color color) {
        super(center, radius, color);
        this.mass = 1.0;
        this.restitution = 0.8;
        this.affectedByGravity = true;
    }

    public PhysicsBall(Point center, double radius) {
        this(center, radius, Color.BLACK);
    }

    @Override
    public void move(double deltaTime) {
        if (affectedByGravity) {
            // 중력을 속도에 추가
            Vector2D currentVelocity = getVelocity();
            Vector2D gravityVelocity = new Vector2D(0, GRAVITY * deltaTime);
            setVelocity(currentVelocity.add(gravityVelocity));
        }

        // 공기저항 적용
        setVelocity(getVelocity().multiply(AIR_RESISTANCE));

        // 부모 클래스의 이동 처리
        super.move(deltaTime);
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void bounce(Vector2D normal) {
        Vector2D velocity = getVelocity();
        Vector2D reflection = velocity.subtract(normal.multiply(2 * velocity.dot(normal)));
        setVelocity(reflection.multiply(restitution));
    }
}
