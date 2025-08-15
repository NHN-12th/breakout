package nhn.breakoutt.cannongame;

import nhn.breakoutt.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 간단한 대포 게임 월드 클래스
 */
public class CannonGameWorld extends BoundedWorld {
    private final Cannon cannon;
    private final List<ProjectTile> projectiles;
    private final List<Target> targets;
    private int score;
    private int currentLevel;
    private boolean gameWon;
    private boolean gameLost;

    public CannonGameWorld(double width, double height) {
        super(width, height);
        this.projectiles = new ArrayList<>();
        this.targets = new ArrayList<>();
        this.cannon = new Cannon(50, height - 50); // 화면 왼쪽 하단에 배치
        this.score = 0;
        this.currentLevel = 1;
        this.gameWon = false;
        this.gameLost = false;

        // 기본 레벨 로드
        loadLevel(currentLevel);
    }

    public void loadLevel(int level) {
        targets.clear();
        projectiles.clear();
        // 기존 projectiles를 월드에서 제거
        getBalls().removeIf(ball -> ball instanceof ProjectTile);

        this.currentLevel = level;

        // 레벨별 목표물 배치
        createTargetsForLevel(level);
    }

    private void createTargetsForLevel(int level) {
        double worldWidth = getWidth();
        double worldHeight = getHeight();

        // 기본 목표물 배치 - 간단한 격자 형태
        for (int i = 0; i < level + 3; i++) {
            double x = worldWidth * 0.6 + (i % 3) * 80;
            double y = worldHeight - 150 - (i / 3) * 60;

            TargetType type = (i % 2 == 0) ? TargetType.WOODEN : TargetType.STONE;
            targets.add(new Target(x, y, 50, 40, type));
        }
    }

    public ProjectTile fire() {
        if (gameLost || gameWon) {
            return null;
        }

        ProjectTile projectile = cannon.fire();
        projectiles.add(projectile);
        add(projectile); // Ball로 물리 엔진에 추가

        return projectile;
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        // 활성 포탄들의 상태 확인
        Iterator<ProjectTile> projectileIter = projectiles.iterator();
        while (projectileIter.hasNext()) {
            ProjectTile projectile = projectileIter.next();

            // 화면 밖으로 나간 포탄 제거
            if (projectile.isOutOfBounds(getWidth(), getHeight())) {
                remove(projectile);
                projectileIter.remove();
            }
            // 목표물과의 충돌 검사
            else {
                checkProjectileCollisions(projectile);
            }
        }

        // 파괴된 목표물 제거 및 점수 계산
        Iterator<Target> targetIter = targets.iterator();
        while (targetIter.hasNext()) {
            Target target = targetIter.next();
            if (target.isDestroyed()) {
                score += target.getPoints();
                targetIter.remove();
            }
        }

        // 게임 종료 조건 확인
        checkWinCondition();
    }

    private void checkProjectileCollisions(ProjectTile projectile) {
        // 목표물과의 충돌 검사
        for (Target target : targets) {
            if (!target.isDestroyed() &&
                target.intersects(projectile.getX(), projectile.getY(), projectile.getRadius())) {
                target.takeDamage(1.0); // 간단히 1의 데미지
                projectile.explode();
                break;
            }
        }
    }

    private void checkWinCondition() {
        // 모든 목표물 파괴 시 승리
        if (targets.stream().allMatch(Target::isDestroyed)) {
            gameWon = true;
        }
    }

    public void paint(GraphicsContext gc) {
        // 배경 그리기
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, getWidth(), getHeight());

        // 바닥 그리기
        gc.setFill(Color.GREEN);
        gc.fillRect(0, getHeight() - 20, getWidth(), 20);

        // 게임 객체들 렌더링
        super.draw(gc);

        // 목표물들 그리기
        for (Target target : targets) {
            target.paint(gc);
        }

        // 대포 그리기
        cannon.paint(gc);

        // UI 정보 그리기
        drawGameInfo(gc);

        // 게임 종료 메시지
        if (gameWon || gameLost) {
            drawGameOverMessage(gc);
        }
    }

    private void drawGameInfo(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillText("점수: " + score, 10, 30);
        gc.fillText("레벨: " + currentLevel, 10, 50);
        gc.fillText("남은 목표물: " + targets.stream().mapToInt(t -> t.isDestroyed() ? 0 : 1).sum(), 10, 70);
        gc.fillText("각도: " + String.format("%.0f°", cannon.getAngleDegrees()), 10, 90);
    }

    private void drawGameOverMessage(GraphicsContext gc) {
        gc.setFill(Color.BLACK.deriveColor(0, 1, 1, 0.8));
        gc.fillRect(0, 0, getWidth(), getHeight());

        gc.setFill(Color.WHITE);
        String message = gameWon ? "레벨 클리어!" : "게임 오버!";
        gc.fillText(message, getWidth()/2 - 50, getHeight()/2 - 50);
        gc.fillText("최종 점수: " + score, getWidth()/2 - 50, getHeight()/2 - 20);

        if (gameWon) {
            gc.fillText("N키를 눌러 다음 레벨", getWidth()/2 - 70, getHeight()/2 + 10);
        }
        gc.fillText("R키를 눌러 재시작", getWidth()/2 - 70, getHeight()/2 + 40);
        gc.fillText("ESC키로 메뉴로 돌아가기", getWidth()/2 - 90, getHeight()/2 + 70);
    }

    public void resetGame() {
        gameWon = false;
        gameLost = false;
        score = 0;
        loadLevel(currentLevel);
    }

    public void nextLevel() {
        if (gameWon) {
            currentLevel++;
            gameWon = false;
            gameLost = false;
            loadLevel(currentLevel);
        }
    }

    public Cannon getCannon() { return cannon; }
    public boolean isGameWon() { return gameWon; }
    public boolean isGameLost() { return gameLost; }
}
