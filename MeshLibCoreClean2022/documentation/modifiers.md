#Modifiers

## Planar Vertex Center Modifier

**Purpose:**

The `PlanarVertexCenterModifier` is a mesh modification tool that
subdivides each face of a 3D mesh into multiple triangles. It does this by
adding a new vertex at the center of each face and connecting it to the
existing vertices.

**How to Use**

**Create an Instance:**

```java
PlanarVertexCenterModifier modifier = new PlanarVertexCenterModifier();
```
**Modify a Mesh:**
You can modify either the entire mesh or specific faces:

Modifying the Entire Mesh:

```java
Mesh3D modifiedMesh = modifier.modify(mesh);
```

Modifying Specific Faces:

```java
// Select the faces you want to modify
List<Face3D> facesToSubdivide = ...;
Mesh3D modifiedMesh = modifier.modify(mesh, facesToSubdivide);
```

**How it works:**

1. Face Center Calculation: For each selected face, the modifier calculates its 
center point. Vertex Addition: A new vertex is added to the mesh at the
calculated center point.
2. Face Subdivision: The original face is subdivided into triangles by
connecting the new center vertex to each of its original vertices.
3. Face Removal: The original face is removed from the mesh.

**Key Points:**

- Subdivision Level: The level of subdivision can be controlled by applying
the modifier multiple times.
- Mesh Complexity: Each subdivision step increases the number of vertices
and faces in the mesh.
- Geometric Accuracy: The subdivision process preserves the
overall shape of the mesh.

**Potential Use Cases:**
- Creating smoother surfaces for 3D models.
- Increasing the level of detail of a mesh.
- Generating more complex shapes through iterative subdivision.

## NoiseModifier

**Purpose:**

The `NoiseModifier` class is designed to add random noise to a 3D mesh.
This is a common technique used to introduce surface irregularities and a more
organic appearance to 3D models.

**How it works:**

1. **Calculates Vertex Normals:** Determines the normal vector for each vertex
in the mesh.
2. **Generates Random Noise:** Generates a random value within a
specified range.
3. **Displaces Vertices:** Displaces each vertex along its normal vector by
the generated random value.

**Parameters:**

- **minimum:** The minimum value for the random noise.
- **maximum:** The maximum value for the random noise.

**Usage:**

```java
Mesh3D mesh = new IcoSphereCreator().create();
NoiseModifier noiseModifier = new NoiseModifier(0.1f, 0.2f);
noiseModifier.modify(mesh);
```

**Using setters:**

```java
Mesh3D mesh = new CubeCreator().create();
NoiseModifier noiseModifier = new NoiseModifier();
noiseModifier.setMinimum(0.1f);
noiseModifier.setMaximum(0.2f);
noiseModifier.modify(mesh);
```

#Catmull-Clark

**Example**

```java
Mesh3D mesh = new CubeCreator().create();
CatmullClarkModifier modifier = new CatmullClarkModifier();
modifier.setSubdivisions(2);
modifier.modify(mesh);

```