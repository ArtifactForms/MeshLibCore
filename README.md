[![Java CI with Maven](https://github.com/ArtifactForms/MeshLibCore/actions/workflows/maven.yml/badge.svg)](https://github.com/ArtifactForms/MeshLibCore/actions/workflows/maven.yml)
[![CodeQL Advanced](https://github.com/ArtifactForms/MeshLibCore/actions/workflows/codeql.yml/badge.svg)](https://github.com/ArtifactForms/MeshLibCore/actions/workflows/codeql.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/5d913b2c5d064bee83780eefee17f400)](https://app.codacy.com/gh/ArtifactForms/MeshLibCore/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Maintainability](https://api.codeclimate.com/v1/badges/c860a89479fdedd6be77/maintainability)](https://codeclimate.com/github/ArtifactForms/MeshLibCore/maintainability)
[![Maven Package](https://github.com/ArtifactForms/MeshLibCore/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/ArtifactForms/MeshLibCore/actions/workflows/maven-publish.yml)

# Artifact Forms

A _JAVA_ library to construct and manipulate geometry in Three-dimensional space.

## Background / Intension

This Java library began as a hobby project in 2015/2016.  I started it to deepen my understanding of creating and manipulating 3D geometry. This built upon knowledge I gained from an earlier internship with product design students. During that time, I was introduced to the programming language Processing.

Processing captivated me from the start. Designed for visual learners, Processing is a great tool to get started with programming. You can learn more at processing.org. While Processing isn't strictly necessary, the library's core functionality is independent of the Processing environment. However, Processing offers a convenient way to visualize constructed meshes through its rendering pipeline, which leverages JAVA, JAVA2D, and OPENGL.

## Status Quo
Currently, my primary focus is on refining the user documentation. As my understanding of code structure and architecture has evolved, I've been actively refactoring the codebase to maintain its cleanliness and organization. Smaller additions are made periodically.

The Processing-specific rendering components are housed in a separate repository, representing another ongoing project.

In essence, this codebase serves as a platform for learning and working with legacy code. I've embraced the challenge of working with existing code, viewing it as an opportunity to explore new testing approaches. Constructing tests around legacy code for refactoring purposes is a fascinating and valuable endeavor, in my opinion, an essential practice.

## Future

There are a lot of corresponding topics out there. So my wishlist of things to learn and implement is unspeakably large.
Some are already listed under 'Planed features'.

## Showcase

The following images are showing the library in action.

![](documentation/images/lib_showcase_1.png)
Subdivision is so beautiful and satisfying too look at.

![](/documentation/images/lib_showcase_2.png)
bend bend bend mesh...

![](documentation/images/lib_showcase_3.png)
Throwing some conway operations on a cube seed.

## Features

Will be added soon...

## Core elements

- Mesh3D
- Face3D
- Edge3D
- [Creators](#creators)
- Modifiers

## Coordinate System

The library is build up on a left-handed coordinate system.
The decision was justified by using the 'Processing' rendering pipeline in the first place.
But the core library is highly decoupled from the 'Processing' environment.
So the library could be used independently.

## Mesh3D

The following example shows how to work with the base mesh class. For this purpose we want to create a simple quad. The quad has four vertices, one for each
corner. To make things a bit more explanatory we compose the quad out of two triangular faces. **Important:** This is just an example to illustrate the base concepts. The library already provides a convenient way to construct primitives and more complex shapes. But we dive into this at a later point. For now
let's keep things simple. But also keep in mind that it might be useful to construct shapes by yourself in some cases.

```
(-1, 0, -1)     (1, 0, -1)
     o--------------o
     |  .           |
     |    .         |
     |      .       |
     |        .     |
     |          .   |
     o--------------o
(-1, 0, 1)      (1, 0, 1)
```

### Mesh3D Object

The base class for all shapes is `mesh.Mesh3D`.

```java
import mesh.Mesh3D;

Mesh3D mesh = new Mesh3D();
```

### Vertex Coordinates

Next we determine the shape's coordinates in Three-Dimensional space. In this case the shape lies flat on the xz plane, so each y-coordinate is 0.0f.

```java
mesh.add(new Vector3f(1, 0, -1));
mesh.add(new Vector3f(1, 0, 1);
mesh.add(new Vector3f(-1, 0, 1);
mesh.add(new Vector3f(-1, 0, -1);
```

Alternatively use `addVertex(x, y, z)`

```java
mesh.addVertex(1, 0, -1);
mesh.addVertex(1, 0, 1);
mesh.addVertex(-1, 0, 1);
mesh.addVertex(-1, 0, -1);
```

### Construct Faces

The added vertices are now at an indexed position within the mesh.

```
     3              0
     o--------------o
     |  .           |
     |    .         |
     |      .       |
     |        .     |
     |          .   |
     o--------------o
     2              1
```

Knowing the index of each vertex makes adding faces a piece of cake. We only have to take care of the winding order. In this case the winding order
is counter-clockwise with all face normals pointing up towards negative y.

```java
mesh.addFace(0, 1, 3);
mesh.addFace(1, 2, 3);
```

![](documentation/images/quad_example.png)

### Modify the mesh

Now we have a mesh constisting of four vertices and two triangular faces. This could be retrieved by using:

```java
int vertexCount = mesh.getVertexCount();
int faceCount = mesh.getFaceCount();
```

We can modify the present mesh by using so called _Modifiers_. Each modifier derives from the root interface `IMeshModifier`.

```java
package mesh.modifier;

import mesh.Mesh3D;

public interface IMeshModifier {

	public Mesh3D modify(Mesh3D mesh);

}
```

Let's say we would like to give our mesh some thickness.
To achieve this we use the _SolidifyModifier_.

```java
SolidifyModifier modifier = new SolidifyModifier();
modifier.setThickness(0.5f);
modifier.modify(mesh);
```

## Creators

The library provides a variety of so called mesh 'Creators' to construct various shapes the convenient way.
More precisely the 'Factory Method' / 'Builder' pattern was applied for this purpose. A mesh creator works like a classis builder.
The creator differs mainly in two points from a classic builder. We also provide getters and chaining was left out.
In the meantime the library contains 100+ different mesh creators divided in various categories.
Get a first impression and overview here: [Mesh Creators](https://github.com/ArtifactForms/MeshLibCoreClean2022/blob/master/MeshLibCoreClean2022/documentation/documentation.md).
Each creator derives from the 'IMeshCreator' interface. The following code example shows the mentioned root interface for all mesh creators.

```java
package mesh.creator;

import mesh.Mesh3D;

public interface IMeshCreator {

	public Mesh3D create();

}
```

To get a little more specific we can plug the quad example code into a custom creator to illustrate the overall concept.
Let's have a look at our example code again.

```java
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

Mesh3D mesh = new Mesh3d();
mesh.addVertex(1, 0, -1);
mesh.addVertex(1, 0, 1);
mesh.addVertex(-1, 0, 1);
mesh.addVertex(-1, 0, -1);
mesh.addFace(0, 1, 3);
mesh.addFace(1, 2, 3);
```

First we move our example code into the factory method of a custom mesh creator class and simply return the mesh.

```java
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class MyQuadCreator implements IMeshCreator {

	public Mesh3D create() {
		Mesh3D mesh = new Mesh3d();
		mesh.addVertex(1, 0, -1);
		mesh.addVertex(1, 0, 1);
		mesh.addVertex(-1, 0, 1);
		mesh.addVertex(-1, 0, -1);
		mesh.addFace(0, 1, 3);
		mesh.addFace(1, 2, 3);
		return mesh;
	}

}
```

Let's assume we want to generalize the code a bit further. We introduce a parameter for the vertex coordinates named _halfSize_.

```java
import mesh.Mesh3D;

public class MyQuadCreator implements IMeshCreator {

	private float halfSize;
	private Mesh3D mesh;

	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		return mesh;
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void createVertices() {
		addVertex(halfSize, 0, -halfSize);
		addVertex(halfSize, 0, halfSize);
		addVertex(-halfSize, 0, halfSize);
		addVertex(-halfSize, 0, -halfSize);
	}

	private void createFaces() {
		addFace(0, 1, 3);
		addFace(1, 2, 3);
	}

	private void addVertex(float x, float y, float z) {
		mesh.addVertex(x, y, z);
	}

	private void addFace(int... indices) {
		mesh.add(new Face3D(indices));
	}

	public void setSize(float size) {
		halfSize = size / 2.0f;
	}

	public float getSize() {
		return halfSize * 2;
	}

}
```

Now we can use our creator the following way:

```java
Mesh3D mesh;
MyQuadCreator creator = new MyQuadCreator();
creator.setSize(4);
mesh = creator.create();
```

This explains the overall concept of mesh creators pretty well. You should now have an idea how to use existing creators and implement your own custom ones.
See also: [Mesh Creators](https://github.com/ArtifactForms/MeshLibCoreClean2022/blob/master/MeshLibCoreClean2022/documentation/documentation.md)

## Planed features

- Convex Hull
- Poisson-Disc Sampling
- Marching Cubes

## Licence

[MIT](https://github.com/ArtifactForms/MeshLibCore/blob/master/LICENSE) License Copyright (c) 2022 Simon Dietz

