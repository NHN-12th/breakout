package nhn.breakoutt;

public abstract class Shape {
    protected Point position;

    public abstract double getArea();

    public abstract String getShapeType();

    public abstract double getPerimeter();

    public abstract double getWidth();

    public abstract double getHeight();

    public Shape(Point point){
        this.position = point;
    }
    public void moveBy(Vector2D delta) {
        this.position = new Point(
                position.getX() + delta.getX(),
                position.getY() + delta.getY()
        );
    }

    public Point getPosition(){
        return position;
    }

    public void moveTo(Point newPosition){
        this.position = newPosition;
    }
}