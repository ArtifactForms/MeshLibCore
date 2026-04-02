package client.scene;

import client.app.GameClient;
import client.resources.Resources;
import client.resources.TextureAtlas;
import client.ui.GameTextures;
import client.ui.button.ButtonClickCallback;
import client.ui.button.SimpleButton;
import client.ui.core.UiComponent;
import client.ui.cursor.SimpleCursorComponent;
import client.usecases.start.StartGame;
import common.game.block.Blocks;
import engine.components.AbstractComponent;
import engine.components.StaticGeometry;
import engine.rendering.Material;
import engine.runtime.input.Input;
import engine.runtime.input.MouseMode;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.audio.SoundManager;
import engine.scene.camera.PerspectiveCamera;
import engine.scene.light.AmbientLight;
import engine.scene.light.DirectionalLight;
import math.Color;
import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.next.surface.SurfaceLayer;

public class StartScene extends Scene {

  private Input input;
  private boolean gameStarted;
  private GameClient client;

  public StartScene(Input input, GameClient client) {
    this.input = input;
    this.client = client;

    setupCamera();
    setupUI();
    setupBackground();
    setupRotatingCube();
    setupLight();
  }

  private void setupLight() {
    addLight(createAmbientLight());
    addLight(createSunLight());
    addLight(createFillLight());
  }

  private AmbientLight createAmbientLight() {
    return new AmbientLight(new Color(0.5f, 0.5f, 0.5f));
  }

  private DirectionalLight createSunLight() {
    return new DirectionalLight(
        new Color(1.0f, 0.98f, 0.85f), new Vector3f(-0.5f, 1.0f, -0.3f).normalize());
  }

  private DirectionalLight createFillLight() {
    return new DirectionalLight(
        new Color(0.15f, 0.15f, 0.18f), new Vector3f(0.5f, -1.0f, 0.3f).normalize());
  }

  private void setupRotatingCube() {
    Mesh3D cube = createCube(6);

    TextureAtlas atlas = GameTextures.TEXTURE_ATLAS;
    applyBlockTexture(Blocks.GRASS_BLOCK.getId(), atlas, cube);

    Material material = new Material();

    material.setDiffuseTexture(atlas.getTexture());

    StaticGeometry geometry = new StaticGeometry(cube, material);

    SceneNode node = new SceneNode("Cube", geometry, new RotateComponent());

    //    getUIRoot().addChild(node);
    addNode(node);
  }

  private Mesh3D createCube(float radius) {
    Mesh3D cube = new CubeCreator(radius).create();
    SurfaceLayer layer = cube.getSurfaceLayer();
    for (int i = 0; i < 8; i++) layer.addUV(0, 0);

    return cube;
  }

  private void setupCamera() {
    PerspectiveCamera camera = new PerspectiveCamera();
    camera.setTarget(new Vector3f(0, 0, 5));
    setActiveCamera(camera);
  }

  private void setupUI() {
    setupCursor();
    setupButtons();
  }

  private void setupBackground() {
    //    getUIRoot().addChild(new Sprite(Resources.START_SCREEN_BACKGROUND));
    setBackground(Color.getColorFromInt(120, 120, 120));
  }

  private void setupCursor() {
    SimpleCursorComponent cursorComponent = new SimpleCursorComponent(input);
    SceneNode cursorNode = new SceneNode("Cursor", cursorComponent);
    getUIRoot().addChild(cursorNode);
  }

  private void setupButtons() {
    setupStartButton();
    setupQuitButton();
  }

  private void setupStartButton() {
    SimpleButton button = new SimpleButton(Resources.GAME_MULTIPLAYER_BUTTON_TEXT, 0, 0, 300, 40);
    button.setCallback(
        new ButtonClickCallback() {

          @Override
          public void onButtonClicked() {
            //            startGame();
            client.getSceneManager().setActiveScene(new DirectConnectScene(input, client));
          }
        });
    UiComponent component = new UiComponent(input, button);
    SceneNode buttonNode = new SceneNode("Start-Button", component);
    getUIRoot().addChild(buttonNode);
  }

  private void setupQuitButton() {
    SimpleButton button = new SimpleButton(Resources.GAME_QUIT_BUTTON_TEXT, 0, 50, 300, 40);
    button.setCallback(
        new ButtonClickCallback() {

          @Override
          public void onButtonClicked() {
            System.exit(0);
          }
        });
    UiComponent component = new UiComponent(input, button);
    SceneNode buttonNode = new SceneNode("Quit-Button", component);
    getUIRoot().addChild(buttonNode);
  }

  private void startGame() {
    if (gameStarted) {
      return;
    }
    gameStarted = true;
    new StartGame(input, client).execute();
  }

  public void applyBlockTexture(int id, TextureAtlas textureAtlas, Mesh3D cube) {
    SurfaceLayer layer = cube.getSurfaceLayer();

    int row = id;
    int col = 0;

    float epsPx = 0.002f;
    float atlasWidth = textureAtlas.getWidth();
    float atlasHeight = textureAtlas.getHeight();

    float tileSize = textureAtlas.getTileSize();

    float px1 = col * tileSize + epsPx;
    float py1 = row * tileSize + epsPx;

    float px2 = (col + 1) * tileSize - epsPx;
    float py2 = (row + 1) * tileSize - epsPx;

    // convert to UV space
    float x1 = px1 / atlasWidth;
    float x2 = px2 / atlasWidth;
    float y1 = 1f - (py2 / atlasHeight);
    float y2 = 1f - (py1 / atlasHeight);

    SurfaceLayer surfaceLayer = cube.getSurfaceLayer();

    surfaceLayer.getUvAt(0).set(x1, y2);
    surfaceLayer.getUvAt(1).set(x2, y2);
    surfaceLayer.getUvAt(2).set(x2, y1);
    surfaceLayer.getUvAt(3).set(x1, y1);

    layer.setFaceUVIndices(0, new int[] {0, 1, 2, 3});
    layer.setFaceUVIndices(1, new int[] {0, 1, 2, 3});
    layer.setFaceUVIndices(2, new int[] {0, 1, 2, 3});
    layer.setFaceUVIndices(3, new int[] {0, 1, 2, 3});
    layer.setFaceUVIndices(4, new int[] {0, 1, 2, 3});
    layer.setFaceUVIndices(5, new int[] {0, 1, 2, 3});
  }

  public class RotateComponent extends AbstractComponent {
    private float time;
    private float speed = 0.5f;

    @Override
    public void onUpdate(float tpf) {
      time += tpf;
      float rot = time * speed;

      float hover = Mathf.sin(time * 2.0f) * 2.0f;

      getOwner().getTransform().setRotation(0.4f, rot, 0);

      getOwner()
          .getTransform()
          .setPosition(getOwner().getTransform().getPosition().set(0, -15 + hover, 80));
    }
  }

  @Override
  public void onEnter() {
    input.setMouseMode(MouseMode.ABSOLUTE);
    SoundManager.loopSound(Resources.MENU_BACKGROUND_MUSIC_KEY);
  }

  @Override
  public void onExit() {
//    SoundManager.stopSound(Resources.MENU_BACKGROUND_MUSIC_KEY);
  }
}
