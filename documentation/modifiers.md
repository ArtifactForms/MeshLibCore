# Mesh Modifiers

## Basic Modifiers
* **BendModifier:** Bends the mesh along a specified axis.
* **BevelEdgesModifier:** Creates a bevel along the edges of the mesh.
* **BevelFacesModifier:** Creates a bevel around the faces of the mesh.
* **BevelVerticesModifier:** Creates a bevel around the vertices of the mesh.
* **CenterAtModifier:** Centers the mesh at a specific point.
* **CrocodileModifier:** 
* **ExtrudeModifier:** Extrudes the faces of the mesh along their normals.
* **FitToAABBModifier:** Scales and **/translates???/** the mesh to fit within an axis-aligned bounding box.
* **FlipFacesModifier:** Flips the orientation of the faces of the mesh.
* **HolesModifier:** Creates holes in the mesh.
* **InsetModifier:** Insets the faces of the mesh inward.
* **NoiseModifier:** Adds noise to the vertex positions of the mesh. **Normals!**
* **PushPullModifier:** Pushes or pulls the faces of the mesh along their normals.
* **RemoveDoubleVerticesModifier:** Removes duplicate vertices from the mesh.
* **RotateXModifier:** Rotates the mesh around the X-axis.
* **RotateYModifier:** Rotates the mesh around the Y-axis.
* **RotateZModifier:** Rotates the mesh around the Z-axis.
* **ScaleModifier:** Scales the mesh uniformly or non-uniformly.
* **SmoothModifier:** Smoothes the mesh by averaging vertex positions. **???**
* **SolidifyModifier:** Adds thickness to the faces of the mesh.
* **SpherifyModifier:** Spherifies the mesh.
* **TranslateModifier:** Translates the mesh.
* **UpdateFaceNormalsModifier** Updates the face normals of the mesh.
* **WireframeModifier:** Converts the mesh to a wireframe representation. **???**

## Subdivision Modifiers
* **CatmullClarkModifier:** Subdivides the mesh using the Catmull-Clark subdivision scheme.
* **DooSabinModifier:** Subdivides the mesh using the Doo-Sabin subdivision scheme.
* **LinearSubdivisionModifier:** Subdivides the mesh using a linear subdivision scheme.
* **PlanarMidEdgeCenterModifier:** Subdivides the mesh by splitting edges and connecting their midpoints.
* **PlanarMidEdgeModifier:** Subdivides the mesh by splitting edges and connecting their midpoints to the face centroids.
* **PlanarVertexCenterModifier:** Subdivides the mesh by connecting vertices to face centroids.
* **PlanarVertexMidEdgeCenterModifier:** Subdivides the mesh by connecting vertices, edge midpoints, and face centroids.
* **PokeFacesModifier:** Adds a vertex to the center of each face.
* **QuadsToTrianglesModifier:** Converts quad faces to triangles.

## Bend Modifier

**Purpose:**

The Bend Modifier is a tool designed to deform a 3D mesh by bending it along the X-axis. It's particularly useful for creating curved shapes or simulating bending effects.

**How it works:**

**Bending Factor:**

The ```factor``` parameter controls the intensity of the bend. A higher factor results in a more pronounced curve.

**Vertex Deformation:**

* For each vertex in the mesh:
* The X-coordinate of the vertex is used to calculate a bending angle.
* The Y and Z coordinates are then modified based on this angle and the ```factor``` to achieve the desired bending effect.

**Using the Bend Modifier:**

1. **Create a Mesh:** Start with a basic 3D mesh, such as a grid, cube, or sphere.
2. **Create the Modifier:** Create an instance of the BendModifier class, specifying the desired bending factor.
3. **Modify the Mesh:** Apply the modify method of the BendModifier to the mesh.

**Example:**

```java
GridCreator creator = new GridCreator();
creator.setSubdivisionsX(10);
creator.setSubdivisionsZ(10);
creator.setTileSizeX(1);
creator.setTileSizeZ(1);

Mesh3D mesh = creator.create(); // Create a grid mesh
BendModifier modifier = new BendModifier(0.5f); // Create a BendModifier with a factor of 0.5

mesh.apply(modifier); // Apply the modifier to the mesh
```

**Additional Considerations:**

* **Extreme Bending:** For very high factor values, the mesh may become distorted or self-intersecting.
* **Mesh Topology:** The effectiveness of the modifier can be influenced by the mesh topology. For complex meshes, additional considerations may be necessary.
* **Combining with Other Modifiers:** The Bend Modifier can be combined with other modifiers to create more complex deformations.

By understanding the basic principles and parameters of the Bend Modifier, you can effectively use it to create a wide range of 3D shapes and effects.

## Push-Pull Modifier

**Purpose:**

The Push-Pull Modifier is a versatile tool for deforming 3D meshes. It allows you to push or pull vertices towards or away 
from a specified center point, creating a variety of effects like bulging, indenting, or extruding parts of the mesh.

**How it works:**

1. Center Point: The modifier uses a defined center point as a reference for the deformation.
2. Distance: The ```distance``` parameter controls the magnitude of the push or pull. A positive value pushes vertices away from the center, while a negative value pulls them towards the center.
3. Vertex Deformation: For each vertex in the mesh:
* The ```distance``` between the vertex and the center point is calculated.
* The vertex is moved along the vector connecting it to the center point, by the specified distance.

**Using the Push-Pull Modifier:**

1. **Create a Mesh:** Start with a basic 3D mesh, such as a plane, cube, or sphere.
2. **Apply the Modifier:** Create an instance of the PushPullModifier class, specifying the desired distance and center point.
3. **Modify the Mesh:** Apply the modify method of the PushPullModifier to the mesh.

**Example:**

```java
// TODO
```

**Additional Considerations:**

* **Mesh Topology:** The effectiveness of the modifier can be influenced by the mesh topology. For complex meshes, additional considerations may be necessary.
* **Combining with Other Modifiers:** The Push-Pull Modifier can be combined with other modifiers to create more complex deformations.

By understanding the basic principles and parameters of the Push-Pull Modifier, you can effectively use it to create a wide range of 3D shapes and effects.

