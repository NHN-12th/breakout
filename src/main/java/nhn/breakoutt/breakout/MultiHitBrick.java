package nhn.breakoutt.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MultiHitBrick extends SimpleBrick implements MultiHit {
    private DamageState damageState;
    private Color originalColor;

    public MultiHitBrick(double x, double y, double width, double height, Color color, int points, int maxHitPoints) {
        super(x, y, width, height, color, points);
        this.maxHitPoints = maxHitPoints;
        this.hitPoints = maxHitPoints;
        this.originalColor = color;
        this.damageState = DamageState.PERFECT;
    }

    @Override
    public void hit(int damage) {
        super.hit(damage);
        updateDamageState();
        updateVisualDamage();
    }

    @Override
    public void updateVisualDamage() {
        if (destroyed) return;

        // 피해 정도에 따라 색상 변경
        double damageRatio = 1.0 - (double) hitPoints / maxHitPoints;

        if (damageRatio <= 0.33) {
            damageState = DamageState.PERFECT;
            color = originalColor;
        } else if (damageRatio <= 0.66) {
            damageState = DamageState.DAMAGED;
            // 약간 어두워짐
            color = originalColor.darker();
        } else {
            damageState = DamageState.CRITICAL;
            // 많이 어두워지고 약간 빨간색 추가
            color = Color.color(
                Math.min(1.0, originalColor.getRed() + 0.2),
                originalColor.getGreen() * 0.6,
                originalColor.getBlue() * 0.6
            );
        }
    }

    private void updateDamageState() {
        double damageRatio = 1.0 - (double) hitPoints / maxHitPoints;

        if (damageRatio <= 0.33) {
            damageState = DamageState.PERFECT;
        } else if (damageRatio <= 0.66) {
            damageState = DamageState.DAMAGED;
        } else {
            damageState = DamageState.CRITICAL;
        }
    }

    @Override
    public void paint(GraphicsContext gc) {
        if (destroyed) return;

        // 기본 벽돌 그리기
        super.paint(gc);

        // 피해 상태에 따른 추가 시각 효과
        if (damageState != DamageState.PERFECT) {
            gc.setStroke(Color.DARKRED);
            gc.setLineWidth(1);

            // 균열 효과
            if (damageState == DamageState.DAMAGED) {
                // 가로 균열 1개
                gc.strokeLine(x + 5, y + height/2, x + width - 5, y + height/2);
            } else if (damageState == DamageState.CRITICAL) {
                // 가로 균열 2개
                gc.strokeLine(x + 3, y + height/3, x + width - 3, y + height/3);
                gc.strokeLine(x + 5, y + 2*height/3, x + width - 5, y + 2*height/3);

                // 세로 균열 1개
                gc.strokeLine(x + width/2, y + 3, x + width/2, y + height - 3);
            }
        }

        // 남은 체력 표시 (작은 점들)
        gc.setFill(Color.WHITE);
        for (int i = 0; i < hitPoints; i++) {
            double dotX = x + width - 8 - (i * 4);
            double dotY = y + 4;
            gc.fillOval(dotX, dotY, 2, 2);
        }
    }
}
