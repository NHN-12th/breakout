package nhn.breakoutt;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InheritanceProblemSummaryTest {

    @Test
    public void testClassExplosionProblem() {
        // 클래스 폭발 문제: 3개 기능 조합 = 8개 클래스 필요

        // 기본 클래스들
        Box box = new Box(new Point(0.0, 0.0), 10, 10);
        Ball ball = new Ball(new Point(0.0, 0.0), 10);

        // 1개 기능 추가 클래스들
        PaintableBox paintableBox = new PaintableBox(new Point(0.0, 0.0), 10, 10, Color.RED);
        MovableBox movableBox = new MovableBox(new Point(0.0, 0.0), 10, 10);
        BoundedBox boundedBox = new BoundedBox(new Point(0.0, 0.0), 10, 10);

        // 2개 기능 조합 클래스들
        PaintableMovableBox paintableMovableBox = new PaintableMovableBox(new Point(0.0, 0.0), 10, 10, Color.BLUE);
        PaintableBoundedBox paintableBoundedBox = new PaintableBoundedBox(new Point(0.0, 0.0), 10, 10, Color.GREEN);
        MovableBoundedBox movableBoundedBox = new MovableBoundedBox(new Point(0.0, 0.0), 10, 10);

        // 3개 기능 모두 포함 클래스
        PaintableMovableBoundedBox allFeaturesBox = new PaintableMovableBoundedBox(new Point(0.0, 0.0), 10, 10, Color.YELLOW);

        // Box만으로도 8개 클래스 필요! (Ball도 마찬가지로 8개)
        // 총 16개 클래스가 필요함
        assertTrue(allFeaturesBox instanceof Box, "모든 기능 클래스도 Box를 상속받아야 함");
        assertTrue(allFeaturesBox instanceof PaintableBox, "Paintable 기능 포함");
        assertTrue(allFeaturesBox instanceof MovableBox, "Movable 기능 포함");

        System.out.println("현재 필요한 클래스 수: 16개 (Box 8개 + Ball 8개)");
        System.out.println("새로운 기능 1개 추가 시: 32개 클래스 필요!");
        System.out.println("새로운 도형 1개 추가 시: 24개 클래스 추가 필요!");
    }

    @Test
    public void testCodeDuplicationProblem() {
        // 코드 중복 문제: 동일한 기능이 여러 클래스에 중복됨

        PaintableBox paintableBox = new PaintableBox(new Point(0.0, 0.0), 10, 10, Color.RED);
        PaintableBall paintableBall = new PaintableBall(new Point(0.0, 0.0), 10, Color.RED);

        // 색상 처리 로직이 완전히 동일함 (코드 중복!)
        paintableBox.setColor(Color.BLUE);
        paintableBall.setColor(Color.BLUE);
        assertEquals(paintableBox.getColor(), paintableBall.getColor());

        // null 처리 로직도 동일함
        assertThrows(IllegalArgumentException.class, () -> paintableBox.setColor(null));
        assertThrows(IllegalArgumentException.class, () -> paintableBall.setColor(null));

        MovableBox movableBox = new MovableBox(new Point(0.0, 0.0), 10, 10);
        MovableBall movableBall = new MovableBall(new Point(0.0, 0.0), 10);

        // 이동 처리 로직도 완전히 동일함 (코드 중복!)
        movableBox.setVelocity(new Vector2D(50, 30));
        movableBall.setVelocity(new Vector2D(50, 30));

        movableBox.move(1.0);
        movableBall.move(1.0);

        Point boxPos = movableBox.getPosition();
        Point ballPos = movableBall.getCenter();
        assertEquals(50, boxPos.getX(), 0.001);
        assertEquals(30, boxPos.getY(), 0.001);
        assertEquals(50, ballPos.getX(), 0.001);
        assertEquals(30, ballPos.getY(), 0.001);
    }

    @Test
    public void testTypeCheckingComplexity() {
        // 타입 체크 복잡성: instanceof 지옥

        Object[] objects = {
                new Ball(new Point(0.0, 0.0), 10),
                new PaintableBall(new Point(0.0, 0.0), 10, Color.RED),
                new MovableBall(new Point(0.0, 0.0), 10),
                new PaintableMovableBall(new Point(0.0, 0.0), 10, Color.BLUE),
                new Box(new Point(0.0, 0.0), 10, 10),
                new PaintableBox(new Point(0.0, 0.0), 10, 10, Color.GREEN),
                new MovableBox(new Point(0.0, 0.0), 10, 10),
                new PaintableMovableBox(new Point(0.0, 0.0), 10, 10, Color.YELLOW)
        };

        // 각 타입별로 처리하려면 복잡한 instanceof 체크 필요
        int paintableCount = 0;
        int movableCount = 0;

        for (Object obj : objects) {
            // Paintable 체크 - 모든 Paintable 타입을 나열해야 함
            if (obj instanceof PaintableBall ||
                    obj instanceof PaintableMovableBall ||
                    obj instanceof PaintableBox ||
                    obj instanceof PaintableMovableBox) {
                paintableCount++;
            }

            // Movable 체크 - 모든 Movable 타입을 나열해야 함
            if (obj instanceof MovableBall ||
                    obj instanceof PaintableMovableBall ||
                    obj instanceof MovableBox ||
                    obj instanceof PaintableMovableBox) {
                movableCount++;
            }
        }

        assertEquals(6, paintableCount, "Paintable 객체 수");
        assertEquals(4, movableCount, "Movable 객체 수");

        // 새로운 타입이 추가될 때마다 모든 instanceof 체크를 수정해야 함!
    }

    @Test
    public void testMaintenanceNightmare() {
        // 유지보수 악몽: 새로운 도형(Triangle) 추가 시나리오

        // 현재 2개 도형(Ball, Box) × 3개 기능 = 16개 클래스
        // Triangle 추가 시 3개 도형 × 3개 기능 = 24개 클래스 (8개 추가)

        // 추가해야 할 클래스들:
        // Triangle, PaintableTriangle, MovableTriangle, BoundedTriangle,
        // PaintableMovableTriangle, PaintableBoundedTriangle,
        // MovableBoundedTriangle, PaintableMovableBoundedTriangle

        // 수정해야 할 기존 코드들:
        // 1. World 클래스에 List<Triangle> triangles 추가
        // 2. 모든 update/render 메서드에 Triangle 처리 로직 추가
        // 3. 충돌 처리에 Triangle 관련 메서드 3개 추가 (Triangle-Ball, Triangle-Box, Triangle-Triangle)
        // 4. 모든 instanceof 체크 코드에 Triangle 타입들 추가

        System.out.println("Triangle 추가 시:");
        System.out.println("- 새로운 클래스: 8개");
        System.out.println("- 수정해야 할 기존 메서드: 수십 개");
        System.out.println("- 추가 충돌 메서드: 3개");
        System.out.println("- instanceof 체크 수정: 모든 타입 체크 코드");

        assertTrue(true, "이것이 상속만 사용했을 때의 유지보수 악몽입니다!");
    }
}