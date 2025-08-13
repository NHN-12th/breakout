package nhn.breakoutt.breakout;

import nhn.breakoutt.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

public class BreakoutWorld {
    private double width;
    private double height;

    private List<UnbreakableBrick> walls;
    private List<Breakable> bricks;
    private List<Ball> balls;
    private BreakoutPaddle paddle;
    private List<PowerUp> powerUps;
    private List<ExplosionEffect> explosions;

    private int score;
    private int level;

    public BreakoutWorld(double width, double height) {
        this.width = width;
        this.height = height;
        this.walls = new ArrayList<>();
        this.bricks = new ArrayList<>();
        this.balls = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.score = 0;
        this.level = 1;

        initializeWalls();
        initializePaddle();
    }

    private void initializeWalls() {
        double wallThickness = 20;
        UnbreakableBrick[] gameWalls = UnbreakableBrick.WallFactory.createGameWalls(width, height, wallThickness);
        Collections.addAll(walls, gameWalls);
    }

    private void initializePaddle() {
        double paddleWidth = 100;
        double paddleHeight = 20;
        double paddleX = width / 2 - paddleWidth / 2;
        double paddleY = height - 40;

        paddle = new BreakoutPaddle(paddleX, paddleY, paddleWidth, paddleHeight);
    }

    public void update(double deltaTime) {
        paddle.update(deltaTime);

        for (Ball ball : balls) {
            ball.move(deltaTime);
        }

        for (PowerUp powerUp : powerUps) {
            powerUp.move(deltaTime);
        }

        for (ExplosionEffect explosion : explosions) {
            explosion.update(deltaTime);
        }

        handleCollisions();

        removeDestroyedObjects();

        processExplosions();
    }

    private void handleCollisions() {
        for (Ball ball : balls) {
            for (UnbreakableBrick wall : walls) {
                if (ball.isColliding(wall)) {
                    ball.handleCollision(wall);
                }
            }
        }

        for (Ball ball : balls) {
            if (ball.isColliding(paddle)) {
                paddle.handleCollision(ball);
            }
        }

        for (Ball ball : balls) {
            for (Breakable brick : bricks) {
                if (brick instanceof Collidable collidable) {
                    if (ball.isColliding(collidable)) {
                        brick.hit(1);
                        ball.handleCollision(collidable);

                        if (brick.isBroken()) {
                            handleBrickDestruction(brick);
                        }
                        break; // 한 번에 하나의 벽돌만 충돌
                    }
                }
            }
        }

        for (PowerUp powerUp : powerUps) {
            if (powerUp.isColliding(paddle)) {
                powerUp.handleCollision(paddle);
                if (powerUp.isCollected()) {
                    applyPowerUp(powerUp);
                }
            }
        }
    }

    private void handleBrickDestruction(Breakable brick) {
        score += brick.getPoints();

        // 폭발 효과
        if (brick instanceof Exploding explodingBrick) {
            // 폭발로 영향받는 벽돌들 찾기
            List<Breakable> affectedBricks = explodingBrick.explode(getBreakableObjects());
            for (Breakable affected : affectedBricks) {
                affected.hit(explodingBrick.getExplosionDamage());
                if (affected.isBroken()) {
                    score += affected.getPoints();
                }
            }

            // 폭발 효과 생성
            explosions.add(explodingBrick.createExplosionEffect());
        }

        // 파워업 드롭
        if (brick instanceof PowerUpProvider provider) {
            if (provider.shouldDropPowerUp()) {
                createPowerUp(brick, provider.getPowerUpType());
            }
        }
    }

    private List<Boundable> getBreakableObjects() {
        List<Boundable> objects = new ArrayList<>();
        for (Breakable brick : bricks) {
            if (brick instanceof Boundable && !brick.isBroken()) {
                objects.add((Boundable) brick);
            }
        }
        return objects;
    }

    private void createPowerUp(Breakable brick, PowerUpProvider.PowerUpType type) {
        if (brick instanceof StaticObject staticBrick) {
            double x = staticBrick.getX() + staticBrick.getWidth() / 2;
            double y = staticBrick.getY() + staticBrick.getHeight() / 2;

            PowerUp powerUp = new PowerUp(x, y, type);
            powerUps.add(powerUp);
        }
    }

    private void applyPowerUp(PowerUp powerUp) {
        switch (powerUp.getType()) {
            case WIDER_PADDLE, STICKY_PADDLE:
                paddle.applyPowerUp(powerUp.getType(), powerUp.getType().getDuration());
                break;

            case MULTI_BALL:
                createMultiBalls();
                break;

            case EXTRA_LIFE:
                // 게임 로직에서 처리 (생명 증가)
                break;

            case SLOW_BALL:
                slowDownBalls();
                break;

            case LASER:
                // 레이저 기능 구현 (추후 확장)
                break;
        }
    }

    private void createMultiBalls() {
        if (!balls.isEmpty()) {
            Ball originalBall = balls.getFirst();

            // 추가 공 2개 생성
            for (int i = 0; i < 2; i++) {
                Ball newBall = new Ball(originalBall.getX(), originalBall.getY(), originalBall.getRadius());

                // 다른 각도로 발사
                double angle = (i + 1) * Math.PI / 4; // 45도, 90도
                double speed = Math.sqrt(originalBall.getDx() * originalBall.getDx() +
                                       originalBall.getDy() * originalBall.getDy());

                newBall.setDx(speed * Math.cos(angle));
                newBall.setDy(speed * Math.sin(angle));
                newBall.setColor(Color.ORANGE);

                balls.add(newBall);
            }
        }
    }

    private void slowDownBalls() {
        for (Ball ball : balls) {
            ball.setDx(ball.getDx() * 0.5);
            ball.setDy(ball.getDy() * 0.5);
        }
    }

    private void removeDestroyedObjects() {
        // 파괴된 벽돌 제거
        bricks.removeIf(Breakable::isBroken);

        // 수집된 파워업 제거
        powerUps.removeIf(powerUp -> powerUp.isCollected() || powerUp.isDestroyed());

        // 완료된 폭발 효과 제거
        explosions.removeIf(ExplosionEffect::isFinished);

        // 화면 밖으로 나간 공 제거
        balls.removeIf(ball -> ball.getY() > height + 50);
    }

    private void processExplosions() {
        // 폭발 효과 처리는 이미 handleBrickDestruction에서 수행됨
    }

    /**
     * 게임 월드 렌더링
     */
    public void render(GraphicsContext gc) {
        // 배경
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        // 벽 그리기
        for (UnbreakableBrick wall : walls) {
            wall.paint(gc);
        }

        // 벽돌 그리기
        for (Breakable brick : bricks) {
            if (brick instanceof Paintable && !brick.isBroken()) {
                ((Paintable) brick).paint(gc);
            }
        }

        // 패들 그리기
        paddle.paint(gc);

        // 공 그리기
        for (Ball ball : balls) {
            ball.paint(gc);
        }

        // 파워업 그리기
        for (PowerUp powerUp : powerUps) {
            powerUp.paint(gc);
        }

        // 폭발 효과 그리기
        for (ExplosionEffect explosion : explosions) {
            explosion.paint(gc);
        }
    }

    public void createLevel(int levelNumber) {
        bricks.clear();
        this.level = levelNumber;

        double brickWidth = 60;
        double brickHeight = 20;
        double spacing = 5;
        double startX = 50;
        double startY = 80;

        int rows = Math.min(5 + levelNumber, 10); // 최대 10줄
        int cols = (int) ((width - 2 * startX) / (brickWidth + spacing));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = startX + col * (brickWidth + spacing);
                double y = startY + row * (brickHeight + spacing);

                Breakable brick = createBrickForLevel(x, y, brickWidth, brickHeight, row, col, levelNumber);
                bricks.add(brick);
            }
        }
    }

    private Breakable createBrickForLevel(double x, double y, double width, double height, int row, int col, int level) {
        Color color = getBrickColor(row);
        int points = (row + 1) * 10;

        // 레벨과 위치에 따른 벽돌 타입 결정
        if (level >= 3 && (row + col) % 7 == 0) {
            // 폭발 벽돌
            return new ExplodingBrick(x, y, width, height, Color.RED, points * 2);
        } else if (level >= 2 && row < 2) {
            // 다중 타격 벽돌 (상위 줄)
            return new MultiHitBrick(x, y, width, height, color, points, 2 + level/3);
        } else if ((row + col) % 5 == 0) {
            // 파워업 벽돌
            return new PowerUpBrick(x, y, width, height, color, points, 0.3);
        } else {
            // 일반 벽돌
            return new SimpleBrick(x, y, width, height, color, points);
        }
    }

    private Color getBrickColor(int row) {
        Color[] colors = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE,
            Color.PURPLE, Color.PINK, Color.CYAN, Color.LIME, Color.MAGENTA
        };
        return colors[row % colors.length];
    }

    public int getScore() { return score; }
    public int getLevel() { return level; }
    public boolean isLevelComplete() { return bricks.isEmpty(); }
    public boolean areAllBallsLost() { return balls.isEmpty(); }
    public BreakoutPaddle getPaddle() { return paddle; }
    public List<Ball> getBalls() {
        return new ArrayList<>(balls);
    }

    public void addBall(Ball ball) { balls.add(ball); }
}
