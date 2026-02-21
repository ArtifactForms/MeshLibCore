package demos.collision;

import engine.components.ControlWASD;
import engine.components.Geometry;
import engine.physics.collision.collider.CapsuleCollider;
import engine.physics.collision.component.ColliderComponent;
import engine.rendering.Material;
import engine.runtime.input.Input;
import engine.runtime.input.Key;
import engine.scene.SceneNode;
import math.Color;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.primitives.CapsuleCreator;

/** Factory for creating a playable character node with capsule physics and WASD controls. */
public class PlayerFactory {

  private final Color capsuleColor = Color.BLUE;
  private final Key jumpKey = Key.N;
  private final Vector3f spawnLocation = new Vector3f(0, -10, 0);

  public SceneNode createTestCapsulePlayer(Input input, Settings settings) {
    SceneNode player = new SceneNode("Player");

    // 1. Visual representation
    player.addComponent(createGeometry(settings));

    // 2. Physics and collision
    ColliderComponent collider =
        createColliderComponent(settings.getCapsuleRadius(), settings.getHalfHeight());
    player.addComponent(collider);

    // 3. Logic and controller
    CharacterControllerComponent controller = new CharacterControllerComponent(collider);
    player.addComponent(controller);
    player.addComponent(createControl(input, controller, settings.getSpeed()));

    // 4. Initial state
    player.getTransform().setPosition(spawnLocation);

    return player;
  }

  private Geometry createGeometry(Settings settings) {
    // Match visual mesh to the collider dimensions
    float cylinderHeight = 2 * settings.getHalfHeight();
    float radius = settings.getCapsuleRadius();

    CapsuleCreator creator = new CapsuleCreator();
    creator.setTopCapHeight(radius);
    creator.setBottomCapHeight(radius);
    creator.setTopRadius(radius);
    creator.setBottomRadius(radius);
    creator.setCylinderHeight(cylinderHeight);

    Mesh3D mesh = creator.create();
    return new Geometry(mesh, new Material(capsuleColor));
  }

  private ColliderComponent createColliderComponent(float radius, float halfHeight) {
    CapsuleCollider collider = new CapsuleCollider(radius, halfHeight);
    return new ColliderComponent(collider);
  }

  private ControlWASD createControl(
      Input input, CharacterControllerComponent controller, float speed) {
    ControlWASD control = new ControlWASD(input);
    control.setSpeed(speed);
    control.mapArrowKeys();
    control.setJumpKey(jumpKey);
    // Wire the WASD input directly to the character controller
    control.setMovementInputConsumer(controller);
    return control;
  }
}
