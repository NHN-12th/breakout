package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PaintableBox extends MovableBox{
    private Color color;
    public PaintableBox(Point point, double width, double height) {
        super(point, width, height);
        this.color = Color.RED;
    }

    public PaintableBox(Point point, double width, double height, Color color){
        super(point, width, height);
        if(color == null) throw new IllegalArgumentException();
        else this.color = color;
    }

    public PaintableBox(double x, double y, double width, double height){
        this(new Point(x, y), width, height);
    }

    public PaintableBox(double x, double y, double width, double height, Color color){
        this(new Point(x, y), width, height, color);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if(color == null) throw new IllegalArgumentException();
        else this.color = color;
    }

    public void paint(GraphicsContext gc) {
        // 공의 왼쪽 상단 좌표 계산
        Point center = getCenter();
        double rightX = center.getX() + getWidth();
        double rightY = center.getY() + getHeight();

        // 공 채우기
        gc.setFill(this.color);
        gc.fillRect(center.getX(), center.getY(), rightX, rightY);
    }
}
