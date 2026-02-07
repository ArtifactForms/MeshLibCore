# Workspace Module – Design Document

## 1. Overview

The **workspace** module is a lightweight, Processing-based application used to **visualize and inspect meshes** during development of mesh creators and modifiers.

It is intentionally **not** a full editor, **not** backend-agnostic, and **not** part of the long-term engine architecture. The workspace exists as a **developer tool** to speed up iteration and debugging.

---

## 2. Purpose & Motivation

The workspace exists to solve a very specific problem:

- Quickly visualize meshes produced by the mesh library
- Inspect mesh structure and topology
- Debug algorithms related to:
  - mesh creators
  - mesh modifiers
  - geometry operations

Processing is used deliberately because:
- it enables extremely fast visual feedback
- it minimizes boilerplate
- it is well-suited for exploratory and experimental development

---

## 3. Scope

### In Scope

The workspace **may**:
- render meshes
- visualize:
  - vertices
  - edges
  - faces
  - vertex normals
  - face normals
- provide simple toggles or debug views
- contain Processing-specific rendering code
- depend directly on Processing APIs

### Out of Scope (Non-Goals)

The workspace **must not**:
- become a full mesh editor
- introduce rendering or graphics abstraction layers
- define reusable engine or graphics contracts
- aim for backend independence
- be used as a library by other modules
- evolve into a production-ready tool

---

## 4. Architectural Position

The workspace is a **leaf module** in the overall project structure.

```
mesh   ─┐
math   ─┼─> workspace
engine ─┘

workspace ─X─> (nothing depends on workspace)
```

Key rule:
> **No other module may depend on workspace.**

---

## 5. Dependency Policy

### Allowed Dependencies

- `math`
- `mesh`
- `engine` (optional, only for convenience)
- Processing (directly)

### Forbidden Responsibilities

The workspace must **not**:
- define graphics contracts (e.g. `Graphics`, `Graphics2D`, `Graphics3D`)
- define engine-level abstractions
- define reusable rendering APIs

Any such contracts belong to the engine or a future viewer module, not here.

---

## 6. Stability & Evolution

The workspace is considered **stable by intention**, not by API.

This means:
- code may be messy or pragmatic
- refactoring is allowed if it improves usability
- feature growth should be resisted
- architectural cleanup is *not* a goal

The workspace may eventually be:
- frozen
- archived
- replaced

without impacting the rest of the system.

---

## 7. Naming & Communication

To avoid future confusion:

- names should reflect viewer/debug intent
- comments should explicitly state Processing-dependence

Example:

```java
// Processing-specific mesh viewer.
// Intentionally not backend-agnostic.
```

---

## 8. Long-Term Vision (Explicitly Out of Scope)

A future, backend-agnostic mesh viewer **may** be built using the engine.

That system:
- will be a new module
- will not reuse workspace architecture
- will not be built incrementally on top of workspace

The current workspace is **not** a stepping stone for that system.

---

## 9. Summary

The workspace is a:
- pragmatic
- disposable
- Processing-based
- developer-facing

mesh visualization tool.

Its value lies in speed and clarity — not architectural purity.
