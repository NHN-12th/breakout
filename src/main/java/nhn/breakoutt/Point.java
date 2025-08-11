package nhn.breakoutt;

public class Point {
    private final Double x;
    private final Double y;

    public Point(Double x, Double y) {
        if(x == null || y == null) throw new IllegalArgumentException();
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceTo(Point other) {
        return Math.sqrt(Math.pow(other.x-this.x, 2) + Math.pow(other.y - this.y, 2));
    }
}