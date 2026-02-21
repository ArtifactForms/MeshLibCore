/**
 * Provides the game layer of the engine.
 *
 * <p>This package defines abstract gameplay concepts and domain-level features that are
 * intentionally decoupled from engine infrastructure.
 *
 * <h2>Responsibilities</h2>
 *
 * <ul>
 *   <li>Gameplay state and logic (e.g. health, lifetime, status effects)
 *   <li>Pure domain behavior independent of rendering or runtime systems
 *   <li>Reusable game mechanics
 * </ul>
 *
 * <h2>Design Constraints</h2>
 *
 * <ul>
 *   <li>Must not depend on rendering, scene, or other engine subsystems.
 *   <li>Must not depend on application bootstrap or runtime infrastructure.
 *   <li>No direct access to engine APIs.
 * </ul>
 *
 * <h2>Integration</h2>
 *
 * The engine may integrate this layer by providing adapters or hooks (e.g. components, systems, or
 * event bridges) that connect gameplay logic to the engine runtime.
 *
 * <h2>Testing</h2>
 *
 * This package must be fully testable in isolation:
 *
 * <ul>
 *   <li>No engine startup required
 *   <li>No rendering context
 *   <li>No mocks of engine subsystems
 * </ul>
 *
 * <p>The game layer represents pure domain logic and should remain stable, deterministic, and free
 * of side effects beyond its own model.
 */
package engine.game;
