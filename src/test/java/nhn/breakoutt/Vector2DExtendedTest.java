package nhn.breakoutt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class Vector2DExtendedTest {

    private Vector2D vector1;
    private Vector2D vector2;

    @BeforeEach
    public void setUp() {
        vector1 = new Vector2D(3.0, 4.0);
        vector2 = new Vector2D(1.0, 2.0);
    }

    @Test
    public void testVectorCreation() {
        assertEquals(3.0, vector1.getX(), 0.001, "Vector X 성분이 잘못되었습니다");
        assertEquals(4.0, vector1.getY(), 0.001, "Vector Y 성분이 잘못되었습니다");
    }

    @Test
    public void testPolarConstruction() {
        // 극좌표로 벡터 생성
        Vector2D polar = Vector2D.fromPolar(5.0, Math.PI / 2); // 크기 5, 각도 90도

        assertEquals(0.0, polar.getX(), 0.001, "극좌표 X 성분이 잘못되었습니다");
        assertEquals(5.0, polar.getY(), 0.001, "극좌표 Y 성분이 잘못되었습니다");
    }

    @Test
    public void testStaticFactoryMethods() {
        Vector2D zero = Vector2D.zero();
        assertEquals(0.0, zero.getX(), 0.001, "영벡터 X 성분이 0이 아닙니다");
        assertEquals(0.0, zero.getY(), 0.001, "영벡터 Y 성분이 0이 아닙니다");

        Vector2D unitX = Vector2D.unitX();
        assertEquals(1.0, unitX.getX(), 0.001, "단위 X 벡터의 X 성분이 1이 아닙니다");
        assertEquals(0.0, unitX.getY(), 0.001, "단위 X 벡터의 Y 성분이 0이 아닙니다");

        Vector2D unitY = Vector2D.unitY();
        assertEquals(0.0, unitY.getX(), 0.001, "단위 Y 벡터의 X 성분이 0이 아닙니다");
        assertEquals(1.0, unitY.getY(), 0.001, "단위 Y 벡터의 Y 성분이 1이 아닙니다");
    }

    @Test
    public void testVectorArithmetic() {
        Vector2D sum = vector1.add(vector2);
        assertEquals(4.0, sum.getX(), 0.001, "벡터 덧셈 X 성분이 잘못되었습니다");
        assertEquals(6.0, sum.getY(), 0.001, "벡터 덧셈 Y 성분이 잘못되었습니다");

        Vector2D diff = vector1.subtract(vector2);
        assertEquals(2.0, diff.getX(), 0.001, "벡터 뺄셈 X 성분이 잘못되었습니다");
        assertEquals(2.0, diff.getY(), 0.001, "벡터 뺄셈 Y 성분이 잘못되었습니다");

        Vector2D scaled = vector1.multiply(2.0);
        assertEquals(6.0, scaled.getX(), 0.001, "벡터 곱셈 X 성분이 잘못되었습니다");
        assertEquals(8.0, scaled.getY(), 0.001, "벡터 곱셈 Y 성분이 잘못되었습니다");

        Vector2D divided = vector1.divide(2.0);
        assertEquals(1.5, divided.getX(), 0.001, "벡터 나눗셈 X 성분이 잘못되었습니다");
        assertEquals(2.0, divided.getY(), 0.001, "벡터 나눗셈 Y 성분이 잘못되었습니다");
    }

    @Test
    public void testVectorMagnitudeAndDirection() {
        assertEquals(5.0, vector1.magnitude(), 0.001, "벡터 크기가 잘못되었습니다");

        double expectedAngle = Math.atan2(4.0, 3.0);
        assertEquals(expectedAngle, vector1.angle(), 0.001, "벡터 각도가 잘못되었습니다");

        Vector2D normalized = vector1.normalize();
        assertEquals(1.0, normalized.magnitude(), 0.001, "정규화된 벡터의 크기가 1이 아닙니다");
        assertEquals(0.6, normalized.getX(), 0.001, "정규화된 벡터 X 성분이 잘못되었습니다");
        assertEquals(0.8, normalized.getY(), 0.001, "정규화된 벡터 Y 성분이 잘못되었습니다");
    }

    @Test
    public void testDotAndCrossProduct() {
        double dotProduct = vector1.dot(vector2);
        assertEquals(11.0, dotProduct, 0.001, "내적 계산이 잘못되었습니다"); // 3*1 + 4*2 = 11

        double crossProduct = vector1.cross(vector2);
        assertEquals(2.0, crossProduct, 0.001, "외적 계산이 잘못되었습니다"); // 3*2 - 4*1 = 2
    }

    @Test
    public void testDistanceAndProjection() {
        double distance = vector1.distance(vector2);
        Vector2D diff = vector1.subtract(vector2);
        assertEquals(diff.magnitude(), distance, 0.001, "거리 계산이 잘못되었습니다");

        Vector2D projection = vector1.project(vector2);
        // v1 투영 v2 = (v1·v2/|v2|²) × v2
        double scalar = vector1.dot(vector2) / vector2.dot(vector2);
        Vector2D expected = vector2.multiply(scalar);
        assertEquals(expected.getX(), projection.getX(), 0.001, "투영 X 성분이 잘못되었습니다");
        assertEquals(expected.getY(), projection.getY(), 0.001, "투영 Y 성분이 잘못되었습니다");
    }

    @Test
    public void testVectorRotation() {
        Vector2D rotated90 = vector1.rotate(Math.PI / 2); // 90도 회전
        assertEquals(-4.0, rotated90.getX(), 0.001, "90도 회전 X 성분이 잘못되었습니다");
        assertEquals(3.0, rotated90.getY(), 0.001, "90도 회전 Y 성분이 잘못되었습니다");

        Vector2D rotated180 = vector1.rotate(Math.PI); // 180도 회전
        assertEquals(-3.0, rotated180.getX(), 0.001, "180도 회전 X 성분이 잘못되었습니다");
        assertEquals(-4.0, rotated180.getY(), 0.001, "180도 회전 Y 성분이 잘못되었습니다");
    }

    @Test
    public void testImmutability() {
        Vector2D original = new Vector2D(5.0, 6.0);
        Vector2D result = original.add(vector1);

        // 원본 벡터는 변경되지 않아야 함
        assertEquals(5.0, original.getX(), 0.001, "원본 벡터가 변경되었습니다");
        assertEquals(6.0, original.getY(), 0.001, "원본 벡터가 변경되었습니다");

        // 새로운 벡터가 반환되어야 함
        assertEquals(8.0, result.getX(), 0.001, "새 벡터 X 성분이 잘못되었습니다");
        assertEquals(10.0, result.getY(), 0.001, "새 벡터 Y 성분이 잘못되었습니다");
    }

    @Test
    public void testEqualsAndHashCode() {
        Vector2D vector3 = new Vector2D(3.0, 4.0);
        Vector2D vector4 = new Vector2D(3.1, 4.0);

        assertEquals(vector1, vector3, "동일한 성분의 벡터들이 같다고 판단되지 않았습니다");
        assertNotEquals(vector1, vector4, "다른 성분의 벡터들이 같다고 판단되었습니다");

        assertEquals(vector1.hashCode(), vector3.hashCode(),
                "동일한 벡터들의 해시코드가 다릅니다");
    }

    @Test
    public void testToString() {
        String vectorString = vector1.toString();
        assertTrue(vectorString.contains("3.0"), "toString에 X 성분이 포함되지 않았습니다");
        assertTrue(vectorString.contains("4.0"), "toString에 Y 성분이 포함되지 않았습니다");
    }
}