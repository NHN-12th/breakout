package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 인터페이스를 활용한 단순화된 World 클래스
 * 다양한 타입의 게임 객체들을 관리하고 업데이트합니다.
 */
public class SimpleWorld {
    private final double width;
    private final double height;
    private final List<Object> gameObjects;
    private final List<Box> boundaries;

    public SimpleWorld(double width, double height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("세계 크기는 양수여야 합니다");
        }

        this.width = width;
        this.height = height;
        this.gameObjects = new ArrayList<>();
        this.boundaries = new ArrayList<>();

        createBoundaries();
    }

    /**
     * 4개의 경계 Box를 생성합니다.
     * 화면 밖에 위치하고 BOUNCE 액션으로 설정됩니다.
     */
    private void createBoundaries() {
        double thickness = 50; // 경계 두께

        // 왼쪽 경계
        Box leftBoundary = new Box(new Point(-thickness, 0.0), thickness, height);
        leftBoundary.setCollisionAction(CollisionAction.BOUNCE);
        boundaries.add(leftBoundary);

        // 오른쪽 경계
        Box rightBoundary = new Box(new Point(width, 0.0), thickness, height);
        rightBoundary.setCollisionAction(CollisionAction.BOUNCE);
        boundaries.add(rightBoundary);

        // 위쪽 경계
        Box topBoundary = new Box(new Point(0.0, -thickness), width, thickness);
        topBoundary.setCollisionAction(CollisionAction.BOUNCE);
        boundaries.add(topBoundary);

        // 아래쪽 경계
        Box bottomBoundary = new Box(new Point(0.0, height), width, thickness);
        bottomBoundary.setCollisionAction(CollisionAction.BOUNCE);
        boundaries.add(bottomBoundary);
    }

    /**
     * 게임 객체를 추가합니다.
     * @param object 추가할 객체
     */
    public void addObject(Object object) {
        if (object != null) {
            gameObjects.add(object);
        }
    }

    /**
     * 게임 객체를 제거합니다.
     * @param object 제거할 객체
     */
    public void removeObject(Object object) {
        gameObjects.remove(object);
    }

    /**
     * 모든 게임 객체를 제거합니다.
     */
    public void clearObjects() {
        gameObjects.clear();
    }

    /**
     * 게임 객체 수를 반환합니다.
     * @return 객체 수
     */
    public int getObjectCount() {
        return gameObjects.size();
    }

    /**
     * 게임 객체 리스트를 반환합니다.
     * @return 게임 객체 리스트의 복사본
     */
    public List<Object> getObjects() {
        return new ArrayList<>(gameObjects);
    }

    /**
     * 세계를 업데이트합니다.
     * @param deltaTime 델타 타임 (초)
     */
    public void update(double deltaTime) {
        // 1. Movable 객체들 이동
        moveObjects(deltaTime);

        // 2. 파괴된 객체들 제거
        removeDestroyedObjects();

        // 3. 경계와의 충돌 처리
        handleBoundaryCollisions();

        // 4. 객체 간 충돌 처리
        handleObjectCollisions();
    }

    /**
     * Movable 객체들을 이동시킵니다.
     */
    private void moveObjects(double deltaTime) {
        for (Object obj : gameObjects) {
            if (obj instanceof Movable) {
                ((Movable) obj).move(deltaTime);
            }
        }
    }

    /**
     * 파괴된 객체들을 제거합니다.
     */
    private void removeDestroyedObjects() {
        Iterator<Object> iterator = gameObjects.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            // Ball 클래스의 isDestroyed 메서드 확인
            if (obj instanceof Ball && ((Ball) obj).isDestroyed()) {
                iterator.remove();
            }
        }
    }

    /**
     * 경계와의 충돌을 처리합니다.
     */
    private void handleBoundaryCollisions() {
        for (Object obj : gameObjects) {
            if (obj instanceof Collidable) {
                Collidable collidableObj = (Collidable) obj;

                for (Box boundary : boundaries) {
                    if (collidableObj.isColliding(boundary)) {
                        handleBoundaryCollision(collidableObj, boundary);
                    }
                }
            }
        }
    }

    /**
     * 특정 객체와 경계 간의 충돌을 처리합니다.
     */
    private void handleBoundaryCollision(Collidable obj, Box boundary) {
        // 경계와의 충돌 시 속도 반전 처리
        if (obj instanceof Movable) {
            Movable movableObj = (Movable) obj;
            Bounds objBounds = obj.getBounds();
            Bounds boundaryBounds = boundary.getBounds();

            // 충돌한 면에 따라 속도 반전
            if (objBounds.getCenterX() < boundaryBounds.getMinX() ||
                objBounds.getCenterX() > boundaryBounds.getMaxX()) {
                // 좌우 충돌: X 속도 반전
                movableObj.setDx(-movableObj.getDx());
            }

            if (objBounds.getCenterY() < boundaryBounds.getMinY() ||
                objBounds.getCenterY() > boundaryBounds.getMaxY()) {
                // 상하 충돌: Y 속도 반전
                movableObj.setDy(-movableObj.getDy());
            }
        }

        // 객체의 충돌 처리 호출
        obj.handleCollision(boundary);
    }

    /**
     * 객체 간 충돌을 처리합니다.
     */
    private void handleObjectCollisions() {
        List<Collidable> collidables = new ArrayList<>();

        // Collidable 객체들만 추출
        for (Object obj : gameObjects) {
            if (obj instanceof Collidable) {
                collidables.add((Collidable) obj);
            }
        }

        // 이중 루프로 충돌 검사 (각 쌍을 한 번만 검사)
        for (int i = 0; i < collidables.size(); i++) {
            for (int j = i + 1; j < collidables.size(); j++) {
                Collidable obj1 = collidables.get(i);
                Collidable obj2 = collidables.get(j);

                if (obj1.isColliding(obj2)) {
                    handleObjectCollision(obj1, obj2);
                }
            }
        }
    }

    /**
     * 두 객체 간의 충돌을 처리합니다.
     */
    private void handleObjectCollision(Collidable obj1, Collidable obj2) {
        // Ball 간의 충돌은 물리적 처리만 수행 (개별 handleCollision 호출하지 않음)
        if (obj1 instanceof Ball && obj2 instanceof Ball) {
            handleElasticCollision((Movable) obj1, (Movable) obj2);
        } else {
            // 다른 타입의 객체들은 각자의 충돌 처리 호출
            obj1.handleCollision(obj2);
            obj2.handleCollision(obj1);

            // 둘 다 Movable인 경우 추가 물리 처리
            if (obj1 instanceof Movable && obj2 instanceof Movable) {
                handleElasticCollision((Movable) obj1, (Movable) obj2);
            }
        }
    }

    /**
     * 탄성 충돌을 처리합니다 (간단한 속도 교환).
     */
    private void handleElasticCollision(Movable obj1, Movable obj2) {
        // 간단한 속도 교환 (실제로는 질량과 각도를 고려해야 함)
        double tempDx = obj1.getDx();
        double tempDy = obj1.getDy();

        obj1.setDx(obj2.getDx());
        obj1.setDy(obj2.getDy());
        obj2.setDx(tempDx);
        obj2.setDy(tempDy);
    }

    /**
     * 세계를 렌더링합니다.
     * @param gc GraphicsContext
     */
    public void render(GraphicsContext gc) {
        // 배경 그리기
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        // 경계 그리기 (선택사항 - 디버깅용)
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, width, height);

        // Paintable 객체들만 그리기
        for (Object obj : gameObjects) {
            if (obj instanceof Paintable) {
                ((Paintable) obj).paint(gc);
            }
        }
    }

    // Getter 메서드들
    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public List<Box> getBoundaries() {
        return new ArrayList<>(boundaries);
    }

    /**
     * 특정 타입의 객체들을 반환합니다.
     * @param clazz 찾을 클래스 타입
     * @param <T> 반환할 타입
     * @return 해당 타입의 객체 리스트
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getObjectsOfType(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for (Object obj : gameObjects) {
            if (clazz.isInstance(obj)) {
                result.add((T) obj);
            }
        }
        return result;
    }

    /**
     * 디버깅을 위한 세계 상태 정보를 반환합니다.
     */
    @Override
    public String toString() {
        int paintableCount = 0;
        int movableCount = 0;
        int collidableCount = 0;

        for (Object obj : gameObjects) {
            if (obj instanceof Paintable) paintableCount++;
            if (obj instanceof Movable) movableCount++;
            if (obj instanceof Collidable) collidableCount++;
        }

        return String.format("SimpleWorld[%dx%d] Objects: %d (Paintable: %d, Movable: %d, Collidable: %d)",
                           (int)width, (int)height, gameObjects.size(),
                           paintableCount, movableCount, collidableCount);
    }
}
