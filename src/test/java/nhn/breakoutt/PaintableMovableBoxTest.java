package nhn.breakoutt;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaintableMovableBoxTest {

    @Test
    public void testPaintableMovableBoxCreation() {
        PaintableMovableBox box = new PaintableMovableBox(new Point(100.0, 100.0), 50, 40, Color.CYAN);

        // Box 속성 확인
        Point position = box.getPosition();
        assertEquals(100, position.getX(), 0.001, "X 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(100, position.getY(), 0.001, "Y 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(50, box.getWidth(), 0.001, "너비가 올바르게 설정되지 않았습니다");
        assertEquals(40, box.getHeight(), 0.001, "높이가 올바르게 설정되지 않았습니다");

        // Paintable 속성 확인
        assertEquals(Color.CYAN, box.getColor(), "색상이 올바르게 설정되지 않았습니다");

        // Movable 속성 확인
        assertEquals(0, box.getDx(), 0.001, "초기 X 속도는 0이어야 합니다");
        assertEquals(0, box.getDy(), 0.001, "초기 Y 속도는 0이어야 합니다");
    }

    @Test
    public void testMultipleInheritanceChain() {
        PaintableMovableBox box = new PaintableMovableBox(new Point(0.0, 0.0), 30, 30, Color.MAGENTA);

        // 상속 체인 확인
        assertTrue(box instanceof Box, "Box를 상속받아야 합니다");
        assertTrue(box instanceof MovableBox, "MovableBox를 상속받아야 합니다");
        assertTrue(box instanceof PaintableMovableBox, "PaintableMovableBox 타입이어야 합니다");

        // 모든 기능이 작동하는지 확인
        box.moveTo(new Point(100.0, 200.0));
        box.setColor(Color.LIME);
        box.setVelocity(new Vector2D(25, 35));

        assertEquals(100, box.getX(), 0.001, "Box 기능이 작동하지 않습니다");
        assertEquals(200, box.getY(), 0.001, "Box 기능이 작동하지 않습니다");
        assertEquals(30, box.getWidth(), 0.001, "Box 기능이 작동하지 않습니다");
        assertEquals(30, box.getHeight(), 0.001, "Box 기능이 작동하지 않습니다");
        assertEquals(Color.LIME, box.getColor(), "Paintable 기능이 작동하지 않습니다");
        assertEquals(25, box.getDx(), 0.001, "Movable 기능이 작동하지 않습니다");
        assertEquals(35, box.getDy(), 0.001, "Movable 기능이 작동하지 않습니다");

        // 이동 기능 확인
        box.move(1.0);
        assertEquals(125, box.getX(), 0.001, "move 기능이 작동하지 않습니다");
        assertEquals(235, box.getY(), 0.001, "move 기능이 작동하지 않습니다");
    }

    @Test
    public void testClassExplosionProblem() {
        // 이 테스트는 기능 조합마다 새로운 클래스가 필요하다는 것을 보여줍니다

        // 3개의 기능: Paintable, Movable, Bounded
        // 각 기능의 조합마다 클래스가 필요 = 2^3 = 8개 클래스

        Box basic = new Box(new Point(0.0, 0.0), 10, 10);
        PaintableBox paintable = new PaintableBox(new Point(0.0, 0.0), 10, 10, Color.RED);
        MovableBox movable = new MovableBox(new Point(0.0, 0.0), 10, 10);
        BoundedBox bounded = new BoundedBox(0, 0, 10, 10);
        PaintableMovableBox paintableMovable = new PaintableMovableBox(0, 0, 10, 10, Color.RED);
        PaintableBoundedBox paintableBounded = new PaintableBoundedBox(0, 0, 10, 10, Color.RED);
        MovableBoundedBox movableBounded = new MovableBoundedBox(0, 0, 10, 10);
        PaintableMovableBoundedBox all = new PaintableMovableBoundedBox(0, 0, 10, 10, Color.RED);

        // 8개의 서로 다른 클래스가 필요함을 확인
        assertNotEquals(basic.getClass(), paintable.getClass());
        assertNotEquals(basic.getClass(), movable.getClass());
        assertNotEquals(paintable.getClass(), paintableMovable.getClass());
        assertNotEquals(movable.getClass(), paintableMovable.getClass());

        // 이것이 클래스 폭발 문제입니다!
        System.out.println("필요한 클래스 수: 8개");
        System.out.println("기능이 하나 더 추가되면: 16개 클래스 필요!");
    }
}