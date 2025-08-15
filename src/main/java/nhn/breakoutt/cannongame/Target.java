package nhn.breakoutt.cannongame;

import nhn.breakoutt.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Target implements Paintable {
    private final double x, y;
    private final double width, height;
    private int points; // 파괴 시 획득 점수
    private final TargetType type; // 목표물 유형
    private double health; // 현재 내구도
    private double maxHealth; // 최대 내구도
    private boolean isDestroyed; // 파괴 여부

    public Target(double x, double y, double width, double height, TargetType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.isDestroyed = false;

        // 타입별 특성 설정
        switch (type) {
            case WOODEN -> {
                points = 50;
                maxHealth = 1.0;
            }
            case STONE -> {
                points = 100;
                maxHealth = 2.0;
            }
        }
        this.health = this.maxHealth;
    }

    @Override
    public void paint(GraphicsContext gc) {
        if (isDestroyed) return;

        // 손상도에 따른 색상 변화
        double healthRatio = health / maxHealth;
        Color baseColor = getBaseColor();
        Color currentColor = baseColor.deriveColor(0, 1, healthRatio, 1);

        // 목표물 본체 그리기
        gc.setFill(currentColor);
        gc.fillRect(x, y, width, height);

        // 테두리 그리기
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, width, height);
    }

    private Color getBaseColor() {
        return switch (type) {
            case WOODEN -> Color.BROWN;
            case STONE -> Color.GRAY;
        };
    }

    public void takeDamage(double damage) {
        if (isDestroyed) return;

        health -= damage;
        if (health <= 0) {
            isDestroyed = true;
        }
    }

    public boolean intersects(double otherX, double otherY, double radius) {
        // 원과 사각형의 충돌 검사
        double closestX = Math.max(x, Math.min(otherX, x + width));
        double closestY = Math.max(y, Math.min(otherY, y + height));

        double distanceSquared = Math.pow(otherX - closestX, 2) + Math.pow(otherY - closestY, 2);
        return distanceSquared <= Math.pow(radius, 2);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public int getPoints() { return points; }
    public boolean isDestroyed() { return isDestroyed; }

    public Point getCenter() {
        return new Point(x + width/2, y + height/2);
    }
}
