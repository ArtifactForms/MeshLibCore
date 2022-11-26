# Artifact Forms
A *JAVA* library to construct and manipulate geometry in Three-dimensional space.

## Background / Intension
This library is a hobby project started around 2015/2016 with the intension to learn more about
creating and manipulating geometry in Three-dimensional space. 
I could build up on some small knowledge I gathered through my internship several years before.
At this time I worked together with university students from the field of product design.
This was also the time I got in touch with a programming language called 'Processing' for the first time. And I was hooked from day one.
Processing is a language created with the intension to help people to learn programming in a visual context.
If you are interested to find out more went to 
[processing.org](https://processing.org). But processing is not absolutely necessary, cause the core of the library is decoupled from the processing
environment. Nevertheless processing provides a convenient way to display constructed meshes through it's rendering pipeline. Under the hood processing makes use of *JAVA*, *JAVA2D* and *OPENGL*.

## Core elements
* Mesh3D
* Face3D
* Edge3D
* Creators
* Modifiers

## Creators
The library provides a variety of so called mesh 'Creators' to construct various shapes the convenient way.
More precisely the 'Factory Pattern' was applied for this purpose.
Each creator derives from the 'IMeshCreator' interface. The following code example shows the mentioned root interface for all mesh creators. 

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
* Poisson-Disc Sampling
* Marching Cubes
