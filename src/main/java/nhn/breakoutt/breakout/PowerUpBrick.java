package nhn.breakoutt.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nhn.breakoutt.*;
import java.util.Random;

public class PowerUpBrick extends SimpleBrick implements PowerUpProvider {
    private double dropChance;
    private PowerUpType powerUpType;
    private Random random;

    public PowerUpBrick(double x, double y, double width, double height, Color color, int points, double dropChance) {
        super(x, y, width, height, color, points);
        this.dropChance = dropChance;
        this.random = new Random();
        this.powerUpType = getRandomPowerUpType();
    }

    @Override
    public boolean shouldDropPowerUp() {
        return random.nextDouble() < dropChance;
    }

    @Override
    public PowerUpType getPowerUpType() {
        return powerUpType;
    }

    private PowerUpType getRandomPowerUpType() {
        PowerUpType[] types = PowerUpType.values();
        return types[random.nextInt(types.length)];
    }

    @Override
    public void paint(GraphicsContext gc) {
        if (destroyed) return;

        // 기본 벽돌 그리기
        super.paint(gc);

        // 파워업 벽돌임을 나타내는 특수 표시 (무지개 테두리)
        gc.setLineWidth(2);

        // 무지개 효과를 위한 여러 색상 테두리
        Color[] rainbowColors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE};

        for (int i = 0; i < rainbowColors.length; i++) {
            gc.setStroke(rainbowColors[i]);
            double offset = i * 0.5;
            gc.strokeRect(x - offset, y - offset, width + 2*offset, height + 2*offset);
        }

        // 파워업 심볼 표시
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        double centerX = x + width/2;
        double centerY = y + height/2;
        double symbolSize = Math.min(width, height) * 0.4;

        // 파워업 타입에 따른 심볼 그리기
        drawPowerUpSymbol(gc, centerX, centerY, symbolSize);
    }

    private void drawPowerUpSymbol(GraphicsContext gc, double centerX, double centerY, double size) {
        // 물음표 심볼 (파워업이 무엇인지 모르게)
        gc.fillOval(centerX - size/2, centerY - size/2, size, size);
        gc.strokeOval(centerX - size/2, centerY - size/2, size, size);

        // "?" 문자
        gc.setFill(Color.BLACK);
        gc.fillText("?", centerX - 3, centerY + 3);
    }
}
