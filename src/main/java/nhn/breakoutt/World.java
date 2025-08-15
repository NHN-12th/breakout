package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class World {
    private final double width;
    private final double height;

    private final List<PaintableBall> balls;

    public World(double width, double height) {
        if(width <= 0 || height <= 0) throw new IllegalArgumentException();
        this.width = width;
        this.height = height;
        this.balls = new ArrayList<>();
    }

    public void add(PaintableBall ball) {
        if(ball == null) throw new IllegalArgumentException();
        if(!isInBounds(ball)) throw new IllegalArgumentException();
        balls.add(ball);  // 색상 정보를 보존하며 직접 추가
    }

    public void add(Ball ball) {
        if(ball == null) throw new IllegalArgumentException();
        if(!isInBounds(ball)) throw new IllegalArgumentException();
        balls.add(new PaintableBall(ball));
    }

    public void remove(Ball ball) {
        if(ball == null) throw new IllegalArgumentException();

        // PaintableBall인 경우 직접 제거
        if(ball instanceof PaintableBall) {
            if(!balls.remove(ball)) {
                throw new NoSuchElementException();
            }
            return;
        }

        // Ball인 경우 해당하는 PaintableBall 찾아서 제거
        PaintableBall toRemove = null;
        for(PaintableBall paintableBall : balls) {
            if(paintableBall.getCenter().equals(ball.getCenter()) &&
               Math.abs(paintableBall.getRadius() - ball.getRadius()) < 0.001) {
                toRemove = paintableBall;
                break;
            }
        }

        if(toRemove == null) {
            throw new NoSuchElementException();
        }

        balls.remove(toRemove);
    }

    public void clear(){
        balls.clear();
    }

    public List<Ball> getBalls(){
        return new ArrayList<>(balls);
    }

    public int getBallCount(){
        return balls.size();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, width, height);
        for(var a: balls){
            a.paint(gc);
        }
    }

    private boolean isInBounds(Ball ball){
        return (ball.getCenter().getX() >= ball.getRadius() && ball.getCenter().getX() <= width - ball.getRadius()) && (ball.getCenter().getY() >= ball.getRadius() && ball.getCenter().getY() <= height - ball.getRadius());
    }
}
