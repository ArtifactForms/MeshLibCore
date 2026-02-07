# Demos Module – Design Document

## Overview

The `demos` module is an **experimental playground** within the project.
It exists to explore ideas, test assumptions, iterate quickly, and have fun
with the current state of the codebase.

It is **not** a polished showcase, **not** production code, and **not** a
stable or maintained API surface.

---

## Purpose

The primary goals of the `demos` module are:

- Rapid experimentation
- Game jam development
- Testing engine, mesh, and math features through real usage
- Exploring workflows and interaction patterns
- Iterative development without architectural pressure

`demos` answers questions like:

- “Can this idea work with the current engine?”
- “How does this feel when actually used?”
- “What breaks if I try this approach?”
- “Is this concept worth extracting into a core module later?”

---

## What `demos` Is

- 🧪 Experimental code
- 🎮 Game jam projects
- 🔁 Fast iteration space
- 🧠 Thinking-in-code area
- 🛠️ Developer tools for exploration
- 🔍 Integration testing through usage

Code in `demos` is allowed to be:

- messy
- tightly coupled
- duplicated
- unfinished
- temporary
- rewritten or deleted at any time

---

## What `demos` Is Not

- ❌ A stable API
- ❌ Production-ready code
- ❌ A benchmark suite
- ❌ A marketing showcase
- ❌ A collection of best practices
- ❌ A guaranteed long-term module

No backward compatibility is promised.
Breaking changes are expected.

---

## Dependencies

`demos` **may depend on**:

- `math`
- `mesh`
- `engine`

`demos` **must not depend on**:

- `workspace`

This rule ensures:

- `workspace` remains a focused visualization tool
- `demos` can freely evolve or break
- no reverse-dependencies into tooling or viewers

---

## Stability Policy

- No API stability guarantees
- No mandatory test coverage
- Refactoring is optional
- Code may stop compiling temporarily
- Code may be removed without replacement

If an idea from `demos` proves valuable, it should be:

1. Extracted into `engine`, `mesh`, or `math`
2. Rewritten cleanly
3. Properly documented
4. Tested independently

---

## Design Philosophy

> `demos` is allowed to fail.

The value of this module lies in:

- speed
- curiosity
- iteration
- learning
- playfulness

Longevity is **not** a goal.

---

## Long-Term Outlook

There is **no long-term roadmap** for `demos`.

Individual demos may:
- evolve into real modules
- be rewritten from scratch
- be archived
- be deleted entirely

All outcomes are valid.

---

## Summary

**One sentence definition:**

> The `demos` module is where ideas are allowed to be wrong, temporary, and fun.

---

## Status

- Scope: fixed
- Intent: frozen
- Code quality: intentionally inconsistent
- Maintenance level: minimal