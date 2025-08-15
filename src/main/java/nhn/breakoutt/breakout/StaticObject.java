package nhn.breakoutt.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nhn.breakoutt.*;

public abstract class StaticObject implements Paintable, Collidable {
    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected Color color;
    protected CollisionAction collisionAction;
    protected boolean destroyed;

    public StaticObject(double x, double y, double width, double height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.collisionAction = CollisionAction.BOUNCE;
        this.destroyed = false;
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

        // 테두리
        gc.setStroke(color.brighter());
        gc.setLineWidth(1);
        gc.strokeRect(x, y, width, height);
    }

    @Override
    public Bounds getBounds() {
        return new RectangleBounds(x, y, width, height);
    }

    @Override
    public boolean isColliding(Boundable other) {
        return getBounds().intersects(other.getBounds());
    }

    @Override
    public void handleCollision(Collidable other) {
    }

    @Override
    public CollisionAction getCollisionAction() {
        return collisionAction;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Color getColor() { return color; }
    public boolean isDestroyed() { return destroyed; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setColor(Color color) { this.color = color; }
    public void setDestroyed(boolean destroyed) { this.destroyed = destroyed; }
    public void setCollisionAction(CollisionAction collisionAction) {
        this.collisionAction = collisionAction;
    }

    public Point getCenter() {
        return new Point(x + width/2, y + height/2);
    }
}
