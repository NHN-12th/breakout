package nhn.breakoutt;

import javafx.scene.paint.Color;

public class PaintableBoundedBox extends PaintableBox{
    public PaintableBoundedBox(Point position, double width, double height) {
        super(position, width, height);
    }

    public PaintableBoundedBox(Point position, double width, double height, Color color) {
        super(position, width, height, color);
    }

    public PaintableBoundedBox(double x, double  y, double width, double height, Color color){
        this(new Point(x, y), width, height, color);
    }
}
