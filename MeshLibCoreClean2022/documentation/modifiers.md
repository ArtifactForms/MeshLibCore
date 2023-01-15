### Flip face normals

The following example shows how to reverse the diretion
of all face normals. The modifiers does not change the
orientation nor it solves any winding order issues.
It simply reverses the vertex index entries of the face.

```java
import mesh.Mesh3D;
import mesh.creator.primitives.PlaneCreator;
import mesh.modifier.FlipFacesModifier;

Mesh3D plane = new PlaneCreator().create();
FlipFacesModifier modifier = new FlipFacesModifier();
modifier.modify(plane);
```
