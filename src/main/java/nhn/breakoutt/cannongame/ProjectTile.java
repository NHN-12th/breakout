package nhn.breakoutt.cannongame;

import nhn.breakoutt.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class ProjectTile extends PhysicsBall {
    private List<Point> trajectory; // 궤적 저장
    private ProjectTileType type; // 포탄 유형
    private boolean hasExploded; // 폭발 여부
    private static final int MAX_TRAJECTORY_SIZE = 50;

    public ProjectTile(Point center, ProjectTileType type) {
        super(center, 8); // 기본 반지름 8
        this.type = type;
        this.trajectory = new ArrayList<>();
        this.hasExploded = false;
        setColor(Color.BLACK);
        setMass(1.0);
    }

    @Override
    public void paint(GraphicsContext gc) {
        // 궤적 그리기 (반투명)
        gc.setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
        gc.setLineWidth(1);

        for (int i = 1; i < trajectory.size(); i++) {
            Point prev = trajectory.get(i - 1);
            Point curr = trajectory.get(i);
            gc.strokeLine(prev.getX(), prev.getY(), curr.getX(), curr.getY());
        }

        // 포탄 본체 그리기
        if (!hasExploded) {
            super.paint(gc);
        }
    }

    public void explode() {
        hasExploded = true;
        setVelocity(new Vector2D(0, 0));
    }

    public boolean isOutOfBounds(double width, double height) {
        return getY() > height + 100 || getX() < -100 || getX() > width + 100;
    }
}
