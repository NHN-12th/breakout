package nhn.breakoutt;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaintableBoxTest {

    @Test
    public void testPaintableBoxCreation() {
        PaintableBox box = new PaintableBox(new Point(100.0, 100.0), 50, 40, Color.BLUE);

        // 상속받은 Box의 속성들 확인
        assertEquals(100, box.getX(), 0.001, "X 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(100, box.getY(), 0.001, "Y 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(50, box.getWidth(), 0.001, "너비가 올바르게 설정되지 않았습니다");
        assertEquals(40, box.getHeight(), 0.001, "높이가 올바르게 설정되지 않았습니다");

        // PaintableBox의 고유 속성 확인
        assertEquals(Color.BLUE, box.getColor(), "색상이 올바르게 설정되지 않았습니다");
    }

    @Test
    public void testColorHandling() {
        PaintableBox box = new PaintableBox(new Point(0.0, 0.0), 50, 50, Color.RED);

        // 색상 변경
        box.setColor(Color.YELLOW);
        assertEquals(Color.YELLOW, box.getColor(), "색상 변경이 올바르게 작동하지 않습니다");

        // null 색상 처리 (PaintableBall과 동일한 로직이어야 함)
        assertThrows(IllegalArgumentException.class, () -> {
            box.setColor(null);
        }, "null 색상 설정 시 예외가 발생해야 합니다");
    }

    @Test
    public void testDefaultColor() {
        // 색상 없이 생성하는 생성자가 있다면
        PaintableBox box = new PaintableBox(new Point(0.0, 0.0), 50, 50);
        assertNotNull(box.getColor(), "기본 색상이 설정되어야 합니다");
        // 기본 색상은 빨간색이어야 함
        assertEquals(Color.RED, box.getColor(), "기본 색상은 빨간색이어야 합니다");
    }

    @Test
    public void testCodeDuplicationWithPaintableBall() {
        // 이 테스트는 PaintableBox와 PaintableBall의 색상 처리 로직이
        // 동일하다는 것을 보여줍니다 (코드 중복 문제)
        PaintableBox box = new PaintableBox(new Point(0.0, 0.0), 50, 50, Color.PURPLE);
        PaintableBall ball = new PaintableBall(new Point(0.0, 0.0), 25, Color.PURPLE);

        // 동일한 색상 처리
        assertEquals(box.getColor(), ball.getColor(), "색상 처리 로직이 동일해야 합니다");

        // 동일한 색상 변경
        box.setColor(Color.ORANGE);
        ball.setColor(Color.ORANGE);
        assertEquals(box.getColor(), ball.getColor(), "색상 변경 로직이 동일해야 합니다");

        // 동일한 null 처리
        assertThrows(IllegalArgumentException.class, () -> box.setColor(null));
        assertThrows(IllegalArgumentException.class, () -> ball.setColor(null));
    }
}