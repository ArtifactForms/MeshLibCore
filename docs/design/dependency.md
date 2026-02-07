# Module & Dependency Overview

## Purpose

This document defines **clear dependency boundaries** between the major modules
of the project. Its primary goal is to:

* reduce architectural entropy
* make long-term maintenance possible
* prevent accidental coupling
* make onboarding easier for future contributors
* protect core learning value of the project

Dependencies are defined by **intent**, not by current implementation details.
Violations may exist in the codebase and are expected to be resolved gradually.

---

## High-Level Dependency Graph

```
math
  ↑
mesh
  ↑
engine
  ↑
demos

workspace (tooling, side-module)
```

* Dependencies only point **upwards**
* No cyclic dependencies are allowed
* Tooling modules are explicitly separated

---

## `math` Module

### Role

The `math` module is the **absolute foundation** of the project.
It defines all mathematical primitives and operations used elsewhere.

### Allowed Dependencies

* Java standard library only

  * `java.lang`
  * `java.util`
  * `java.nio` (if needed)

### Forbidden Dependencies

`math` **must not depend on**:

* `mesh`
* `engine`
* `workspace`
* `demos`
* Processing
* JOGL / OpenGL bindings
* Any rendering, IO, or platform-specific code

### Design Rules

* No visualization logic
* No rendering helpers
* No engine concepts
* No framework abstractions
* No global state

The `math` module should be:

* deterministic
* testable in isolation
* reusable in other projects
* stable over long periods of time

### Stability Intent

* API changes are allowed but should be deliberate
* Prefer evolution over frequent redesign
* Backward compatibility is *nice to have*, not required

---

## `mesh` Module

### Role

The `mesh` module builds upon `math` to represent and manipulate geometry.

It contains:

* mesh data structures
* topology representations
* mesh creators
* mesh modifiers

### Allowed Dependencies

* `math`
* Java standard library

### Forbidden Dependencies

* `engine`
* `workspace`
* `demos`
* Processing / rendering APIs

### Design Rules

* No rendering code
* No engine lifecycle concepts
* Geometry-focused only

Visualization must happen *outside* this module.

---

## `engine` Module

### Role

The `engine` module explores:

* scene management
* rendering abstraction layers
* input systems
* component-based design

It is an **experimental learning engine**, not a production framework.

### Allowed Dependencies

* `math`
* `mesh`
* Java standard library

### Forbidden Dependencies

* `workspace`
* `demos`

The engine may optionally define **abstraction interfaces** that are
implemented by platform-specific layers elsewhere.

---

## `demos` Module

### Role

The `demos` module is an **experimental playground**.

### Allowed Dependencies

* `math`
* `mesh`
* `engine`

### Forbidden Dependencies

* `workspace`

No stability or cleanliness guarantees apply.

---

## `workspace` Module

### Role

The `workspace` module is a **Processing-based visualization tool**.

It exists solely to:

* visualize meshes
* debug geometry
* support mesh and modifier development

### Allowed Dependencies

* `math`
* `mesh`
* Processing

### Forbidden Dependencies

* `engine`
* `demos`

### Design Rules

* Highly Processing-dependent
* Not backend-agnostic
* No engine abstractions
* No attempt at long-term generalization

---

## Dependency Rules Summary

| Module    | May Depend On          | Must Not Depend On       |
| --------- | ---------------------- | ------------------------ |
| math      | Java stdlib            | everything else          |
| mesh      | math                   | engine, workspace, demos |
| engine    | math, mesh             | workspace, demos         |
| demos     | math, mesh, engine     | workspace                |
| workspace | math, mesh, Processing | engine, demos            |

---

## Philosophy

> Dependency rules exist to protect learning velocity, not to restrict it.

Breaking a rule temporarily is acceptable.
Ignoring them long-term is not.

---

## Status

* Conceptual boundaries: defined
* Physical enforcement: work in progress
* Refactoring approach: incremental
