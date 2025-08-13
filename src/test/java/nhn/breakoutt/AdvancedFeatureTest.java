package nhn.breakoutt;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdvancedFeatureTest {

    @Test
    public void testBouncingTriangle() {
        // BouncingTriangle: 중심(100, 100), 크기 30 -> 대략적 영역: 85-115, 85-115
        BouncingTriangle triangle = new BouncingTriangle(100, 100, 30, Color.PURPLE);
        triangle.setDx(60);
        triangle.setDy(40);
        triangle.setRotationSpeed(45); // 45도/초
        triangle.setCollisionAction(CollisionAction.BOUNCE);

        // Box: 시작점(110, 110), 크기 20x60 -> 영역: 110-130, 110-170
        // Triangle과 Box가 겹치는 영역: 110-115, 110-115
        Box wall = new Box(110, 110, 20, 60, Color.GRAY);

        // 충돌 전 상태 저장
        double oldRotationSpeed = triangle.getRotationSpeed();
        Color oldColor = triangle.getColor();

        // 충돌 처리
        assertTrue(triangle.isColliding(wall), "Triangle과 Wall이 충돌해야 합니다");
        triangle.handleCollision(wall);

        // BouncingTriangle의 특수 기능 확인
        assertEquals(-oldRotationSpeed, triangle.getRotationSpeed(), 0.001,
                "충돌 후 회전 속도가 반전되어야 합니다");
        assertNotEquals(oldColor, triangle.getColor(),
                "충돌 후 색상이 변경되어야 합니다");
    }

    @Test
    public void testExplodingBall() {
        ExplodingBall explodingBall = new ExplodingBall(100, 100, 25, Color.ORANGE);
        explodingBall.setCollisionAction(CollisionAction.CUSTOM);
        explodingBall.setMiniballCount(5);

        Box trigger = new Box(120, 120, 20, 20, Color.RED);

        // 폭발 전 상태 확인
        assertFalse(explodingBall.hasExploded(), "폭발 전에는 hasExploded가 false여야 합니다");

        // 충돌로 폭발 유발
        assertTrue(explodingBall.isColliding(trigger), "ExplodingBall과 Trigger가 충돌해야 합니다");
        List<Ball> miniBalls = explodingBall.handleCollisionAndGetMiniBalls(trigger);

        // 폭발 후 상태 확인
        assertTrue(explodingBall.hasExploded(), "폭발 후 hasExploded가 true여야 합니다");
        assertTrue(explodingBall.isDestroyed(), "폭발 후 원본 공은 제거되어야 합니다");
        assertEquals(5, miniBalls.size(), "5개의 작은 공이 생성되어야 합니다");

        // 작은 공들이 모두 움직이는지 확인
        for (Ball miniBall : miniBalls) {
            assertTrue(Math.abs(miniBall.getDx()) > 0 || Math.abs(miniBall.getDy()) > 0,
                    "작은 공들은 모두 움직여야 합니다");
            assertTrue(miniBall instanceof Movable, "작은 공들은 Movable이어야 합니다");
            assertTrue(miniBall instanceof Collidable, "작은 공들은 Collidable이어야 합니다");
        }
    }

    @Test
    public void testSpecialZone() {
        SpecialZone speedZone = new SpecialZone(150, 150, 100, 80, SpecialZone.ZoneType.SPEED_UP);
        SpecialZone slowZone = new SpecialZone(300, 300, 120, 90, SpecialZone.ZoneType.SLOW_DOWN);
        SpecialZone gravityZone = new SpecialZone(450, 450, 80, 100, SpecialZone.ZoneType.GRAVITY);

        Ball ball = new Ball(175, 175, 15, Color.WHITE);
        ball.setDx(40);
        ball.setDy(30);

        // SPEED_UP 효과 테스트
        double oldDx = ball.getDx();
        double oldDy = ball.getDy();

        assertTrue(speedZone.isColliding(ball), "Ball이 SpeedZone에 있어야 합니다");
        speedZone.applyEffect(ball);

        assertEquals(oldDx * 2, ball.getDx(), 0.001, "SPEED_UP 효과로 X 속도가 2배가 되어야 합니다");
        assertEquals(oldDy * 2, ball.getDy(), 0.001, "SPEED_UP 효과로 Y 속도가 2배가 되어야 합니다");

        // SLOW_DOWN 효과 테스트
        ball.setX(350);
        ball.setY(350);
        oldDx = ball.getDx();
        oldDy = ball.getDy();

        assertTrue(slowZone.isColliding(ball), "Ball이 SlowZone에 있어야 합니다");
        slowZone.applyEffect(ball);

        assertEquals(oldDx * 0.5, ball.getDx(), 0.001, "SLOW_DOWN 효과로 X 속도가 절반이 되어야 합니다");
        assertEquals(oldDy * 0.5, ball.getDy(), 0.001, "SLOW_DOWN 효과로 Y 속도가 절반이 되어야 합니다");

        // GRAVITY 효과 테스트
        ball.setX(480);
        ball.setY(480);
        oldDy = ball.getDy();

        assertTrue(gravityZone.isColliding(ball), "Ball이 GravityZone에 있어야 합니다");
        gravityZone.applyEffect(ball);

        assertEquals(oldDy + 10, ball.getDy(), 0.001, "GRAVITY 효과로 Y 속도가 증가해야 합니다");
    }

    @Test
    public void testMazeWorld() {
        int[][] mazeLayout = {
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 0, 2, 1},
                {1, 1, 1, 1, 1}
        };

        MazeWorld mazeWorld = new MazeWorld(500, 400, mazeLayout);

        // 미로가 올바르게 생성되었는지 확인
        List<Box> walls = mazeWorld.getWalls();
        List<Box> exits = mazeWorld.getExits();

        assertTrue(walls.size() > 0, "미로 벽이 생성되어야 합니다");
        assertTrue(exits.size() > 0, "출구가 생성되어야 합니다");

        // 모든 벽이 BOUNCE 액션을 가지는지 확인
        for (Box wall : walls) {
            assertEquals(CollisionAction.BOUNCE, wall.getCollisionAction(),
                    "미로 벽은 BOUNCE 액션을 가져야 합니다");
        }

        // 출구는 특별한 처리를 위해 CUSTOM 액션
        for (Box exit : exits) {
            assertEquals(CollisionAction.CUSTOM, exit.getCollisionAction(),
                    "출구는 CUSTOM 액션을 가져야 합니다");
        }

        // Ball을 안전한 통로 중앙에 배치 - 셀 [1][1]의 중심
        // 셀 크기: 100x80, 셀 [1][1]의 중심: (150, 120)
        Ball player = new Ball(150, 120, 10, Color.YELLOW); // 통로 중앙 위치
        mazeWorld.addObject(player);

        // 미로 검증: Ball이 벽과 충돌하지 않는 위치에 있는지 확인
        boolean canMove = true;
        for (Box wall : walls) {
            if (player.isColliding(wall)) {
                canMove = false;
                break;
            }
        }
        assertTrue(canMove, "Ball이 통로에 올바르게 배치되어야 합니다");
    }
}