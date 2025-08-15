package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class MixedWorld {
    private List<Ball> balls;
    private List<Box> boxes;
    private double width;
    private double height;

    public MixedWorld(double width, double height) {
        balls = new ArrayList<>();
        boxes = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getBallCount(){
            return balls.size();
    }

    public int getBoxCount(){
        return boxes.size();
    }

    public void addBall(Ball ball){
        if(ball == null) throw new IllegalArgumentException("ball is null");
        balls.add(ball);
    }

    public void addBox(Box box){
        if(box == null) throw new IllegalArgumentException("box is null");
        boxes.add(box);
    }

    public void update(double deltaTime){
        for(var a: balls){
            if(a instanceof MovableBall){
                ((MovableBall) a).move();
            }
        }

        for(var a:boxes){
            if(a instanceof MovableBox){
                ((MovableBox) a).move(deltaTime);
            }
        }
    }

    public void render(GraphicsContext gc){
        gc.clearRect(0,0,width, height);

        for(var a: balls){
            if(a instanceof PaintableBall){
                ((PaintableBall) a).paint(gc);
            }
        }

        for(var a:boxes){
            if(a instanceof PaintableBox){
                ((PaintableBox) a).paint(gc);
            }
        }
    }
}
