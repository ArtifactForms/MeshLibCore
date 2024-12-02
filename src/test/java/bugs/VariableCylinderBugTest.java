package bugs;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.special.VariableCylinderCreator;
import util.MeshTestUtil;

public class VariableCylinderBugTest {

    /**
     * Bottom cap was rotated around the wrong axis. Bug fixed: rotate around x
     * instead of z Wrong rotation makes this test fail.
     */
    @Test
    public void triggerBug() {
        float rotationSegments = 15.850006f;
        VariableCylinderCreator creator = new VariableCylinderCreator();
        creator.setRotationSegments((int) rotationSegments);
        creator.add(1, 0);
        creator.add(2, 0);
        Mesh3D mesh = creator.create();
        assertTrue(MeshTestUtil.isManifold(mesh));
    }

}
