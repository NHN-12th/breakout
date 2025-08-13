package nhn.breakoutt.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nhn.breakoutt.*;

public class SimpleBrick extends StaticObject implements Breakable {
    protected int hitPoints;
    protected int maxHitPoints;
    protected int points;

    public SimpleBrick(double x, double y, double width, double height, Color color, int points) {
        super(x, y, width, height, color);
        this.hitPoints = 1;
        this.maxHitPoints = 1;
        this.points = points;
        setCollisionAction(CollisionAction.CUSTOM);
    }

    @Override
    public void hit(int damage) {
        if (destroyed) return;

        hitPoints -= damage;
        if (hitPoints <= 0) {
            hitPoints = 0;
            destroyed = true;
        }
    }

    @Override
    public boolean isBroken() {
        return destroyed;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Ball) {
            hit(1);
        }
    }

    @Override
    public void paint(GraphicsContext gc) {
        if (destroyed) return;

        // 3D 효과를 위한 그림자
        gc.setFill(color.darker().darker());
        gc.fillRect(x + 2, y + 2, width, height);

        // 본체
        gc.setFill(color);
        gc.fillRect(x, y, width, height);

        // 하이라이트 (3D 효과)
        gc.setFill(color.brighter());
        gc.fillRect(x, y, width, 3); // 상단 하이라이트
        gc.fillRect(x, y, 3, height); // 좌측 하이라이트

        // 테두리
        gc.setStroke(color.darker());
        gc.setLineWidth(1);
        gc.strokeRect(x, y, width, height);
    }
}
