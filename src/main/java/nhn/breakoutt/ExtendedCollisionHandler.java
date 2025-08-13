package nhn.breakoutt;

import java.util.List;
import java.lang.reflect.Method;

public class ExtendedCollisionHandler {
    public void checkBallToBallCollisions(List<Ball> balls) {
        for (int i = 0; i < balls.size(); i++) {
            for (int j = i + 1; j < balls.size(); j++) {
                Ball ball1 = balls.get(i);
                Ball ball2 = balls.get(j);

                if (isBallColliding(ball1, ball2)) {
                    resolveBallCollision(ball1, ball2);
                }
            }
        }
    }

    public void checkBoxToBoxCollisions(List<Box> boxes) {
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = i + 1; j < boxes.size(); j++) {
                Box box1 = boxes.get(i);
                Box box2 = boxes.get(j);

                if (isBoxColliding(box1, box2)) {
                    resolveBoxCollision(box1, box2);
                }
            }
        }
    }

    public void checkBallToBoxCollisions(List<Ball> balls, List<Box> boxes) {
        for (Ball ball : balls) {
            for (Box box : boxes) {
                CollisionSide side = getBallBoxCollisionSide(ball, box);
                if (side != null) {
                    resolveBallBoxCollision(ball, box, side);
                }
            }
        }
    }

    private boolean isBallColliding(Ball ball1, Ball ball2) {
        Point center1 = ball1.getCenter();
        Point center2 = ball2.getCenter();

        double dx = center1.getX() - center2.getX();
        double dy = center1.getY() - center2.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance <= (ball1.getRadius() + ball2.getRadius());
    }

    private static boolean isBoxColliding(Box box1, Box box2) {
        // Box1의 경계 계산
        double box1Left = box1.getCenter().getX() - box1.getWidth() / 2;
        double box1Right = box1.getCenter().getX() + box1.getWidth() / 2;
        double box1Top = box1.getCenter().getY() - box1.getHeight() / 2;
        double box1Bottom = box1.getCenter().getY() + box1.getHeight() / 2;

        // Box2의 경계 계산
        double box2Left = box2.getCenter().getX() - box2.getWidth() / 2;
        double box2Right = box2.getCenter().getX() + box2.getWidth() / 2;
        double box2Top = box2.getCenter().getY() - box2.getHeight() / 2;
        double box2Bottom = box2.getCenter().getY() + box2.getHeight() / 2;

        return !(box1Right < box2Left || box1Left > box2Right ||
                 box1Bottom < box2Top || box1Top > box2Bottom);
    }

    private static CollisionSide getBallBoxCollisionSide(Ball ball, Box box) {
        Point ballCenter = ball.getCenter();
        Point boxCenter = box.getCenter();
        double ballRadius = ball.getRadius();

        // Box의 경계 계산
        double boxLeft = boxCenter.getX() - box.getWidth() / 2;
        double boxRight = boxCenter.getX() + box.getWidth() / 2;
        double boxTop = boxCenter.getY() - box.getHeight() / 2;
        double boxBottom = boxCenter.getY() + box.getHeight() / 2;

        // Box에서 Ball 중심까지 가장 가까운 점 찾기
        double closestX = Math.max(boxLeft, Math.min(ballCenter.getX(), boxRight));
        double closestY = Math.max(boxTop, Math.min(ballCenter.getY(), boxBottom));

        // 가장 가까운 점과 Ball 중심의 거리 계산
        double dx = ballCenter.getX() - closestX;
        double dy = ballCenter.getY() - closestY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // 충돌하지 않으면 null 반환
        if (distance > ballRadius) {
            return null;
        }

        // Ball이 Box 경계에 정확히 있는 경우의 면 판단
        if (ballCenter.getX() <= boxLeft && ballCenter.getY() >= boxTop && ballCenter.getY() <= boxBottom) {
            return CollisionSide.LEFT;
        } else if (ballCenter.getX() >= boxRight && ballCenter.getY() >= boxTop && ballCenter.getY() <= boxBottom) {
            return CollisionSide.RIGHT;
        } else if (ballCenter.getY() <= boxTop && ballCenter.getX() >= boxLeft && ballCenter.getX() <= boxRight) {
            return CollisionSide.TOP;
        } else if (ballCenter.getY() >= boxBottom && ballCenter.getX() >= boxLeft && ballCenter.getX() <= boxRight) {
            return CollisionSide.BOTTOM;
        } else {
            // Ball이 Box 내부나 코너에 있는 경우 - 중심 위치로 판단
            if (Math.abs(dx) > Math.abs(dy)) {
                if (ballCenter.getX() < boxCenter.getX()) {
                    return CollisionSide.LEFT;
                } else {
                    return CollisionSide.RIGHT;
                }
            } else {
                if (ballCenter.getY() < boxCenter.getY()) {
                    return CollisionSide.TOP;
                } else {
                    return CollisionSide.BOTTOM;
                }
            }
        }
    }

    private void resolveBallCollision(Ball ball1, Ball ball2) {
        // MovableBall인지 확인하고 속도 처리
        if (ball1 instanceof MovableBall && ball2 instanceof MovableBall) {
            MovableBall movableBall1 = (MovableBall) ball1;
            MovableBall movableBall2 = (MovableBall) ball2;

            // 간단한 속도 교환 (탄성 충돌)
            Vector2D velocity1 = movableBall1.getVelocity();
            Vector2D velocity2 = movableBall2.getVelocity();

            movableBall1.setVelocity(velocity2);
            movableBall2.setVelocity(velocity1);
        }

        // 위치 분리 처리
        Point center1 = ball1.getCenter();
        Point center2 = ball2.getCenter();

        double dx = center1.getX() - center2.getX();
        double dy = center1.getY() - center2.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) return; // 같은 위치에 있는 경우 처리 불가

        // 정규화된 방향 벡터
        double nx = dx / distance;
        double ny = dy / distance;

        // 겹침 거리
        double overlap = (ball1.getRadius() + ball2.getRadius() - distance) / 2;

        // Ball들을 분리
        ball1.moveTo(new Point(center1.getX() + nx * overlap, center1.getY() + ny * overlap));
        ball2.moveTo(new Point(center2.getX() - nx * overlap, center2.getY() - ny * overlap));
    }

    private void resolveBoxCollision(Box box1, Box box2) {
        // MovableBox인지 확인하고 속도 처리
        if (box1 instanceof MovableBox && box2 instanceof MovableBox) {
            MovableBox movableBox1 = (MovableBox) box1;
            MovableBox movableBox2 = (MovableBox) box2;

            // 간단한 속도 교환 (탄성 충돌)
            Vector2D velocity1 = movableBox1.getVelocity();
            Vector2D velocity2 = movableBox2.getVelocity();

            movableBox1.setVelocity(velocity2);
            movableBox2.setVelocity(velocity1);
        }

        // 위치 분리 처리
        Point center1 = box1.getCenter();
        Point center2 = box2.getCenter();

        double dx = center1.getX() - center2.getX();
        double dy = center1.getY() - center2.getY();

        // 겹침 거리 계산
        double overlapX = (box1.getWidth() + box2.getWidth()) / 2 - Math.abs(dx);
        double overlapY = (box1.getHeight() + box2.getHeight()) / 2 - Math.abs(dy);

        // 더 작은 겹침 방향으로 분리
        if (overlapX < overlapY) {
            // X축 방향으로 분리
            double moveDistance = overlapX / 2;
            if (dx > 0) {
                box1.moveTo(new Point(center1.getX() + moveDistance, center1.getY()));
                box2.moveTo(new Point(center2.getX() - moveDistance, center2.getY()));
            } else {
                box1.moveTo(new Point(center1.getX() - moveDistance, center1.getY()));
                box2.moveTo(new Point(center2.getX() + moveDistance, center2.getY()));
            }
        } else {
            // Y축 방향으로 분리
            double moveDistance = overlapY / 2;
            if (dy > 0) {
                box1.moveTo(new Point(center1.getX(), center1.getY() + moveDistance));
                box2.moveTo(new Point(center2.getX(), center2.getY() - moveDistance));
            } else {
                box1.moveTo(new Point(center1.getX(), center1.getY() - moveDistance));
                box2.moveTo(new Point(center2.getX(), center2.getY() + moveDistance));
            }
        }
    }

    private static void resolveBallBoxCollision(Ball ball, Box box, CollisionSide side) {
        // MovableBall인지 확인하고 속도 반전 처리
        if (ball instanceof MovableBall) {
            MovableBall movableBall = (MovableBall) ball;
            Vector2D velocity = movableBall.getVelocity();

            switch (side) {
                case LEFT:
                case RIGHT:
                    // X 속도 반전
                    movableBall.setVelocity(new Vector2D(-velocity.getX(), velocity.getY()));
                    break;
                case TOP:
                case BOTTOM:
                    // Y 속도 반전
                    movableBall.setVelocity(new Vector2D(velocity.getX(), -velocity.getY()));
                    break;
                case CORNER:
                    // 코너 충돌 시 양방향 반전
                    movableBall.setVelocity(new Vector2D(-velocity.getX(), -velocity.getY()));
                    break;
            }
        }

        // 위치 보정
        Point ballCenter = ball.getCenter();
        Point boxCenter = box.getCenter();
        double ballRadius = ball.getRadius();

        switch (side) {
            case LEFT:
                // Ball을 Box 왼쪽으로 이동
                double newX = boxCenter.getX() - box.getWidth() / 2 - ballRadius;
                ball.moveTo(new Point(newX, ballCenter.getY()));
                break;
            case RIGHT:
                // Ball을 Box 오른쪽으로 이동
                newX = boxCenter.getX() + box.getWidth() / 2 + ballRadius;
                ball.moveTo(new Point(newX, ballCenter.getY()));
                break;
            case TOP:
                // Ball을 Box 위쪽으로 이동
                double newY = boxCenter.getY() - box.getHeight() / 2 - ballRadius;
                ball.moveTo(new Point(ballCenter.getX(), newY));
                break;
            case BOTTOM:
                // Ball을 Box 아래쪽으로 이동
                newY = boxCenter.getY() + box.getHeight() / 2 + ballRadius;
                ball.moveTo(new Point(ballCenter.getX(), newY));
                break;
            case CORNER:
                // 코너 충돌의 경우 Ball을 가장 가까운 안전한 위치로 이동
                resolveCornerCollision(ball, box);
                break;
        }
    }

    private static void resolveCornerCollision(Ball ball, Box box) {
        Point ballCenter = ball.getCenter();
        Point boxCenter = box.getCenter();

        double dx = ballCenter.getX() - boxCenter.getX();
        double dy = ballCenter.getY() - boxCenter.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) return;

        // 정규화된 방향 벡터
        double nx = dx / distance;
        double ny = dy / distance;

        // Ball을 Box에서 멀어지는 방향으로 이동
        double safeDistance = Math.max(box.getWidth(), box.getHeight()) / 2 + ball.getRadius() + 1;
        ball.moveTo(new Point(
            boxCenter.getX() + nx * safeDistance,
            boxCenter.getY() + ny * safeDistance
        ));
    }

    public CollisionSide detectCollisionSide(double x, double y, Box box) {
        Point boxCenter = box.getCenter();

        // Box의 경계 계산
        double boxLeft = boxCenter.getX() - box.getWidth() / 2;
        double boxRight = boxCenter.getX() + box.getWidth() / 2;
        double boxTop = boxCenter.getY() - box.getHeight() / 2;
        double boxBottom = boxCenter.getY() + box.getHeight() / 2;

        // 점이 Box 외부에 있는 경우
        if (x < boxLeft) {
            return CollisionSide.LEFT;
        } else if (x > boxRight) {
            return CollisionSide.RIGHT;
        } else if (y < boxTop) {
            return CollisionSide.TOP;
        } else if (y > boxBottom) {
            return CollisionSide.BOTTOM;
        } else {
            // Box 내부의 점인 경우
            
            // Box 중심점인 경우 CORNER 반환
            if (Math.abs(x - boxCenter.getX()) < 0.1 && Math.abs(y - boxCenter.getY()) < 0.1) {
                return CollisionSide.CORNER;
            }
            
            // 가장 가까운 면을 찾음
            double distToLeft = x - boxLeft;
            double distToRight = boxRight - x;
            double distToTop = y - boxTop;
            double distToBottom = boxBottom - y;

            double minDist = Math.min(Math.min(distToLeft, distToRight), Math.min(distToTop, distToBottom));

            if (minDist == distToLeft) return CollisionSide.LEFT;
            if (minDist == distToRight) return CollisionSide.RIGHT;
            if (minDist == distToTop) return CollisionSide.TOP;
            if (minDist == distToBottom) return CollisionSide.BOTTOM;

            return CollisionSide.CORNER; // fallback
        }
    }

    public boolean hasMethod(String methodName) {
        try {
            Method[] methods = this.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
