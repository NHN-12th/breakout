package nhn.breakoutt;

/**
 * 공을 표현하는 추상 클래스입니다.
 * Template Method 패턴을 사용하여 업데이트 프로세스를 정의합니다.
 */
public abstract class AbstractBall {
    protected Point center;
    protected double radius;
    protected Bounds bounds;

    public AbstractBall(Point center, double radius) {
        if (center == null) {
            throw new IllegalArgumentException("중심점은 null일 수 없습니다.");
        }
        if (radius <= 0) {
            throw new IllegalArgumentException("반지름은 양수여야 합니다.");
        }

        this.center = center;
        this.radius = radius;
        updateBounds();
    }

    protected void setX(double x){
        this.center = new Point(x, center.getY());
    }

    protected void setY(double y){
        this.center = new Point(center.getX(), y);
    }

    public double getX(){
        return center.getX();
    }

    public double getY(){
        return center.getY();
    }

    public AbstractBall(double x, double y, double radius) {
        this(new Point(x, y), radius);
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void moveTo(Point newCenter) {
        if (newCenter == null) {
            throw new IllegalArgumentException("새로운 중심점은 null일 수 없습니다.");
        }
        this.center = newCenter;
        updateBounds();
    }

    public void moveTo(double x, double y) {
        moveTo(new Point(x, y));
    }

    public double getArea() {
        return Math.PI * radius * radius;
    }

    public double getCircumference() {
        return Math.PI * 2 * radius;
    }

    public boolean contains(Point point) {
        if (point == null) {
            return false;
        }
        return center.distanceTo(point) <= radius;
    }

    public boolean contains(double x, double y) {
        return this.contains(new Point(x, y));
    }

    public boolean isColliding(AbstractBall other) {
        if (other == null) {
            throw new IllegalArgumentException("다른 공은 null일 수 없습니다.");
        }
        return center.distanceTo(other.getCenter()) < this.getRadius() + other.getRadius();
    }

    public final void update(double deltaTime) {
        beforeUpdate(deltaTime);
        performUpdate(deltaTime);
        afterUpdate(deltaTime);
        updateBounds();
    }

    protected void beforeUpdate(double deltaTime) {
    }

    protected abstract void performUpdate(double deltaTime);

    protected void afterUpdate(double deltaTime) {
    }

    protected void updateBounds() {
        this.bounds = new CircleBounds(center, radius);
    }
}
