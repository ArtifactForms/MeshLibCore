# MeshLibrary

## Core elements
* Mesh3D
* Face3D
* Edge3D
* Creators
* Modifiers

## Creators
The library provides a variety of mesh 'Creators' to construct various shapes the convenient way.
More precisely the 'Factory Pattern' was applied for this purpose.
Each creator derives from the 'IMeshCreator' interface. The following code example shows the interface for all mesh creators.

```java
package mesh.creator;

import mesh.Mesh3D;

public interface IMeshCreator {

	public Mesh3D create();
	
}
```
In the meantime the library contains 100+ different mesh creators divided in various categories.

## Coordinate System
The library is build up on a left-handed coordinate system.
The decision was justified by using the 'Processing' rendering pipeline in the first place.
But the core library is highly decoupled from the 'Processing' environment.
So the library could be used independently.

## Planed features
* Convex Hull
* Poisson-Disc sampling
