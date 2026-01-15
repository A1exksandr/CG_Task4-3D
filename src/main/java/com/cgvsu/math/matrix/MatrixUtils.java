package com.cgvsu.math.matrix;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public interface MatrixUtils {
    static Float[] toFloatArray(float[] floats) {
        Float[] floats1 = new Float[floats.length];
        for (int i = 0; i < floats.length; i++) {
            floats1[i] = floats[i];
        }
        return floats1;
    }

    static Float sumToFloat(Number... numbers) {
        float doubleSum = 0;

        for (Number number : numbers) {
            doubleSum += number.floatValue();
        }
        return doubleSum;
    }

    static Float multiplyToFloat(Number... numbers) {
        float doubleMultiply = 1;

        for (Number number : numbers) {
            doubleMultiply *= number.floatValue();
        }
        return doubleMultiply;
    }

    public static <T extends Number> Matrix<T> addition(Matrix<T> matrix1, Matrix<T> matrix2) {
        checkMatrices(matrix1, matrix2);
        List<T> valuesOfMatrix1 = matrix1.getValues();
        List<T> valuesOfMatrix2 = matrix2.getValues();
        List<T> newMatrix = new LinkedList<>();
        for (int i = 0; i < valuesOfMatrix1.size(); i++) {
            //todo it's really awful but i don't know how to do it better.
            //I need to be sure about T parameters are the same for both matrices
            newMatrix.add((T) sumToFloat(valuesOfMatrix1.get(i), valuesOfMatrix2.get(i)));
//            newMatrix.add((T) Double.valueOf(valuesOfMatrix1.get(i).doubleValue() + valuesOfMatrix2.get(i).doubleValue()));
        }

        return new Matrix<>(newMatrix, matrix1.getRows(), matrix1.getCols());
    }


    public static <T extends Number> Matrix<T> subtraction(Matrix<T> matrix1, Matrix<T> matrix2) {
        checkMatrices(matrix1, matrix2);
        List<T> matrix1F = matrix1.getValues();
        List<T> matrix2F = matrix2.getValues();
        List<T> newMatrix = new LinkedList<>();

        for (int i = 0; i < matrix1F.size(); i++) {
            //todo it's really awful but i don't know how to do it better.
            //I need to be sure about T parameters are the same for both matrices
            newMatrix.add((T) Float.valueOf(matrix1F.get(i).floatValue() - matrix2F.get(i).floatValue()));
        }

        return new Matrix<>(newMatrix, matrix1.getRows(), matrix1.getCols());
    }

    public static <T extends Number> Matrix<T> multiplication(Matrix<T> matrix, T c) {
        List<T> matrix1F = matrix.getValues();
        List<T> newMatrix = new LinkedList<>();
        for (T number : matrix1F) {
            newMatrix.add((T) multiplyToFloat(c, number));
        }

        return new Matrix<>(newMatrix, matrix.getRows(), matrix.getCols());
    }

    public static <T extends Number> Matrix<T> multiplication(Matrix<T> matrix1, Matrix<T> matrix2) {
        if (matrix1 == null || matrix2 == null) {
            throw new MatrixException("Matrix is null");
        }
        if (matrix1.getCols() != matrix2.getRows()) {
            throw new MatrixException("Width of the first matrix isn't equal to the height of the second one");
        }

        List<T> matrix1F = matrix1.getValues();
        List<T> matrix2F = matrix2.getValues();
        List<T> newMatrix = new LinkedList<>();

//
        for (int i = 0; i < matrix1.getRows() * matrix2.getCols(); i++) {
            float value = 0;
            for (int j = 0; j < matrix1.getCols(); j++) {
                //matrix.get(i + j) + matrix.get(j*cols)
                //matrix.get(i + j)
                //matrix.get(i + j)
                value += getElement(matrix1F, matrix1.cols, i / matrix2.cols, j).doubleValue() * getElement(matrix2F, matrix2.cols, j, i % matrix2.rows).floatValue();
//                value += matrix1F.get(i * matrix1.getCols() + j).doubleValue() * matrix2F.get(j * matrix2.getRows() + i).doubleValue();

            }
            newMatrix.add((T) Float.valueOf(value));
        }

        return new Matrix<>(newMatrix, matrix1.getRows(), matrix2.getCols());
    }


    static <T extends Number> T getElement(List<T> matrix, int cols, int row, int col) {
        return matrix.get(cols * row + col);
    }

    static <T extends Number> void checkMatrices(Matrix<T> matrix1, Matrix<T> matrix2) {
        //переименовать метод и разделить проверки
        if (matrix1 == null || matrix2 == null) {
            throw new MatrixException("Matrix is null");
        }
        if (matrix1.getRows() != matrix2.getRows() || matrix1.getCols() != matrix2.getCols()) {
            throw new MatrixException("length of the matrices aren't equal");
        }
    }

    public static <T extends Number> Matrix<T> transposition(Matrix<T> matrix) {
        if (matrix == null) {
            throw new MatrixException("Matrix is null");
        }

        List<T> source = matrix.getValues();
        int srcRows = matrix.getRows();
        int srcCols = matrix.getCols();

        // Для транспонирования меняем размеры местами
        int dstRows = srcCols;  // Высота = старая ширина
        int dstCols = srcRows;  // Ширина = старая высота

        List<T> destination = new ArrayList<>(dstRows * dstCols);

        // Инициализируем список (setElement требует существующих индексов)
        for (int i = 0; i < dstRows * dstCols; i++) {
            destination.add(null);
        }

        // Простое транспонирование: элемент [i][j] -> элемент [j][i]
        for (int row = 0; row < srcRows; row++) {
            for (int col = 0; col < srcCols; col++) {
                T value = matrix.getElement(row, col);
                setElement(destination, dstCols, col, row, value); // Индексы меняются местами!
            }
        }

        return new Matrix<>(destination, dstRows, dstCols);
    }

    static <T extends Number> void setElement(List<T> values, int width, int row, int col, T value) {
        int index = width * row + col;

        if (index < 0 || index >= values.size()) {
            // Расширяем список если нужно
            while (values.size() <= index) {
                values.add(null);
            }
        }

        values.set(index, value);
    }
}
