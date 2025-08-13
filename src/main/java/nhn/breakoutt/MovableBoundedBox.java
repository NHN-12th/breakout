package nhn.breakoutt;

import javafx.scene.paint.Color;

public class MovableBoundedBox extends MovableBox{
    public MovableBoundedBox(Point position, double width, double height) {
        super(position, width, height);
    }

    public MovableBoundedBox(Point position, double width, double height, Color color) {
        super(position, width, height, color);
    }

    public MovableBoundedBox(Point position, double width, double height, Color color, Vector2D velocity) {
        super(position, width, height, color, velocity);
    }

    public MovableBoundedBox(double x, double y, double width, double height){
        this(new Point(x, y), width, height);
    }
}
