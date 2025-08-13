package nhn.breakoutt.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nhn.breakoutt.Paintable;

public class ExplosionEffect implements Paintable {
    private double centerX;
    private double centerY;
    private double currentRadius;
    private double maxRadius;
    private double duration;
    private double elapsedTime;
    private Color color;
    private boolean finished;

    public ExplosionEffect(double centerX, double centerY, double maxRadius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.maxRadius = maxRadius;
        this.duration = 0.5; // 0.5초 동안 지속
        this.elapsedTime = 0;
        this.currentRadius = 0;
        this.color = Color.ORANGE;
        this.finished = false;
    }

    public void update(double deltaTime) {
        if (finished) return;

        elapsedTime += deltaTime;

        // 반지름 증가 (처음에는 빠르게, 나중에는 느리게)
        double progress = elapsedTime / duration;
        if (progress >= 1.0) {
            finished = true;
            currentRadius = maxRadius;
        } else {
            // 이징 효과: ease-out
            double easedProgress = 1 - Math.pow(1 - progress, 3);
            currentRadius = maxRadius * easedProgress;
        }
    }

    @Override
    public void paint(GraphicsContext gc) {
        if (finished || currentRadius <= 0) return;

        // 투명도 계산 (시간이 지날수록 투명해짐)
        double alpha = 1.0 - (elapsedTime / duration);
        alpha = Math.max(0, Math.min(1, alpha));

        // 폭발 원 그리기 (여러 개의 동심원으로 효과 강화)
        for (int i = 0; i < 3; i++) {
            double radius = currentRadius * (1.0 - i * 0.2);
            double ringAlpha = alpha * (1.0 - i * 0.3);

            Color ringColor = new Color(
                color.getRed(),
                color.getGreen() * (1.0 - i * 0.2),
                color.getBlue() * (1.0 - i * 0.4),
                ringAlpha
            );

            gc.setGlobalAlpha(ringAlpha);
            gc.setStroke(ringColor);
            gc.setLineWidth(3 - i);
            gc.strokeOval(
                centerX - radius,
                centerY - radius,
                radius * 2,
                radius * 2
            );
        }

        // 투명도 복원
        gc.setGlobalAlpha(1.0);
    }

    public boolean isFinished() {
        return finished;
    }
    public double getCenterX() { return centerX; }
    public double getCenterY() { return centerY; }
}
