package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball implements Paintable, Movable, Collidable {
    private double x;
    private double y;
    private double radius;
    private double dx;
    private double dy;
    private Color color;
    private CollisionAction collisionAction;
    private boolean destroyed = false;

    public Ball(double x, double y, double radius) {
        this(x, y, radius, 0, 0, Color.RED, CollisionAction.BOUNCE);
    }

    public Ball(Point center, double radius) {
        this(center.getX(), center.getY(), radius);
    }

    public Ball(double x, double y, double radius, double dx, double dy, Color color, CollisionAction collisionAction) {
        if(radius <= 0) {
            throw new IllegalArgumentException("반지름은 양수여야 합니다.");
        }
        if(color == null) {
            throw new IllegalArgumentException("색상은 null일 수 없습니다.");
        }
        if(collisionAction == null) {
            throw new IllegalArgumentException("충돌 액션은 null일 수 없습니다.");
        }

        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = dx;
        this.dy = dy;
        this.color = color;
        this.collisionAction = collisionAction;
    }

    public Ball(int i, int i1, int i2, Color red) {
        this((double) i, (double) i1, (double) i2);
        this.color = red;
    }

    // 기존 메서드들
    public Point getCenter() {
        return new Point(x, y);
    }

    public double getRadius() {
        return radius;
    }

    public void moveTo(Point center){
        this.x = center.getX();
        this.y = center.getY();
    }

    public double getArea(){
        return Math.PI * radius * radius;
    }

    public double getPerimeter(){
        return Math.PI * 2 * radius;
    }

    public boolean contains(Point p){
        return getCenter().distanceTo(p) <= radius;
    }

    public boolean contains(double x, double y){
        return this.contains(new Point(x, y));
    }

    public boolean isColliding(Ball other){
        if(other == null) throw new IllegalArgumentException();
        return getCenter().distanceTo(other.getCenter()) < this.getRadius() + other.getRadius();
    }

    // Paintable 구현
    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    // Movable 구현
    @Override
    public void move(double deltaTime) {
        x += dx * deltaTime;
        y += dy * deltaTime;
    }

    @Override
    public double getDx() {
        return dx;
    }

    @Override
    public double getDy() {
        return dy;
    }

    @Override
    public void setDx(double dx) {
        this.dx = dx;
    }

    @Override
    public void setDy(double dy) {
        this.dy = dy;
    }

    // Collidable 구현 (Boundable 포함)
    @Override
    public Bounds getBounds() {
        return new CircleBounds(x, y, radius);
    }

    @Override
    public boolean isColliding(Boundable other) {
        return getBounds().intersects(other.getBounds());
    }

    @Override
    public void handleCollision(Collidable other) {
        switch (collisionAction) {
            case BOUNCE:
                // 반사 로직 - 간단한 속도 반전
                dx = -dx;
                dy = -dy;
                break;
            case DESTROY:
                // 제거 표시
                destroyed = true;
                break;
            case STOP:
                // 정지
                dx = 0;
                dy = 0;
                break;
            case PASS:
                // 통과 - 아무것도 하지 않음
                break;
            case CUSTOM:
                // 사용자 정의 로직
                handleCustomCollision(other);
                break;
        }
    }

    @Override
    public CollisionAction getCollisionAction() {
        return collisionAction;
    }

    // 추가 메서드들
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if(color == null) {
            throw new IllegalArgumentException("색상은 null일 수 없습니다.");
        }
        this.color = color;
    }

    public void setCollisionAction(CollisionAction collisionAction) {
        if(collisionAction == null) {
            throw new IllegalArgumentException("충돌 액션은 null일 수 없습니다.");
        }
        this.collisionAction = collisionAction;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    protected void handleCustomCollision(Collidable other) {
        // 기본 구현: 반사
        dx = -dx;
        dy = -dy;
    }

    @Override
    public String toString(){
        return String.format("Ball(%.2f, %.2f, %.2f, color=%s, action=%s)",
                           x, y, radius, color, collisionAction);
    }
}