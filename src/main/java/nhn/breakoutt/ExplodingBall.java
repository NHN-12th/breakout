package nhn.breakoutt;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 폭발하는 공 클래스
 * Ball을 상속받아 충돌 시 여러 개의 작은 공으로 분해되는 기능을 추가합니다.
 */
public class ExplodingBall extends Ball {
    private boolean hasExploded;        // 폭발 여부
    private int miniballCount;          // 생성할 작은 공 개수
    private Random random;
    private List<Ball> miniBalls;       // 폭발로 생성된 작은 공들

    /**
     * 기본 생성자
     */
    public ExplodingBall(double x, double y, double radius) {
        this(x, y, radius, 0, 0, Color.ORANGE, CollisionAction.CUSTOM, 4);
    }

    public ExplodingBall(double x, double y, double radius, Color color){
        this(x, y, radius, 0, 0, color, CollisionAction.CUSTOM, 4);
    }

    /**
     * 완전한 생성자
     */
    public ExplodingBall(double x, double y, double radius, double dx, double dy,
                        Color color, CollisionAction collisionAction, int miniballCount) {
        super(x, y, radius, dx, dy, color, collisionAction);
        this.hasExploded = false;
        this.miniballCount = Math.max(3, Math.min(5, miniballCount)); // 3-5개 범위로 제한
        this.random = new Random();
        this.miniBalls = new ArrayList<>();
    }

    /**
     * handleCollision() 오버라이드: CUSTOM 액션일 때 폭발
     */
    @Override
    public void handleCollision(Collidable other) {
        switch (getCollisionAction()) {
            case BOUNCE:
                super.handleCollision(other);
                break;
            case DESTROY:
                setDestroyed(true);
                break;
            case STOP:
                setDx(0);
                setDy(0);
                break;
            case PASS:
                // 통과 - 아무것도 하지 않음
                break;
            case CUSTOM:
                if (!hasExploded) {
                    explode();
                }
                break;
        }
    }

    /**
     * 충돌 후 폭발하고 생성된 작은 공들을 반환합니다 (테스트용)
     */
    public List<Ball> handleCollisionAndGetMiniBalls(Collidable other) {
        handleCollision(other);
        return hasExploded ? new ArrayList<>(miniBalls) : new ArrayList<>();
    }

    /**
     * 폭발 메서드: 여러 개의 작은 Ball 생성
     */
    public void explode() {
        if (hasExploded) {
            return; // 이미 폭발했으면 무시
        }

        hasExploded = true;
        setDestroyed(true); // 원본 공 제거 표시

        // 작은 공들 생성
        miniBalls.clear();
        double centerX = getX();
        double centerY = getY();
        double originalRadius = getRadius();

        for (int i = 0; i < miniballCount; i++) {
            // 랜덤 각도 (0 ~ 2π)
            double angle = random.nextDouble() * 2 * Math.PI;

            // 랜덤 속도 (50 ~ 150)
            double speed = 50 + random.nextDouble() * 100;

            // 속도 성분 계산
            double miniDx = speed * Math.cos(angle);
            double miniDy = speed * Math.sin(angle);

            // 작은 공의 크기 (원본의 30-50%)
            double miniRadius = originalRadius * (0.3 + random.nextDouble() * 0.2);

            // 시작 위치 (원본 공 경계에서 시작)
            double startDistance = originalRadius + miniRadius;
            double startX = centerX + startDistance * Math.cos(angle);
            double startY = centerY + startDistance * Math.sin(angle);

            // 작은 공의 색상 (원본과 비슷하지만 약간 다름)
            Color miniColor = generateMiniColor();

            // 작은 공 생성
            Ball miniBall = new Ball(startX, startY, miniRadius, miniDx, miniDy,
                                   miniColor, CollisionAction.BOUNCE);

            miniBalls.add(miniBall);
        }
    }

    /**
     * 작은 공의 색상을 생성합니다 (원본과 비슷하지만 약간 다름).
     */
    private Color generateMiniColor() {
        Color originalColor = getColor();

        // 원본 색상에서 약간의 변화 추가
        double red = Math.max(0, Math.min(1, originalColor.getRed() + (random.nextDouble() - 0.5) * 0.3));
        double green = Math.max(0, Math.min(1, originalColor.getGreen() + (random.nextDouble() - 0.5) * 0.3));
        double blue = Math.max(0, Math.min(1, originalColor.getBlue() + (random.nextDouble() - 0.5) * 0.3));

        return new Color(red, green, blue, 1.0);
    }

    /**
     * 폭발로 생성된 작은 공들을 반환합니다.
     * @return 작은 공들의 리스트 (복사본)
     */
    public List<Ball> getMiniBalls() {
        return new ArrayList<>(miniBalls);
    }

    /**
     * 작은 공들을 다른 컬렉션에 추가합니다.
     * 보통 World의 객체 리스트에 추가할 때 사용합니다.
     */
    public void addMiniBallsTo(List<Object> targetList) {
        if (hasExploded) {
            targetList.addAll(miniBalls);
        }
    }

    /**
     * 폭발 효과를 시각적으로 나타내기 위한 메서드
     * (실제 구현에서는 파티클 효과 등을 추가할 수 있음)
     */
    public void createExplosionEffect() {
        // 폭발 효과 구현 (예: 파티클, 사운드 등)
        // 여기서는 단순히 색상을 밝게 변경
        if (!hasExploded) {
            Color currentColor = getColor();
            Color brightColor = currentColor.brighter().brighter();
            setColor(brightColor);
        }
    }

    // Getter/Setter 메서드들
    public boolean hasExploded() {
        return hasExploded;
    }

    public int getMiniballCount() {
        return miniballCount;
    }

    public void setMiniballCount(int miniballCount) {
        if (!hasExploded) { // 폭발 전에만 변경 가능
            this.miniballCount = Math.max(3, Math.min(5, miniballCount));
        }
    }

    /**
     * 폭발 준비 상태를 확인합니다.
     */
    public boolean isReadyToExplode() {
        return !hasExploded && getCollisionAction() == CollisionAction.CUSTOM;
    }

    /**
     * 수동으로 폭발을 트리거합니다.
     */
    public void triggerExplosion() {
        if (!hasExploded) {
            explode();
        }
    }

    /**
     * 폭발 상태를 초기화합니다 (테스트용).
     */
    public void reset() {
        hasExploded = false;
        setDestroyed(false);
        miniBalls.clear();
    }

    @Override
    public String toString() {
        return String.format("ExplodingBall[pos=%.1f,%.1f, radius=%.1f, exploded=%s, miniCount=%d, miniballsGenerated=%d]",
            getX(), getY(), getRadius(), hasExploded, miniballCount, miniBalls.size());
    }
}
