package com.cgvsu.math.matrix.floatMatrix;

import com.cgvsu.math.matrix.Matrix;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Matrix3fTest {

    @Test
    void testMatrix3fCreation() {
        // Тест создания из массива float
        float[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Matrix3f matrix = new Matrix3f(data);

        assertEquals(3, matrix.getRows());
        assertEquals(3, matrix.getCols());
        assertEquals(1.0f, matrix.getElement(0, 0), 1e-6);
        assertEquals(5.0f, matrix.getElement(1, 1), 1e-6);
        assertEquals(9.0f, matrix.getElement(2, 2), 1e-6);
    }


    @Test
    void testMatrix3fDefaultConstructor() {
        // Тест конструктора по умолчанию
        Matrix3f matrix = new Matrix3f();

        assertEquals(3, matrix.getRows());
        assertEquals(3, matrix.getCols());
        assertEquals(0.0f, matrix.getElement(0, 0), 1e-6);
        assertEquals(0.0f, matrix.getElement(2, 2), 1e-6);
    }

    @Test
    void testMatrix3fFromMatrix() {
        // Тест создания из другой матрицы
        float[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Matrix3f source = new Matrix3f(data);
        Matrix3f copy = new Matrix3f(source);

        assertEquals(source.getRows(), copy.getRows());
        assertEquals(source.getCols(), copy.getCols());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(
                        source.getElement(i, j),
                        copy.getElement(i, j),
                        1e-6,
                        String.format("Element [%d][%d] mismatch", i, j)
                );
            }
        }
    }

    @Test
    void testMatrix3fAddition() {
        Matrix3f a = new Matrix3f(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Matrix3f b = new Matrix3f(new float[]{9, 8, 7, 6, 5, 4, 3, 2, 1});

        a.add(b);

        assertEquals(10.0f, a.getElement(0, 0), 1e-6);  // 1 + 9
        assertEquals(10.0f, a.getElement(1, 1), 1e-6);  // 5 + 5
        assertEquals(10.0f, a.getElement(2, 2), 1e-6);  // 9 + 1
    }

    @Test
    void testMatrix3fMultiplication() {
        Matrix3f a = new Matrix3f(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Matrix3f b = new Matrix3f(new float[]{9, 8, 7, 6, 5, 4, 3, 2, 1});

        Matrix<Float> result = a.mul(b);

        // Проверяем несколько элементов результата
        // Первая строка: [1,2,3] × столбцы b
        // (1*9 + 2*6 + 3*3) = 9 + 12 + 9 = 30
        assertEquals(30.0f, result.getElement(0, 0), 1e-6);

        // (1*8 + 2*5 + 3*2) = 8 + 10 + 6 = 24
        assertEquals(24.0f, result.getElement(0, 1), 1e-6);

        // (1*7 + 2*4 + 3*1) = 7 + 8 + 3 = 18
        assertEquals(18.0f, result.getElement(0, 2), 1e-6);
    }

    @Test
    void testMatrix3fScalarMultiplication() {
        Matrix3f matrix = new Matrix3f(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});

        Matrix<Float> result = matrix.mul(2.0f);

        assertEquals(2.0f, result.getElement(0, 0), 1e-6);   // 1 * 2
        assertEquals(4.0f, result.getElement(0, 1), 1e-6);   // 2 * 2
        assertEquals(10.0f, result.getElement(1, 1), 1e-6);  // 5 * 2
        assertEquals(18.0f, result.getElement(2, 2), 1e-6);  // 9 * 2
    }

    @Test
    void testMatrix3fTranspose() {
        Matrix3f matrix = new Matrix3f(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});

        Matrix<Float> transposed = matrix.transpose();

        assertEquals(1.0f, transposed.getElement(0, 0), 1e-6);
        assertEquals(4.0f, transposed.getElement(0, 1), 1e-6);  // Было (1,0)
        assertEquals(7.0f, transposed.getElement(0, 2), 1e-6);  // Было (2,0)
        assertEquals(2.0f, transposed.getElement(1, 0), 1e-6);  // Было (0,1)
        assertEquals(5.0f, transposed.getElement(1, 1), 1e-6);
        assertEquals(8.0f, transposed.getElement(1, 2), 1e-6);  // Было (2,1)
    }

    @Test
    void testMatrix3fSubtraction() {
        Matrix3f a = new Matrix3f(new float[]{10, 20, 30, 40, 50, 60, 70, 80, 90});
        Matrix3f b = new Matrix3f(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});

        Matrix<Float> result = a.sub(b);

        assertEquals(9.0f, result.getElement(0, 0), 1e-6);   // 10 - 1
        assertEquals(18.0f, result.getElement(0, 1), 1e-6);  // 20 - 2
        assertEquals(27.0f, result.getElement(0, 2), 1e-6);  // 30 - 3
        assertEquals(45.0f, result.getElement(1, 1), 1e-6);  // 50 - 5
    }

    @Test
    void testMatrix3fToMatrix3() {
        Matrix3f matrix3f = new Matrix3f(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});

        // Метод toMatrix3() возвращает Matrix3<T>
        com.cgvsu.math.matrix.Matrix3<Float> matrix3 = matrix3f.toMatrix3();

        assertEquals(3, matrix3.getRows());
        assertEquals(3, matrix3.getCols());
        assertEquals(1.0f, matrix3.getElement(0, 0), 1e-6);
        assertEquals(9.0f, matrix3.getElement(2, 2), 1e-6);
    }

    @Test
    void testMatrix3fToString() {
        Matrix3f matrix = new Matrix3f(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});

        String str = matrix.toString();

        assertNotNull(str);
        assertFalse(str.isEmpty());
        // Должен содержать элементы матрицы
        assertTrue(str.contains("1.0") || str.contains("1"));
        assertTrue(str.contains("9.0") || str.contains("9"));
    }

    @Test
    void testMatrix3fSetElement() {
        Matrix3f matrix = new Matrix3f();

        matrix.setElement(1, 2, 99.0f);

        assertEquals(99.0f, matrix.getElement(1, 2), 1e-6);
    }

    @Test
    void testMatrix3fEquality() {
        Matrix3f a = new Matrix3f(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Matrix3f b = new Matrix3f(new float[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Matrix3f c = new Matrix3f(new float[]{9, 8, 7, 6, 5, 4, 3, 2, 1});

        // Тест equals (должен быть унаследован от Matrix)
        assertEquals(a, b);
        assertNotEquals(a, c);
    }
}