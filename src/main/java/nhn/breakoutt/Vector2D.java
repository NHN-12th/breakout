package nhn.breakoutt;

public class Vector2D extends Vector{
    private final double x;
    private final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public Vector2D add(Vector2D other){
        return new Vector2D(x + other.getX(), y + other.getY());
    }

    public Vector2D subtract(Vector2D other){
        return new Vector2D(x - other.getX(), y - other.getY());
    }

    public Vector2D multiply(double scalar){
        return new Vector2D(x * scalar, y * scalar);
    }

    @Override
    public double get(int index) {
        if (index == 0) {
            return x;
        } else if (index == 1) {
            return y;
        }
        throw new IndexOutOfBoundsException("Index out of bounds for Vector2D");
    }

    @Override
    public void set(int index, double value) {
        throw new UnsupportedOperationException("Vector2D is immutable");
    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    protected Vector createNew(double... values) {
        if (values.length != 2) {
            throw new IllegalArgumentException("Vector2D requires two values.");
        }
        return new Vector2D(values[0], values[1]);
    }

    public double magnitude(){
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D normalize(){
        if(magnitude() == 0.0) return new Vector2D(0.0, 0.0);
        return new Vector2D(x / magnitude(), y / magnitude());
    }

    public double dot(Vector2D other){
        return x * other.getX() + y * other.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("Vector2D(%.2f, %.2f)", x, y);
    }

    public double angle() {
        return Math.atan2(y, x);
    }

    public double cross(Vector2D other) {
        return this.x * other.y - this.y * other.x;
    }

    public double distance(Vector2D other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    public Vector2D project(Vector2D other) {
        double scalar = this.dot(other) / other.dot(other);
        return other.multiply(scalar);
    }

    public Vector2D rotate(double angle) {
        double newX = x * Math.cos(angle) - y * Math.sin(angle);
        double newY = x * Math.sin(angle) + y * Math.cos(angle);
        return new Vector2D(newX, newY);
    }

    public Vector2D divide(double scalar) {
        if (scalar == 0) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        return new Vector2D(x / scalar, y / scalar);
    }

    public static Vector2D fromPolar(double magnitude, double angle) {
        return new Vector2D(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
    }

    public static Vector2D zero() {
        return new Vector2D(0, 0);
    }

    public static Vector2D unitX() {
        return new Vector2D(1, 0);
    }

    public static Vector2D unitY() {
        return new Vector2D(0, 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector2D vector2D = (Vector2D) obj;
        return Double.compare(vector2D.x, x) == 0 &&
                Double.compare(vector2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }
}
