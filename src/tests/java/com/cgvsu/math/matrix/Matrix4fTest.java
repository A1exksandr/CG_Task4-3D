package com.cgvsu.math.matrix.floatMatrix;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Matrix4fTest {

    @Test
    void testMatrixMultiplication() {
        Matrix4f a = new Matrix4f(new float[]{
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        });

        Matrix4f b = new Matrix4f(new float[]{
                16, 15, 14, 13,
                12, 11, 10, 9,
                8, 7, 6, 5,
                4, 3, 2, 1
        });

        // mul() возвращает Matrix<Float>, приводим к Matrix4f
        com.cgvsu.math.matrix.Matrix<Float> resultMatrix = a.mul(b);

        // Проверяем как Matrix<Float>
        assertEquals(80.0f, resultMatrix.getElement(0, 0), 1e-6);
        assertEquals(70.0f, resultMatrix.getElement(0, 1), 1e-6);
        assertEquals(60.0f, resultMatrix.getElement(0, 2), 1e-6);
        assertEquals(50.0f, resultMatrix.getElement(0, 3), 1e-6);
    }

    @Test
    void testMatrixAddition() {
        Matrix4f a = new Matrix4f(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
        Matrix4f b = new Matrix4f(new float[]{16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1});

        a.add(b); // Метод изменяет текущую матрицу

        assertEquals(17.0f, a.getElement(0, 0), 1e-6);
        assertEquals(17.0f, a.getElement(0, 1), 1e-6);
        assertEquals(17.0f, a.getElement(3, 3), 1e-6);
    }

    @Test
    void testTranspose() {
        Matrix4f m = new Matrix4f(new float[]{
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        });

        com.cgvsu.math.matrix.Matrix<Float> transposed = m.transpose();

        assertEquals(1.0f, transposed.getElement(0, 0), 1e-6);
        assertEquals(5.0f, transposed.getElement(0, 1), 1e-6);
        assertEquals(9.0f, transposed.getElement(0, 2), 1e-6);
        assertEquals(13.0f, transposed.getElement(0, 3), 1e-6);
        assertEquals(2.0f, transposed.getElement(1, 0), 1e-6);
    }

    @Test
    void testConstructorWithFloatArray() {
        float[] data = {
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        };

        Matrix4f identity = new Matrix4f(data);

        assertEquals(1.0f, identity.getElement(0, 0), 1e-6);
        assertEquals(1.0f, identity.getElement(1, 1), 1e-6);
        assertEquals(1.0f, identity.getElement(2, 2), 1e-6);
        assertEquals(1.0f, identity.getElement(3, 3), 1e-6);
        assertEquals(0.0f, identity.getElement(0, 1), 1e-6);
    }

    @Test
    void testScalarMultiplication() {
        Matrix4f m = new Matrix4f(new float[]{
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16
        });

        com.cgvsu.math.matrix.Matrix<Float> result = m.mul(2.0f);

        assertEquals(2.0f, result.getElement(0, 0), 1e-6);
        assertEquals(4.0f, result.getElement(0, 1), 1e-6);
        assertEquals(10.0f, result.getElement(1, 0), 1e-6);
        assertEquals(32.0f, result.getElement(3, 3), 1e-6);
    }
}