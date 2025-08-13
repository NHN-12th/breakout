package nhn.breakoutt.breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nhn.breakoutt.*;

public class PowerUp extends Ball {
    private PowerUpProvider.PowerUpType type;
    private boolean collected;
    private Color backgroundColor;

    public PowerUp(double x, double y, PowerUpProvider.PowerUpType type) {
        super(x, y, 20); // 반지름 20의 원형 파워업
        this.type = type;
        this.collected = false;
        this.backgroundColor = getTypeColor(type);
        setColor(backgroundColor);
        setDy(100); // 초당 100픽셀로 떨어짐
        setDx(0);   // 수직으로만 떨어짐
        setCollisionAction(CollisionAction.CUSTOM);
    }

    private Color getTypeColor(PowerUpProvider.PowerUpType type) {
        return switch (type) {
            case WIDER_PADDLE -> Color.BLUE;
            case MULTI_BALL -> Color.RED;
            case EXTRA_LIFE -> Color.GREEN;
            case LASER -> Color.YELLOW;
            case SLOW_BALL -> Color.PURPLE;
            case STICKY_PADDLE -> Color.ORANGE;
        };
    }

    @Override
    public void paint(GraphicsContext gc) {
        if (collected || isDestroyed()) return;

        // 원형 배경
        gc.setFill(backgroundColor);
        gc.fillOval(getX() - getRadius(), getY() - getRadius(),
                   getRadius() * 2, getRadius() * 2);

        // 테두리
        gc.setStroke(backgroundColor.brighter());
        gc.setLineWidth(2);
        gc.strokeOval(getX() - getRadius(), getY() - getRadius(),
                     getRadius() * 2, getRadius() * 2);

        // 파워업 심볼 텍스트
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        String symbol = type.getSymbol();
        double textX = getX() - symbol.length() * 3;
        double textY = getY() + 4;

        // 텍스트 그림자
        gc.setFill(Color.BLACK);
        gc.fillText(symbol, textX + 1, textY + 1);

        // 텍스트 본체
        gc.setFill(Color.WHITE);
        gc.fillText(symbol, textX, textY);
    }

    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof BreakoutPaddle && !collected) {
            collected = true;
            setDestroyed(true);
        }
    }

    @Override
    public void move(double deltaTime) {
        if (collected || isDestroyed()) return;

        // 수직으로만 떨어짐
        setY(getY() + getDy() * deltaTime);

        // 화면 밖으로 나가면 제거
        if (getY() > 600) { // 게임 화면 높이를 600으로 가정
            setDestroyed(true);
        }
    }

    // Getter 메서드들
    public PowerUpProvider.PowerUpType getType() { return type; }
    public boolean isCollected() { return collected; }

    @Override
    public String toString() {
        return String.format("PowerUp[type=%s, pos=%.1f,%.1f, collected=%s]",
            type, getX(), getY(), collected);
    }
}
