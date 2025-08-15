package nhn.breakoutt;

import javafx.scene.paint.Color;

public class PaintableMovableBall extends MovableBall{
    public PaintableMovableBall(Point center, double radius) {
        super(center, radius);
    }

    public PaintableMovableBall(Point center, double radius, Color color) {
        super(center, radius, color);
    }

    public PaintableMovableBall(Point center, double radius, Color color, Vector2D velocity) {
        super(center, radius, color, velocity);
    }
}
