package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 5각 별 모양 객체 클래스
 * Paintable, Movable, Collidable 인터페이스를 구현합니다.
 */
public class Star implements Paintable, Movable, Collidable {
    private Point center;           // 별의 중심점
    private double outerRadius;     // 외곽 반지름
    private double innerRadius;     // 내부 반지름
    private double rotation;        // 회전 각도 (라디안)
    private double dx;              // X 방향 속도
    private double dy;              // Y 방향 속도
    private Color color;            // 색상
    private CollisionAction collisionAction; // 충돌 액션
    private boolean destroyed = false;

    /**
     * 기본 별 생성자
     * @param centerX 중심 X 좌표
     * @param centerY 중심 Y 좌표
     * @param outerRadius 외곽 반지름
     */
    public Star(double centerX, double centerY, double outerRadius) {
        this(centerX, centerY, outerRadius, outerRadius * 0.4, 0, 0, 0, Color.YELLOW, CollisionAction.BOUNCE);
    }

    /**
     * 완전한 생성자
     */
    public Star(double centerX, double centerY, double outerRadius, double innerRadius,
               double rotation, double dx, double dy, Color color, CollisionAction collisionAction) {
        if (outerRadius <= 0) {
            throw new IllegalArgumentException("외곽 반지름은 양수여야 합니다");
        }
        if (innerRadius <= 0) {
            throw new IllegalArgumentException("내부 반지름은 양수여야 합니다");
        }
        if (innerRadius >= outerRadius) {
            throw new IllegalArgumentException("내부 반지름은 외곽 반지름보다 작아야 합니다");
        }
        if (color == null) {
            throw new IllegalArgumentException("색상은 null일 수 없습니다");
        }
        if (collisionAction == null) {
            throw new IllegalArgumentException("충돌 액션은 null일 수 없습니다");
        }

        this.center = new Point(centerX, centerY);
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
        this.rotation = rotation;
        this.dx = dx;
        this.dy = dy;
        this.color = color;
        this.collisionAction = collisionAction;
    }

    /**
     * 5각 별의 꼭짓점들을 계산합니다.
     * @return 10개의 점 배열 (외곽 5개 + 내부 5개 교대로)
     */
    private Point[] calculateVertices() {
        Point[] vertices = new Point[10];
        double angleStep = Math.PI / 5; // 36도 (2π/10)

        for (int i = 0; i < 10; i++) {
            double angle = rotation + i * angleStep;
            double radius = (i % 2 == 0) ? outerRadius : innerRadius;

            double x = center.getX() + radius * Math.cos(angle);
            double y = center.getY() + radius * Math.sin(angle);
            vertices[i] = new Point(x, y);
        }

        return vertices;
    }

    // Paintable 구현
    @Override
    public void paint(GraphicsContext gc) {
        Point[] vertices = calculateVertices();

        // 별 채우기
        gc.setFill(color);
        double[] xPoints = new double[10];
        double[] yPoints = new double[10];

        for (int i = 0; i < 10; i++) {
            xPoints[i] = vertices[i].getX();
            yPoints[i] = vertices[i].getY();
        }

        gc.fillPolygon(xPoints, yPoints, 10);

        // 테두리 그리기
        gc.setStroke(color.darker());
        gc.setLineWidth(2);
        gc.strokePolygon(xPoints, yPoints, 10);
    }

    // Movable 구현
    @Override
    public void move(double deltaTime) {
        // 위치 업데이트
        double newX = center.getX() + dx * deltaTime;
        double newY = center.getY() + dy * deltaTime;
        center = new Point(newX, newY);
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
        // 별을 포함하는 사각형 (외곽 반지름 기준)
        double minX = center.getX() - outerRadius;
        double minY = center.getY() - outerRadius;
        double width = outerRadius * 2;
        double height = outerRadius * 2;

        return new RectangleBounds(minX, minY, width, height);
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
        // 기본 구현: 반사 + 회전
        dx = -dx;
        dy = -dy;
        rotation += Math.PI / 4; // 45도 회전
    }

    // 회전 관련 메서드들
    public void rotate(double angle) {
        this.rotation += angle;
        // 0 ~ 2π 범위로 정규화
        while (rotation >= 2 * Math.PI) {
            rotation -= 2 * Math.PI;
        }
        while (rotation < 0) {
            rotation += 2 * Math.PI;
        }
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    // 추가 메서드들
    public Point getCenter() {
        return center;
    }

    public void moveTo(Point newCenter) {
        this.center = newCenter;
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(double outerRadius) {
        if (outerRadius <= 0) {
            throw new IllegalArgumentException("외곽 반지름은 양수여야 합니다");
        }
        if (outerRadius <= innerRadius) {
            throw new IllegalArgumentException("외곽 반지름은 내부 반지름보다 커야 합니다");
        }
        this.outerRadius = outerRadius;
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(double innerRadius) {
        if (innerRadius <= 0) {
            throw new IllegalArgumentException("내부 반지름은 양수여야 합니다");
        }
        if (innerRadius >= outerRadius) {
            throw new IllegalArgumentException("내부 반지름은 외곽 반지름보다 작아야 합니다");
        }
        this.innerRadius = innerRadius;
    }

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

    @Override
    public String toString() {
        return String.format("Star[center=%.1f,%.1f, outerRadius=%.1f, innerRadius=%.1f, rotation=%.2f, color=%s, action=%s]",
            center.getX(), center.getY(), outerRadius, innerRadius, rotation, color, collisionAction);
    }
}
