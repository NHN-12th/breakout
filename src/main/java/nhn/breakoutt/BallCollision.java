package nhn.breakoutt;

/**
 * 두 공 사이의 충돌을 감지하고 처리하는 클래스입니다.
 */
public class BallCollision {

    /**
     * 두 공이 충돌하고 있는지 검사합니다.
     * @param ball1 첫 번째 공
     * @param ball2 두 번째 공
     * @return 충돌하면 true, 아니면 false
     */
    public static boolean areColliding(Ball ball1, Ball ball2) {
        Point center1 = ball1.getCenter();
        Point center2 = ball2.getCenter();

        // 두 공의 중심 거리 계산
        double distance = center1.distanceTo(center2);

        // 거리 < 두 반지름의 합이면 충돌
        return distance < (ball1.getRadius() + ball2.getRadius());
    }

    /**
     * 두 공의 탄성 충돌을 처리합니다.
     * MovableBall인 경우에만 충돌 처리를 수행합니다.
     * @param ball1 첫 번째 공
     * @param ball2 두 번째 공
     */
    public static void resolveElasticCollision(Ball ball1, Ball ball2) {
        // MovableBall이 아닌 경우 충돌 처리하지 않음
        if (!(ball1 instanceof MovableBall movableBall1) ||
            !(ball2 instanceof MovableBall movableBall2)) {
            return;
        }

        // 멀어지고 있는지 확인 (이미 처리된 충돌 방지)
        Vector2D relativeVelocity = movableBall1.getVelocity().subtract(movableBall2.getVelocity());
        Vector2D collisionNormal = ball2.getCenter().subtract(ball1.getCenter()).normalize();

        // 상대 속도가 충돌 방향과 반대면 이미 멀어지고 있음
        if (relativeVelocity.dot(collisionNormal) <= 0) {
            return;
        }

        // 겹침 해결 (공들을 분리)
        separateBalls(ball1, ball2);

        // 탄성 충돌 처리
        // 질량이 같다고 가정 (반지름의 세제곱에 비례)
        double mass1 = Math.pow(ball1.getRadius(), 3);
        double mass2 = Math.pow(ball2.getRadius(), 3);

        // 충돌 방향의 속도 성분 계산
        double v1n = movableBall1.getVelocity().dot(collisionNormal);
        double v2n = movableBall2.getVelocity().dot(collisionNormal);

        // 탄성 충돌 공식 적용
        double v1nNew = (v1n * (mass1 - mass2) + 2 * mass2 * v2n) / (mass1 + mass2);
        double v2nNew = (v2n * (mass2 - mass1) + 2 * mass1 * v1n) / (mass1 + mass2);

        // 새로운 속도 계산
        Vector2D v1nVector = collisionNormal.multiply(v1n);
        Vector2D v1t = movableBall1.getVelocity().subtract(v1nVector);
        Vector2D newV1 = v1t.add(collisionNormal.multiply(v1nNew));

        Vector2D v2nVector = collisionNormal.multiply(v2n);
        Vector2D v2t = movableBall2.getVelocity().subtract(v2nVector);
        Vector2D newV2 = v2t.add(collisionNormal.multiply(v2nNew));

        // 속도 업데이트
        movableBall1.setVelocity(newV1);
        movableBall2.setVelocity(newV2);
    }

    /**
     * 겹친 공들을 분리합니다.
     * @param ball1 첫 번째 공
     * @param ball2 두 번째 공
     */
    public static void separateBalls(Ball ball1, Ball ball2) {
        Point center1 = ball1.getCenter();
        Point center2 = ball2.getCenter();

        double distance = center1.distanceTo(center2);
        double overlap = ball1.getRadius() + ball2.getRadius() - distance;

        if (overlap > 0) {
            // 분리 방향 (정규화된 벡터)
            Vector2D separationDirection = center1.subtract(center2).normalize();

            // 각 공을 겹침의 절반만큼 밀어냄
            double separationDistance = overlap / 2;

            Vector2D separation1 = separationDirection.multiply(separationDistance);
            Vector2D separation2 = separationDirection.multiply(-separationDistance);

            // 위치 업데이트 (MovableBall인 경우에만)
            if (ball1 instanceof MovableBall movableBall1) {
                movableBall1.moveTo(center1.add(separation1));
            }
            if (ball2 instanceof MovableBall movableBall2) {
                movableBall2.moveTo(center2.add(separation2));
            }
        }
    }
}
