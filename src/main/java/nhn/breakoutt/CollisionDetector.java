package nhn.breakoutt;

public class CollisionDetector {
    enum Wall {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }

    // 내부 클래스 - WallCollision
    public static class WallCollision {
        private final Wall wall;
        private final double penetration;

        public WallCollision(Wall wall, double penetration) {
            this.wall = wall;
            this.penetration = penetration;
        }

        public Wall getWall() {
            return wall;
        }

        public double getPenetration() {
            return penetration;
        }
    }

    /**
     * 공과 벽의 충돌을 검사합니다.
     * @param ball 검사할 공
     * @param minX 최소 X 좌표 (왼쪽 벽)
     * @param minY 최소 Y 좌표 (위쪽 벽)
     * @param maxX 최대 X 좌표 (오른쪽 벽)
     * @param maxY 최대 Y 좌표 (아래쪽 벽)
     * @return 충돌이 있으면 WallCollision 객체, 없으면 null
     */
    public static WallCollision checkWallCollision(BoundedBall ball, double minX, double minY, double maxX, double maxY) {
        Point center = ball.getCenter();
        double radius = ball.getRadius();
        double ballX = center.getX();
        double ballY = center.getY();

        // 왼쪽 벽 충돌 검사
        if (ballX - radius < minX) {
            double penetration = minX - (ballX - radius);
            return new WallCollision(Wall.LEFT, penetration);
        }

        // 오른쪽 벽 충돌 검사
        if (ballX + radius > maxX) {
            double penetration = (ballX + radius) - maxX;
            return new WallCollision(Wall.RIGHT, penetration);
        }

        // 위쪽 벽 충돌 검사
        if (ballY - radius < minY) {
            double penetration = minY - (ballY - radius);
            return new WallCollision(Wall.TOP, penetration);
        }

        // 아래쪽 벽 충돌 검사
        if (ballY + radius > maxY) {
            double penetration = (ballY + radius) - maxY;
            return new WallCollision(Wall.BOTTOM, penetration);
        }

        // 충돌이 없음
        return null;
    }

    /**
     * 벽 충돌을 해결합니다 (속도 반전).
     * 이 구현에서는 완전 탄성 충돌(반발 계수 1.0)을 가정합니다.
     * @param ball 충돌한 공
     * @param collision 충돌 정보
     */
    public static void resolveWallCollision(BoundedBall ball, WallCollision collision) {
        Vector2D currentVelocity = ball.getVelocity();
        Point currentCenter = ball.getCenter();

        switch (collision.getWall()) {
            case LEFT:
                // X 속도 반전 및 위치 보정
                ball.setVelocity(new Vector2D(-currentVelocity.getX(), currentVelocity.getY()));
                // 위치를 왼쪽 벽에서 반지름만큼 떨어뜨림
                ball.moveTo(new Point(ball.getRadius(), currentCenter.getY()));
                break;

            case RIGHT:
                // X 속도 반전 및 위치 보정
                ball.setVelocity(new Vector2D(-currentVelocity.getX(), currentVelocity.getY()));
                // 위치를 오른쪽 벽에서 반지름만큼 떨어뜨림
                double rightWallX = currentCenter.getX() - collision.getPenetration();
                ball.moveTo(new Point(rightWallX, currentCenter.getY()));
                break;

            case TOP:
                // Y 속도 반전 및 위치 보정
                ball.setVelocity(new Vector2D(currentVelocity.getX(), -currentVelocity.getY()));
                // 위치를 위쪽 벽에서 반지름만큼 떨어뜨림
                ball.moveTo(new Point(currentCenter.getX(), ball.getRadius()));
                break;

            case BOTTOM:
                // Y 속도 반전 및 위치 보정
                ball.setVelocity(new Vector2D(currentVelocity.getX(), -currentVelocity.getY()));
                // 위치를 아래쪽 벽에서 반지름만큼 떨어뜨림
                double bottomWallY = currentCenter.getY() - collision.getPenetration();
                ball.moveTo(new Point(currentCenter.getX(), bottomWallY));
                break;
        }
    }
}
