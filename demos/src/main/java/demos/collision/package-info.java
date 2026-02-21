/**
 * Collision and character controller sandbox demo.
 *
 * <p>This package provides an experimental test environment for evaluating character controller
 * behavior within the engine.
 *
 * <h2>Purpose</h2>
 *
 * <ul>
 *   <li>Prototype and validate kinematic character controller concepts
 *   <li>Experiment with collision detection and capsule sweep algorithms
 *   <li>Evaluate how collision logic can be integrated into the engine core
 *   <li>Provide a configurable test scene with multiple collision scenarios
 * </ul>
 *
 * <h2>Current State</h2>
 *
 * <p>The implementation is highly prototypical and focused on experimentation. Architectural
 * cleanliness is secondary to fast iteration and validation.
 *
 * <h2>Additional Components</h2>
 *
 * <ul>
 *   <li>Basic graybox tooling for rapid environment setup
 *   <li>Initial experiments with procedural textures
 * </ul>
 *
 * <h2>Technical Notes</h2>
 *
 * <ul>
 *   <li>Currently strongly coupled to AWT (desktop-only)
 *   <li>Evaluation ongoing whether a dedicated rasterizer abstraction is required
 *   <li>Future goal: clearer separation from AWT dependencies
 * </ul>
 *
 * <p>This package is experimental and not intended as a reference implementation. It exists solely
 * for exploration, integration testing, and design validation.
 */
package demos.collision;
