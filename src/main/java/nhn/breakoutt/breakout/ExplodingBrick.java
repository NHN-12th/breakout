package nhn.breakoutt.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nhn.breakoutt.*;
import java.util.ArrayList;
import java.util.List;

public class ExplodingBrick extends SimpleBrick implements Exploding {
    private double explosionRadius;
    private int explosionDamage;

    public ExplodingBrick(double x, double y, double width, double height, Color color, int points) {
        super(x, y, width, height, color, points);
        this.explosionRadius = 80.0; // 폭발 반경
        this.explosionDamage = 1;    // 폭발 피해량
    }

    @Override
    public int getExplosionDamage() {
        return explosionDamage;
    }

    @Override
    public List<Breakable> explode(List<? extends Boundable> allObjects) {
        List<Breakable> affectedObjects = new ArrayList<>();
        Point center = getCenter();

        for (Boundable obj : allObjects) {
            if (obj == this) continue; // 자기 자신 제외

            if (obj instanceof Breakable breakable) {
                if (breakable.isBroken()) continue; // 이미 파괴된 객체 제외

                // 거리 계산
                Point objCenter = getObjectCenter(obj);
                double distance = center.distanceTo(objCenter);

                if (distance <= explosionRadius) {
                    affectedObjects.add(breakable);
                }
            }
        }

        return affectedObjects;
    }

    @Override
    public ExplosionEffect createExplosionEffect() {
        Point center = getCenter();
        return new ExplosionEffect(center.getX(), center.getY(), explosionRadius);
    }

    private Point getObjectCenter(Boundable obj) {
        Bounds bounds = obj.getBounds();
        return new Point(bounds.getCenterX(), bounds.getCenterY());
    }

    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Ball && !destroyed) {
            hit(1);
            // 파괴되면 폭발 효과는 게임 로직에서 처리
        }
    }

    @Override
    public void paint(GraphicsContext gc) {
        if (destroyed) return;

        // 기본 벽돌 그리기
        super.paint(gc);

        // 폭발 벽돌임을 나타내는 특수 표시
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);

        // 폭발 심볼 (작은 별)
        double centerX = x + width/2;
        double centerY = y + height/2;
        double size = Math.min(width, height) * 0.3;

        // 별 모양 그리기
        drawExplosionSymbol(gc, centerX, centerY, size);
    }

    private void drawExplosionSymbol(GraphicsContext gc, double centerX, double centerY, double size) {
        // 간단한 폭발 심볼 (X 모양)
        gc.strokeLine(centerX - size/2, centerY - size/2, centerX + size/2, centerY + size/2);
        gc.strokeLine(centerX + size/2, centerY - size/2, centerX - size/2, centerY + size/2);

        // 추가 선들로 폭발 효과 강화
        gc.strokeLine(centerX, centerY - size/2, centerX, centerY + size/2);
        gc.strokeLine(centerX - size/2, centerY, centerX + size/2, centerY);
    }
}
