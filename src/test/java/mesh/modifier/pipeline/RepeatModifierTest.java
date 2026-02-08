package mesh.modifier.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import mesh.Mesh3D;
import mesh.creator.primitives.UVSphereCreator;
import mesh.modifier.IMeshModifier;
import mesh.pipeline.RepeatModifier;

class RepeatModifierTest {

  // -------------------------------------------------------------------------
  // Validation
  // -------------------------------------------------------------------------

  @Test
  void rejectsRepetitionsLessThanOne() {
    IMeshModifier noop = mesh -> mesh;

    assertThrows(IllegalArgumentException.class, () -> new RepeatModifier(0, noop));

    assertThrows(IllegalArgumentException.class, () -> new RepeatModifier(-3, noop));
  }

  @Test
  void rejectsNullModifier() {
    assertThrows(IllegalArgumentException.class, () -> new RepeatModifier(3, null));
  }

  // -------------------------------------------------------------------------
  // Core behavior
  // -------------------------------------------------------------------------

  @Test
  void appliesModifierExactNumberOfTimes() {
    Mesh3D mesh = new UVSphereCreator().create();
    CountingModifier modifier = new CountingModifier();

    new RepeatModifier(5, modifier).modify(mesh);

    assertEquals(5, modifier.getCount());
  }

  @Test
  void returnsSameMeshInstance() {
    Mesh3D mesh = new UVSphereCreator().create();
    IMeshModifier noop = m -> m;

    Mesh3D result = new RepeatModifier(3, noop).modify(mesh);

    assertEquals(mesh, result);
  }

  @Test
  void propagatesNullMeshCheckFromModifier() {
    IMeshModifier counting = new CountingModifier();

    RepeatModifier repeat = new RepeatModifier(3, counting);

    assertThrows(IllegalArgumentException.class, () -> repeat.modify(null));
  }

  // -------------------------------------------------------------------------
  // Helper modifier
  // -------------------------------------------------------------------------

  private static class CountingModifier implements IMeshModifier {

    private int count = 0;

    @Override
    public Mesh3D modify(Mesh3D mesh) {
      if (mesh == null) {
        throw new IllegalArgumentException("Mesh cannot be null.");
      }
      count++;
      return mesh;
    }

    int getCount() {
      return count;
    }
  }
}
