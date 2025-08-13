package nhn.breakoutt;

import javafx.scene.paint.Color;

public class MovableBox extends Box implements Movable {
    private Vector2D velocity;

    public MovableBox(Point position, double width, double height) {
        super(position, width, height);
        this.velocity = new Vector2D(0, 0); // 초기 속도는 0
    }

    public MovableBox(Point position, double width, double height, Vector2D velocity) {
        super(position, width, height);
        this.velocity = velocity != null ? velocity : new Vector2D(0, 0);
    }

    // 색상을 받는 생성자 추가
    public MovableBox(Point position, double width, double height, Color color) {
        super(position, width, height);
        this.velocity = new Vector2D(0, 0);
        setColor(color);
    }

    // 색상과 속도를 모두 받는 생성자 추가
    public MovableBox(Point position, double width, double height, Color color, Vector2D velocity) {
        super(position, width, height);
        this.velocity = velocity != null ? velocity : new Vector2D(0, 0);
        setColor(color);
    }

    public MovableBox(double i1, double i2, double width, double height, Color color){
        this(new Point(i1, i2), width, height, color);
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public double getDx(){
        return velocity.getX();
    }

    public double getDy(){
        return velocity.getY();
    }

    @Override
    public void setDx(double dx) {
        this.velocity = new Vector2D(dx, getDy());
    }

    @Override
    public void setDy(double dy) {
        this.velocity = new Vector2D(getDx(), dy);
    }

    public void setVelocity(Vector2D velocity) {
        if (velocity == null) {
            throw new IllegalArgumentException("속도는 null일 수 없습니다");
        }
        this.velocity = velocity;
    }

    public void move(double deltaTime) {
        if (deltaTime < 0) {
            throw new IllegalArgumentException("시간 간격은 음수일 수 없습니다");
        }

        // 새로운 위치 계산: 현재 위치 + 속도 * 시간
        Point currentPosition = getCenter();
        double newX = currentPosition.getX() + velocity.getX() * deltaTime;
        double newY = currentPosition.getY() + velocity.getY() * deltaTime;

        // 새로운 위치로 이동
        moveTo(new Point(newX, newY));
    }

    public Point getPosition() {
        return getCenter();
    }
}
