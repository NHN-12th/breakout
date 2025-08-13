package nhn.breakoutt;

import java.util.*;

/**
 * 여러 Bounds의 조합을 표현하는 복합 경계 클래스입니다.
 */
public class CompositeBounds extends Bounds {
    private final List<Bounds> boundsCollection;

    /**
     * 빈 복합 경계를 생성합니다.
     */
    public CompositeBounds() {
        this.boundsCollection = new ArrayList<>();
    }

    /**
     * 여러 경계로 복합 경계를 생성합니다.
     * @param bounds 포함할 경계들
     */
    public CompositeBounds(Bounds... bounds) {
        this();
        for (Bounds bound : bounds) {
            if (bound != null) {
                this.boundsCollection.add(bound);
            }
        }
    }

    /**
     * 경계 컬렉션으로 복합 경계를 생성합니다.
     * @param bounds 포함할 경계 컬렉션
     */
    public CompositeBounds(Collection<Bounds> bounds) {
        this();
        if (bounds != null) {
            for (Bounds bound : bounds) {
                if (bound != null) {
                    this.boundsCollection.add(bound);
                }
            }
        }
    }

    /**
     * 경계를 추가합니다.
     * @param bounds 추가할 경계
     * @return 이 CompositeBounds 인스턴스 (메서드 체이닝용)
     */
    public CompositeBounds addBounds(Bounds bounds) {
        if (bounds != null) {
            this.boundsCollection.add(bounds);
        }
        return this;
    }

    /**
     * 경계를 제거합니다.
     * @param bounds 제거할 경계
     * @return 제거되었으면 true, 아니면 false
     */
    public boolean removeBounds(Bounds bounds) {
        return this.boundsCollection.remove(bounds);
    }

    /**
     * 모든 경계를 제거합니다.
     */
    public void clearBounds() {
        this.boundsCollection.clear();
    }

    /**
     * 포함된 경계의 개수를 반환합니다.
     * @return 경계 개수
     */
    public int getBoundsCount() {
        return boundsCollection.size();
    }

    /**
     * 경계가 비어있는지 확인합니다.
     * @return 경계가 없으면 true, 있으면 false
     */
    public boolean isEmptyBounds() {
        return boundsCollection.isEmpty();
    }

    // Bounds 추상 메서드 구현 - 모든 경계를 포함하는 최소 경계 사각형 계산

    @Override
    public double getMinX() {
        if (boundsCollection.isEmpty()) {
            return 0.0;
        }

        double minX = Double.POSITIVE_INFINITY;
        for (Bounds bounds : boundsCollection) {
            minX = Math.min(minX, bounds.getMinX());
        }
        return minX;
    }

    @Override
    public double getMinY() {
        if (boundsCollection.isEmpty()) {
            return 0.0;
        }

        double minY = Double.POSITIVE_INFINITY;
        for (Bounds bounds : boundsCollection) {
            minY = Math.min(minY, bounds.getMinY());
        }
        return minY;
    }

    @Override
    public double getMaxX() {
        if (boundsCollection.isEmpty()) {
            return 0.0;
        }

        double maxX = Double.NEGATIVE_INFINITY;
        for (Bounds bounds : boundsCollection) {
            maxX = Math.max(maxX, bounds.getMaxX());
        }
        return maxX;
    }

    @Override
    public double getMaxY() {
        if (boundsCollection.isEmpty()) {
            return 0.0;
        }

        double maxY = Double.NEGATIVE_INFINITY;
        for (Bounds bounds : boundsCollection) {
            maxY = Math.max(maxY, bounds.getMaxY());
        }
        return maxY;
    }

    // 복합 경계 특화 메서드들

    /**
     * 점이 어떤 경계에든 포함되는지 확인합니다.
     * @param point 확인할 점
     * @return 어떤 경계에든 포함되면 true, 아니면 false
     */
    public boolean containsInAnyBounds(Point point) {
        for (Bounds bounds : boundsCollection) {
            if (bounds.contains(point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 점이 모든 경계에 포함되는지 확인합니다.
     * @param point 확인할 점
     * @return 모든 경계에 포함되면 true, 아니면 false
     */
    public boolean containsInAllBounds(Point point) {
        if (boundsCollection.isEmpty()) {
            return false;
        }

        for (Bounds bounds : boundsCollection) {
            if (!bounds.contains(point)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 다른 경계와 어떤 경계든 교차하는지 확인합니다.
     * @param other 확인할 다른 경계
     * @return 어떤 경계든 교차하면 true, 아니면 false
     */
    public boolean intersectsWithAnyBounds(Bounds other) {
        for (Bounds bounds : boundsCollection) {
            if (bounds.intersects(other)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 포함된 모든 경계의 총 면적을 계산합니다.
     * 주의: 겹치는 영역은 중복으로 계산됩니다.
     * @return 총 면적
     */
    public double getTotalArea() {
        double totalArea = 0.0;
        for (Bounds bounds : boundsCollection) {
            totalArea += bounds.getArea();
        }
        return totalArea;
    }

    /**
     * 포함된 경계들의 읽기 전용 리스트를 반환합니다.
     * @return 경계 리스트 (읽기 전용)
     */
    public List<Bounds> getBoundsList() {
        return Collections.unmodifiableList(boundsCollection);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CompositeBounds[");
        sb.append("count=").append(boundsCollection.size());
        if (!boundsCollection.isEmpty()) {
            sb.append(", bounds=[");
            for (int i = 0; i < boundsCollection.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(boundsCollection.get(i).toString());
            }
            sb.append("]");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof CompositeBounds)) return false;

        CompositeBounds other = (CompositeBounds) obj;
        return boundsCollection.equals(other.boundsCollection);
    }

    @Override
    public int hashCode() {
        return boundsCollection.hashCode();
    }
}
