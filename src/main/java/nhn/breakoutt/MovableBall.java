package nhn.breakoutt;

import javafx.scene.paint.Color;

public class MovableBall extends PaintableBall {
    private Vector2D velocity;

    public MovableBall(Point center, double radius) {
        super(center, radius);
        // 속도 초기화
        velocity = new Vector2D(0.0, 0.0);
    }

    public MovableBall(Point center, double radius, Color color) {
        super(center, radius, color);
        // 속도 초기화
        velocity = new Vector2D(0.0, 0.0);
    }

    public MovableBall(Point center, double radius, Color color, Vector2D velocity) {
        super(center, radius, color);
        // 속도 초기화
        this.velocity = velocity;
    }

    // 시간 기반 이동 + 경계 충돌 검사
    public void move(double deltaTime) {
        Point currentCenter = getCenter();
        Vector2D displacement = new Vector2D(velocity.getX() * deltaTime, velocity.getY() * deltaTime);
        Point newCenter = currentCenter.add(displacement);

        // 경계 충돌 검사 및 처리
        newCenter = checkBoundaryCollision(newCenter);

        moveTo(newCenter);
    }

    public void move() {
        // 기본 60 FPS 가정
        // 메서드 활용
        move((double) 1 /60);
    }

    public void setVelocity(Vector2D vector2D) {
        this.velocity = vector2D;
    }

    public Vector2D getVelocity(){
        return velocity;
    }

    // 경계 충돌 검사 및 속도 반전
    private Point checkBoundaryCollision(Point newCenter) {
        double radius = getRadius();
        double x = newCenter.getX();
        double y = newCenter.getY();

        // 왼쪽 경계 충돌
        if (x - radius <= 0) {
            x = radius;
            velocity = new Vector2D(-velocity.getX(), velocity.getY());
        }
        // 오른쪽 경계 충돌 (World 너비 가정: 800)
        else if (x + radius >= 800) {
            x = 800 - radius;
            velocity = new Vector2D(-velocity.getX(), velocity.getY());
        }

        // 위쪽 경계 충돌
        if (y - radius <= 0) {
            y = radius;
            velocity = new Vector2D(velocity.getX(), -velocity.getY());
        }
        // 아래쪽 경계 충돌 (World 높이 가정: 600)
        else if (y + radius >= 600) {
            y = 600 - radius;
            velocity = new Vector2D(velocity.getX(), -velocity.getY());
        }

        return new Point(x, y);
    }
}
