package mesh.creator.assets;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CylinderCreator;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.FitToAABBModifier;
import mesh.selection.FaceSelection;
import mesh.util.Mesh3DUtil;

public class SciFiFloorCreator implements IMeshCreator {

    private float latticeHeight;

    private float width;

    private float height;

    private float depth;

    private float wallHeight;

    private Mesh3D mesh;

    public SciFiFloorCreator() {
        latticeHeight = 0.1f;
        width = 4;
        depth = 4;
        height = 4;
    }

    @Override
    public Mesh3D create() {
        initializeMesh();

//		LatticeCreator creator = new LatticeCreator();
//		creator.setHeight(latticeHeight);
//		creator.setOpeningPercent(0.9f);
//		creator.setSubdivisionsX(10);
//		creator.setSubdivisionsZ(10);
//		creator.setTileSizeX(0.5f);
//		creator.setTileSizeZ(0.5f);
//		Mesh3D lattice = creator.create();
//		mesh.append(lattice);

//		FlangePipeCreator creator = new FlangePipeCreator();
//		creator.setFlangeOuterRadius(0.5f);
//		creator.setPipeRadius(0.3f);
//		creator.setPipeSegmentLength(depth);
//		Mesh3D pipe = creator.create();
//		pipe.rotateY(Mathf.HALF_PI);
//		mesh.append(pipe);

        createBaseCylinder();
        createFloor();
//		snapToGround();

        return mesh;
    }

    private void snapToGround() {
        mesh.translateY(-height / 2f);
    }

    private void createFloor() {
        Mesh3D plane = new PlaneCreator(0.5f).create();
        plane.scale(width, 1, depth);
        plane.translateY(wallHeight / 2f);
        mesh.append(plane);
    }

    private void createBaseCylinder() {
        CylinderCreator creator = new CylinderCreator();
        creator.setBottomCapFillType(FillType.NOTHING);
        creator.setTopCapFillType(FillType.NOTHING);
        creator.setBottomRadius(0.5f);
        creator.setTopRadius(0.5f);
        creator.setHeight(1);
        creator.setVertices(8);
        Mesh3D cylinder = creator.create();
        cylinder.rotateY(Mathf.TWO_PI / 16f);
        cylinder.rotateX(Mathf.HALF_PI);
        new FitToAABBModifier(width, height, depth).modify(cylinder);
        Mesh3DUtil.flipDirection(cylinder);
        mesh.append(cylinder);
        calculateWallHeight();
    }

    private void calculateWallHeight() {
        float minY = 0;
        float maxY = 0;
        FaceSelection selection = new FaceSelection(mesh);
        selection.selectSimilarNormal(new Vector3f(-1, 0, 0), 0);
        for (Face3D face : selection.getFaces()) {
            for (int i = 0; i < face.indices.length; i++) {
                Vector3f v = mesh.getVertexAt(face.indices[i]);
                minY = v.getY() < minY ? v.getY() : minY;
                maxY = v.getY() > maxY ? v.getY() : maxY;
            }
        }
        wallHeight = Mathf.abs(maxY - minY);
    }

    private void initializeMesh() {
        mesh = new Mesh3D();
    }

}
