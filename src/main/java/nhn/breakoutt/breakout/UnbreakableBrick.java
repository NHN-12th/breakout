package nhn.breakoutt.breakout;

import javafx.scene.paint.Color;
import nhn.breakoutt.Collidable;
import nhn.breakoutt.CollisionAction;

public class UnbreakableBrick extends StaticObject {

    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height, Color.DARKGRAY);
        setCollisionAction(CollisionAction.BOUNCE);
    }

    public UnbreakableBrick(double x, double y, double width, double height, Color color) {
        super(x, y, width, height, color);
        setCollisionAction(CollisionAction.BOUNCE);
    }

    @Override
    public void handleCollision(Collidable other) {
        // 깨지지 않는 벽은 충돌해도 변화 없음
        // 공이나 다른 객체만 반사됨
    }

    public static class WallFactory {

        /**
         * 상단 벽 생성
         */
        public static UnbreakableBrick createTopWall(double gameWidth, double thickness) {
            return new UnbreakableBrick(0, 0, gameWidth, thickness, Color.DARKSLATEGRAY);
        }

        /**
         * 좌측 벽 생성
         */
        public static UnbreakableBrick createLeftWall(double gameHeight, double thickness) {
            return new UnbreakableBrick(0, 0, thickness, gameHeight, Color.DARKSLATEGRAY);
        }

        /**
         * 우측 벽 생성
         */
        public static UnbreakableBrick createRightWall(double gameWidth, double gameHeight, double thickness) {
            return new UnbreakableBrick(gameWidth - thickness, 0, thickness, gameHeight, Color.DARKSLATEGRAY);
        }

        /**
         * 모든 게임 벽을 생성 (상/좌/우만, 하단은 열려있음)
         */
        public static UnbreakableBrick[] createGameWalls(double gameWidth, double gameHeight, double thickness) {
            return new UnbreakableBrick[] {
                createTopWall(gameWidth, thickness),
                createLeftWall(gameHeight, thickness),
                createRightWall(gameWidth, gameHeight, thickness)
            };
        }
    }
}
