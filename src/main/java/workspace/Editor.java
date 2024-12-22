package workspace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import workspace.command.KeyCommandMap;
import workspace.command.ResetPanningCommand;
import workspace.command.ShadeSmoothFlatCommand;
import workspace.command.ShowHideEdgesCommand;
import workspace.command.ShowHideFaceNormalsCommand;
import workspace.command.ShowHideGridCommand;
import workspace.command.ShowHideSideBarCommand;
import workspace.command.ShowHideVertexNormalsCommand;
import workspace.command.ShowHideXAxisCommand;
import workspace.command.ShowHideYAxisCommand;
import workspace.command.ShowHideZAxisCommand;
import workspace.command.WireframeCommand;
import workspace.laf.LookAndFeel;
import workspace.ui.UiComponent;
import workspace.ui.elements.UiEditorMenu;
import workspace.ui.elements.ViewportCompass;

public class Editor implements ModelListener {

  protected List<SceneObject> sceneObjects;

  protected UiComponent rootUi;

  protected KeyCommandMap commands;

  protected WorkspaceModel model;

  protected ViewportCompass gizmo;

  protected WorkspaceSideBarUi sideBar;

  protected UiEditorMenu menu;

  public Editor() {
    setup();
  }

  private void setup() {
    setupLookAndFeel();
    initializeModel();
    initializeSceneObjects();
    initializeRootUi();
    createUi();
    initializeCommandMap();
    registerKeyCommands();
  }

  @Override
  public void onModelChanged() {
    rootUi.setVisible(model.isUiVisible());
  }

  private void initializeSceneObjects() {
    sceneObjects = new ArrayList<SceneObject>();
  }

  private void createUi() {
    rootUi.add(getSideBar());
    rootUi.add(getGizmo());
    rootUi.add(getMenu());
  }

  private WorkspaceSideBarUi getSideBar() {
    if (sideBar == null) {
      sideBar = new WorkspaceSideBarUi(model);
    }
    return sideBar;
  }

  private ViewportCompass getGizmo() {
    if (gizmo == null) {
      gizmo = new ViewportCompass();
    }
    return gizmo;
  }

  private UiEditorMenu getMenu() {
    if (menu == null) {
      menu = new UiEditorMenu();
    }
    return menu;
  }

  private void initializeRootUi() {
    rootUi = new UiComponent();
  }

  private void setupLookAndFeel() {
    LookAndFeel.setup();
  }

  private void initializeCommandMap() {
    commands = new KeyCommandMap();
  }

  private void initializeModel() {
    model = new WorkspaceModel();
  }

  private void registerKeyCommands() {
    commands.register(new ShowHideGridCommand(model));
    commands.register(new ShowHideXAxisCommand(model));
    commands.register(new ShowHideYAxisCommand(model));
    commands.register(new ShowHideZAxisCommand(model));
    commands.register(new ShowHideSideBarCommand(model));
    commands.register(new ShowHideFaceNormalsCommand(model));
    commands.register(new ResetPanningCommand(model));
    commands.register(new ShowHideVertexNormalsCommand(model));
    commands.register(new ShowHideEdgesCommand(model));
    commands.register(new WireframeCommand(model));
    commands.register(new ShadeSmoothFlatCommand(model));
  }

  private void resizeRootUi(int x, int y, int width, int height) {
    rootUi.setX(x);
    rootUi.setY(y);
    rootUi.setWidth(width);
    rootUi.setHeight(height);
  }

  public void resize(int x, int y, int width, int height) {
    resizeRootUi(x, y, width, height);
    updateGizmo(width, height);
  }

  public void handleMouseClicked(int x, int y) {
    rootUi.onMouseClicked(x, y);
  }

  public void handleMousePressed(int x, int y) {
    rootUi.onMousePressed(x, y);
  }

  public void handleMouseDragged(int x, int y) {
    rootUi.onMouseDragged(x, y);
  }

  public void handleMouseReleased(int x, int y) {
    rootUi.onMouseReleased(x, y);
  }

  public void handleMouseWheel(float amount) {
    float scale = model.getScale();
    scale -= amount * scale * 0.2f;
    model.setScale(scale);
  }

  private void updateGizmo(int width, int height) {
    gizmo.setX(width - 80);
    gizmo.setY(130);
  }

  public void add(UiComponent component) {
    rootUi.add(component);
  }

  public void addSceneObject(SceneObject sceneObject) {
    sceneObjects.add(sceneObject);
  }

  public void addAll(Collection<SceneObject> sceneObjects) {
    this.sceneObjects.addAll(sceneObjects);
  }

  public void clearSceneObjects() {
    sceneObjects.clear();
  }
}
