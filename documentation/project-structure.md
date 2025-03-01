# Project Structure

High-level overview of the project structure.

**Core Functionality:**

* **src.main.java.math:** This package contains fundamental math classes like `Vector3f`, `Matrix3f`, and `Matrix4f` used for 3D geometry calculations.
* **src.main.java.mesh:** This package is the heart of the project and deals with meshes. It includes:
    * **animator:** Classes for animating meshes (e.g., `AbstractAnimator`, `MoveAlongNormalAnimator`).
    * **conway:** Classes specific to Conway polyhedra generation (e.g., `Conway`, various modifier classes).
    * **creator:** Classes for creating different types of meshes (e.g., Platonic solids, architectural elements).
        * **archimedian:** Creators for Archimedean solids.
        * **assets:** Creators for various assets used in meshes.
        * **beam:** Creators for different beam profiles.
        * **catalan:** Creators for Catalan solids.
        * **creative:** Creators for custom and creative mesh types.
        * **platonic:** Creators for Platonic solids.
        * **primitives:** Creators for basic geometric primitives.
        * **special:** Creators for specialized meshes (e.g., Accordion Torus, Mobius Strip).
        * **unsorted:** Creators for miscellaneous mesh types.
    * **Edge3D, Face3D, Mesh3D:** Classes representing the building blocks of a mesh (edges, faces, and the entire mesh).
    * **io:** Classes for reading and writing meshes from/to files (e.g., `SimpleObjectReader`, `SimpleObjectWriter`).
    * **modifier:** Classes for modifying existing meshes (e.g., `BendModifier`, `BevelFacesModifier`, subdivision modifiers).
    * **selection:** Classes for selecting faces or vertices within a mesh.
    * **util:** Utility classes for mesh-related operations (e.g., `Bounds3`, `Mesh3DUtil`).

**Workspace Integration (Optional):**

* **src.main.java.workspace:** This package handles integrating meshes within a workspace environment.

**Testing:**

* **src.test.java:** This package contains unit tests for various functionalities in the project. It includes tests for:
    * **bugs:** Tests for identified bugs in specific mesh creators or modifiers.
    * **math:** Tests for math utility functions.
    * **mesh:** Tests for mesh creation, modification, and utility functions.
    * **util:** Tests for utility classes used throughout the project.

**Resources:**

* **src.main.resources:** This directory contains resource files used by the project (e.g., configuration files, icons).
