package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PaintableBall extends Ball{
    private Color color;

    public PaintableBall(Ball ball){
        this(new Point(ball.getCenter().getX(), ball.getCenter().getY()), ball.getRadius());
    }

    public PaintableBall(Point point, double radius){
        this(point.getX(), point.getY(), radius, Color.RED);
    }

    public PaintableBall(Point point, double radius, Color color){
        this(point.getX(), point.getY(), radius, color);
    }

    public PaintableBall(double x, double y, double radius, Color color) {
        super(x, y, radius);
        if(color == null) throw new IllegalArgumentException();
        else this.color = color;
    }

    public Color getColor() {
        return color;  // null 체크 제거 - 생성자에서 이미 null 검증함
    }

    public void setColor(Color color) {
        if(color == null) throw new IllegalArgumentException();
        else this.color = color;
    }

    public void paint(GraphicsContext gc) {
        // 공의 왼쪽 상단 좌표 계산
        Point center = getCenter();
        double leftX = center.getX() - getRadius();
        double topY = center.getY() - getRadius();
        double diameter = getRadius() * 2;

        // 공 채우기
        gc.setFill(this.color);
        gc.fillOval(leftX, topY, diameter, diameter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PaintableBall that = (PaintableBall) o;
        return java.util.Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), color);
    }
}
