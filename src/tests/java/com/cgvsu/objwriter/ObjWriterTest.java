package com.cgvsu.objwriter;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import org.junit.jupiter.api.Test;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class ObjWriterTest {

    @Test
    void testWriteSimpleModel() throws Exception {
        Model model = new Model();

        // Вершины
        model.getVertices().add(new Vector3f(0, 0, 0));
        model.getVertices().add(new Vector3f(1, 0, 0));
        model.getVertices().add(new Vector3f(0, 1, 0));

        // Полигон
        Polygon polygon = new Polygon();
        polygon.getVertexIndices().addAll(Arrays.asList(0, 1, 2));
        model.getPolygons().add(polygon);

        ArrayList<String> lines = ObjWriter.write(model);

        assertNotNull(lines);
        assertFalse(lines.isEmpty());

        // Проверяем формат
        assertEquals("v 0.000000 0.000000 0.000000", lines.get(0));
        assertEquals("v 1.000000 0.000000 0.000000", lines.get(1));
        assertEquals("v 0.000000 1.000000 0.000000", lines.get(2));
        assertTrue(lines.get(3).contains("3 vertices"));
        assertEquals("f 1 2 3", lines.get(4));
        assertTrue(lines.get(5).contains("1 polygons"));
    }

    @Test
    void testWriteModelWithTexturesAndNormals() throws Exception {
        Model model = new Model();

        // Вершины
        model.getVertices().add(new Vector3f(0, 0, 0));
        model.getVertices().add(new Vector3f(1, 0, 0));
        model.getVertices().add(new Vector3f(0, 1, 0));

        // Текстурные координаты
        model.getTextureVertices().add(new Vector2f(0, 0));
        model.getTextureVertices().add(new Vector2f(1, 0));
        model.getTextureVertices().add(new Vector2f(0, 1));

        // Нормали
        model.getNormals().add(new Vector3f(0, 0, 1));
        model.getNormals().add(new Vector3f(0, 0, 1));
        model.getNormals().add(new Vector3f(0, 0, 1));

        // Полигон с текстурами и нормалями
        Polygon polygon = new Polygon();
        polygon.getVertexIndices().addAll(Arrays.asList(0, 1, 2));
        polygon.getTextureVertexIndices().addAll(Arrays.asList(0, 1, 2));
        polygon.getNormalIndices().addAll(Arrays.asList(0, 1, 2));
        model.getPolygons().add(polygon);

        ArrayList<String> lines = ObjWriter.write(model);

        assertFalse(lines.isEmpty());

        // Ищем строку с полигоном
        String faceLine = "";
        for (String line : lines) {
            if (line.startsWith("f ")) {
                faceLine = line;
                break;
            }
        }

        assertEquals("f 1/1/1 2/2/2 3/3/3", faceLine);
    }

    @Test
    void testWriteModelWithOnlyNormals() throws Exception {
        Model model = new Model();

        model.getVertices().add(new Vector3f(0, 0, 0));
        model.getVertices().add(new Vector3f(1, 0, 0));
        model.getVertices().add(new Vector3f(0, 1, 0));

        model.getNormals().add(new Vector3f(0, 0, 1));
        model.getNormals().add(new Vector3f(0, 0, 1));
        model.getNormals().add(new Vector3f(0, 0, 1));

        Polygon polygon = new Polygon();
        polygon.getVertexIndices().addAll(Arrays.asList(0, 1, 2));
        polygon.getNormalIndices().addAll(Arrays.asList(0, 1, 2));
        model.getPolygons().add(polygon);

        ArrayList<String> lines = ObjWriter.write(model);

        String faceLine = "";
        for (String line : lines) {
            if (line.startsWith("f ")) {
                faceLine = line;
                break;
            }
        }

        // Должен быть формат v//vn
        assertEquals("f 1//1 2//2 3//3", faceLine);
    }

    @Test
    void testWriteEmptyModel() throws Exception {
        Model model = new Model();

        ArrayList<String> lines = ObjWriter.write(model);

        // Должен быть пустой список или только комментарии
        assertNotNull(lines);
        // Может быть пустым или содержать только комментарии
    }

    @Test
    void testWriteNullModel() throws Exception {
        ArrayList<String> lines = ObjWriter.write(null);

        assertNotNull(lines);
        assertTrue(lines.isEmpty());
    }

    @Test
    void testWriteModelWithMismatchedIndicesShouldThrow() {
        Model model = new Model();

        model.getVertices().add(new Vector3f(0, 0, 0));
        model.getVertices().add(new Vector3f(1, 0, 0));

        // Больше текстурных координат, чем вершин
        model.getTextureVertices().add(new Vector2f(0, 0));
        model.getTextureVertices().add(new Vector2f(1, 0));
        model.getTextureVertices().add(new Vector2f(0, 1));

        Polygon polygon = new Polygon();
        polygon.getVertexIndices().addAll(Arrays.asList(0, 1));
        polygon.getTextureVertexIndices().addAll(Arrays.asList(0, 1, 2)); // На одну больше!
        model.getPolygons().add(polygon);

        assertThrows(ObjWriterExceptions.class, () -> ObjWriter.write(model));
    }

    @Test
    void testWriteQuadPolygon() throws Exception {
        Model model = new Model();

        model.getVertices().add(new Vector3f(0, 0, 0));
        model.getVertices().add(new Vector3f(1, 0, 0));
        model.getVertices().add(new Vector3f(1, 1, 0));
        model.getVertices().add(new Vector3f(0, 1, 0));

        Polygon polygon = new Polygon();
        polygon.getVertexIndices().addAll(Arrays.asList(0, 1, 2, 3));
        model.getPolygons().add(polygon);

        ArrayList<String> lines = ObjWriter.write(model);

        String faceLine = "";
        for (String line : lines) {
            if (line.startsWith("f ")) {
                faceLine = line;
                break;
            }
        }

        assertEquals("f 1 2 3 4", faceLine);
    }
}