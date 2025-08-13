package nhn.breakoutt;

public class RectangleBounds extends Bounds {
    private final double minX;
    private final double minY;
    private final double width;
    private final double height;

    /**
     * 사각형 경계를 생성합니다.
     * @param minX 최소 X 좌표 (왼쪽 상단 모서리)
     * @param minY 최소 Y 좌표 (왼쪽 상단 모서리)
     * @param width 너비
     * @param height 높이
     * @throws IllegalArgumentException 너비나 높이가 음수인 경우
     */
    public RectangleBounds(double minX, double minY, double width, double height) {
        if (width < 0) {
            throw new IllegalArgumentException("Width cannot be negative: " + width);
        }
        if (height < 0) {
            throw new IllegalArgumentException("Height cannot be negative: " + height);
        }

        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
    }

    /**
     * 두 점으로부터 사각형 경계를 생성합니다.
     * @param topLeft 왼쪽 상단 점
     * @param bottomRight 오른쪽 하단 점
     * @throws IllegalArgumentException topLeft가 bottomRight보다 오른쪽이나 아래에 있는 경우
     */
    public RectangleBounds(Point topLeft, Point bottomRight) {
        if (topLeft == null || bottomRight == null) {
            throw new IllegalArgumentException("Points cannot be null");
        }
        if (topLeft.getX() > bottomRight.getX() || topLeft.getY() > bottomRight.getY()) {
            throw new IllegalArgumentException("TopLeft must be above and to the left of bottomRight");
        }

        this.minX = topLeft.getX();
        this.minY = topLeft.getY();
        this.width = bottomRight.getX() - topLeft.getX();
        this.height = bottomRight.getY() - topLeft.getY();
    }

    @Override
    public double getMinX() {
        return minX;
    }

    @Override
    public double getMinY() {
        return minY;
    }

    @Override
    public double getMaxX() {
        return minX + width;
    }

    @Override
    public double getMaxY() {
        return minY + height;
    }

    public double getX(){
        return minX;
    }

    public double getY(){
        return minY;
    }
}
