package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PolymorphismSolutionTest {

    @Test
    public void testPolymorphicRendering() {
        // 6장의 문제: 각 타입별로 별도 렌더링 로직 필요
        // 7장의 해결: Paintable 인터페이스로 통합 처리

        List<Paintable> paintables = new ArrayList<>();
        paintables.add(new Ball(100, 100, 20, Color.RED));
        paintables.add(new Box(200, 200, 50, 40, Color.BLUE));
        paintables.add(new Triangle(300, 300, 30, Color.GREEN));
        paintables.add(new MovableBox(400, 400, 60, 50, Color.YELLOW));

        GraphicsContext gc = mock(GraphicsContext.class);

        // 모든 타입을 동일한 방식으로 처리 가능!
        for (Paintable paintable : paintables) {
            assertDoesNotThrow(() -> paintable.paint(gc),
                    "Paintable 객체 렌더링 중 예외 발생: " + paintable.getClass().getSimpleName());
        }

        // 6장에서는 각 타입별로 instanceof 체크가 필요했지만
        // 7장에서는 단일 루프로 모든 객체 처리 가능!
        assertEquals(4, paintables.size(), "모든 타입의 객체가 하나의 리스트에서 관리됩니다");
    }

    @Test
    public void testPolymorphicMovement() {
        // 6장의 문제: MovableBall과 MovableBox를 별도로 처리
        // 7장의 해결: Movable 인터페이스로 통합 처리

        List<Movable> movables = new ArrayList<>();
        movables.add(new Ball(100, 100, 20, Color.RED));
        movables.add(new MovableBox(200, 200, 50, 40, Color.BLUE));
        movables.add(new Triangle(300, 300, 30, Color.GREEN));

        // 모든 객체에 동일한 속도 설정
        for (Movable movable : movables) {
            movable.setDx(50);
            movable.setDy(30);
        }

        // 위치 저장
        List<Double> oldXPositions = new ArrayList<>();
        List<Double> oldYPositions = new ArrayList<>();
        for (Movable movable : movables) {
            if (movable instanceof Ball) {
                oldXPositions.add(((Ball) movable).getX());
                oldYPositions.add(((Ball) movable).getY());
            } else if (movable instanceof MovableBox) {
                oldXPositions.add(((MovableBox) movable).getX());
                oldYPositions.add(((MovableBox) movable).getY());
            } else if (movable instanceof Triangle) {
                oldXPositions.add(((Triangle) movable).getX());
                oldYPositions.add(((Triangle) movable).getY());
            }
        }

        // 모든 객체를 동일한 방식으로 이동!
        for (Movable movable : movables) {
            movable.move(1.0);
        }

        // 이동 확인
        int index = 0;
        for (Movable movable : movables) {
            double newX, newY;
            if (movable instanceof Ball) {
                newX = ((Ball) movable).getX();
                newY = ((Ball) movable).getY();
            } else if (movable instanceof MovableBox) {
                newX = ((MovableBox) movable).getX();
                newY = ((MovableBox) movable).getY();
            } else if (movable instanceof Triangle) {
                newX = ((Triangle) movable).getX();
                newY = ((Triangle) movable).getY();
            } else {
                continue;
            }

            assertNotEquals(oldXPositions.get(index), newX,
                    "객체가 X 방향으로 이동하지 않았습니다: " + movable.getClass().getSimpleName());
            assertNotEquals(oldYPositions.get(index), newY,
                    "객체가 Y 방향으로 이동하지 않았습니다: " + movable.getClass().getSimpleName());
            index++;
        }
    }

    @Test
    public void testPolymorphicCollision() {
        // 6장의 문제: Ball-Ball, Box-Box, Ball-Box 등 조합별 메서드 필요
        // 7장의 해결: Collidable 인터페이스로 통합 처리

        List<Collidable> collidables = new ArrayList<>();
        Ball ball = new Ball(100, 100, 20, Color.RED);
        Box box = new Box(150, 150, 40, 30, Color.BLUE);
        Triangle triangle = new Triangle(200, 200, 25, Color.GREEN);

        ball.setCollisionAction(CollisionAction.BOUNCE);
        box.setCollisionAction(CollisionAction.BOUNCE);
        triangle.setCollisionAction(CollisionAction.BOUNCE);

        collidables.add(ball);
        collidables.add(box);
        collidables.add(triangle);

        // 모든 조합의 충돌을 동일한 로직으로 처리!
        for (int i = 0; i < collidables.size(); i++) {
            for (int j = i + 1; j < collidables.size(); j++) {
                Collidable obj1 = collidables.get(i);
                Collidable obj2 = collidables.get(j);

                if (obj1.isColliding(obj2)) {
                    // 두 객체 모두 충돌 처리
                    assertDoesNotThrow(() -> obj1.handleCollision(obj2),
                            "충돌 처리 중 예외 발생: " + obj1.getClass().getSimpleName() + " vs " + obj2.getClass().getSimpleName());
                    assertDoesNotThrow(() -> obj2.handleCollision(obj1),
                            "충돌 처리 중 예외 발생: " + obj2.getClass().getSimpleName() + " vs " + obj1.getClass().getSimpleName());
                }
            }
        }

        // 6장에서는 n(n+1)/2개의 별도 메서드가 필요했지만
        // 7장에서는 단일 이중 루프로 모든 조합 처리!
        System.out.println("3개 타입의 모든 충돌 조합을 단일 로직으로 처리 완료!");
    }

    @Test
    public void testNoMoreClassExplosion() {
        // 6장의 문제: 기능 조합마다 새 클래스 필요 (2^n 개)
        // 7장의 해결: 인터페이스를 통한 유연한 조합

        // 하나의 Ball 클래스가 모든 기능을 동시에 제공!
        Ball ball = new Ball(100, 100, 20, Color.RED);

        // 모든 기능을 하나의 객체에서 사용 가능
        assertTrue(ball instanceof Paintable, "그리기 기능");
        assertTrue(ball instanceof Movable, "움직임 기능");
        assertTrue(ball instanceof Collidable, "충돌 기능");
        assertTrue(ball instanceof Boundable, "경계 기능");

        // 하나의 Triangle 클래스도 마찬가지
        Triangle triangle = new Triangle(200, 200, 30, Color.BLUE);
        assertTrue(triangle instanceof Paintable, "Triangle 그리기 기능");
        assertTrue(triangle instanceof Movable, "Triangle 움직임 기능");
        assertTrue(triangle instanceof Collidable, "Triangle 충돌 기능");
        assertTrue(triangle instanceof Boundable, "Triangle 경계 기능");

        // 6장에서는 16개 클래스가 필요했지만 (Ball 8개 + Box 8개)
        // 7장에서는 기본 클래스 3개 + 특수 클래스 몇 개만 필요!
        System.out.println("클래스 폭발 문제 해결: 소수의 클래스로 모든 기능 조합 제공!");
    }
}