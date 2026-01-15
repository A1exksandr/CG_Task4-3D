package com.cgvsu.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Vector3fTest {
    private static final float EPS = 1e-6f;

    @Test
    void testConstructorAndGetters() {
        Vector3f v = new Vector3f(1.5f, 2.5f, 3.5f);

        assertEquals(1.5f, v.getX(), EPS);
        assertEquals(2.5f, v.getY(), EPS);
        assertEquals(3.5f, v.getZ(), EPS);
    }

    @Test
    void testSetters() {
        Vector3f v = new Vector3f(0, 0, 0);

        v.setX(10.0f);
        v.setY(20.0f);
        v.setZ(30.0f);

        assertEquals(10.0f, v.getX(), EPS);
        assertEquals(20.0f, v.getY(), EPS);
        assertEquals(30.0f, v.getZ(), EPS);
    }

    @Test
    void testEqualsMethod() {
        Vector3f v1 = new Vector3f(1.0f, 2.0f, 3.0f);
        Vector3f v2 = new Vector3f(1.0f, 2.0f, 3.0f);
        Vector3f v3 = new Vector3f(1.0000001f, 2.0000001f, 3.0000001f);
        Vector3f v4 = new Vector3f(1.1f, 2.0f, 3.0f);

        // Используем ваш метод equals(Vector3f), а не Object.equals()
        assertTrue(v1.equals(v2));
        assertTrue(v1.equals(v3)); // В пределах 1e-7

        // Неравенство
        assertFalse(v1.equals(v4));
        assertFalse(v1.equals(null));
    }

    @Test
    void testFields() {
        Vector3f v = new Vector3f(5.0f, 6.0f, 7.0f);

        // Проверка прямого доступа к полям (если они public или через рефлексию)
        // Или проверка через геттеры
        assertEquals(5.0f, v.getX(), EPS);
        assertEquals(6.0f, v.getY(), EPS);
        assertEquals(7.0f, v.getZ(), EPS);
    }
}