package nhn.breakoutt;

import javafx.scene.paint.Color;

public class BoundedBall extends MovableBall{
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;
    private boolean boundsSet = false; // 경계 설정 여부 추적

    public BoundedBall(Point center, double radius, Color color) {
        super(center, radius, color);
        this.minX = Double.NEGATIVE_INFINITY;
        this.minY = Double.NEGATIVE_INFINITY;
        this.maxX = Double.POSITIVE_INFINITY;
        this.maxY = Double.POSITIVE_INFINITY;
    }

    public BoundedBall(Point center, double radius){
        super(center, radius);
        this.minX = Double.NEGATIVE_INFINITY;
        this.minY = Double.NEGATIVE_INFINITY;
        this.maxX = Double.POSITIVE_INFINITY;
        this.maxY = Double.POSITIVE_INFINITY;
    }

    // 경계 설정 시 공의 중심이 이동 가능한 범위
    public void setBounds(double minX, double minY, double maxX, double maxY) {
        this.minX = minX + getRadius();
        this.maxX = maxX - getRadius();

        this.minY = minY + getRadius();
        this.maxY = maxY - getRadius();

        this.boundsSet = true; // 경계 설정됨을 표시
    }

    // move 메서드에서 경계 충돌 처리
    @Override
    public void move(double deltaTime) {
        // 경계가 설정되지 않은 경우 부모 클래스의 move 사용
        if (!boundsSet) {
            super.move(deltaTime);
            return;
        }

        // 현재 위치와 속도
        Point currentCenter = getCenter();
        Vector2D currentVelocity = getVelocity();

        // 다음 위치 계산
        Point nextPoint = currentCenter.add(currentVelocity.multiply(deltaTime));
        Vector2D newVelocity = currentVelocity;

        // X축 경계 충돌 검사 및 처리
        if (nextPoint.getX() <= minX) {
            // 왼쪽 벽 충돌
            newVelocity = new Vector2D(-newVelocity.getX(), newVelocity.getY());
            nextPoint = new Point(minX, nextPoint.getY());
        } else if (nextPoint.getX() >= maxX) {
            // 오른쪽 벽 충돌
            newVelocity = new Vector2D(-newVelocity.getX(), newVelocity.getY());
            nextPoint = new Point(maxX, nextPoint.getY());
        }

        // Y축 경계 충돌 검사 및 처리
        if (nextPoint.getY() <= minY) {
            // 위쪽 벽 충돌
            newVelocity = new Vector2D(newVelocity.getX(), -newVelocity.getY());
            nextPoint = new Point(nextPoint.getX(), minY);
        } else if (nextPoint.getY() >= maxY) {
            // 아래쪽 벽 충돌
            newVelocity = new Vector2D(newVelocity.getX(), -newVelocity.getY());
            nextPoint = new Point(nextPoint.getX(), maxY);
        }

        // 속도와 위치 업데이트
        setVelocity(newVelocity);
        moveTo(nextPoint);
    }

}
