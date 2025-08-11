package com.nhnacademy.cannongame;

import nhn.breakoutt.Ball;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class BallTest {
    private Ball ball;

    @BeforeEach
    public void setUp() {
        ball = new Ball(100, 100, 20);
    }

    @Test
    public void testConstructor() {
        assertEquals(100, ball.getX());
        assertEquals(100, ball.getY());
        assertEquals(20, ball.getRadius());
    }

    @Test
    public void testInvalidRadius() {
        assertThrows(IllegalArgumentException.class,
                () -> new Ball(0, 0, -5));
    }
}