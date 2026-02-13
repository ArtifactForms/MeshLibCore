package legacy.characterization.creator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.util.EnumSet;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import math.Bounds;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

import mesh.io.SimpleObjectReader;
import mesh.io.SimpleObjectWriter;
import mesh.util.MeshBoundsCalculator;
import util.MeshTestUtil;

import mesh.creator.archimedian.RhombicosidodecahedronCreator;

/**
 * AUTO-GENERATED CHARACTERIZATION TEST.
 * 
 * <p>Created at: Feb. 13,2026 22:25 
 *
 * <p>Auto-generated characterization test for legacy mesh creators.
 *
 * <p>This test captures the behavior of the IMeshCreator implementation
 * at generation time. It reflects snapshot behavior and is not a formal
 * API contract.
 *
 * <p>Regenerate if creator semantics intentionally change.
 *
 * <p>The purpose of this test suite is to capture and preserve the
 * <strong>current, known-working behavior</strong> of a mesh creator
 * implementation. These tests intentionally focus on observable behavior
 * rather than geometric or mathematical correctness.
 *
 * <p>The resulting assertions form a behavioral footprint that documents
 * how the creator behaves under its default configuration.
 *
 * <p>This footprint is based on the assumption that the creator has been
 * previously validated through manual and visual inspection (for example
 * using mesh viewers, normal visualization, or exploratory testing).
 *
 * <p>Any future change that causes these tests to fail indicates a
 * <strong>behavioral change</strong> and should be reviewed intentionally,
 * especially during refactoring, optimization, or architectural cleanup.
 *
 * <p>Concrete creator tests are generated from this template by supplying
 * expected default values such as vertex and face counts.
 */
public class RhombicosidodecahedronCreatorCharacterizationTest {

 /**
  * Documents the capability model used by characterization tests for {@link IMeshCreator}.
  *
  * <p>
  * At the time the footprint / characterization tests were introduced,
  * no formal contract existed for {@code IMeshCreator} implementations.
  * In particular, there was no clear distinction between:
  * </p>
  *
  * <ul>
  *   <li>Creators that generate vertex-only meshes (e.g. arcs or circles)</li>
  *   <li>Creators that generate flat surface meshes (e.g. disk-like meshes)</li>
  *   <li>Creators that generate closed volumetric meshes</li>
  * </ul>
  *
  * <p>
  * To avoid modifying legacy production code, expected mesh properties
  * are defined and enforced on the test side instead of introducing
  * new core-level contracts.
  * </p>
  *
  * <p>
  * This capability model allows tests to explicitly declare which
  * structural properties a mesh is expected to satisfy, such as:
  * manifoldness, volume enclosure, or allowance of loose vertices.
  * </p>
  *
  * <p>
  * The goal is to formalize expectations in tests while keeping
  * legacy mesh creators unchanged.
  * </p>
  */
  enum MeshCapabilities {
    HAS_FACES,
    IS_MANIFOLD,
    HAS_VOLUME,
    ALLOWS_LOOSE_VERTICES,
    CALCULATES_VERTEX_NORMALS
  }

 /**
  * <p>IMPORTANT: This describes the creator behavior at snapshot time and is not 
  * a formal API contract.</p>
  * 
  * Defines the set of mesh capabilities expected from this creator
  * at the time the characterization tests were introduced.
  *
  * <p>
  * These capabilities represent the implicit test-side contract
  * describing which structural properties the generated mesh
  * must satisfy (e.g. presence of faces, manifoldness, volume enclosure).
  * </p>
  *
  * <p>
  * The capability set reflects the expected behavior at snapshot time.
  * If the creator's responsibilities evolve, this declaration must be
  * reviewed and adapted accordingly.
  * </p>
  */
  private static final EnumSet<MeshCapabilities> CAPABILITIES =
    EnumSet.of(
      MeshCapabilities.HAS_FACES,
      MeshCapabilities.IS_MANIFOLD,
      MeshCapabilities.HAS_VOLUME);

 /**
  * Expected number of vertices produced by the mesh creator when using
  * its default configuration.
  *
  * <p>This value is part of the characterization footprint and documents
  * the current structural complexity of the generated mesh.
  */
  private static final int DEFAULT_VERTEX_COUNT = 60;

 /**
  * Expected total number of faces produced by the mesh creator when using
  * its default configuration.
  *
  * <p>This value captures the default mesh topology and acts as a
  * regression guard against unintended structural changes.
  */
  private static final int DEFAULT_FACE_COUNT = 62;

 /**
  * Expected number of triangular faces produced by the mesh creator when
  * using its default configuration.
  *
  * <p>This value captures the current face-type distribution as part of
  * the characterization footprint.
  */
  private static final int EXPECTED_DEFAULT_TRIANGLE_COUNT = 20;

 /**
  * Expected number of quadrilateral faces produced by the mesh creator
  * when using its default configuration.
  *
  * <p>This value captures the current face-type distribution as part of
  * the characterization footprint.
  */
  private static final int EXPECTED_DEFAULT_QUAD_COUNT = 30;

 /**
  * Verifies that the creator implements the {@link IMeshCreator} interface.
  *
  * <p>This ensures that the creator conforms to the expected contract and
  * can be used polymorphically by systems operating on mesh creators.
  */
  @Test
  public void testImplementsMeshCreatorInstance() {
    RhombicosidodecahedronCreator creator = new RhombicosidodecahedronCreator();
    assertTrue(creator instanceof IMeshCreator);
  }

 /**
  * Verifies that {@link RhombicosidodecahedronCreator#create()} produces a non-null mesh
  * using the default configuration.
  *
  * <p>This test captures the most basic expectation of a functioning
  * mesh creator and guards against incomplete or broken construction
  * logic.
  */
  @Test
  public void testCreatedMeshIsNotNullByDefault() {
    RhombicosidodecahedronCreator creator = new RhombicosidodecahedronCreator();
    assertNotNull(creator.create());
  }
  
 /**
  * Verifies that all vertices produced by the creator contain only finite,
  * well-defined coordinate values.
  *
  * <p>This test captures the expectation that the mesh creation process does
  * not generate {@code NaN} or infinite values for any vertex component
  * (x, y, z) when using the default configuration.
  *
  * <p>The test serves as a numerical stability check and establishes a
  * behavioral baseline for the creator. Any failure indicates a regression
  * in mathematical robustness or invalid intermediate computations.
  */
  @Test
  public void testVerticesContainNoNaNOrInfinity() {
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    mesh.getVertices().forEach(v -> {
      assertFalse(Float.isNaN(v.x) || Float.isNaN(v.y) || Float.isNaN(v.z));
      assertFalse(Float.isInfinite(v.x) || Float.isInfinite(v.y) || Float.isInfinite(v.z));
    });
  }
  
 /**
  * Contract Test: Bounding Box Validation (HAS_VOLUME Capability)
  *
  * <p>
  * This contract verifies that a mesh declaring the capability
  * {@link MeshCapabilities#HAS_VOLUME} produces a valid,
  * non-degenerate axis-aligned bounding box.
  * </p>
  *
  * <h2>Intent</h2>
  * <p>
  * Any mesh that claims to have volume must occupy a non-zero
  * three-dimensional space. This is validated by ensuring that
  * width, height, and depth of the calculated bounds are strictly greater than zero.
  * </p>
  *
  * <h2>Precondition</h2>
  * <ul>
  *   <li>The mesh class declares {@code MeshCapabilities.HAS_VOLUME}.</li>
  * </ul>
  *
  * <h2>Validation Rules</h2>
  * <ul>
  *   <li>{@code bounds.getWidth()  > 0}</li>
  *   <li>{@code bounds.getHeight() > 0}</li>
  *   <li>{@code bounds.getDepth()  > 0}</li>
  * </ul>
  *
  * <h2>Guarantees</h2>
  * <ul>
  *   <li>No collapsed geometry (all vertices at same position).</li>
  *   <li>No accidental zero scaling.</li>
  *   <li>Bounding box calculation is spatially correct.</li>
  *   <li>Primitive creators initialize valid geometry.</li>
  * </ul>
  *
  * <h2>Skipped Cases</h2>
  * <p>
  * If the mesh does not declare {@code HAS_VOLUME},
  * this test is skipped via {@code assumeTrue(...)}.
  * Typical examples:
  * </p>
  * <ul>
  *   <li>Plane meshes</li>
  *   <li>2D UI meshes</li>
  *   <li>Debug line meshes</li>
  * </ul>
  *
  * <h2>Common Failure Causes</h2>
  * <ul>
  *   <li>Vertices not initialized correctly</li>
  *   <li>All vertices positioned at origin</li>
  *   <li>Incorrect coordinate space during bounds calculation</li>
  *   <li>Degenerate primitive generation</li>
  * </ul>
  *
  * <p>
  * This contract represents one of the most fundamental geometric invariants
  * in the mesh validation framework.
  * </p>
  */
  @Test
  public void testMeshHasValidBoundingBox() {
      assumeTrue(CAPABILITIES.contains(MeshCapabilities.HAS_VOLUME));

      Mesh3D mesh = new RhombicosidodecahedronCreator().create();
      Bounds bounds = MeshBoundsCalculator.calculateBounds(mesh);

      assertTrue(bounds.getWidth() > 0);
      assertTrue(bounds.getHeight() > 0);
      assertTrue(bounds.getDepth() > 0);
  }

 /**
  * Verifies that each invocation of {@link RhombicosidodecahedronCreator#create()} produces
  * a distinct {@link Mesh3D} instance.
  *
  * <p>This test documents the current instance lifecycle behavior and
  * asserts that the creator does not reuse or internally cache meshes.
  *
  * <p>A failure indicates a behavioral change that may affect mutation
  * safety, downstream processing, or parallel usage.
  */
  @Test
  public void testCreatesUniqueInstances() {
    RhombicosidodecahedronCreator creator = new RhombicosidodecahedronCreator();
    Mesh3D mesh0 = creator.create();
    Mesh3D mesh1 = creator.create();
    Mesh3D mesh2 = creator.create();
    assertTrue(mesh0 != mesh1);
    assertTrue(mesh1 != mesh2);
  }

 /**
  * Verifies the default vertex count of the generated mesh.
  *
  * <p>This test captures the current vertex count as part of the
  * characterization footprint.
  *
  * <p>A failure indicates a change in structural complexity, either
  * intentional or as a side effect of refactoring.
  */
  @Test
  public void testDefaultVertexCount() {
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    assertEquals(DEFAULT_VERTEX_COUNT, mesh.getVertexCount());
    assertEquals(DEFAULT_VERTEX_COUNT, mesh.getVertices().size());
  }

 /**
  * Verifies the default face count of the generated mesh.
  *
  * <p>This test captures the current face count as part of the
  * characterization footprint.
  *
  * <p>A failure indicates a change in mesh topology and should be
  * reviewed intentionally.
  */
  @Test
  public void testDefaultFaceCount() {
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    assertEquals(DEFAULT_FACE_COUNT, mesh.getFaceCount());
    assertEquals(DEFAULT_FACE_COUNT, mesh.getFaces().size());
  }

 /**
  * Verifies the number of triangular faces produced by the mesh creator
  * when using its default configuration.
  *
  * <p>This test captures the current face-type distribution and documents
  * existing behavior rather than defining a required design constraint.
  */
  @Test
  public void testDefaultTriangleCount() {
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    int actual = 0;
    
    for (Face3D face : mesh.getFaces()) {
      if (face.getVertexCount() == 3) {
        actual++;
      }
    }
    assertEquals(EXPECTED_DEFAULT_TRIANGLE_COUNT, actual);
  }

 /**
  * Verifies the number of quadrilateral faces produced by the mesh creator
  * when using its default configuration.
  *
  * <p>This test captures the current face-type distribution and documents
  * existing behavior rather than defining a required design constraint.
  */
  @Test
  public void testDefaultQuadCount() {
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    int actual = 0;
    
    for (Face3D face : mesh.getFaces()) {
      if (face.getVertexCount() == 4) {
        actual++;
      }
    }
    assertEquals(EXPECTED_DEFAULT_QUAD_COUNT, actual);
  }
  
 /**
  * Ensures that all faces reference valid vertex indices.
  *
  * <p>
  * For every face in the generated mesh, each referenced vertex index
  * must fall within the valid range {@code [0, vertexCount)}.
  * </p>
  *
  * <h3>Guaranteed by this test</h3>
  * <ul>
  *   <li><b>Index Bounds Safety:</b>
  *       No face references a negative or out-of-range vertex index.</li>
  *   <li><b>Structural Consistency:</b>
  *       All face definitions are backed by existing vertices.</li>
  * </ul>
  *
  * <h3>Purpose</h3>
  * <p>
  * This test guards against invalid mesh construction states such as
  * dangling indices, corrupted face data, or incomplete vertex buffers.
  * It ensures basic topological integrity at the index level.
  * </p>
  *
  * <h3>Not Covered</h3>
  * <ul>
  *   <li>Duplicate vertex references within a face</li>
  *   <li>Manifoldness or winding order correctness</li>
  *   <li>Geometric validity of vertex positions</li>
  * </ul>
  */
  @Test
  public void testFacesReferenceValidVertices() {
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    int vertexCount = mesh.getVertexCount();

    for (Face3D face : mesh.getFaces()) {
      for (int i = 0; i < face.getVertexCount(); i++) {
        int idx = face.getIndexAt(i);
        assertTrue(idx >= 0 && idx < vertexCount);
      }
    }
  }
  
 /**
  * Verifies that mesh generation is deterministic.
  *
  * <p>
  * Two independent invocations of {@code RhombicosidodecahedronCreator.create()} must
  * produce identical vertex sequences. The test compares vertices
  * index-by-index to ensure stable ordering and reproducible geometry.
  * </p>
  *
  * <h3>Guaranteed by this test</h3>
  * <ul>
  *   <li><b>Deterministic Vertex Ordering:</b>
  *       Vertices are generated in a stable and predictable order.</li>
  *   <li><b>Reproducible Geometry:</b>
  *       No hidden randomness or state-dependent variation affects output.</li>
  * </ul>
  *
  * <h3>Not Covered</h3>
  * <ul>
  *   <li>Face index ordering</li>
  *   <li>Topological invariants</li>
  *   <li>Floating-point tolerance comparisons</li>
  * </ul>
  *
  * <p>
  * This test acts as a regression guard against non-deterministic
  * behavior in mesh construction logic.
  * </p>
  */
  @Test
  public void testVertexPositionsAreDeterministic() {
    RhombicosidodecahedronCreator creator = new RhombicosidodecahedronCreator();
    Mesh3D a = creator.create();
    Mesh3D b = creator.create();

    for (int i = 0; i < a.getVertexCount(); i++) {
      assertEquals(a.getVertices().get(i), b.getVertices().get(i));
    }
  }
  
 /**
  * Ensures that vertex data is not shared between generated mesh instances.
  *
  * <p>
  * After mutating the vertex collection of one mesh instance,
  * a subsequent invocation of {@code create()} must still produce
  * a mesh with the expected default vertex count.
  * </p>
  *
  * <p>
  * This guards against shared mutable vertex buffers or
  * accidental internal caching inside the mesh creator.
  * </p>
  */
  @Test
  public void testVertexDataIsNotSharedBetweenInstances() {
    RhombicosidodecahedronCreator creator = new RhombicosidodecahedronCreator();

    Mesh3D mesh0 = creator.create();
    mesh0.clearVertices();

    Mesh3D mesh1 = creator.create();

    assertEquals(DEFAULT_VERTEX_COUNT, mesh1.getVertexCount());
  }

 /**
  * Ensures that face data is not shared between generated mesh instances.
  *
  * <p>
  * Mutating the face collection of a previously created mesh must not
  * affect newly generated meshes.
  * </p>
  *
  * <p>
  * This verifies referential independence of face index buffers.
  * </p>
  */
  @Test
  public void testFaceDataIsNotSharedBetweenInstances() {
    RhombicosidodecahedronCreator creator = new RhombicosidodecahedronCreator();

    Mesh3D mesh0 = creator.create();
    mesh0.clearFaces();

    Mesh3D mesh1 = creator.create();

    assertEquals(DEFAULT_FACE_COUNT, mesh1.getFaceCount());
  }

 /**
  * Verifies that the generated mesh contains at least one vertex.
  *
  * <p>This test captures the expectation that the creator produces a
  * structurally non-empty mesh when using the default configuration.
  *
  * <p>It also asserts consistency between the reported vertex count and
  * the underlying vertex collection.
  */
  @Test
  public void testVertexListIsNotEmpty() {
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    assertFalse(mesh.getVertices().isEmpty());
    assertEquals(mesh.getVertexCount(), mesh.getVertices().size());
  }

 /**
  * Verifies that the generated mesh does not contain loose (unreferenced)
  * vertices.
  *
  * <p>This test captures the expectation that every vertex participates
  * in at least one face under the default configuration.
  */
  @Test
  public void testMeshHasNoLooseVertices() {
    assumeFalse(CAPABILITIES.contains(MeshCapabilities.ALLOWS_LOOSE_VERTICES));
  
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    assertTrue(MeshTestUtil.meshHasNoLooseVertices(mesh));
  }

 /**
  * Verifies that repeated invocations of the creator produce meshes with
  * a consistent vertex count.
  */
  @Test
  public void testVertexCountIsConsistent() {
    RhombicosidodecahedronCreator creator = new RhombicosidodecahedronCreator();
    Mesh3D mesh0 = creator.create();
    Mesh3D mesh1 = creator.create();
    assertEquals(mesh0.getVertexCount(), mesh1.getVertexCount());
  }

 /**
  * Verifies that repeated invocations of the creator produce meshes with
  * a consistent face count.
  */
  @Test
  public void testFaceCountIsConsistent() {
    RhombicosidodecahedronCreator creator = new RhombicosidodecahedronCreator();
    Mesh3D mesh0 = creator.create();
    Mesh3D mesh1 = creator.create();
    assertEquals(mesh0.getFaceCount(), mesh1.getFaceCount());
  }

 /**
  * Verifies that the generated mesh does not contain duplicated faces.
  *
  * <p>This check is part of the characterization baseline and documents
  * the current integrity of face construction.
  */
  @Test
  public void testMeshHasNoDuplicatedFaces() {
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    assertTrue(MeshTestUtil.meshHasNoDuplicatedFaces(mesh));
  }

 /**
  * Verifies a basic manifold property of the generated mesh.
  *
  * <p>This test is part of the characterization baseline and assumes that
  * the creator is intended to produce a watertight mesh.
  */
  @Test
  public void testIsManifold() {
    assumeTrue(CAPABILITIES.contains(MeshCapabilities.IS_MANIFOLD));

    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    assertTrue(MeshTestUtil.isManifold(mesh));
  }
  
 /**
  * Verifies that the mesh creator does not initialize face normals
  * when generating the mesh.
  *
  * <p>This test captures the current behavior that {@link Face3D#normal}
  * vectors remain zero-initialized after mesh creation.
  *
  * <p>The absence of initialized face normals is considered part of the
  * creator's characterization footprint and does not imply incorrect
  * behavior. Normal computation may be expected to occur in a later
  * processing step (e.g. normal generation, shading, or export).
  *
  * <p>Any change causing this test to fail indicates a behavioral change
  * in responsibility allocation and should be reviewed intentionally.
  */
  @Test
  public void testFaceNormalsAreNotInitializedByCreator() {
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();

    for (Face3D face : mesh.getFaces()) {
      Vector3f faceNormal = face.normal;
      assertEquals(0f, faceNormal.x);
      assertEquals(0f, faceNormal.y);
      assertEquals(0f, faceNormal.z);
    }
  }
  
 /**
  * Characterization test documenting the vertex normal behavior
  * of {@link IMeshCreator} implementations that do NOT declare
  * {@link MeshCapabilities#CALCULATES_VERTEX_NORMALS}.
  *
  * <p>
  * This test is only executed if the creator does not advertise
  * the {@code CALCULATES_VERTEX_NORMALS} capability. Implementations
  * that explicitly support vertex normal generation are excluded
  * via {@code assumeFalse(...)}.
  * </p>
  *
  * <p>
  * Historically, mesh creators did not calculate or attach per-vertex
  * normals during mesh generation. Vertex normal support was introduced
  * later and is now an optional capability.
  * </p>
  *
  * <p>
  * This test captures the expected behavior for creators without
  * vertex normal support and serves as a regression guard to ensure
  * that normals are not generated implicitly unless explicitly declared
  * via capabilities.
  * </p>
  *
  * <h3>Verified Behavior (for creators without vertex normal capability)</h3>
  * <ul>
  *   <li>No vertex normals are generated during mesh creation.</li>
  *   <li>The vertex normal collection remains empty.</li>
  * </ul>
  */
  @Test
  public void testCreatorDoesNotCalculateVertexNormals() {
      assumeFalse(CAPABILITIES.contains(MeshCapabilities.CALCULATES_VERTEX_NORMALS));
      
      Mesh3D mesh = new RhombicosidodecahedronCreator().create();

      assertFalse(mesh.hasVertexNormals());
      assertTrue(mesh.getVertexNormals().isEmpty());
  }
  
 /**
  * Verifies that the mesh creator does not modify or initialize face tags
  * during mesh creation.
  *
  * <p>This test captures the current behavior that {@link Face3D#tag}
  * values remain empty after the mesh has been generated.
  *
  * <p>Uninitialized face tags are considered part of the creator's
  * characterization footprint and do not imply incorrect behavior.
  * Tags may be intended for use by downstream systems such as labeling,
  * selection, grouping, or export pipelines.
  *
  * <p>Any change causing this test to fail indicates a behavioral change
  * in how metadata is assigned and should be reviewed intentionally.
  */
  @Test
  public void testCreatorDoesNotModifyFaceTags() {
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    String expected = "";

    for (Face3D face : mesh.getFaces()) {
      String actual = face.getTag();
      assertEquals(expected, actual);
    }
  }
  
 /**
  * Captures the current output of the mesh creator as an OBJ-based
  * reference footprint.
  *
  * <p>This method is intentionally disabled and must be run manually.
  * It is used to (re)generate the golden reference mesh after deliberate
  * and reviewed changes.
  *
  * <p>The exported mesh is expected to be deterministic. Vertex positions
  * are rounded to four decimal places during export to ensure stability
  * across platforms and JVM implementations.
  */
  @Test
  @Disabled("Used to (re)generate the reference mesh footprint")
  public void captureReferenceMesh() throws Exception {
    Mesh3D mesh = new RhombicosidodecahedronCreator().create();
    Mesh3D output = new Mesh3D();
    
    output.addFaces(mesh.getFaces());
    
    for (Vector3f v : mesh.getVertices()) {
    	v.roundLocalDecimalPlaces(4);
    	output.add(v);
    }
    
    File file = new File("./src/test/java/resources/characterization/RhombicosidodecahedronCreator.obj");
    
    if (file.exists()) {
      throw new IllegalStateException(
        "Reference mesh already exists. Delete it explicitly to re-capture.");
    }
    
    String objName = "RhombicosidodecahedronCreator";
    SimpleObjectWriter objectWriter = new SimpleObjectWriter(file);
    objectWriter.write(output, objName);
    objectWriter.close();
  }
  
 /**
  * Validates that the generated mesh matches the predefined reference mesh.
  *
  * <p>The reference mesh is loaded from an OBJ file located in the characterization resources
  * directory. The mesh produced by {@link #captureReferenceMesh()} is compared against this
  * reference on a per-vertex and per-face basis.
  *
  * <h3>Validation Scope</h3>
  *
  * <ul>
  *   <li><b>Vertex Position Equality:</b> Each generated vertex is compared against the
  *       corresponding reference vertex using a floating-point tolerance of 1e-4.
  *   <li><b>Face Index Equality:</b> Each face's vertex indices must match the reference ordering
  *       exactly.
  * </ul>
  *
  * <h3>Assumptions</h3>
  *
  * <ul>
  *   <li>Vertex ordering is deterministic and stable.
  *   <li>Face ordering and index winding are consistent with the reference file.
  * </ul>
  *
  * <h3>Purpose</h3>
  *
  * <p>This test acts as a characterization test, ensuring that future modifications to the mesh
  * generation logic do not introduce unintended geometric or topological changes.
  */
  @Test
  public void testMatchesReferenceMesh() {
    File file = new File("./src/test/java/resources/characterization/RhombicosidodecahedronCreator.obj");
    SimpleObjectReader reader = new SimpleObjectReader();
    Mesh3D referenceMesh = reader.read(file);
    Mesh3D actual = new RhombicosidodecahedronCreator().create();

    for (int i = 0; i < actual.getVertexCount(); i++) {
      Vector3f actualV = actual.getVertexAt(i);
      Vector3f referenceV = referenceMesh.getVertexAt(i);

      assertEquals(referenceV.x, actualV.x, 0.0001f);
      assertEquals(referenceV.y, actualV.y, 0.0001f);
      assertEquals(referenceV.z, actualV.z, 0.0001f);
    }

    for (int i = 0; i < actual.getFaceCount(); i++) {
      Face3D actualF = actual.getFaceAt(i);
      Face3D referenceF = referenceMesh.getFaceAt(i);
      for (int j = 0; j < actualF.getVertexCount(); j++) {
        assertEquals(referenceF.getIndexAt(j), actualF.getIndexAt(j));
      }
    }
  }
}
