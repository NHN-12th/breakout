package nhn.breakoutt.breakout;

public interface Breakable {
    void hit(int damage);
    boolean isBroken();
    int getPoints();
}
