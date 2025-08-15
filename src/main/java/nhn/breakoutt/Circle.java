package nhn.breakoutt;

public class Circle extends Shape{
    private double radius;

    public Circle(Point point, double radius) {
        super(point);
        this.radius = radius;
    }

    public Circle(double x, double y, double radius){
        super(new Point(x, y));
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public String getShapeType() {
        return "Circle";
    }

    @Override
    public double getPerimeter() {
        return Math.PI * radius * 2;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    public double getRadius() {
        return radius;
    }
}
