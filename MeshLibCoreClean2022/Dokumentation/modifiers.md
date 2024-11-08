### RemoveDoubleVerticesModifier
This modifier removes all diplicated vertices from the 
the provided mesh. Vertices are compared with 
a zero tolerance. No threshhold option is provided.

```java
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.RemoveDoubleVerticesModifier;

Mesh3D cube0 = new CubeCreator().create();
Mesh3D cube1 = new CubeCreator().create();
cube0.add
```

### SpherifyModifier
This modifier projects all vertices onto the surface of a 
given sphere. In the following example the sphere is defined 
by it`s radius. The center of the sphere is at (0, 0, 0).

```java
import mesh.Mesh3D;
import mesh.creator.primitives.SegmentedCubeCreator;
import mesh.modifier.SpherifyModifier;

float radius = 4.0f;
Mesh3D cube = new SegmentedCubeCreator().create();
SpherifyModifier modifier = new SpherifyModifier();
modifier.modify(cube);
```

### FitToAABModifier
Scales the mesh so that it fits into the given
bounding box. The propotions of the mesh are kept.
Also the modifier does not adapt the location of
the bounding box to the mesh. The following example
illustrates how to use the modifier.

```java
import mesh.Mesh3D;
import mesh.creator.primitives.IcoSphere;
import mesh.modifier.FitToAABModifier;

float width = 4.0f;
float height = 3.0f;
float depth = 1.0f;
Mesh3D sphere = new IcoSphereCreator().create();
FitToAABModifier modifier = new FitToAABModifier(width, height, depth);
modifier.modify(sphere);
```

### NoiseModifier

This modifier moves all vertices along their corresponding 
vertex normals by a random amount. Minimum and maximum are defining
the range of rhe random amount. For each vertex an individual
random value is applied.

```java
import mesh.Mesh3D;
import mesh.creator.IcoSphereCreator;
import mesh.modifier.NoiseModifier;

Mesh3D sphere = new IcoSphereCreator().create();
NoiseModifier modifier = new NoiseModifier();
setMinimum(1.0f);
setMaximum(1.5f);
modifier.modify(sphere);
```

### ExtrudeModifier

The following example shows how to extrude all faces of a mesh. The faces are extruded along 
their own individual normals. Newly created geometry is kept
connected with the original vertices.

```java
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.ExtrudeModifier;

Mesh3D cube = new CubeCreator().create();
ExtrudeModifier modifier = new ExtrudeModifier();
modifier.setAmount(3.5f);
modofier.setScale(1);
modifier.modify(cube);
```

### CenterAtModifier

The following example shows how to center the mesh
at a specified location. In other words this modifier 
sets the center of the mesh to the specified location.

```java
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.CenterAtModifier;

Mesh3D cube = new CubeCreator().create();
CenterAtModifier modifier = new CenterAtModifier();
modifier.setCenter(new Vector3f(-4,0,8));
modifier.modify(cube);
```

### FlipFacesModifier

The following example shows how to reverse the diretion
of all face normals. The modifier does not change the
orientation of the face normals nor it solves any winding order issues.
It simply reverses the vertex index entries of the face.

```java
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.FlipFacesModifier;

Mesh3D plane = new PlaneCreator().create();
FlipFacesModifier modifier = new FlipFacesModifier();
modifier.modify(plane);
```

### ScaleModifier

This modifier scales the mesh.

```java
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.ScaleModifier;

Mesh3D cube = new CubeCreator().create();
ScaleModifier modifier = new ScaleModifier();
modifier.setScaleX(2);
modifier.setScaleY(10);
modifier.setScaleZ(4);
modifier.modify(cube);
```
