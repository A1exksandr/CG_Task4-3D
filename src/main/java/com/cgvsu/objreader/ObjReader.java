package com.cgvsu.objreader;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ObjReader {

    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";
    private static final String OBJ_COMMENT_TOKEN = "#";
    private static final String OBJ_GROUP_TOKEN = "g";
    private static final String OBJ_EMPTY_TOKEN = "";

    public static Model read(final String fileContent, boolean writeInfo) {

        Model resultModel = new Model();
        int lineInd = 0;
        Scanner scanner = new Scanner(fileContent);
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("\ufeff")) {
                throw new ObjReaderExceptions.WrongFileException("Save your file without BOM");
            }
            while (true) {
                ++lineInd;
                final List<String> wordsInLine = new ArrayList<>(Arrays.asList(line.split("\\s+")));
                if (wordsInLine.isEmpty()) {
                    if (!scanner.hasNextLine()) {
                        break;
                    }
                    line = scanner.nextLine();
                    continue;
                }
                final String keyWord = wordsInLine.remove(0);
                switch (keyWord) {
                    case OBJ_VERTEX_TOKEN -> resultModel.getVertices().add(parseVertex(wordsInLine, lineInd));
                    case OBJ_TEXTURE_TOKEN -> resultModel.getTextureVertices().add(parseTextureVertex(wordsInLine, lineInd));
                    case OBJ_NORMAL_TOKEN -> resultModel.getNormals().add(parseNormal(wordsInLine, lineInd));
                    case OBJ_FACE_TOKEN -> resultModel.getPolygons().add(parseFace(wordsInLine, lineInd));
                    case OBJ_COMMENT_TOKEN -> {
                        if (writeInfo) System.out.println("Comment on the line: " + lineInd);
                    }
                    case OBJ_GROUP_TOKEN -> {
                        if (writeInfo) System.out.println("Group not supported: " + lineInd);
                    }
                    case OBJ_EMPTY_TOKEN -> {
                    }
                    default -> throw new ObjReaderExceptions.ObjReaderException("Wrong key word.", lineInd);
                }
                if (!scanner.hasNextLine()) {
                    break;
                }
                line = scanner.nextLine();
            }
        }
        resultModel.checkConsistency();
        return resultModel;
    }

    // Всем методам кроме основного я поставил модификатор доступа protected, чтобы обращаться к ним в тестах
    protected static Vector3f parseVertex(final List<String> listOfWordsWithoutToken, final int lineInd) {
        if (listOfWordsWithoutToken.size() != 3)
            throw new ObjReaderExceptions.ObjReaderException("Wrong number of vertex arguments.", lineInd);
        try {
            return new Vector3f(
                    Float.parseFloat(listOfWordsWithoutToken.get(0)),
                    Float.parseFloat(listOfWordsWithoutToken.get(1)),
                    Float.parseFloat(listOfWordsWithoutToken.get(2)));
        } catch (NumberFormatException e) {
            throw new ObjReaderExceptions.ObjReaderException("Failed to parse float value.", lineInd);
        }
    }

    protected static Vector2f parseTextureVertex(final List<String> listOfWordsWithoutToken, final int lineInd) {
        if (listOfWordsWithoutToken.size() != 2 && listOfWordsWithoutToken.size() != 3)
            throw new ObjReaderExceptions.ObjReaderException("Wrong number of texture vertex arguments.", lineInd);
        try {
            return new Vector2f(
                    Float.parseFloat(listOfWordsWithoutToken.get(0)),
                    Float.parseFloat(listOfWordsWithoutToken.get(1)));
        } catch (NumberFormatException e) {
            throw new ObjReaderExceptions.ObjReaderException("Failed to parse float value.", lineInd);
        }
    }

    protected static Vector3f parseNormal(final List<String> listOfWordsWithoutToken, final int lineInd) {
        if (listOfWordsWithoutToken.size() != 3)
            throw new ObjReaderExceptions.ObjReaderException("Wrong number of normal arguments.", lineInd);
        try {
            return new Vector3f(
                    Float.parseFloat(listOfWordsWithoutToken.get(0)),
                    Float.parseFloat(listOfWordsWithoutToken.get(1)),
                    Float.parseFloat(listOfWordsWithoutToken.get(2)));
        } catch (NumberFormatException e) {
            throw new ObjReaderExceptions.ObjReaderException("Failed to parse float value.", lineInd);
        }
    }

    protected static Polygon parseFace(final List<String> listOfWordsWithoutToken, final int lineInd) {
        if (listOfWordsWithoutToken.size() < 3) {
            throw new ObjReaderExceptions.ObjReaderException("Not enough vertexes for polygon.", lineInd);
        }
        ArrayList<Integer> onePolygonVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<>();
        ArrayList<Integer> onePolygonNormalIndices = new ArrayList<>();
        for (String s : listOfWordsWithoutToken) {
            parseOneFaceWord(s, onePolygonVertexIndices, onePolygonTextureVertexIndices, onePolygonNormalIndices, lineInd);
        }
        Polygon resultPolygon = new Polygon();
        resultPolygon.setVertexIndices(onePolygonVertexIndices);
        resultPolygon.setTextureVertexIndices(onePolygonTextureVertexIndices);
        resultPolygon.setNormalIndices(onePolygonNormalIndices);
        return resultPolygon;
    }

    protected static void parseOneFaceWord(
            final String wordInLine,
            final List<Integer> onePolygonVertexIndices,
            final List<Integer> onePolygonTextureVertexIndices,
            final List<Integer> onePolygonNormalIndices,
            final int lineInd) {

        String[] vertexData = wordInLine.split("/", -1);

        try {
            // Вершина обязательна
            if (vertexData.length > 0 && !vertexData[0].isEmpty()) {
                int vertexIndex = Integer.parseInt(vertexData[0]);
                if (vertexIndex < 1) {
                    throw new ObjReaderExceptions.ObjReaderException("Vertex index must be positive", lineInd);
                }
                onePolygonVertexIndices.add(vertexIndex - 1);
            } else {
                throw new ObjReaderExceptions.ObjReaderException("Missing vertex index", lineInd);
            }

            // Текстурная координата
            if (vertexData.length > 1 && !vertexData[1].isEmpty()) {
                int textureIndex = Integer.parseInt(vertexData[1]);
                if (textureIndex < 1) {
                    throw new ObjReaderExceptions.ObjReaderException("Texture index must be positive", lineInd);
                }
                onePolygonTextureVertexIndices.add(textureIndex - 1);
            }

            if (vertexData.length > 2 && !vertexData[2].isEmpty()) {
                int normalIndex = Integer.parseInt(vertexData[2]);
                if (normalIndex < 1) {
                    throw new ObjReaderExceptions.ObjReaderException("Normal index must be positive", lineInd);
                }
                onePolygonNormalIndices.add(normalIndex - 1);
            }

        } catch (NumberFormatException e) {
            throw new ObjReaderExceptions.ObjReaderException("Failed to parse int value: " + wordInLine, lineInd);
        }
    }
}
