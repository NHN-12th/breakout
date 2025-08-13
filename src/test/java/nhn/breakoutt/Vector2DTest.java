package nhn.breakoutt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector2DTest {
        @Test
        public void testVectorCreation() {
            Vector2D vector = new Vector2D(3.0, 4.0);
            assertEquals(3.0, vector.getX(), 0.001, "X 성분이 올바르게 설정되지 않았습니다");
            assertEquals(4.0, vector.getY(), 0.001, "Y 성분이 올바르게 설정되지 않았습니다");
        }

        @Test
        public void testDefaultConstructor() {
            Vector2D vector = new Vector2D();
            assertEquals(0.0, vector.getX(), 0.001, "기본 X 성분은 0이어야 합니다");
            assertEquals(0.0, vector.getY(), 0.001, "기본 Y 성분은 0이어야 합니다");
        }

        @Test
        public void testVectorAddition() {
            Vector2D v1 = new Vector2D(2.0, 3.0);
            Vector2D v2 = new Vector2D(1.0, 4.0);

            Vector2D result = v1.add(v2);

            assertEquals(3.0, result.getX(), 0.001, "벡터 덧셈 X 성분이 잘못되었습니다");
            assertEquals(7.0, result.getY(), 0.001, "벡터 덧셈 Y 성분이 잘못되었습니다");

            // 원본 벡터는 변경되지 않아야 함
            assertEquals(2.0, v1.getX(), 0.001, "원본 벡터가 변경되었습니다");
            assertEquals(3.0, v1.getY(), 0.001, "원본 벡터가 변경되었습니다");
        }

        @Test
        public void testVectorSubtraction() {
            Vector2D v1 = new Vector2D(5.0, 8.0);
            Vector2D v2 = new Vector2D(2.0, 3.0);

            Vector2D result = v1.subtract(v2);

            assertEquals(3.0, result.getX(), 0.001, "벡터 뺄셈 X 성분이 잘못되었습니다");
            assertEquals(5.0, result.getY(), 0.001, "벡터 뺄셈 Y 성분이 잘못되었습니다");
        }

        @Test
        public void testVectorMultiplication() {
            Vector2D vector = new Vector2D(3.0, 4.0);
            Vector2D result = vector.multiply(2.0);

            assertEquals(6.0, result.getX(), 0.001, "벡터 곱셈 X 성분이 잘못되었습니다");
            assertEquals(8.0, result.getY(), 0.001, "벡터 곱셈 Y 성분이 잘못되었습니다");
        }

        @Test
        public void testVectorMagnitude() {
            Vector2D vector = new Vector2D(3.0, 4.0);
            double magnitude = vector.magnitude();

            assertEquals(5.0, magnitude, 0.001, "벡터 크기 계산이 잘못되었습니다");

            // 영벡터 테스트
            Vector2D zeroVector = new Vector2D(0.0, 0.0);
            assertEquals(0.0, zeroVector.magnitude(), 0.001, "영벡터의 크기는 0이어야 합니다");
        }

        @Test
        public void testVectorNormalize() {
            Vector2D vector = new Vector2D(3.0, 4.0);
            Vector2D normalized = vector.normalize();

            assertEquals(1.0, normalized.magnitude(), 0.001, "정규화된 벡터의 크기는 1이어야 합니다");
            assertEquals(0.6, normalized.getX(), 0.001, "정규화된 벡터 X 성분이 잘못되었습니다");
            assertEquals(0.8, normalized.getY(), 0.001, "정규화된 벡터 Y 성분이 잘못되었습니다");
        }

        @Test
        public void testZeroVectorNormalize() {
            Vector2D zeroVector = new Vector2D(0.0, 0.0);
            Vector2D normalized = zeroVector.normalize();

            // 영벡터의 정규화는 영벡터를 반환해야 함
            assertEquals(0.0, normalized.getX(), 0.001, "영벡터 정규화 X 성분이 잘못되었습니다");
            assertEquals(0.0, normalized.getY(), 0.001, "영벡터 정규화 Y 성분이 잘못되었습니다");
        }

        @Test
        public void testDotProduct() {
            Vector2D v1 = new Vector2D(3.0, 4.0);
            Vector2D v2 = new Vector2D(2.0, 1.0);

            double dotProduct = v1.dot(v2);

            assertEquals(10.0, dotProduct, 0.001, "내적 계산이 잘못되었습니다");
        }
}