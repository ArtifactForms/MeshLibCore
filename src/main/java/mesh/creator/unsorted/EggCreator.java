package mesh.creator.unsorted;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.util.Mesh3DUtil;

public class EggCreator implements IMeshCreator {

    private float size;

    private float topScale;

    private int subdivisions;

    private Mesh3D mesh;

    public EggCreator() {
        size = 1f;
        topScale = 0.5f;
        subdivisions = 3;
    }

    private void createCube() {
        mesh = new CubeCreator(size).create();
    }

    private void scaleCubeTopFace() {
        Mesh3DUtil.scaleFaceAt(mesh, 0, topScale);
    }

    private void subdivideCube() {
        new CatmullClarkModifier(subdivisions).modify(mesh);
    }

    @Override
    public Mesh3D create() {
        createCube();
        scaleCubeTopFace();
        subdivideCube();
        return mesh;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getTopScale() {
        return topScale;
    }

    public void setTopScale(float topScale) {
        this.topScale = topScale;
    }

    public int getSubdivisions() {
        return subdivisions;
    }

    public void setSubdivisions(int subdivisions) {
        this.subdivisions = subdivisions;
    }

}
