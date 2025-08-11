package nhn.breakoutt;

// Ball 클래스의 기본 구조
public class Ball {
    // 필드 선언 방법
    private double x;        // x 좌표
    private double y;        // y 좌표
    private double radius;   // 반지름

    // 생성자 예시
    public Ball(double x, double y, double radius) {
        // this는 현재 객체를 가리킴
        this.x = x;
        this.y = y;
        if(radius < 0) throw new IllegalArgumentException();
        this.radius = radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }
}