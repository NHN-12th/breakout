package nhn.breakoutt;

public class MovableWorld extends World{

    public MovableWorld(double width, double height) {
        super(width, height);
    }

    public void update(double deltaTime){
        for(Ball ball: getBalls()){
            if(ball instanceof MovableBall movableBall){
                movableBall.move(deltaTime);
            }
        }
    }

    public void move(double deltaTime) {
        update(deltaTime);
    }
}
