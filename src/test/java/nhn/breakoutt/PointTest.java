package nhn.breakoutt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointTest {
    private Point point;

    @BeforeEach
    public void setUp(){
        point = new Point(1.0, 2.0);
    }

    @Test
    public void testConstructor(){
        assertEquals(1, point.getX());
        assertEquals(2, point.getY());
    }

    @Test
    public void testDistanceTo() {
        assertEquals(5, point.distanceTo(new Point(4.0,6.0)));
    }

    @Test
    public void testInvalidInput(){
        assertThrows(IllegalArgumentException.class,
                () -> new Point(null, 0.0));

        assertThrows(IllegalArgumentException.class,
                () -> new Point(0.0, null));

        assertThrows(IllegalArgumentException.class,
                () -> new Point(null, null));
    }
}