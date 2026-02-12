package bugs;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import mesh.Mesh3D;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.modifier.topology.BevelFacesModifier;
import util.MeshTestUtil;

/**
 * Modifier removed faces.
 */
public class BevelFacesModifierBugTest {

    @Test
    public void testManifold() {
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
