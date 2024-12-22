package workspace.ui.elements;

import workspace.ui.UiComponent;
import workspace.ui.renderer.PanelRenderer;

/**
 * Represents a simple UI panel component that serves as a container for other UI elements.
 *
 * <p>This class extends {@link UiComponent} and initializes itself with the default rendering logic
 * provided by {@link PanelRenderer}. The panel is responsible for managing its background rendering
 * and acts as a building block for creating layouts and grouping child components within the user
 * interface.
 */
public class UiPanel extends UiComponent {

  /**
   * Constructs a new {@code UiPanel} instance and sets its renderer to the default panel renderer.
   *
   * <p>The {@link PanelRenderer} handles the rendering of this panel's visual appearance, including
   * painting its background and respecting any defined insets or layout configuration.
   */
  public UiPanel() {
    setRenderer(new PanelRenderer());
  }
}
