# Project Structure

High-level overview of the project structure.

## Core Functionality

- **src.main.java.math:**  
  This package contains fundamental math classes like `Vector3f`, `Matrix3f`, and `Matrix4f` used for 3D geometry calculations.

- **src.main.java.mesh:**  
  This package is the heart of the project and deals with meshes. It includes:

  - **animator:**  
    Classes for animating meshes (e.g., `AbstractAnimator`, `MoveAlongNormalAnimator`).

  - **conway:**  
    Classes specific to Conway polyhedra generation (e.g., `Conway`, various modifier classes).

  - **creator:**  
    Classes for creating different types of meshes (e.g., Platonic solids, architectural elements).

    - **archimedian:** Creators for Archimedean solids.  
    - **assets:** Creators for various assets used in meshes.  
    - **beam:** Creators for different beam profiles.  
    - **catalan:** Creators for Catalan solids.  
    - **creative:** Creators for custom and creative mesh types.  
    - **platonic:** Creators for Platonic solids.  
    - **primitives:** Creators for basic geometric primitives.  
    - **special:** Creators for specialized meshes (e.g., Accordion Torus, Möbius Strip).  
    - **unsorted:** Creators for miscellaneous mesh types.  

  - **core classes:**  
    `Edge3D`, `Face3D`, `Mesh3D` — representing the building blocks of a mesh.

  - **io:**  
    Classes for reading and writing meshes (e.g., `SimpleObjectReader`, `SimpleObjectWriter`).

  - **modifier:**  
    Classes for modifying meshes (e.g., `BendModifier`, `BevelFacesModifier`, subdivision modifiers).

  - **selection:**  
    Classes for selecting faces or vertices within a mesh.

  - **util:**  
    Utility classes for mesh-related operations (e.g., `Bounds3`, `Mesh3DUtil`).

## Workspace Integration (Optional)

- **src.main.java.workspace:**  
  Handles integration of meshes within a workspace environment.

## Testing

- **src.test.java:**  
  Contains unit tests for various functionalities:

  - **bugs:** Tests for identified bugs in mesh creators or modifiers.  
  - **math:** Tests for math utilities.  
  - **mesh:** Tests for mesh creation, modification, and utilities.  
  - **util:** Tests for general utility classes.  

## Resources

- **src.main.resources:**  
  Contains resource files (e.g., configuration files, icons).