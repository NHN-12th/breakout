package nhn.breakoutt;

/**
 * MovableWorld를 상속받아 충돌 처리 기능을 통합한 클래스입니다.
 *
 * 상속 구조:
 * World (2장: 기본 세계)
 *   ↓ 상속
 * MovableWorld (3장: 움직이는 공 관리)
 *   ↓ 상속
 * BoundedWorld (4장: 충돌 처리 추가)
 */
public class BoundedWorld extends MovableWorld {

    public BoundedWorld(double width, double height) {
        super(width, height);
    }

    /**
     * 공을 월드에 추가하면서 BoundedBall인 경우 경계를 설정합니다.
     */
    @Override
    public void add(Ball ball) {
        super.add(ball);

        // BoundedBall인 경우 월드의 경계를 설정
        if (ball instanceof BoundedBall boundedBall) {
            boundedBall.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * 매 프레임 업데이트 - 이동, 벽 충돌, 공 간 충돌 처리
     * BoundedBall의 자체 충돌 처리에 의존합니다.
     */
    @Override
    public void update(double deltaTime) {
        // 1. 모든 공 이동 (BoundedBall은 자체적으로 벽 충돌 처리)
        super.update(deltaTime);

        // 2. 공 간의 충돌 검사 및 처리
        handleBallCollisions();
    }

    /**
     * 모든 공 간의 충돌을 검사하고 처리합니다.
     * 이중 루프로 모든 쌍을 검사하되, 중복 검사를 방지합니다.
     */
    private void handleBallCollisions() {
        var balls = getBalls();

        for (int i = 0; i < balls.size(); i++) {
            for (int j = i + 1; j < balls.size(); j++) {
                Ball ball1 = balls.get(i);
                Ball ball2 = balls.get(j);

                // 충돌 검사 및 처리
                if (BallCollision.areColliding(ball1, ball2)) {
                    BallCollision.resolveElasticCollision(ball1, ball2);
                }
            }
        }
    }
}
