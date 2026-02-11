package demos.codex.snake;

import engine.application.ApplicationSettings;
import engine.application.BasicApplication;
import engine.components.StaticGeometry;
import engine.input.Key;
import engine.render.Material;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.camera.PerspectiveCamera;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import math.Color;
import math.Mathf;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.primitives.PlaneCreator;
import workspace.ui.Graphics;

/** Kleines 3D-Snake Playground im Demo-Package. */
public class Snake3DDemo extends BasicApplication {

  private static final int GRID_WIDTH = 18;
  private static final int GRID_HEIGHT = 18;
  private static final float CELL_SIZE = 24f;
  private static final float STEP_INTERVAL = 0.16f;

  private static final Color BOARD_COLOR = new Color(0.08f, 0.10f, 0.12f);
  private static final Color HEAD_COLOR = new Color(0.2f, 0.95f, 0.35f);
  private static final Color BODY_COLOR = new Color(0.1f, 0.6f, 0.2f);
  private static final Color FOOD_COLOR = new Color(1f, 0.25f, 0.1f);

  private final Random random = new Random();
  private final List<GridPos> snake = new ArrayList<>();

  private Scene scene;
  private SceneNode snakeRoot;
  private SceneNode foodNode;

  private Direction direction;
  private Direction queuedDirection;
  private GridPos food;

  private float accumulator;
  private int growth;
  private int score;
  private boolean gameOver;

  public static void main(String[] args) {
    Snake3DDemo app = new Snake3DDemo();
    ApplicationSettings settings = new ApplicationSettings();
    settings.setWidth(1280);
    settings.setHeight(720);
    settings.setTitle("MeshLibCore - 3D Snake (demos.codex.snake)");
    app.launch(settings);
  }

  @Override
  public void onInitialize() {
    scene = new Scene();
    scene.setBackground(new Color(0.03f, 0.03f, 0.04f));

    setupCamera();
    setupBoard();

    snakeRoot = new SceneNode("SnakeRoot");
    scene.addNode(snakeRoot);

    setActiveScene(scene);
    resetGame();
  }

  private void setupCamera() {
    PerspectiveCamera camera = new PerspectiveCamera();
    camera.setFarPlane(5000f);
    camera.getTransform().setPosition(0f, -520f, 0f);
    camera.getTransform().setRotation(-Mathf.PI * 0.5f, 0f, 0f);
    scene.setActiveCamera(camera);
  }

  private void setupBoard() {
    float boardSize = Math.max(GRID_WIDTH, GRID_HEIGHT) * CELL_SIZE;

    Material boardMaterial = new Material(BOARD_COLOR);
    boardMaterial.setUseLighting(false);

    SceneNode board = new SceneNode("Board");
    board.addComponent(new StaticGeometry(new PlaneCreator(boardSize * 0.5f).create(), boardMaterial));
    scene.addNode(board);

    createBorder("Border-Top", 0, -0.5f, -GRID_HEIGHT / 2f - 0.5f, GRID_WIDTH + 2, 1);
    createBorder("Border-Bottom", 0, -0.5f, GRID_HEIGHT / 2f + 0.5f, GRID_WIDTH + 2, 1);
    createBorder("Border-Left", -GRID_WIDTH / 2f - 0.5f, -0.5f, 0, 1, GRID_HEIGHT);
    createBorder("Border-Right", GRID_WIDTH / 2f + 0.5f, -0.5f, 0, 1, GRID_HEIGHT);
  }

  private void createBorder(String name, float gridX, float gridY, float gridZ, float sx, float sz) {
    Material borderMaterial = new Material(new Color(0.2f, 0.22f, 0.28f));
    borderMaterial.setUseLighting(false);

    SceneNode wall = new SceneNode(name);
    wall.addComponent(new StaticGeometry(new CubeCreator(CELL_SIZE * 0.5f).create(), borderMaterial));
    wall.getTransform().setPosition(gridToWorldX(gridX), gridY * CELL_SIZE, gridToWorldZ(gridZ));
    wall.getTransform().setScale(sx, 0.4f, sz);
    scene.addNode(wall);
  }

  private void resetGame() {
    snake.clear();
    direction = Direction.RIGHT;
    queuedDirection = direction;
    accumulator = 0f;
    growth = 2;
    score = 0;
    gameOver = false;

    int cx = GRID_WIDTH / 2;
    int cy = GRID_HEIGHT / 2;
    snake.add(new GridPos(cx, cy));
    snake.add(new GridPos(cx - 1, cy));
    snake.add(new GridPos(cx - 2, cy));

    food = randomFreeCell();
    renderSnake();
    renderFood();
  }

  @Override
  public void onUpdate(float tpf) {
    handleDirectionInput();

    if (gameOver) {
      if (input.wasKeyPressed(Key.ENTER) || input.wasKeyPressed(Key.SPACE)) {
        resetGame();
      }
      return;
    }

    accumulator += tpf;
    while (accumulator >= STEP_INTERVAL) {
      accumulator -= STEP_INTERVAL;
      stepSimulation();
    }
  }

  private void handleDirectionInput() {
    if (input.wasKeyPressed(Key.ARROW_UP) || input.wasKeyPressed(Key.W)) {
      tryQueueDirection(Direction.UP);
    }
    if (input.wasKeyPressed(Key.ARROW_DOWN) || input.wasKeyPressed(Key.S)) {
      tryQueueDirection(Direction.DOWN);
    }
    if (input.wasKeyPressed(Key.ARROW_LEFT) || input.wasKeyPressed(Key.A)) {
      tryQueueDirection(Direction.LEFT);
    }
    if (input.wasKeyPressed(Key.ARROW_RIGHT) || input.wasKeyPressed(Key.D)) {
      tryQueueDirection(Direction.RIGHT);
    }
  }

  private void tryQueueDirection(Direction candidate) {
    if (candidate == null || candidate == direction.opposite()) {
      return;
    }
    queuedDirection = candidate;
  }

  private void stepSimulation() {
    direction = queuedDirection;

    GridPos head = snake.get(0);
    GridPos next = new GridPos(head.x + direction.dx, head.y + direction.dy);

    if (isOutside(next) || snake.contains(next)) {
      gameOver = true;
      return;
    }

    snake.add(0, next);

    if (next.equals(food)) {
      score++;
      growth += 2;
      food = randomFreeCell();
      renderFood();
    }

    if (growth > 0) {
      growth--;
    } else {
      snake.remove(snake.size() - 1);
    }

    renderSnake();
  }

  private boolean isOutside(GridPos pos) {
    return pos.x < 0 || pos.x >= GRID_WIDTH || pos.y < 0 || pos.y >= GRID_HEIGHT;
  }

  private GridPos randomFreeCell() {
    GridPos candidate;
    do {
      candidate = new GridPos(random.nextInt(GRID_WIDTH), random.nextInt(GRID_HEIGHT));
    } while (snake.contains(candidate));
    return candidate;
  }

  private void renderSnake() {
    snakeRoot.destroy();
    snakeRoot = new SceneNode("SnakeRoot");
    scene.addNode(snakeRoot);

    for (int i = 0; i < snake.size(); i++) {
      GridPos segment = snake.get(i);
      Material material = new Material(i == 0 ? HEAD_COLOR : BODY_COLOR);
      material.setUseLighting(false);

      SceneNode cube = new SceneNode(i == 0 ? "SnakeHead" : "SnakeBody-" + i);
      cube.addComponent(new StaticGeometry(new CubeCreator(CELL_SIZE * 0.44f).create(), material));
      cube.getTransform().setPosition(gridToWorldX(segment.x), -CELL_SIZE * 0.45f, gridToWorldZ(segment.y));
      snakeRoot.addChild(cube);
    }
  }

  private void renderFood() {
    if (foodNode != null) {
      foodNode.destroy();
    }

    Material material = new Material(FOOD_COLOR);
    material.setUseLighting(false);

    foodNode = new SceneNode("Food");
    foodNode.addComponent(new StaticGeometry(new CubeCreator(CELL_SIZE * 0.34f).create(), material));
    foodNode.getTransform().setPosition(gridToWorldX(food.x), -CELL_SIZE * 0.34f, gridToWorldZ(food.y));
    scene.addNode(foodNode);
  }

  private float gridToWorldX(float x) {
    return (x - GRID_WIDTH * 0.5f + 0.5f) * CELL_SIZE;
  }

  private float gridToWorldZ(float y) {
    return (y - GRID_HEIGHT * 0.5f + 0.5f) * CELL_SIZE;
  }

  @Override
  public void onRender(Graphics g) {}

  @Override
  public void onRenderUI(Graphics g) {
    g.setColor(Color.WHITE);
    g.textSize(20);
    g.text("3D Snake Demo", 16, 28);
    g.text("Score: " + score, 16, 52);
    g.text("Steuerung: Pfeile / WASD", 16, 76);

    if (gameOver) {
      g.setColor(Color.RED);
      g.textCenteredBoth("Game Over - Enter/Space für Neustart");
    }
  }

  @Override
  public void onCleanup() {}

  private enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
      this.dx = dx;
      this.dy = dy;
    }

    private Direction opposite() {
      return switch (this) {
        case UP -> DOWN;
        case DOWN -> UP;
        case LEFT -> RIGHT;
        case RIGHT -> LEFT;
      };
    }
  }

  private record GridPos(int x, int y) {}
}
