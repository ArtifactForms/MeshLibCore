package engine.components;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The {@code TagComponent} allows attaching arbitrary string-based tags to a scene node.
 *
 * <p>Tags are commonly used to categorize or identify objects in a scene, for example:
 *
 * <ul>
 *   <li>{@code "Player"}
 *   <li>{@code "Enemy"}
 *   <li>{@code "Damageable"}
 *   <li>{@code "Pickup"}
 * </ul>
 *
 * <p>This component supports adding, removing, and querying tags at runtime. A node can have
 * multiple tags simultaneously. Tags are case-sensitive.
 *
 * <p>Typical use cases include:
 *
 * <ul>
 *   <li>Filtering collision or trigger interactions
 *   <li>AI target selection
 *   <li>Camera follow logic
 *   <li>Gameplay rule checks (e.g. damage, pickups, teams)
 * </ul>
 */
public class TagComponent extends AbstractComponent {

  /** Internal set storing all assigned tags */
  private final Set<String> tags = new HashSet<>();

  /** Creates an empty {@code TagComponent} with no initial tags. */
  public TagComponent() {}

  /**
   * Creates a {@code TagComponent} with the given initial tags.
   *
   * @param initialTags One or more tags to assign to the owner node
   */
  public TagComponent(String... initialTags) {
    for (String tag : initialTags) {
      add(tag);
    }
  }

  /**
   * Adds a tag to this component.
   *
   * <p>If the tag is {@code null} or empty, the call is ignored.
   *
   * @param tag The tag to add
   */
  public void add(String tag) {
    if (tag == null || tag.isEmpty()) return;
    tags.add(tag);
  }

  /**
   * Removes a tag from this component.
   *
   * @param tag The tag to remove
   */
  public void remove(String tag) {
    tags.remove(tag);
  }

  /**
   * Checks whether this component contains the given tag.
   *
   * @param tag The tag to check
   * @return {@code true} if the tag exists, {@code false} otherwise
   */
  public boolean has(String tag) {
    return tags.contains(tag);
  }

  /**
   * Checks whether this component contains at least one of the given tags.
   *
   * @param checkTags The tags to check
   * @return {@code true} if any of the tags exist, {@code false} otherwise
   */
  public boolean hasAny(String... checkTags) {
    for (String tag : checkTags) {
      if (tags.contains(tag)) return true;
    }
    return false;
  }

  /**
   * Checks whether this component contains all of the given tags.
   *
   * @param checkTags The tags to check
   * @return {@code true} if all tags exist, {@code false} otherwise
   */
  public boolean hasAll(String... checkTags) {
    for (String tag : checkTags) {
      if (!tags.contains(tag)) return false;
    }
    return true;
  }

  /**
   * Returns an unmodifiable view of all assigned tags.
   *
   * <p>This prevents external modification of the internal tag set.
   *
   * @return An unmodifiable set of tags
   */
  public Set<String> getTags() {
    return Collections.unmodifiableSet(tags);
  }

  /**
   * Called when this component is attached to a scene node.
   *
   * <p>Currently unused.
   */
  @Override
  public void onAttach() {}

  /**
   * Called when this component is detached from a scene node.
   *
   * <p>Currently unused.
   */
  @Override
  public void onDetach() {}

  /**
   * Updates the component.
   *
   * <p>The {@code TagComponent} does not require per-frame updates, so this method is intentionally
   * empty.
   *
   * @param tpf Time per frame (delta time)
   */
  @Override
  public void onUpdate(float tpf) {}
}
