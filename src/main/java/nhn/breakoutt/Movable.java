package nhn.breakoutt;

public interface Movable {
    void move(double deltaTime);
    double getDx();
    double getDy();
    void setDx(double dx);
    void setDy(double dy);
}
