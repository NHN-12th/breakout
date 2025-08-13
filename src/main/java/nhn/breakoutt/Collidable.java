package nhn.breakoutt;

public interface Collidable extends Boundable{
    void handleCollision(Collidable other);
    CollisionAction getCollisionAction();
    void setCollisionAction(CollisionAction action);
}
