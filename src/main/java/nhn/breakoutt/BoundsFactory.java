package nhn.breakoutt;

/**
 * Factory Method 패턴을 사용하여 다양한 Bounds 객체를 생성하는 팩토리 클래스입니다.
 */
public class BoundsFactory {

    /**
     * 원형 경계를 생성합니다.
     * @param center 중심점
     * @param radius 반지름
     * @return CircleBounds 객체
     */
    public static Bounds createCircleBounds(Point center, double radius) {
        return new CircleBounds(center, radius);
    }

    /**
     * 원형 경계를 생성합니다.
     * @param centerX 중심 X 좌표
     * @param centerY 중심 Y 좌표
     * @param radius 반지름
     * @return CircleBounds 객체
     */
    public static Bounds createCircleBounds(double centerX, double centerY, double radius) {
        return new CircleBounds(centerX, centerY, radius);
    }

    /**
     * 사각형 경계를 생성합니다.
     * @param x 최소 X 좌표
     * @param y 최소 Y 좌표
     * @param width 너비
     * @param height 높이
     * @return RectangleBounds 객체
     */
    public static Bounds createRectangleBounds(double x, double y, double width, double height) {
        return new RectangleBounds(x, y, width, height);
    }

    /**
     * 점을 기반으로 한 사각형 경계를 생성합니다.
     * @param topLeft 왼쪽 상단 모서리
     * @param width 너비
     * @param height 높이
     * @return RectangleBounds 객체
     */
    public static Bounds createRectangleBounds(Point topLeft, double width, double height) {
        return new RectangleBounds(topLeft.getX(), topLeft.getY(), width, height);
    }
}
