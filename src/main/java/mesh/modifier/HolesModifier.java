	package mesh.modifier;
	
	import java.util.ArrayList;
	import java.util.Collection;
	
	import mesh.Face3D;
	import mesh.Mesh3D;
	
	/**
	 * The {@code HolesModifier} class modifies a 3D mesh by creating holes in its
	 * faces. This is achieved by insetting specified faces to a percentage of their
	 * original size, effectively leaving holes where the faces used to be.
	 * 
	 * <pre>
	 *  
	 * The behavior is defined as follows:
	 * - A `holePercentage` of 0.0 leaves the face unchanged (no hole).
	 * - A `holePercentage` of 1.0 removes the face entirely
	 *      (hole consumes the full face area).
	 * - Values between 0.0 and 1.0 create proportionally smaller holes,
	 *      with the face scaled to (1 - holePercentage).
	 *      
	 * Key features:
	 * - Adjusts faces to create holes based on a specified percentage.
	 * - Can work with the entire mesh, specific collections of faces, or a 
	 *   single face.
	 * </pre>
	 */
	public class HolesModifier implements IMeshModifier, FaceModifier {
	
		/**
		 * The percentage of the face's size to retain after creating the hole. A
		 * value of 0.0 leaves the face unchanged, while hole consumes the full face
		 * area with a value of 1.0.
		 */
		private float holePercentage;
	
		/**
		 * Default constructor initializes the modifier with a hole percentage of 0.5
		 * (50%).
		 */
		public HolesModifier() {
			this.holePercentage = 0.5f;
		}
	
		/**
		 * Constructs the {@code HolesModifier} with a specified hole percentage.
		 * 
		 * @param holePercentage The percentage of the face's size to retain after
		 *                       creating the hole. Must be between 0.0 and 1.0,
		 *                       inclusive.
		 * @throws IllegalArgumentException if the percentage is outside the valid
		 *                                  range.
		 */
		public HolesModifier(float holePercentage) {
			setHolePercentage(holePercentage);
		}
	
		/**
		 * Modifies the given mesh to create holes by insetting it's faces to a
		 * specified percentage of their original size.
		 * 
		 * @see #HolesModifier()
		 * @param mesh The mesh to modify.
		 * @return The modified mesh.
		 * @throws IllegalArgumentException if the mesh is null.
		 */
		@Override
		public Mesh3D modify(Mesh3D mesh) {
			validateMesh(mesh);
			return modify(mesh, mesh.getFaces());
		}
	
		/**
		 * Modifies the given mesh to create holes by insetting the specified faces to
		 * a specified percentage of their original size.
		 * 
		 * @see #HolesModifier()
		 * @param mesh  The mesh to modify.
		 * @param faces The faces to be inset.
		 * @return The modified mesh.
		 * @throws IllegalArgumentException if the mesh or faces are null.
		 */
		@Override
		public Mesh3D modify(Mesh3D mesh, Collection<Face3D> faces) {
			validateMesh(mesh);
			validateFaces(faces);
			if (canExitEarly(mesh)) {
				return mesh;
			}
			Collection<Face3D> facesToModify = faces;
			if (faces == mesh.faces) {
				facesToModify = new ArrayList<Face3D>(mesh.faces);
			}
			createExtrudeModifier().modify(mesh, facesToModify);
			return mesh;
		}
	
		/**
		 * Modifies the given mesh to create holes by insetting the specified face to
		 * a specified percentage of it's original size.
		 * 
		 * @see #HolesModifier()
		 * @param mesh The mesh to modify.
		 * @param face The face to be inset.
		 * @return The modified mesh.
		 * @throws IllegalArgumentException if the mesh or face are null.
		 */
		@Override
		public Mesh3D modify(Mesh3D mesh, Face3D face) {
			validateMesh(mesh);
			validateFace(face);
			if (canExitEarly(mesh)) {
				return mesh;
			}
			createExtrudeModifier().modify(mesh, face);
			return mesh;
		}
	
		/**
		 * Creates an instance of {@code ExtrudeModifier} configured to create holes.
		 * The modifier will scale faces based on the specified hole percentage, with
		 * no extrusion applied, and will remove the original faces.
		 *
		 * @return A configured {@code ExtrudeModifier} instance.
		 */
		private ExtrudeModifier createExtrudeModifier() {
			ExtrudeModifier modifier = new ExtrudeModifier();
			modifier.setScale(holePercentage);
			modifier.setAmount(0);
			modifier.setRemoveFaces(true);
			return modifier;
		}
	
		/**
		 * Determines whether the modification can exit early based on the current
		 * mesh state and hole percentage.
		 *
		 * @param mesh The mesh to evaluate for early exit.
		 * @return {@code true} if the modification can exit early (e.g., the mesh has
		 *         no faces or the hole percentage is zero), {@code false} otherwise.
		 */
		private boolean canExitEarly(Mesh3D mesh) {
			return mesh.faces.isEmpty() || holePercentage == 0;
		}
	
		/**
		 * Validates that the face is not null.
		 *
		 * @param face the face to validate.
		 * @throws IllegalArgumentException if the face is null.
		 */
		private void validateFace(Face3D face) {
			if (face == null) {
				throw new IllegalArgumentException("Face cannot be null.");
			}
		}
	
		/**
		 * Validates that the mesh is not null.
		 *
		 * @param mesh the mesh to validate.
		 * @throws IllegalArgumentException if the mesh is null.
		 */
		private void validateMesh(Mesh3D mesh) {
			if (mesh == null) {
				throw new IllegalArgumentException("Mesh cannot be null.");
			}
		}
	
		/**
		 * Validates that the faces collection is not null.
		 *
		 * @param faces the collection of faces to validate.
		 * @throws IllegalArgumentException if the collection is null.
		 */
		private void validateFaces(Collection<Face3D> faces) {
			if (faces == null) {
				throw new IllegalArgumentException("Faces cannot be null.");
			}
		}
	
		/**
		 * Gets the current percentage of the face's size retained after creating the
		 * hole.
		 * 
		 * @return The hole percentage.
		 */
		public float getHolePercentage() {
			return holePercentage;
		}
	
		/**
		 * Sets the percentage of the face's size to retain after creating the hole.
		 * 
		 * @param holePercentage The new hole percentage to set. Must be between 0.0
		 *                       and 1.0, inclusive.
		 * @throws IllegalArgumentException if the percentage is outside the valid
		 *                                  range.
		 */
		public void setHolePercentage(float holePercentage) {
			if (holePercentage < 0 || holePercentage > 1) {
				throw new IllegalArgumentException(
				    "Hole percentage must be between 0 and 1 inclusive.");
			}
			this.holePercentage = holePercentage;
		}
	
	}
