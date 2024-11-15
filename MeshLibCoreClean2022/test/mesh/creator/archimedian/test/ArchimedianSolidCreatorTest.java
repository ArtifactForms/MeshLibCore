package mesh.creator.archimedian.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.archimedian.ArchimedianSolid;
import mesh.creator.archimedian.ArchimedianSolidCreator;
import mesh.creator.archimedian.CuboctahedronCreator;
import mesh.creator.archimedian.IcosidodecahedronCreator;
import mesh.creator.archimedian.RhombicosidodecahedronCreator;
import mesh.creator.archimedian.RhombicuboctahedronCreator;
import mesh.creator.archimedian.SnubCubeCreator;
import mesh.creator.archimedian.SnubDodecahedronCreator;
import mesh.creator.archimedian.TruncatedCubeCreator;
import mesh.creator.archimedian.TruncatedCuboctahedronCreator;
import mesh.creator.archimedian.TruncatedDodecahedronCreator;
import mesh.creator.archimedian.TruncatedIcosahedronCreator;
import mesh.creator.archimedian.TruncatedIcosidodecahedronCreator;
import mesh.creator.archimedian.TruncatedOctahedronCreator;
import mesh.creator.archimedian.TruncatedTetrahedronCreator;
import util.MeshCompare;

public class ArchimedianSolidCreatorTest {

    @Test
    public void implementsMeshCreatorInterface() {
        assertTrue(new ArchimedianSolidCreator(null) instanceof IMeshCreator);
    }

    @Test
    public void returnsNoneNullMeshConstructedWithType() {
        ArchimedianSolid[] types = ArchimedianSolid.values();
        for (ArchimedianSolid type : types) {
            ArchimedianSolidCreator creator = new ArchimedianSolidCreator(type);
            Mesh3D mesh = creator.create();
            assertNotNull(mesh);
        }
    }

    @Test
    public void getSetType() {
        ArchimedianSolidCreator creator = new ArchimedianSolidCreator(null);
        ArchimedianSolid[] types = ArchimedianSolid.values();
        for (ArchimedianSolid type : types) {
            creator.setType(type);
            assertEquals(type, creator.getType());
        }
    }

    @Test
    public void verticesEqualIcosidodecahedronCase() {
        Mesh3D expected = new IcosidodecahedronCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.ICOSIDODECAHEDRON).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualTruncatedCuboctahedronCase() {
        Mesh3D expected = new TruncatedCuboctahedronCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.TRUNCATED_CUBOCTAHEDRON).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualTruncatedIcosidodecahedronCase() {
        Mesh3D expected = new TruncatedIcosidodecahedronCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.TRUNCATED_ICOSIDODECAHEDRON).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualCubocahedronCase() {
        Mesh3D expected = new CuboctahedronCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.CUBOCTAHEDRON).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualRhombicuboctahedronCase() {
        Mesh3D expected = new RhombicuboctahedronCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.RHOMBICUBOCTAHEDRON).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualSnubCubeCase() {
        Mesh3D expected = new SnubCubeCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(ArchimedianSolid.SNUB_CUBE)
                .create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualRhombicosadodecahedronCase() {
        Mesh3D expected = new RhombicosidodecahedronCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.RHOMBISOSIDODECAHEDRON).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualSnubDodecahedronCase() {
        Mesh3D expected = new SnubDodecahedronCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.SNUB_DODECAHEDRON).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualTruncatedTetrahedronCase() {
        Mesh3D expected = new TruncatedTetrahedronCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.TRUNCATED_TETRAHEDRON).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualTruncatedOctahedronCase() {
        Mesh3D expected = new TruncatedOctahedronCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.TRUNCATED_OCTAHEDRON).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualTruncatedCubeCase() {
        Mesh3D expected = new TruncatedCubeCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.TRUNCATED_CUBE).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualTruncatedIcosahedronCase() {
        Mesh3D expected = new TruncatedIcosahedronCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.TRUNCATED_ICOSAHEDRON).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

    @Test
    public void verticesEqualTruncatedDodecahedronCase() {
        Mesh3D expected = new TruncatedDodecahedronCreator().create();
        Mesh3D actual = new ArchimedianSolidCreator(
                ArchimedianSolid.TRUNCATED_DODECAHEDRON).create();
        MeshCompare.assertVerticesAreEqual(expected, actual, 0.000001f);
    }

}
