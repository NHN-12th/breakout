package nhn.breakoutt.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nhn.breakoutt.*;
import java.util.ArrayList;
import java.util.List;

public class BreakoutPaddle extends Box {
    private double speed;
    private double targetX;
    private List<TimedPowerUp> activePowerUps;
    private boolean stickyMode;
    private Ball stuckBall;

    private static class TimedPowerUp {
        PowerUpProvider.PowerUpType type;
        double remainingTime;
        double originalValue;

        TimedPowerUp(PowerUpProvider.PowerUpType type, double duration) {
            this.type = type;
            this.remainingTime = duration;
        }
    }

    public BreakoutPaddle(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.speed = 300; // 초당 300픽셀 이동
        this.targetX = x + width/2;
        this.activePowerUps = new ArrayList<>();
        this.stickyMode = false;
        this.stuckBall = null;
        setColor(Color.DARKBLUE);
    }

    public void setTargetX(double mouseX) {
        this.targetX = mouseX;
    }

    public void expand(double factor) {
        double newWidth = getWidth() * factor;
        double centerX = getX() + getWidth() / 2;

        setWidth(newWidth);
        moveTo(new Point(centerX - newWidth / 2, getY()));
    }

    public void shrink(double factor) {
        expand(1.0 / factor);
    }

    public void applyPowerUp(PowerUpProvider.PowerUpType type, double duration) {
        switch (type) {
            case WIDER_PADDLE:
                TimedPowerUp widerEffect = new TimedPowerUp(type, duration);
                widerEffect.originalValue = getWidth();
                expand(1.5);
                activePowerUps.add(widerEffect);
                break;

            case STICKY_PADDLE:
                TimedPowerUp stickyEffect = new TimedPowerUp(type, duration);
                stickyMode = true;
                activePowerUps.add(stickyEffect);
                break;
        }
    }

    public void reflectBall(Ball ball) {
        // 충돌 지점 계산 (0.0 ~ 1.0)
        double hitPosition = (ball.getX() - getX()) / getWidth();
        hitPosition = Math.max(0.0, Math.min(1.0, hitPosition));

        // 반사 각도 계산 (-60도 ~ +60도)
        double angle = (hitPosition - 0.5) * Math.PI / 3;

        // 공의 속도 크기 유지
        double speed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());

        // 새로운 속도 벡터 설정
        ball.setDx(speed * Math.sin(angle));
        ball.setDy(-Math.abs(speed * Math.cos(angle))); // 항상 위로

        // 끈끈한 패들 모드인 경우
        if (stickyMode && stuckBall == null) {
            stuckBall = ball;
            ball.setDx(0);
            ball.setDy(0);
        }
    }

    public void releaseBall() {
        if (stuckBall != null) {
            stuckBall.setDx(0);
            stuckBall.setDy(-200); // 위로 발사
            stuckBall = null;
        }
    }

    public void update(double deltaTime) {
        // 부드러운 이동
        double currentCenterX = getX() + getWidth() / 2;
        double difference = targetX - currentCenterX;

        if (Math.abs(difference) > 2) { // 임계값을 두어 떨림 방지
            double dx = difference * speed * deltaTime * 0.1;
            moveTo(new Point(getX() + dx, getY()));
        }

        if (stuckBall != null) {
            stuckBall.setX(getX() + getWidth() / 2);
            stuckBall.setY(getY() - stuckBall.getRadius());
        }

        updatePowerUps(deltaTime);
    }

    private void updatePowerUps(double deltaTime) {
        List<TimedPowerUp> expiredPowerUps = new ArrayList<>();

        for (TimedPowerUp powerUp : activePowerUps) {
            powerUp.remainingTime -= deltaTime;

            if (powerUp.remainingTime <= 0) {
                expiredPowerUps.add(powerUp);

                // 파워업 효과 해제
                switch (powerUp.type) {
                    case WIDER_PADDLE:
                        setWidth(powerUp.originalValue);
                        break;
                    case STICKY_PADDLE:
                        stickyMode = false;
                        if (stuckBall != null) {
                            releaseBall();
                        }
                        break;
                }
            }
        }
        activePowerUps.removeAll(expiredPowerUps);
    }

    @Override
    public void paint(GraphicsContext gc) {
        // 3D 효과가 있는 패들 그리기
        Color baseColor = getColor();

        // 그림자
        gc.setFill(baseColor.darker().darker());
        gc.fillRoundRect(getX() + 3, getY() + 3, getWidth(), getHeight(), 10, 10);

        // 본체
        gc.setFill(baseColor);
        gc.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 10, 10);

        // 하이라이트
        gc.setFill(baseColor.brighter());
        gc.fillRoundRect(getX(), getY(), getWidth(), getHeight() * 0.3, 10, 10);

        // 끈끈한 패들 효과 표시
        if (stickyMode) {
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(2);
            gc.strokeRoundRect(getX() - 2, getY() - 2, getWidth() + 4, getHeight() + 4, 12, 12);
        }

        // 활성 파워업 표시
        displayActivePowerUps(gc);
    }

    private void displayActivePowerUps(GraphicsContext gc) {
        double iconSize = 12;
        double iconY = getY() - iconSize - 5;

        for (int i = 0; i < activePowerUps.size(); i++) {
            TimedPowerUp powerUp = activePowerUps.get(i);
            double iconX = getX() + i * (iconSize + 2);

            // 파워업 아이콘 배경
            gc.setFill(Color.WHITE);
            gc.fillOval(iconX, iconY, iconSize, iconSize);

            // 파워업 심볼
            gc.setFill(Color.BLACK);
            gc.fillText(powerUp.type.getSymbol(), iconX + 3, iconY + 8);

            // 남은 시간 표시 (원형 진행 바)
            double angle = (powerUp.remainingTime / powerUp.type.getDuration()) * 360;
            gc.setStroke(Color.GREEN);
            gc.setLineWidth(2);
            gc.strokeArc(iconX - 1, iconY - 1, iconSize + 2, iconSize + 2, 90, -angle, javafx.scene.shape.ArcType.OPEN);
        }
    }

    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Ball) {
            reflectBall((Ball) other);
        }
    }

    public boolean isStickyMode() {
        return stickyMode;
    }
}
