package mesh.modifier;

import java.util.Collection;

import mesh.Face3D;
import mesh.Mesh3D;

public class HolesModifier implements IMeshModifier, FaceModifier {

	private float holePercentage;

	public HolesModifier() {
		this.holePercentage = 0.5f;
	}

	public HolesModifier(float holePercentage) {
		this.holePercentage = holePercentage;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		return modify(mesh, mesh.getFaces());
	}

	/**
	 * Modifies the given mesh to create holes by insetting the specified faces to
	 * a specified percentage of their original size.
	 * 
	 * <pre>
	 * The behavior is defined as follows:
	 * - A `holePercentage` of 0.0 leaves the face unchanged (no hole).
	 * - A `holePercentage` of 1.0 removes the face entirely
	 *      (hole consumes the full face area).
	 * - Values between 0.0 and 1.0 create proportionally smaller holes,
	 *      with the face scaled to (1 - holePercentage).
	 * </pre>
	 *
	 * @param mesh  The mesh to modify.
	 * @param faces The faces to be inset.
	 * @return The modified mesh.
	 * @throws IllegalArgumentException if the mesh or faces are null.
	 */
	@Override
	public Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		if (faces == null) {
			throw new IllegalArgumentException("Faces cannot be null.");
		}
		if (mesh.faces.isEmpty() || holePercentage == 0) {
			return mesh;
		}
		createExtrudeModifier().modify(mesh, faces);
		return mesh;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh, Face3D face) {
		if (mesh == null) {
			throw new IllegalArgumentException("Mesh cannot be null.");
		}
		if (face == null) {
			throw new IllegalArgumentException("Face cannot be null.");
		}
		createExtrudeModifier().modify(mesh, face);
		return mesh;
	}

	private ExtrudeModifier createExtrudeModifier() {
		ExtrudeModifier modifier = new ExtrudeModifier();
		modifier.setScale(holePercentage);
		modifier.setAmount(0);
		modifier.setRemoveFaces(true);
		return modifier;
	}

	public float getHolePercentage() {
		return holePercentage;
	}

	public void setHolePercentage(float holePercentage) {
		if (holePercentage < 0 || holePercentage > 1) {
			throw new IllegalArgumentException(
			    "Hole percentage must be between 0 and 1.");
		}
		this.holePercentage = holePercentage;
	}

}
