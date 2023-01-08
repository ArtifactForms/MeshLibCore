package mesh.creator.archimedian;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class ArchimedianSolidCreator implements IMeshCreator {

	private ArchimedianSolid type;

	public ArchimedianSolidCreator(ArchimedianSolid type) {
		this.type = type;
	}

	protected IMeshCreator getCreator() {
		switch (type) {
		case ICOSIDODECAHEDRON:
			return new IcosidodecahedronCreator();
		case TRUNCATED_CUBOCTAHEDRON:
			return new TruncatedCuboctahedronCreator();
		case TRUNCATED_ICOSIDODECAHEDRON:
			return new TruncatedIcosidodecahedronCreator();
		case CUBOCTAHEDRON:
			return new CuboctahedronCreator();
		case RHOMBICUBOCTAHEDRON:
			return new RhombicuboctahedronCreator();
		case SNUB_CUBE:
			return new SnubCubeCreator();
		case RHOMBISOSIDODECAHEDRON:
			return new RhombicosidodecahedronCreator();
		case SNUB_DODECAHEDRON:
			return new SnubDodecahedronCreator();
		case TRUNCATED_TETRAHEDRON:
			return new TruncatedTetrahedronCreator();
		case TRUNCATED_OCTAHEDRON:
			return new TruncatedOctahedronCreator();
		case TRUNCATED_CUBE:
			return new TruncatedCubeCreator();
		case TRUNCATED_ICOSAHEDRON:
			return new TruncatedIcosahedronCreator();
		case TRUNCATED_DODECAHEDRON:
			return new TruncatedDodecahedronCreator();
		default:
			return null;
		}
	}

	@Override
	public Mesh3D create() {
		IMeshCreator creator = getCreator();
		return creator.create();
	}

	public ArchimedianSolid getType() {
		return type;
	}

	public void setType(ArchimedianSolid type) {
		this.type = type;
	}

}
