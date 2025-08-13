package nhn.breakoutt;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GameLoop extends AnimationTimer {
    private double lastUpdate;
    private World world;
    private GraphicsContext gc;

    @Override
    public void handle(long low) {
        double now = System.currentTimeMillis();
        double deltaTime = (now - lastUpdate) / 1_000_000_000.0;

        update(deltaTime);
        render();
    }

    private void render() {
        world = new World(800, 600);
        Canvas canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        world.draw(gc);
    }

    private void update(double deltaTime) {
        for(Ball ball: world.getBalls()){
            if(ball instanceof MovableBall movableBall){
                movableBall.move(deltaTime);
            }
        }
    }
}
