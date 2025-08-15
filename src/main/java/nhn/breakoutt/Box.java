package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Box implements Paintable, Collidable{
    private Point center;
    private double width;
    private double height;
    private Color color = Color.BLUE;
    private CollisionAction collisionAction = CollisionAction.BOUNCE;

    public Box(int i, int i1, int i2, int i3, Color blue) {
        this(new Point((double) i, (double) i1), (double)i2, (double)i3 );
        this.color = blue;
    }

    public void setPosition(Point center) {
        if(center == null) throw new IllegalArgumentException("위치는 null일 수 없습니다");
        this.center = center;
    }

    public void setWidth(double width) {
        if(width <= 0) throw new IllegalArgumentException("음수 너비에 대해 예외가 발생하지 않았습니다");
        this.width = width;
    }

    public void setHeight(double height) {
        if(height <= 0) throw new IllegalArgumentException("음수 높이에 대해 예외가 발생하지 않았습니다");
        this.height = height;
    }

    public Box(Point center, double width, double height) {
        if(center == null) throw new IllegalArgumentException("위치는 null일 수 없습니다");
        this.center = center;
        if(width <= 0) throw new IllegalArgumentException("음수 너비에 대해 예외가 발생하지 않았습니다");
        if(height <= 0) throw new IllegalArgumentException("음수 높이에 대해 예외가 발생하지 않았습니다");
        this.width = width;
        this.height = height;
    }

    public Box(double x, double y, double width, double height){
        this.center = new Point(x, y);
        if(width <= 0) throw new IllegalArgumentException("음수 너비에 대해 예외가 발생하지 않았습니다");
        if(height <= 0) throw new IllegalArgumentException("음수 높이에 대해 예외가 발생하지 않았습니다");
        this.width = width;
        this.height = height;
    }

    public Point getCenter() {
        return center;
    }

    public double getX(){
        return center.getX();
    }

    public double getY(){
        return center.getY();
    }

    public void moveTo(Point center){
        if(center == null) return;
        this.center = center;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean contains(double px, double py){
        return px >= center.getX() && px <= center.getX() + width && py >= center.getY() && py <= center.getY() + height;
    }

    public boolean contains(Point point){
        return contains(point.getX(), point.getY());
    }

    // Paintable 구현
    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(center.getX(), center.getY(), width, height);
    }

    // Collidable 구현 (Boundable 포함)
    @Override
    public Bounds getBounds(){
        return new RectangleBounds(center.getX(), center.getY(), width, height);
    }

    @Override
    public boolean isColliding(Boundable other) {
        return getBounds().intersects(other.getBounds());
    }

    @Override
    public void handleCollision(Collidable other) {
    }

    @Override
    public CollisionAction getCollisionAction() {
        return collisionAction;
    }

    // 추가 메서드들
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if(color == null) {
            throw new IllegalArgumentException("색상은 null일 수 없습니다.");
        }
        this.color = color;
    }

    public void setCollisionAction(CollisionAction collisionAction) {
        if(collisionAction == null) {
            throw new IllegalArgumentException("충돌 액션은 null일 수 없습니다.");
        }
        this.collisionAction = collisionAction;
    }

    public Point getPosition() {
        return center;
    }


}
