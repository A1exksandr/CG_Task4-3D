package com.cgvsu.math.matrix;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class MatrixTests {
    private static final double EPS = 1e-9;
    //private static final float FLOAT_PRECISION = 0.001f;

    // ============ Vec2 Tests ============

    @Test
    public void testVec2Add() {
        Vec2 a = new Vec2(1, 2);
        Vec2 b = new Vec2(3, 4);
        Vec2 result = a.add(b);
        assertEquals(4, result.x, EPS);
        assertEquals(6, result.y, EPS);
    }

    @Test
    void testVec2Sub() {
        Vec2 a = new Vec2(5, 7);
        Vec2 b = new Vec2(2, 3);
        Vec2 result = a.sub(b);
        assertEquals(3, result.x, EPS);
        assertEquals(4, result.y, EPS);
    }

    @Test
    void testVec2MulScalar() {
        Vec2 v = new Vec2(2, -3);
        Vec2 result = v.mul(2.5);
        assertEquals(5.0, result.x, EPS);
        assertEquals(-7.5, result.y, EPS);
    }

    @Test
    void testVec2DivScalar() {
        Vec2 v = new Vec2(6, -9);
        Vec2 result = v.div(3);
        assertEquals(2.0, result.x, EPS);
        assertEquals(-3.0, result.y, EPS);
    }
}