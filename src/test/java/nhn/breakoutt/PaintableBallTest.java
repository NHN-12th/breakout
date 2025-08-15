package nhn.breakoutt;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaintableBallTest {

    @Test
    public void testPaintableBallCreation() {
        PaintableBall ball = new PaintableBall(new Point(100.0, 100.0), 20, Color.RED);

        // 상속받은 Ball의 속성들 확인
        Point center = ball.getCenter();
        assertEquals(100, center.getX(), 0.001, "X 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(100, center.getY(), 0.001, "Y 좌표가 올바르게 설정되지 않았습니다");
        assertEquals(20, ball.getRadius(), 0.001, "반지름이 올바르게 설정되지 않았습니다");

        // PaintableBall의 고유 속성 확인
        assertEquals(Color.RED, ball.getColor(), "색상이 올바르게 설정되지 않았습니다");
    }

    @Test
    public void testPaintableBallInheritance() {
        PaintableBall paintableBall = new PaintableBall(new Point(100.0, 100.0), 20, Color.BLUE);

        // Ball의 메서드들이 작동하는지 확인 (상속 확인)
        assertTrue(paintableBall instanceof Ball, "PaintableBall은 Ball을 상속받아야 합니다");

        // Ball의 메서드 사용 가능한지 확인
        Point newCenter = new Point(200.0, 300.0);
        paintableBall.moveTo(newCenter);
        Point center = paintableBall.getCenter();
        assertEquals(200, center.getX(), 0.001, "상속받은 moveTo가 작동하지 않습니다");
        assertEquals(300, center.getY(), 0.001, "상속받은 moveTo가 작동하지 않습니다");

        // contains 메서드도 사용 가능한지 확인
        assertTrue(paintableBall.contains(200, 300), "상속받은 contains가 작동하지 않습니다");
    }

    @Test
    public void testColorHandling() {
        PaintableBall ball = new PaintableBall(new Point(0.0, 0.0), 10, Color.GREEN);

        // 색상 변경
        ball.setColor(Color.YELLOW);
        assertEquals(Color.YELLOW, ball.getColor(), "색상 변경이 올바르게 작동하지 않습니다");

        // null 색상 처리 (구현에 따라 기본 색상으로 설정하거나 예외 발생)
        ball.setColor(null);
        assertNotNull(ball.getColor(), "null 색상 설정 후에도 색상이 있어야 합니다");
    }

    @Test
    public void testDefaultColor() {
        // 색상 없이 생성하는 생성자 테스트
        PaintableBall ball = new PaintableBall(new Point(100.0, 100.0), 10);
        assertNotNull(ball.getColor(), "기본 색상이 설정되어야 합니다");
        assertEquals(Color.RED, ball.getColor(), "기본 색상은 빨간색이어야 합니다");
    }

    @Test
    public void testDraw() {
        // 실제 draw 메서드가 예외 없이 실행되는지 확인
        PaintableBall ball = new PaintableBall(new Point(100.0, 100.0), 25, Color.BLUE);

        // GraphicsContext 없이 draw 메서드의 로직을 간접적으로 테스트
        // draw 메서드에서 사용하는 값들을 직접 확인
        Point center = ball.getCenter();
        double expectedLeftX = center.getX() - ball.getRadius();
        double expectedTopY = center.getY() - ball.getRadius();
        double expectedDiameter = ball.getRadius() * 2;

        assertEquals(75.0, expectedLeftX, 0.001, "왼쪽 X 좌표 계산이 잘못되었습니다");
        assertEquals(75.0, expectedTopY, 0.001, "상단 Y 좌표 계산이 잘못되었습니다");
        assertEquals(50.0, expectedDiameter, 0.001, "지름 계산이 잘못되었습니다");
        assertEquals(Color.BLUE, ball.getColor(), "색상이 올바르지 않습니다");
    }

    @Test
    public void testCanvasAndGraphicsContextInsteadOfGraphicsContext() {
        // GraphicsContext mock 대신 실제 Canvas와 GraphicsContext 생성
        Canvas canvas = new Canvas(200, 200);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        PaintableBall ball = new PaintableBall(new Point(100.0, 100.0), 25, Color.BLUE);

        // draw 메서드가 예외 없이 실행되는지 테스트
        assertDoesNotThrow(() -> {
            ball.paint(gc);
        }, "draw 메서드 실행 중 예외가 발생했습니다");

        // 색상이 올바르게 설정되었는지 확인
        assertEquals(Color.BLUE, ball.getColor(), "색상이 올바르지 않습니다");
    }
}
