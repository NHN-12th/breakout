package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 특수 효과를 제공하는 영역 클래스
 * Box를 상속받아 특수 효과를 추가합니다.
 */
public class SpecialZone extends Box {

    /**
     * 영역 타입 열거형
     */
    public enum ZoneType {
        SPEED_UP,      // 속도 2배
        SLOW_DOWN,     // 속도 0.5배
        GRAVITY,       // 아래로 당김 (dy += 10)
        ANTI_GRAVITY,  // 위로 밂 (dy -= 10)
        TELEPORT       // 다른 위치로 순간이동
    }

    private ZoneType zoneType;
    private Color effectColor;
    private Point teleportDestination; // TELEPORT 타입일 때 사용

    /**
     * 기본 생성자
     */
    public SpecialZone(double x, double y, double width, double height, ZoneType zoneType) {
        super(x, y, width, height);
        this.zoneType = zoneType;
        this.effectColor = getEffectColor(zoneType);
        setColor(effectColor);
        setCollisionAction(CollisionAction.CUSTOM);
    }

    /**
     * TELEPORT용 생성자
     */
    public SpecialZone(double x, double y, double width, double height, Point teleportDestination) {
        this(x, y, width, height, ZoneType.TELEPORT);
        this.teleportDestination = teleportDestination;
    }

    /**
     * 효과를 적용합니다
     */
    public void applyEffect(Movable movable) {
        if (!(movable instanceof Ball)) {
            return; // Ball에만 효과 적용
        }

        Ball ball = (Ball) movable;

        switch (zoneType) {
            case SPEED_UP:
                ball.setDx(ball.getDx() * 2.0);
                ball.setDy(ball.getDy() * 2.0);
                break;

            case SLOW_DOWN:
                ball.setDx(ball.getDx() * 0.5);
                ball.setDy(ball.getDy() * 0.5);
                break;

            case GRAVITY:
                ball.setDy(ball.getDy() + 10);
                break;

            case ANTI_GRAVITY:
                ball.setDy(ball.getDy() - 10);
                break;

            case TELEPORT:
                if (teleportDestination != null) {
                    ball.setX(teleportDestination.getX());
                    ball.setY(teleportDestination.getY());
                }
                break;
        }
    }

    /**
     * 효과에 따른 색상을 반환합니다
     */
    private Color getEffectColor(ZoneType type) {
        switch (type) {
            case SPEED_UP:
                return Color.RED.deriveColor(0, 1, 1, 0.3); // 빨간색 반투명
            case SLOW_DOWN:
                return Color.BLUE.deriveColor(0, 1, 1, 0.3); // 파란색 반투명
            case GRAVITY:
                return Color.BROWN.deriveColor(0, 1, 1, 0.3); // 갈색 반투명
            case ANTI_GRAVITY:
                return Color.CYAN.deriveColor(0, 1, 1, 0.3); // 시안색 반투명
            case TELEPORT:
                return Color.PURPLE.deriveColor(0, 1, 1, 0.3); // 보라색 반투명
            default:
                return Color.GRAY.deriveColor(0, 1, 1, 0.3);
        }
    }

    /**
     * 특수 시각 효과로 그리기
     */
    @Override
    public void paint(GraphicsContext gc) {
        // 반투명 배경
        gc.setGlobalAlpha(0.3);
        gc.setFill(effectColor);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());

        // 테두리
        gc.setGlobalAlpha(0.8);
        gc.setStroke(effectColor.brighter());
        gc.setLineWidth(2);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());

        // 효과 표시 아이콘
        gc.setGlobalAlpha(1.0);
        drawEffectIcon(gc);

        // 투명도 복원
        gc.setGlobalAlpha(1.0);
    }

    /**
     * 효과 타입에 따른 아이콘을 그립니다
     */
    private void drawEffectIcon(GraphicsContext gc) {
        double centerX = getX() + getWidth() / 2;
        double centerY = getY() + getHeight() / 2;
        double iconSize = Math.min(getWidth(), getHeight()) * 0.3;

        gc.setFill(effectColor.brighter().brighter());
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        switch (zoneType) {
            case SPEED_UP:
                // 화살표 (오른쪽)
                drawArrow(gc, centerX, centerY, iconSize, 0);
                break;

            case SLOW_DOWN:
                // 원 (느림 표시)
                gc.fillOval(centerX - iconSize/2, centerY - iconSize/2, iconSize, iconSize);
                gc.strokeOval(centerX - iconSize/2, centerY - iconSize/2, iconSize, iconSize);
                break;

            case GRAVITY:
                // 아래 화살표
                drawArrow(gc, centerX, centerY, iconSize, Math.PI/2);
                break;

            case ANTI_GRAVITY:
                // 위 화살표
                drawArrow(gc, centerX, centerY, iconSize, -Math.PI/2);
                break;

            case TELEPORT:
                // 별 모양
                drawStar(gc, centerX, centerY, iconSize);
                break;
        }
    }

    /**
     * 화살표를 그립니다
     */
    private void drawArrow(GraphicsContext gc, double x, double y, double size, double angle) {
        double arrowLength = size;
        double arrowWidth = size * 0.6;

        // 화살표 몸체
        double endX = x + arrowLength * Math.cos(angle);
        double endY = y + arrowLength * Math.sin(angle);

        gc.strokeLine(x, y, endX, endY);

        // 화살표 머리
        double headAngle1 = angle + Math.PI * 3/4;
        double headAngle2 = angle - Math.PI * 3/4;
        double headLength = arrowWidth * 0.5;

        gc.strokeLine(endX, endY,
                     endX + headLength * Math.cos(headAngle1),
                     endY + headLength * Math.sin(headAngle1));
        gc.strokeLine(endX, endY,
                     endX + headLength * Math.cos(headAngle2),
                     endY + headLength * Math.sin(headAngle2));
    }

    /**
     * 별 모양을 그립니다
     */
    private void drawStar(GraphicsContext gc, double centerX, double centerY, double size) {
        int numPoints = 5;
        double[] xPoints = new double[numPoints * 2];
        double[] yPoints = new double[numPoints * 2];

        double outerRadius = size / 2;
        double innerRadius = outerRadius * 0.4;

        for (int i = 0; i < numPoints * 2; i++) {
            double angle = i * Math.PI / numPoints;
            double radius = (i % 2 == 0) ? outerRadius : innerRadius;

            xPoints[i] = centerX + radius * Math.cos(angle - Math.PI/2);
            yPoints[i] = centerY + radius * Math.sin(angle - Math.PI/2);
        }

        gc.fillPolygon(xPoints, yPoints, numPoints * 2);
        gc.strokePolygon(xPoints, yPoints, numPoints * 2);
    }

    /**
     * 충돌 처리: 효과 적용
     */
    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Movable) {
            applyEffect((Movable) other);
        }
    }

    // Getter/Setter
    public ZoneType getZoneType() {
        return zoneType;
    }

    public void setZoneType(ZoneType zoneType) {
        this.zoneType = zoneType;
        this.effectColor = getEffectColor(zoneType);
        setColor(effectColor);
    }

    public Point getTeleportDestination() {
        return teleportDestination;
    }

    public void setTeleportDestination(Point teleportDestination) {
        this.teleportDestination = teleportDestination;
    }

    @Override
    public String toString() {
        return String.format("SpecialZone[pos=%.1f,%.1f, size=%.1fx%.1f, type=%s]",
            getX(), getY(), getWidth(), getHeight(), zoneType);
    }
}
