package nhn.breakoutt;

import javafx.scene.paint.Color;

public class MovableBallV2 extends PaintableBall {
    private Vector2D velocity;
    private Vector2D acceleration;

    public MovableBallV2(Point center, double radius, Color color) {
        super(center, radius, color);
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);
    }

    // 힘 적용 (F = ma, a = F/m)
    public void applyForce(Vector2D force) {
        // 질량을 반지름에 비례한다고 가정
        double mass = getRadius();
        Vector2D acc = force.multiply(1.0 / mass);
        acceleration = acceleration.add(acc);
    }

    // 물리 업데이트
    public void update(double deltaTime) {
        // 1. 속도 업데이트: v = v + a × Δt
        velocity = velocity.add(acceleration.multiply(deltaTime));

        // 2. 위치 업데이트: p = p + v × Δt
        Point currentCenter = getCenter();
        Vector2D displacement = velocity.multiply(deltaTime);
        Point newCenter = currentCenter.add(displacement);
        moveTo(newCenter);

        // 3. 가속도 초기화 (중요!)
        acceleration = new Vector2D(0, 0);
    }

    // 속도 제한
    public void limitSpeed(double maxSpeed) {
        double currentSpeed = velocity.magnitude();
        if (currentSpeed > maxSpeed) {
            velocity = velocity.normalize().multiply(maxSpeed);
        }
    }
}