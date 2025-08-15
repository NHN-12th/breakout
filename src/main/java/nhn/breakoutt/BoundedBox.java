package nhn.breakoutt;

public class BoundedBox extends MovableBox{
    public BoundedBox(Point position, double width, double height) {
        super(position, width, height);
    }

    public BoundedBox(Point position, double width, double height, Vector2D velocity) {
        super(position, width, height, velocity);
    }

    public BoundedBox(double x, double y, double width, double height){
        this(new Point(x, y), width, height);
    }
}
