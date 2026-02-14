package regression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import mesh.Mesh3D;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.modifier.topology.BevelFacesModifier;
import util.MeshTestUtil;

/** Modifier removed faces. */
public class BevelFacesModifierBugTest {

  @Test
  public void testManifold() {
    IcosahedronCreator creator = new IcosahedronCreator();
    Mesh3D mesh = creator.create();
    new BevelFacesModifier().modify(mesh);
    assertTrue(MeshTestUtil.isManifold(mesh));
  }

  @Test
  public void testexpectedFaceCount() {
    int expectedFaceCount = 80;
    Mesh3D mesh = new IcosahedronCreator().create();
    new BevelFacesModifier().modify(mesh);
    assertEquals(expectedFaceCount, mesh.getFaceCount());
  }
}
