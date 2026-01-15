package com.cgvsu.objwriter;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class ObjWriter {
    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    public static ArrayList<String> write(Model mesh) throws ObjWriterExceptions {
        ArrayList<String> listFileContent = new ArrayList<>();

        if (mesh == null) {
            return listFileContent;
        }

        writeVertices(mesh.getVertices(), listFileContent);
        if (!mesh.getVertices().isEmpty()) {
            listFileContent.add("# " + mesh.getVertices().size() + " vertices");
        }

        writeTextureVertices(mesh.getTextureVertices(), listFileContent);
        if (!mesh.getTextureVertices().isEmpty()) {
            listFileContent.add("# " + mesh.getTextureVertices().size() + " texture coordinates");
        }

        writeNormals(mesh.getNormals(), listFileContent);
        if (!mesh.getNormals().isEmpty()) {
            listFileContent.add("# " + mesh.getNormals().size() + " normals");
        }

        writePolygons(mesh.getPolygons(), listFileContent);
        if (!mesh.getPolygons().isEmpty()) {
            listFileContent.add("# " + mesh.getPolygons().size() + " polygons");
        }

        return listFileContent;
    }

    protected static void writeVertices(final List<Vector3f> vertices, ArrayList<String> outListFileContent) {
        if (vertices == null) return;

        for (Vector3f vertex : vertices) {
            // Форматируем с точностью до 6 знаков после запятой
            outListFileContent.add(String.format("%s %.6f %.6f %.6f",
                    OBJ_VERTEX_TOKEN, vertex.x, vertex.y, vertex.z));
        }
    }

    protected static void writeTextureVertices(final List<Vector2f> textureVertices, ArrayList<String> outListFileContent) {
        if (textureVertices == null) return;

        for (Vector2f textureVertex : textureVertices) {
            outListFileContent.add(String.format("%s %.6f %.6f",
                    OBJ_TEXTURE_TOKEN, textureVertex.x, textureVertex.y));
        }
    }

    protected static void writeNormals(final List<Vector3f> normals, ArrayList<String> outListFileContent) {
        if (normals == null) return;

        for (Vector3f normal : normals) {
            outListFileContent.add(String.format("%s %.6f %.6f %.6f",
                    OBJ_NORMAL_TOKEN, normal.x, normal.y, normal.z));
        }
    }

    protected static void writePolygons(final List<Polygon> polygons, ArrayList<String> outListFileContent) throws ObjWriterExceptions {
        if (polygons == null) return;

        for (Polygon polygon : polygons) {
            writeOnePolygon(polygon, outListFileContent);
        }
    }

    protected static void writeOnePolygon(final Polygon polygon, List<String> outListFileContent) throws ObjWriterExceptions {
        if (polygon == null || polygon.getVertexIndices().isEmpty()) {
            return;
        }

        StringBuilder strPolygon = new StringBuilder();
        strPolygon.append(OBJ_FACE_TOKEN).append(" ");

        int vertexCount = polygon.getVertexIndices().size();
        int textureCount = polygon.getTextureVertexIndices().size();
        int normalCount = polygon.getNormalIndices().size();

        try {
            for (int i = 0; i < vertexCount; i++) {
                strPolygon.append(polygon.getVertexIndices().get(i) + 1);

                if (textureCount > 0 && i < textureCount) {
                    strPolygon.append("/").append(polygon.getTextureVertexIndices().get(i) + 1);
                } else if (normalCount > 0) {
                    strPolygon.append("/");
                }

                if (normalCount > 0 && i < normalCount) {
                    strPolygon.append("/").append(polygon.getNormalIndices().get(i) + 1);
                }

                strPolygon.append(" ");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new ObjWriterExceptions("Mismatch in polygon indices count. Vertices: " +
                    vertexCount + ", Textures: " + textureCount + ", Normals: " + normalCount);
        }

        outListFileContent.add(strPolygon.toString().trim());
    }
}