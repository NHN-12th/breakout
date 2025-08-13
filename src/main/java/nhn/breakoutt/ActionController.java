package nhn.breakoutt;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 런타임에 충돌 액션을 변경하는 시스템
 * 마우스 클릭으로 객체 선택, 키보드로 액션 변경, 자동 액션 순환 등을 관리합니다.
 */
public class ActionController {
    private Collidable selectedObject;              // 현재 선택된 객체
    private double actionChangeTimer;               // 자동 변경 타이머
    private Map<Collidable, Integer> collisionCounts; // 각 객체의 충돌 횟수 기록
    private SimpleWorld world;                      // 관리할 World
    private double autoChangeInterval = 10.0;       // 자동 변경 간격 (초)
    private CollisionAction[] actionCycle = {       // 액션 순환 배열
        CollisionAction.BOUNCE,
        CollisionAction.STOP,
        CollisionAction.PASS
    };

    public ActionController(SimpleWorld world) {
        if (world == null) {
            throw new IllegalArgumentException("World는 null일 수 없습니다");
        }
        this.world = world;
        this.selectedObject = null;
        this.actionChangeTimer = 0;
        this.collisionCounts = new HashMap<>();
    }

    /**
     * 시간 업데이트 (자동 액션 변경 처리)
     */
    public void update(double deltaTime) {
        actionChangeTimer += deltaTime;

        // 10초마다 모든 객체의 액션 순환
        if (actionChangeTimer >= autoChangeInterval) {
            actionChangeTimer = 0;
            cycleAllObjectActions();
        }

        // 충돌 횟수 기반 진화 처리
        processCollisionBasedEvolution();
    }

    /**
     * 마우스 클릭 이벤트 처리 (객체 선택)
     */
    public void handleMouseClick(MouseEvent event) {
        double clickX = event.getX();
        double clickY = event.getY();

        // 클릭 위치에서 가장 가까운 Collidable 객체 찾기
        Collidable nearestObject = findNearestCollidableObject(clickX, clickY);

        if (nearestObject != null) {
            selectedObject = nearestObject;
            System.out.println("객체 선택됨: " + nearestObject.getClass().getSimpleName() +
                             " (현재 액션: " + nearestObject.getCollisionAction() + ")");
        } else {
            selectedObject = null;
            System.out.println("선택 해제됨");
        }
    }

    /**
     * 키보드 이벤트 처리 (액션 변경)
     */
    public void handleKeyPress(KeyEvent event) {
        if (selectedObject == null) {
            return; // 선택된 객체가 없으면 무시
        }

        CollisionAction newAction = null;
        KeyCode keyCode = event.getCode();

        // 키 매핑
        switch (keyCode) {
            case B:
                newAction = CollisionAction.BOUNCE;
                break;
            case D:
                newAction = CollisionAction.DESTROY;
                break;
            case S:
                newAction = CollisionAction.STOP;
                break;
            case P:
                newAction = CollisionAction.PASS;
                break;
            case C:
                newAction = CollisionAction.CUSTOM;
                break;
            default:
                return; // 매핑되지 않은 키는 무시
        }

        // 액션 변경
        if (newAction != null) {
            selectedObject.setCollisionAction(newAction);
            System.out.println("액션 변경됨: " + selectedObject.getClass().getSimpleName() +
                             " -> " + newAction);
        }
    }

    /**
     * 클릭 위치에서 가장 가까운 Collidable 객체를 찾습니다.
     */
    private Collidable findNearestCollidableObject(double x, double y) {
        List<Object> objects = world.getObjects();
        Collidable nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Object obj : objects) {
            if (obj instanceof Collidable) {
                Collidable collidable = (Collidable) obj;
                double distance = calculateDistance(x, y, collidable);

                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = collidable;
                }
            }
        }

        return nearest;
    }

    /**
     * 점과 객체 사이의 거리를 계산합니다.
     */
    private double calculateDistance(double x, double y, Collidable obj) {
        Bounds bounds = obj.getBounds();
        double centerX = bounds.getCenterX();
        double centerY = bounds.getCenterY();

        double dx = x - centerX;
        double dy = y - centerY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 모든 객체의 액션을 순환시킵니다.
     */
    private void cycleAllObjectActions() {
        List<Object> objects = world.getObjects();

        for (Object obj : objects) {
            if (obj instanceof Collidable) {
                Collidable collidable = (Collidable) obj;
                CollisionAction currentAction = collidable.getCollisionAction();

                // 현재 액션의 다음 액션 찾기
                CollisionAction nextAction = getNextAction(currentAction);
                collidable.setCollisionAction(nextAction);
            }
        }

        System.out.println("모든 객체 액션 순환 완료");
    }

    /**
     * 액션 순환에서 다음 액션을 반환합니다.
     */
    private CollisionAction getNextAction(CollisionAction current) {
        for (int i = 0; i < actionCycle.length; i++) {
            if (actionCycle[i] == current) {
                return actionCycle[(i + 1) % actionCycle.length];
            }
        }
        // 순환 배열에 없는 액션이면 첫 번째 액션 반환
        return actionCycle[0];
    }

    /**
     * 충돌 이벤트를 기록합니다.
     */
    public void recordCollision(Collidable obj) {
        int count = collisionCounts.getOrDefault(obj, 0) + 1;
        collisionCounts.put(obj, count);
    }

    /**
     * 충돌 횟수 기반 진화를 처리합니다.
     */
    private void processCollisionBasedEvolution() {
        for (Map.Entry<Collidable, Integer> entry : collisionCounts.entrySet()) {
            Collidable obj = entry.getKey();
            int count = entry.getValue();

            if (count == 5 && obj.getCollisionAction() == CollisionAction.BOUNCE) {
                // 5회 충돌: BOUNCE → STOP
                obj.setCollisionAction(CollisionAction.STOP);
                System.out.println("진화: " + obj.getClass().getSimpleName() + " BOUNCE → STOP (5회 충돌)");
            } else if (count == 10 && obj.getCollisionAction() == CollisionAction.STOP) {
                // 10회 충돌: STOP → DESTROY
                obj.setCollisionAction(CollisionAction.DESTROY);
                System.out.println("진화: " + obj.getClass().getSimpleName() + " STOP → DESTROY (10회 충돌)");
            } else if (count == 15) {
                // 15회 충돌: 새로운 기능 해금
                unlockSpecialFeature(obj);
            }
        }
    }

    /**
     * 15회 충돌 시 특수 기능을 해금합니다.
     */
    private void unlockSpecialFeature(Collidable obj) {
        obj.setCollisionAction(CollisionAction.CUSTOM);

        // 객체 타입에 따른 특수 효과
        if (obj instanceof Ball) {
            Ball ball = (Ball) obj;
            ball.setColor(Color.GOLD); // 황금색으로 변경
        } else if (obj instanceof Triangle) {
            Triangle triangle = (Triangle) obj;
            triangle.setColor(Color.SILVER); // 은색으로 변경
        } else if (obj instanceof Star) {
            Star star = (Star) obj;
            star.setColor(Color.CYAN); // 사이안색으로 변경
        }

        System.out.println("특수 기능 해금: " + obj.getClass().getSimpleName() + " (15회 충돌)");
    }

    /**
     * 선택된 객체를 하이라이트로 그립니다.
     */
    public void renderSelection(GraphicsContext gc) {
        if (selectedObject != null) {
            Bounds bounds = selectedObject.getBounds();

            // 선택된 객체 주위에 하이라이트 테두리 그리기
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(3);
            gc.strokeRect(bounds.getMinX() - 5, bounds.getMinY() - 5,
                         bounds.getWidth() + 10, bounds.getHeight() + 10);

            // 선택 정보 텍스트 표시
            gc.setFill(Color.WHITE);
            gc.fillText("선택됨: " + selectedObject.getClass().getSimpleName() +
                       " (액션: " + selectedObject.getCollisionAction() + ")",
                       10, 30);
        }

        // 조작 안내 텍스트
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("조작: 클릭으로 선택, B(Bounce) D(Destroy) S(Stop) P(Pass) C(Custom)", 10, world.getHeight() - 20);
    }

    // Getter/Setter 메서드들
    public Collidable getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(Collidable selectedObject) {
        this.selectedObject = selectedObject;
    }

    public double getAutoChangeInterval() {
        return autoChangeInterval;
    }

    public void setAutoChangeInterval(double autoChangeInterval) {
        this.autoChangeInterval = Math.max(1.0, autoChangeInterval); // 최소 1초
    }

    /**
     * 특정 객체의 충돌 횟수를 반환합니다.
     */
    public int getCollisionCount(Collidable obj) {
        return collisionCounts.getOrDefault(obj, 0);
    }

    /**
     * 충돌 횟수를 초기화합니다.
     */
    public void resetCollisionCounts() {
        collisionCounts.clear();
        actionChangeTimer = 0;
    }

    /**
     * 액션 순환 배열을 변경합니다.
     */
    public void setActionCycle(CollisionAction... actions) {
        if (actions.length > 0) {
            this.actionCycle = actions.clone();
        }
    }

    @Override
    public String toString() {
        return String.format("ActionController[selectedObject=%s, timer=%.1f, trackedObjects=%d]",
            selectedObject != null ? selectedObject.getClass().getSimpleName() : "none",
            actionChangeTimer, collisionCounts.size());
    }
}
