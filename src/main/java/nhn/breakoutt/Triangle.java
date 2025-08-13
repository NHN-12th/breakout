package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle implements Paintable, Movable, Collidable {
    private Point vertex1;  // 첫 번째 꼭짓점
    private Point vertex2;  // 두 번째 꼭짓점
    private Point vertex3;  // 세 번째 꼭짓점
    private double dx;      // X 방향 속도
    private double dy;      // Y 방향 속도
    private Color color;    // 색상
    private CollisionAction collisionAction; // 충돌 액션
    private boolean destroyed = false;

    /**
     * 3개의 꼭짓점으로 삼각형을 생성합니다.
     * @param vertex1 첫 번째 꼭짓점
     * @param vertex2 두 번째 꼭짓점
     * @param vertex3 세 번째 꼭짓점
     */
    public Triangle(Point vertex1, Point vertex2, Point vertex3) {
        this(vertex1, vertex2, vertex3, 0, 0, Color.GREEN, CollisionAction.BOUNCE);
    }

    /**
     * 완전한 생성자
     */
    public Triangle(Point vertex1, Point vertex2, Point vertex3,
                   double dx, double dy, Color color, CollisionAction collisionAction) {
        if (vertex1 == null || vertex2 == null || vertex3 == null) {
            throw new IllegalArgumentException("꼭짓점은 null일 수 없습니다");
        }
        if (color == null) {
            throw new IllegalArgumentException("색상은 null일 수 없습니다");
        }
        if (collisionAction == null) {
            throw new IllegalArgumentException("충돌 액션은 null일 수 없습니다");
        }

        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.dx = dx;
        this.dy = dy;
        this.color = color;
        this.collisionAction = collisionAction;
    }

    /**
     * 중심점과 크기로 정삼각형을 생성하는 편의 생성자
     * @param centerX 중심 X 좌표
     * @param centerY 중심 Y 좌표
     * @param size 삼각형 크기 (반지름)
     */
    public Triangle(double centerX, double centerY, double size) {
        this(centerX, centerY, size, 0, 0, Color.GREEN, CollisionAction.BOUNCE);
    }

    public Triangle(double centerX, double centerY, double size, Color color){
        this(centerX, centerY, size, 0, 0, color, CollisionAction.BOUNCE);
    }

    public Triangle(double centerX, double centerY, double size,
                   double dx, double dy, Color color, CollisionAction collisionAction) {
        // 정삼각형의 꼭짓점 계산 (위쪽을 향하는 삼각형)
        double height = size * Math.sqrt(3) / 2; // 정삼각형의 높이

        this.vertex1 = new Point(centerX, centerY - height / 2);           // 위쪽 꼭짓점
        this.vertex2 = new Point(centerX - size / 2, centerY + height / 2); // 왼쪽 아래
        this.vertex3 = new Point(centerX + size / 2, centerY + height / 2); // 오른쪽 아래

        this.dx = dx;
        this.dy = dy;
        this.color = color != null ? color : Color.GREEN;
        this.collisionAction = collisionAction != null ? collisionAction : CollisionAction.BOUNCE;
    }

    // Paintable 구현
    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(color);

        // 다각형 그리기
        double[] xPoints = {vertex1.getX(), vertex2.getX(), vertex3.getX()};
        double[] yPoints = {vertex1.getY(), vertex2.getY(), vertex3.getY()};

        gc.fillPolygon(xPoints, yPoints, 3);

        // 테두리 그리기
        gc.setStroke(color.darker());
        gc.setLineWidth(1);
        gc.strokePolygon(xPoints, yPoints, 3);
    }

    // Movable 구현
    @Override
    public void move(double deltaTime) {
        // 모든 꼭짓점을 같은 거리만큼 이동
        double moveX = dx * deltaTime;
        double moveY = dy * deltaTime;

        vertex1 = new Point(vertex1.getX() + moveX, vertex1.getY() + moveY);
        vertex2 = new Point(vertex2.getX() + moveX, vertex2.getY() + moveY);
        vertex3 = new Point(vertex3.getX() + moveX, vertex3.getY() + moveY);
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
        // 삼각형을 포함하는 최소 사각형 계산
        double minX = Math.min(Math.min(vertex1.getX(), vertex2.getX()), vertex3.getX());
        double maxX = Math.max(Math.max(vertex1.getX(), vertex2.getX()), vertex3.getX());
        double minY = Math.min(Math.min(vertex1.getY(), vertex2.getY()), vertex3.getY());
        double maxY = Math.max(Math.max(vertex1.getY(), vertex2.getY()), vertex3.getY());

        return new RectangleBounds(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public boolean isColliding(Boundable other) {
        return getBounds().intersects(other.getBounds());
    }

    @Override
    public void handleCollision(Collidable other) {
        switch (collisionAction) {
            case BOUNCE:
                dx = -dx;
                dy = -dy;
                break;
            case DESTROY:
                destroyed = true;
                break;
            case STOP:
                dx = 0;
                dy = 0;
                break;
            case PASS:
                // 통과 - 아무것도 하지 않음
                break;
            case CUSTOM:
                handleCustomCollision(other);
                break;
        }
    }

    @Override
    public CollisionAction getCollisionAction() {
        return collisionAction;
    }

    protected void handleCustomCollision(Collidable other) {
        // 기본 구현: 반사
        dx = -dx;
        dy = -dy;
    }

    // 추가 메서드들
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("색상은 null일 수 없습니다");
        }
        this.color = color;
    }

    public void setCollisionAction(CollisionAction collisionAction) {
        if (collisionAction == null) {
            throw new IllegalArgumentException("충돌 액션은 null일 수 없습니다");
        }
        this.collisionAction = collisionAction;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /**
     * 삼각형의 중심점을 계산합니다.
     */
    public Point getCenter() {
        double centerX = (vertex1.getX() + vertex2.getX() + vertex3.getX()) / 3;
        double centerY = (vertex1.getY() + vertex2.getY() + vertex3.getY()) / 3;
        return new Point(centerX, centerY);
    }

    /**
     * 삼각형을 새로운 위치로 이동시킵니다.
     */
    public void moveTo(Point newCenter) {
        Point currentCenter = getCenter();
        double deltaX = newCenter.getX() - currentCenter.getX();
        double deltaY = newCenter.getY() - currentCenter.getY();

        vertex1 = new Point(vertex1.getX() + deltaX, vertex1.getY() + deltaY);
        vertex2 = new Point(vertex2.getX() + deltaX, vertex2.getY() + deltaY);
        vertex3 = new Point(vertex3.getX() + deltaX, vertex3.getY() + deltaY);
    }

    /**
     * 꼭짓점들을 반환합니다.
     */
    public Point[] getVertices() {
        return new Point[]{vertex1, vertex2, vertex3};
    }

    @Override
    public String toString() {
        return String.format("Triangle[center=%.1f,%.1f, vertices=[%.1f,%.1f %.1f,%.1f %.1f,%.1f], color=%s, action=%s]",
            getCenter().getX(), getCenter().getY(),
            vertex1.getX(), vertex1.getY(),
            vertex2.getX(), vertex2.getY(),
            vertex3.getX(), vertex3.getY(),
            color, collisionAction);
    }

    public double getX() {
        return getCenter().getX();
    }

    public double getY() {
        return getCenter().getY();
    }
}
