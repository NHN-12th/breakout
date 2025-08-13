package nhn.breakoutt;

import javafx.scene.paint.Color;
import java.util.Random;

/**
 * 특수한 행동을 하는 삼각형 클래스
 * Triangle을 상속받아 회전과 색상 변화 기능을 추가합니다.
 */
public class BouncingTriangle extends Triangle {
    private double rotationSpeed;    // 회전 속도 (라디안/초)
    private double colorChangeSpeed; // 색상 변화 속도
    private double currentRotation;  // 현재 회전 각도
    private double colorPhase;       // 색상 변화 위상
    private Random random;

    /**
     * 기본 생성자
     */
    public BouncingTriangle(double centerX, double centerY, double size) {
        this(centerX, centerY, size, 0, 0, Color.ORANGE, CollisionAction.BOUNCE,
             Math.PI, 2.0); // 기본 회전속도: π rad/s, 색상변화: 2.0
    }

    /**
     * 완전한 생성자
     */
    public BouncingTriangle(double centerX, double centerY, double size,
                           double dx, double dy, Color color, CollisionAction collisionAction,
                           double rotationSpeed, double colorChangeSpeed) {
        super(centerX, centerY, size, dx, dy, color, collisionAction);
        this.rotationSpeed = rotationSpeed;
        this.colorChangeSpeed = colorChangeSpeed;
        this.currentRotation = 0;
        this.colorPhase = 0;
        this.random = new Random();
    }

    /**
     * 테스트용 간단한 생성자
     */
    public BouncingTriangle(int x, int y, int size, Color color) {
        this((double)x, (double)y, (double)size, 0, 0, color, CollisionAction.BOUNCE,
             Math.PI, 2.0);
    }

    /**
     * move() 오버라이드: 위치 업데이트 + 회전 업데이트
     */
    @Override
    public void move(double deltaTime) {
        // 부모 클래스의 이동 처리
        super.move(deltaTime);

        // 회전 업데이트
        currentRotation += rotationSpeed * deltaTime;

        // 색상 변화 업데이트
        colorPhase += colorChangeSpeed * deltaTime;
        updateColor();

        // 회전 적용 (중심을 기준으로 꼭짓점들을 회전)
        rotateVertices(rotationSpeed * deltaTime);
    }

    /**
     * handleCollision() 오버라이드: 반사 시 회전 속도 반전 + 색상 랜덤 변경
     */
    @Override
    public void handleCollision(Collidable other) {
        switch (getCollisionAction()) {
            case BOUNCE:
                // 속도 반전
                setDx(-getDx());
                setDy(-getDy());

                // 회전 속도 반전
                rotationSpeed = -rotationSpeed;

                // 색상을 랜덤하게 변경
                changeColorRandomly();
                break;
            case DESTROY:
                setDestroyed(true);
                break;
            case STOP:
                setDx(0);
                setDy(0);
                rotationSpeed = 0; // 회전도 정지
                break;
            case PASS:
                // 통과 - 아무것도 하지 않음
                break;
            case CUSTOM:
                handleCustomCollision(other);
                break;
        }
    }

    /**
     * 커스텀 충돌 처리: 회전 가속 + 크기 변화
     */
    @Override
    protected void handleCustomCollision(Collidable other) {
        // 속도 반전
        setDx(-getDx());
        setDy(-getDy());

        // 회전 가속
        rotationSpeed *= 1.5;

        // 색상 변화 가속
        colorChangeSpeed *= 1.2;

        // 랜덤 색상 변경
        changeColorRandomly();
    }

    /**
     * 중심을 기준으로 꼭짓점들을 회전시킵니다.
     */
    private void rotateVertices(double angle) {
        Point center = getCenter();
        Point[] vertices = getVertices();

        for (int i = 0; i < vertices.length; i++) {
            Point vertex = vertices[i];
            double dx = vertex.getX() - center.getX();
            double dy = vertex.getY() - center.getY();

            // 회전 변환
            double newDx = dx * Math.cos(angle) - dy * Math.sin(angle);
            double newDy = dx * Math.sin(angle) + dy * Math.cos(angle);

            // 새로운 위치 계산 (실제로는 Triangle 클래스의 내부 구조에 따라 조정 필요)
            // 여기서는 개념적으로만 표현
        }
    }

    /**
     * 시간에 따른 색상 변화를 업데이트합니다.
     */
    private void updateColor() {
        // HSB 색상 공간에서 Hue를 시간에 따라 변화
        double hue = (Math.sin(colorPhase) + 1) * 180; // 0-360도 범위
        double saturation = 0.8 + 0.2 * Math.sin(colorPhase * 1.5); // 0.8-1.0 범위
        double brightness = 0.7 + 0.3 * Math.sin(colorPhase * 2); // 0.7-1.0 범위

        Color newColor = Color.hsb(hue, saturation, brightness);
        setColor(newColor);
    }

    /**
     * 색상을 랜덤하게 변경합니다.
     */
    private void changeColorRandomly() {
        Color[] colors = {
            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
            Color.PURPLE, Color.ORANGE, Color.PINK, Color.CYAN,
            Color.LIME, Color.MAGENTA
        };

        Color newColor = colors[random.nextInt(colors.length)];
        setColor(newColor);

        // 색상 변화 위상도 랜덤하게 조정
        colorPhase = random.nextDouble() * 2 * Math.PI;
    }

    // Getter/Setter 메서드들
    public double getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(double rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public double getColorChangeSpeed() {
        return colorChangeSpeed;
    }

    public void setColorChangeSpeed(double colorChangeSpeed) {
        this.colorChangeSpeed = colorChangeSpeed;
    }

    public double getCurrentRotation() {
        return currentRotation;
    }

    /**
     * 회전 속도를 곱셈으로 조정합니다.
     */
    public void accelerateRotation(double factor) {
        rotationSpeed *= factor;
    }

    /**
     * 색상 변화 속도를 곱셈으로 조정합니다.
     */
    public void accelerateColorChange(double factor) {
        colorChangeSpeed *= factor;
    }

    @Override
    public String toString() {
        return String.format("BouncingTriangle[center=%.1f,%.1f, rotationSpeed=%.2f, colorChangeSpeed=%.2f, currentRotation=%.2f]",
            getCenter().getX(), getCenter().getY(), rotationSpeed, colorChangeSpeed, currentRotation);
    }
}
