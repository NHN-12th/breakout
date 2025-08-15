package nhn.breakoutt.cannongame;

import nhn.breakoutt.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cannon implements Paintable {
    private double x;
    private double y;
    private double angle; // 발사 각도 (라디안)
    private double power; // 발사 힘 (100~1000)
    private static final double MIN_ANGLE = Math.toRadians(-90); // -90도
    private static final double MAX_ANGLE = Math.toRadians(0);   // 0도
    private static final double MIN_POWER = 100;
    private static final double MAX_POWER = 1000;

    public Cannon(double x, double y) {
        this.x = x;
        this.y = y;
        this.angle = Math.toRadians(-45); // 기본 45도
        this.power = 500; // 기본 파워
    }

    @Override
    public void paint(GraphicsContext gc) {
        // 대포 본체 그리기 (원형)
        gc.setFill(Color.DARKGRAY);
        gc.fillOval(x - 20, y - 20, 40, 40);

        // 포신 그리기
        gc.save();
        gc.translate(x, y);
        gc.rotate(Math.toDegrees(angle));
        gc.setFill(Color.GRAY);
        gc.fillRect(0, -5, 50, 10);
        gc.restore();

        // 파워 게이지 그리기
        double gaugeX = x - 50;
        double gaugeY = y + 40;
        double gaugeWidth = 100;
        double gaugeHeight = 10;

        // 게이지 배경
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(gaugeX, gaugeY, gaugeWidth, gaugeHeight);

        // 파워 표시
        double powerRatio = (power - MIN_POWER) / (MAX_POWER - MIN_POWER);
        gc.setFill(Color.BLUE);
        gc.fillRect(gaugeX, gaugeY, gaugeWidth * powerRatio, gaugeHeight);

        // 테두리
        gc.setStroke(Color.BLACK);
        gc.strokeRect(gaugeX, gaugeY, gaugeWidth, gaugeHeight);

        // 파워 텍스트
        gc.setFill(Color.BLACK);
        gc.fillText("Power: " + (int)power, gaugeX, gaugeY - 5);
    }

    public ProjectTile fire() {
        // 포신 끝 위치 계산
        double barrelEndX = x + Math.cos(angle) * 50;
        double barrelEndY = y + Math.sin(angle) * 50;

        // 포탄 생성
        ProjectTile projectile = new ProjectTile(
            new Point(barrelEndX, barrelEndY),
            ProjectTileType.NORMAL
        );

        // 초기 속도 계산
        double velocityMagnitude = power * 0.5;
        Vector2D velocity = new Vector2D(
            Math.cos(angle) * velocityMagnitude,
            Math.sin(angle) * velocityMagnitude
        );

        projectile.setVelocity(velocity);
        return projectile;
    }

    public void adjustAngle(double delta) {
        angle += delta;
        angle = Math.max(MIN_ANGLE, Math.min(MAX_ANGLE, angle));
    }

    public void setPower(double power) {
        this.power = Math.max(MIN_POWER, Math.min(MAX_POWER, power));
    }

    public void aimAt(double mouseX, double mouseY) {
        double dx = mouseX - x;
        double dy = mouseY - y;
        double newAngle = Math.atan2(dy, dx);

        if (newAngle >= MIN_ANGLE && newAngle <= MAX_ANGLE) {
            angle = newAngle;
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getAngle() { return angle; }
    public double getPower() { return power; }

    public double getAngleDegrees() {
        return Math.toDegrees(angle);
    }
}