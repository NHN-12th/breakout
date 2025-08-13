package nhn.breakoutt;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * 미로 형태의 세계 클래스
 * 2차원 배열을 사용하여 미로를 구성합니다.
 * 1 = 벽, 0 = 통로, 2 = 출구
 */
public class MazeWorld {
    private double width;
    private double height;
    private int[][] mazeLayout;
    private List<Box> walls;
    private List<Box> exits;
    private List<Object> gameObjects;
    private double cellWidth;
    private double cellHeight;

    /**
     * 미로 생성자
     */
    public MazeWorld(double width, double height, int[][] mazeLayout) {
        this.width = width;
        this.height = height;
        this.mazeLayout = mazeLayout;
        this.walls = new ArrayList<>();
        this.exits = new ArrayList<>();
        this.gameObjects = new ArrayList<>();

        // 셀 크기 계산
        this.cellWidth = width / mazeLayout[0].length;
        this.cellHeight = height / mazeLayout.length;

        // 미로 구조 생성
        generateMaze();
    }

    /**
     * 미로 구조를 생성합니다
     */
    private void generateMaze() {
        for (int row = 0; row < mazeLayout.length; row++) {
            for (int col = 0; col < mazeLayout[row].length; col++) {
                double x = col * cellWidth;
                double y = row * cellHeight;

                switch (mazeLayout[row][col]) {
                    case 1: // 벽
                        Box wall = new Box(x, y, cellWidth, cellHeight);
                        wall.setColor(Color.DARKGRAY);
                        wall.setCollisionAction(CollisionAction.BOUNCE);
                        walls.add(wall);
                        gameObjects.add(wall);
                        break;

                    case 2: // 출구
                        Box exit = new Box(x, y, cellWidth, cellHeight);
                        exit.setColor(Color.GOLD);
                        exit.setCollisionAction(CollisionAction.CUSTOM);
                        exits.add(exit);
                        gameObjects.add(exit);
                        break;

                    case 0: // 통로
                        // 아무것도 하지 않음 (빈 공간)
                        break;
                }
            }
        }
    }

    /**
     * 객체를 미로에 추가합니다
     */
    public void addObject(Object obj) {
        gameObjects.add(obj);
    }

    /**
     * 객체를 미로에서 제거합니다
     */
    public void removeObject(Object obj) {
        gameObjects.remove(obj);
    }

    /**
     * 특정 위치가 통로인지 확인합니다
     */
    public boolean isPassable(double x, double y) {
        int col = (int) (x / cellWidth);
        int row = (int) (y / cellHeight);

        if (row < 0 || row >= mazeLayout.length || col < 0 || col >= mazeLayout[0].length) {
            return false; // 경계 밖
        }

        return mazeLayout[row][col] == 0; // 0은 통로
    }

    /**
     * 특정 위치가 출구인지 확인합니다
     */
    public boolean isExit(double x, double y) {
        int col = (int) (x / cellWidth);
        int row = (int) (y / cellHeight);

        if (row < 0 || row >= mazeLayout.length || col < 0 || col >= mazeLayout[0].length) {
            return false;
        }

        return mazeLayout[row][col] == 2; // 2는 출구
    }

    /**
     * 미로를 업데이트합니다
     */
    public void update(double deltaTime) {
        List<Object> toRemove = new ArrayList<>();
        List<Object> toAdd = new ArrayList<>();

        for (Object obj : gameObjects) {
            // Movable 객체들 이동
            if (obj instanceof Movable) {
                Movable movable = (Movable) obj;
                movable.move(deltaTime);

                // 벽과의 충돌 검사
                if (obj instanceof Collidable) {
                    Collidable collidable = (Collidable) obj;
                    checkWallCollisions(collidable);
                    checkExitCollisions(collidable, toRemove, toAdd);
                }
            }

            // 제거 표시된 객체들 확인
            if (obj instanceof Ball) {
                Ball ball = (Ball) obj;
                if (ball.isDestroyed()) {
                    toRemove.add(ball);
                }
            }
        }

        // 객체 추가/제거
        gameObjects.removeAll(toRemove);
        gameObjects.addAll(toAdd);
    }

    /**
     * 벽과의 충돌을 검사합니다
     */
    private void checkWallCollisions(Collidable collidable) {
        for (Box wall : walls) {
            if (collidable.isColliding(wall)) {
                collidable.handleCollision(wall);
                wall.handleCollision(collidable);
            }
        }
    }

    /**
     * 출구와의 충돌을 검사합니다
     */
    private void checkExitCollisions(Collidable collidable, List<Object> toRemove, List<Object> toAdd) {
        for (Box exit : exits) {
            if (collidable.isColliding(exit)) {
                // 출구 도달 처리
                handleExitReached(collidable, exit, toRemove, toAdd);
            }
        }
    }

    /**
     * 출구 도달 처리
     */
    private void handleExitReached(Collidable collidable, Box exit, List<Object> toRemove, List<Object> toAdd) {
        if (collidable instanceof Ball) {
            Ball ball = (Ball) collidable;

            // 출구 도달 효과
            ball.setColor(Color.GOLD.brighter());

            // 성공 표시 (실제 게임에서는 레벨 클리어 등의 처리)
            System.out.println("출구 도달! Ball: " + ball);

            // 선택적으로 Ball을 제거하거나 다음 레벨로 이동
            // toRemove.add(ball);
        }
    }

    /**
     * 미로를 그립니다
     */
    public void render(GraphicsContext gc) {
        // 배경 그리기
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, width, height);

        // 통로 표시 (선택적)
        gc.setFill(Color.WHITE);
        for (int row = 0; row < mazeLayout.length; row++) {
            for (int col = 0; col < mazeLayout[row].length; col++) {
                if (mazeLayout[row][col] == 0) { // 통로
                    double x = col * cellWidth;
                    double y = row * cellHeight;
                    gc.fillRect(x, y, cellWidth, cellHeight);
                }
            }
        }

        // 모든 객체 그리기
        for (Object obj : gameObjects) {
            if (obj instanceof Paintable) {
                ((Paintable) obj).paint(gc);
            }
        }

        // 격자 그리기 (선택적)
        drawGrid(gc);
    }

    /**
     * 격자를 그립니다 (디버깅용)
     */
    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);

        // 세로선
        for (int col = 0; col <= mazeLayout[0].length; col++) {
            double x = col * cellWidth;
            gc.strokeLine(x, 0, x, height);
        }

        // 가로선
        for (int row = 0; row <= mazeLayout.length; row++) {
            double y = row * cellHeight;
            gc.strokeLine(0, y, width, y);
        }
    }

    /**
     * 랜덤한 통로 위치를 반환합니다
     */
    public Point getRandomPassablePosition() {
        List<Point> passablePositions = new ArrayList<>();

        for (int row = 0; row < mazeLayout.length; row++) {
            for (int col = 0; col < mazeLayout[row].length; col++) {
                if (mazeLayout[row][col] == 0) { // 통로
                    double x = col * cellWidth + cellWidth / 2;
                    double y = row * cellHeight + cellHeight / 2;
                    passablePositions.add(new Point(x, y));
                }
            }
        }

        if (passablePositions.isEmpty()) {
            return new Point(width / 2, height / 2); // 기본값
        }

        int randomIndex = (int) (Math.random() * passablePositions.size());
        return passablePositions.get(randomIndex);
    }

    /**
     * 플레이어를 안전한 시작 위치에 배치합니다
     */
    public void placePlayerAtStart(Ball player) {
        Point startPosition = getRandomPassablePosition();
        player.setX(startPosition.getX());
        player.setY(startPosition.getY());
        addObject(player);
    }

    // Getter 메서드들
    public List<Box> getWalls() {
        return new ArrayList<>(walls);
    }

    public List<Box> getExits() {
        return new ArrayList<>(exits);
    }

    public List<Object> getGameObjects() {
        return new ArrayList<>(gameObjects);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public double getCellHeight() {
        return cellHeight;
    }

    public int[][] getMazeLayout() {
        return mazeLayout.clone();
    }

    /**
     * 미로 정보를 출력합니다
     */
    public void printMazeInfo() {
        System.out.println("=== 미로 정보 ===");
        System.out.printf("크기: %.1f x %.1f\n", width, height);
        System.out.printf("셀 크기: %.1f x %.1f\n", cellWidth, cellHeight);
        System.out.printf("벽 개수: %d\n", walls.size());
        System.out.printf("출구 개수: %d\n", exits.size());
        System.out.printf("전체 객체 개수: %d\n", gameObjects.size());

        System.out.println("미로 레이아웃:");
        for (int[] row : mazeLayout) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        return String.format("MazeWorld[%dx%d, walls=%d, exits=%d, objects=%d]",
            mazeLayout[0].length, mazeLayout.length, walls.size(), exits.size(), gameObjects.size());
    }
}
