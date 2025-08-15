package nhn.breakoutt;

import javafx.scene.paint.Color;

public class PaintableMovableBoundedBox extends PaintableBox{
    public PaintableMovableBoundedBox(Point position, double width, double height) {
        super(position, width, height);
    }

    public PaintableMovableBoundedBox(Point position, double width, double height, Color color) {
        super(position, width, height, color);
    }

    public PaintableMovableBoundedBox(double x, double y, double width, double height, Color color){
        this(new Point(x, y), width, height, color);
    }
}
