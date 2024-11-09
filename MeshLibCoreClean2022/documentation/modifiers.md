#Modifiers

## NoiseModifier

**Purpose:**

The `NoiseModifier` class is designed to add random noise to a 3D mesh. This is a common technique used to introduce surface irregularities and a more organic appearance to 3D models.

**How it works:**

1. **Calculates Vertex Normals:** Determines the normal vector for each vertex in the mesh.
2. **Generates Random Noise:** Generates a random value within a specified range.
3. **Displaces Vertices:** Displaces each vertex along its normal vector by the generated random value.

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