package nhn.breakoutt;

public interface Boundable {
    Bounds getBounds();
    boolean isColliding(Boundable other);
}
