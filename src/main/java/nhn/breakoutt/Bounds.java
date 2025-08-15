package nhn.breakoutt;

public abstract class Bounds {
    public abstract double getMinX();
    public abstract double getMinY();
    public abstract double getMaxX();
    public abstract double getMaxY();

    public double getWidth() {
        return getMaxX() - getMinX();
    }

    public double getHeight() {
        return getMaxY() - getMinY();
    }

    public double getCenterX() {
        return (getMinX() + getMaxX()) / 2.0;
    }

    public double getCenterY() {
        return (getMinY() + getMaxY()) / 2.0;
    }

    public Point getCenter() {
        return new Point(getCenterX(), getCenterY());
    }

    public boolean contains(Point point) {
        if (point == null) {
            return false;
        }
        return point.getX() >= getMinX() && point.getX() <= getMaxX() &&
               point.getY() >= getMinY() && point.getY() <= getMaxY();
    }

    public boolean contains(double x, double y) {
        return x >= getMinX() && x <= getMaxX() &&
               y >= getMinY() && y <= getMaxY();
    }

    public boolean contains(Bounds other) {
        if (other == null) {
            return false;
        }
        return other.getMinX() >= getMinX() && other.getMaxX() <= getMaxX() &&
               other.getMinY() >= getMinY() && other.getMaxY() <= getMaxY();
    }

    public boolean intersects(Bounds other) {
        if (other == null) {
            return false;
        }
        return !(other.getMaxX() < getMinX() || other.getMinX() > getMaxX() ||
                 other.getMaxY() < getMinY() || other.getMinY() > getMaxY());
    }

    public double getArea() {
        return getWidth() * getHeight();
    }

    public boolean isEmpty() {
        return getWidth() <= 0 || getHeight() <= 0;
    }

    public Bounds intersection(Bounds other) {
        if (other == null || !intersects(other)) {
            return null;
        }

        double intersectionMinX = Math.max(getMinX(), other.getMinX());
        double intersectionMinY = Math.max(getMinY(), other.getMinY());
        double intersectionMaxX = Math.min(getMaxX(), other.getMaxX());
        double intersectionMaxY = Math.min(getMaxY(), other.getMaxY());

        // 익명 클래스로 간단한 Bounds 구현 반환
        return new Bounds() {
            @Override
            public double getMinX() { return intersectionMinX; }
            @Override
            public double getMinY() { return intersectionMinY; }
            @Override
            public double getMaxX() { return intersectionMaxX; }
            @Override
            public double getMaxY() { return intersectionMaxY; }
        };
    }

    /**
     * 두 경계의 합집합 영역을 반환합니다.
     * @param other 다른 경계
     * @return 합집합 경계
     */
    public Bounds union(Bounds other) {
        if (other == null) {
            return this;
        }

        double unionMinX = Math.min(getMinX(), other.getMinX());
        double unionMinY = Math.min(getMinY(), other.getMinY());
        double unionMaxX = Math.max(getMaxX(), other.getMaxX());
        double unionMaxY = Math.max(getMaxY(), other.getMaxY());

        // 익명 클래스로 간단한 Bounds 구현 반환
        return new Bounds() {
            @Override
            public double getMinX() { return unionMinX; }
            @Override
            public double getMinY() { return unionMinY; }
            @Override
            public double getMaxX() { return unionMaxX; }
            @Override
            public double getMaxY() { return unionMaxY; }
        };
    }

    @Override
    public String toString() {
        return String.format("Bounds[minX=%.2f, minY=%.2f, maxX=%.2f, maxY=%.2f, width=%.2f, height=%.2f]",
                getMinX(), getMinY(), getMaxX(), getMaxY(), getWidth(), getHeight());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Bounds)) return false;

        Bounds other = (Bounds) obj;
        return Double.compare(getMinX(), other.getMinX()) == 0 &&
               Double.compare(getMinY(), other.getMinY()) == 0 &&
               Double.compare(getMaxX(), other.getMaxX()) == 0 &&
               Double.compare(getMaxY(), other.getMaxY()) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getMinX(), getMinY(), getMaxX(), getMaxY());
    }
}
