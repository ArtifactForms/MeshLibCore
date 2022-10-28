package mesh.creator.platonic;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class PlatonicSolidCreator implements IMeshCreator {

	private PlatonicSolid type;

	public PlatonicSolidCreator(PlatonicSolid type) {
		this.type = type;
	}
	
	protected IMeshCreator getCreator() {
		switch (type) {
		case TETRAHEDRON:
			return new TetrahedronCreator();
		case HEXAHEDRON:
			return new HexahedronCreator();
		case OCTAHEDRON:
			return new OctahedronCreator();
		case ICOSAHEDRON:
			return new IcosahedronCreator();
		case DODECAHEDRON:
			return new DodecahedronCreator();
		default:
			return null;
		}
	}

	@Override
	public Mesh3D create() {
		IMeshCreator creator = getCreator();
		return creator.create();
	}

	public PlatonicSolid getType() {
		return type;
	}

	public void setType(PlatonicSolid type) {
		this.type = type;
	}
	
}
