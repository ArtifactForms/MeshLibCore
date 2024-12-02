package bugs;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.modifier.BevelFacesModifier;
import util.MeshTestUtil;

/**
 * Modifier removed faces.
 */
public class BevelFacesModifierBug {

    @Test
    public void test() {
        IcosahedronCreator creator = new IcosahedronCreator();
        Mesh3D mesh = creator.create();
        mesh.apply(new BevelFacesModifier());
        assertTrue(MeshTestUtil.isManifold(mesh));
    }

    @Test
    public void testexpectedFaceCount() {
        int expectedFaceCount = 80;
        Mesh3D mesh = new IcosahedronCreator().create();
        mesh.apply(new BevelFacesModifier());
        assertEquals(expectedFaceCount, mesh.getFaceCount());
    }

}
