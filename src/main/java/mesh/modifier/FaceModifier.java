package mesh.modifier;

import java.util.Collection;

import mesh.Face3D;
import mesh.Mesh3D;

public interface FaceModifier {
	
	Mesh3D modify(Mesh3D mesh, Face3D face);
	
	Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces);

}
