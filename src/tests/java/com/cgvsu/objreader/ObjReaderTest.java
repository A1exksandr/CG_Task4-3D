package objreader;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objreader.ObjReaderExceptions;
import org.junit.jupiter.api.Test;
import javax.vecmath.Vector3f;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjReaderTest {

    @Test
    void testParseSimpleCube() {
        String cubeObj = """
            v 1.0 1.0 -1.0
            v 1.0 -1.0 -1.0
            v -1.0 -1.0 -1.0
            v -1.0 1.0 -1.0
            v 1.0 1.0 1.0
            v 1.0 -1.0 1.0
            v -1.0 -1.0 1.0
            v -1.0 1.0 1.0
            f 1 2 3 4
            f 5 6 2 1
            """;

        Model model = ObjReader.read(cubeObj, false);

        assertEquals(8, model.getVertices().size());
        assertEquals(2, model.getPolygons().size());
        assertEquals(4, model.getPolygons().get(0).getVertexIndices().size());
    }

    @Test
    void testParseMixedFaceFormats() {
        String mixedObj = """
            v 0 0 0
            v 1 0 0
            v 0 1 0
            vt 0 0
            vt 1 0
            vt 0 1
            vn 0 0 1
            vn 0 1 0
            f 1/1/1 2/2/1 3/3/2
            f 1//1 2//1 3//2
            """;

        Model model = ObjReader.read(mixedObj, false);

        assertEquals(3, model.getVertices().size());
        assertEquals(2, model.getPolygons().size());
        // Первый полигон имеет текстурные координаты и нормали
        assertEquals(3, model.getPolygons().get(0).getTextureVertexIndices().size());
        // Второй полигон имеет только нормали
        assertEquals(0, model.getPolygons().get(1).getTextureVertexIndices().size());
        assertEquals(3, model.getPolygons().get(1).getNormalIndices().size());
    }

    @Test
    void testParseInvalidFaceShouldThrow() {
        String invalidObj = """
            v 0 0 0
            f 1 2 3
            """;

        assertThrows(ObjReaderExceptions.FaceException.class,
                () -> ObjReader.read(invalidObj, false));
    }
}