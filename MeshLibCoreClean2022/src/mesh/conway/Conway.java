package mesh.conway;

import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class Conway {

    public IMeshModifier getMeshModifier(char c) {

	switch (c) {
	case 'd':
	    return new ConwayDualModifier();
	case 'z':
	    return new ConwayZipModifier();
	case 'k':
	    return new ConwayKisModifier();
	case 'a':
	    return new ConwayAmboModifier();
	case 'j':
	    return new ConwayJoinModifier();
	case 'n':
	    return new ConwayNeedleModifier();
	case 't':
	    return new ConwayTruncateModifier();
	case 'o':
	    return new ConwayOrthoModifier();
	case 'e':
	    return new ConwayExpandModifier();
	case 'm':
	    return new ConwayMetaModifier();
	case 'b':
	    return new ConwayBevelModifier();
	default:
	    break;
	}

	return new IMeshModifier() {

	    @Override
	    public Mesh3D modify(Mesh3D mesh) {
		return mesh;
	    }
	};
    }

    public void create(Mesh3D seed, String ops) {
	for (int i = ops.length() - 1; i >= 0; i--) {
	    char c = ops.charAt(i);
	    IMeshModifier modifier = getMeshModifier(c);
	    modifier.modify(seed);
	}
    }

}
