package nhn.breakoutt;

public class Rectangle extends Shape{
    private double width;
    private double height;

    public Rectangle(Point point, double width, double height) {
        super(point);
        this.width = width;
        this.height = height;
    }

    public Rectangle(double x, double y, double width, double height) {
        super(new Point(x, y));
        this.width = width;
        this.height = height;
    }



    @Override
    public double getArea() {
        return width * height;
    }

    @Override
    public String getShapeType() {
        return "Rectangle";
    }

    @Override
    public double getPerimeter() {
        return (width + height) * 2;
    }

    public double getWidth(){
        return width;
    }

    public double getHeight(){
        return height;
    }
}
