package demos.collision;

import demos.skybox.SkyBox;
import engine.scene.SceneNode;
import math.Vector3f;

public class TestEnvironmentFactory {

  private float capsuleRadius = 0.5f;
  private float stepHeight = 0.3f;
  private float capsuleHeight = 2;
  private GrayBox grayBox;

  public SceneNode createEnvironment() {
    grayBox = new GrayBox(4);

    SceneNode root = new SceneNode("CollisionTestEnvironment");

    root.addChild(createFloor());
    root.addChild(createMovingPlatformZone());

    root.addChild(createGratingZone());
    root.addChild(createMousetrapZone());

    root.addChild(createSpawnBox());
    root.addChild(createSingleBoxZone());
    root.addChild(createCornerZone());
    root.addChild(createNarrowPassageZone());
    root.addChild(createCeilingZone());
    root.addChild(createThinPillarZone());
    root.addChild(createRampZone());
    root.addChild(createStepZone());
    root.addChild(createWallZone());
    root.addChild(createStairsZone());
    root.addChild(createJumpZone());

    root.addChild(new SkyBox(2000));

    return root;
  }

  private SceneNode createStairsZone() {
    SceneNode stairs = grayBox.blockOutStrairs(new Vector3f(40, 0, 0));
    stairs.setName("Stairs");
    return stairs;
  }

  // ---------------------------------------------------
  // FLOOR
  // ---------------------------------------------------

  private SceneNode createFloor() {
    Vector3f position = new Vector3f(0, 0.1f, 0);
    Vector3f size = new Vector3f(4000, 0.1f, 4000);

    SceneNode node = grayBox.blockOutBox("Floor", position, size);
    //    node.getTransform().setPosition(0, 0.1f, 0); // center correction

    return node;
  }

  // ---------------------------------------------------
  // MOVING PLATFORM (Adhesion & Delta Test)
  // ---------------------------------------------------

  /**
   * Creates a platform that moves back and forth. Tests if the CharacterController can stay on top
   * of a moving surface and how sweeps handle dynamic geometry.
   */
  private SceneNode createMovingPlatformZone() {
    SceneNode root = new SceneNode("MovingPlatformZone");
    Vector3f position = new Vector3f(60, 0, 0);
    Vector3f size = new Vector3f(6, 1, 6);

    // Create the visual and collision box
    SceneNode platform = grayBox.blockOutBox("MovingPlatform", position, size);

    // Attach the movement logic
    platform.addComponent(new MovingPlatformComponent());

    root.addChild(platform);
    return root;
  }

  // ---------------------------------------------------
  // THE GRATING (Small gap / jitter test)
  // ---------------------------------------------------

  /**
   * Creates a floor made of many thin beams with small gaps (0.1f).
   *
   * <p>This test zone is essential to ensure that the Capsule Sweep does not get stuck on tiny
   * irregularities (the so-called "Ghost Collision" problem). When the capsule slides over a 0.1f
   * gap, the mathematical sweep must not register a blockage within the gap that would stop forward
   * movement.
   */
  private SceneNode createGratingZone() {
    SceneNode root = new SceneNode("GratingZone");

    float beamWidth = 0.5f;
    float gapWidth = 0.1f;
    int beamCount = 20;
    Vector3f startPos = new Vector3f(-50, 0, 0);
    Vector3f beamSize = new Vector3f(beamWidth, 0.2f, 10f);

    for (int i = 0; i < beamCount; i++) {
      // Calculate offset: each step is one beam width plus the gap
      float xOffset = i * (beamWidth + gapWidth);
      Vector3f currentPos = startPos.add(new Vector3f(xOffset, 0, 0));

      SceneNode beam = grayBox.blockOutBox("GratingBeam-" + i, currentPos, beamSize);

      root.addChild(beam);
    }

    return root;
  }

  // ---------------------------------------------------
  // THE MOUSETRAP (Gap & Depenetration Test)
  // ---------------------------------------------------

  /**
   * Creates a V-shaped or narrow floor gap. Tests if the controller gets stuck when the capsule is
   * partially inside a gap.
   */
  private SceneNode createMousetrapZone() {
    SceneNode root = new SceneNode("MousetrapZone");

    // The capsule radius is 0.5f (diameter 1.0f).
    // We create a gap of 0.7f - too small to fall through,
    // but large enough for the bottom hemisphere to sink in.
    float gap = 0.7f;
    float boxWidth = 5f;
    Vector3f basePos = new Vector3f(-10, 0, -40);

    // Left platform
    root.addChild(
        grayBox.blockOutBox(
            "TrapLeft",
            basePos.add(new Vector3f(-(boxWidth / 2 + gap / 2), 0, 0)),
            new Vector3f(boxWidth, 1, 5)));

    // Right platform
    root.addChild(
        grayBox.blockOutBox(
            "TrapRight",
            basePos.add(new Vector3f(boxWidth / 2 + gap / 2, 0, 0)),
            new Vector3f(boxWidth, 1, 5)));

    return root;
  }

  // ---------------------------------------------------
  // JUMP ZONE
  // ---------------------------------------------------

  private SceneNode createJumpZone() {
    SceneNode jumpZone = new SceneNode("JumpZone");

    for (int i = 0; i < 15; i++) {
      SceneNode platform =
          grayBox.blockOutBox(
              "Platform-" + i, new Vector3f(35 + (i * 4), i * -0.8f, 25), new Vector3f(2, 0.5f, 4));
      jumpZone.addChild(platform);
    }

    return jumpZone;
  }

  // ---------------------------------------------------
  // SPAWN BOX
  // ---------------------------------------------------

  private SceneNode createSpawnBox() {
    return grayBox.blockOutBox("SingleBox", new Vector3f(0, 0, 0), new Vector3f(3, 3, 3));
  }

  // ---------------------------------------------------
  // SINGLE BOX TEST
  // ---------------------------------------------------

  private SceneNode createSingleBoxZone() {
    return grayBox.blockOutBox("SingleBox", new Vector3f(15, 0, 15), new Vector3f(4, 4, 4));
  }

  // ---------------------------------------------------
  // CORNER TEST (Multi-contact)
  // ---------------------------------------------------

  private SceneNode createCornerZone() {

    SceneNode cornerRoot = new SceneNode("CornerZone");

    SceneNode wallA =
        grayBox.blockOutBox("CornerWallA", new Vector3f(-15, 0, 15), new Vector3f(8, 4, 1));

    SceneNode wallB =
        grayBox.blockOutBox("CornerWallB", new Vector3f(-11.5f, 0, 11.5f), new Vector3f(1, 4, 8));

    cornerRoot.addChild(wallA);
    cornerRoot.addChild(wallB);

    return cornerRoot;
  }

  // ---------------------------------------------------
  // NARROW PASSAGE (1.1 * capsule diameter)
  // ---------------------------------------------------

  private SceneNode createNarrowPassageZone() {

    SceneNode root = new SceneNode("NarrowPassage");

    float diameter = 2f * capsuleRadius;
    float gap = 1.1f * diameter;

    SceneNode wallLeft =
        grayBox.blockOutBox("PassageLeft", new Vector3f(-15, 0, -15), new Vector3f(1, 4, 8));

    SceneNode wallRight =
        grayBox.blockOutBox(
            "PassageRight", new Vector3f(-15 + gap + 1, 0, -15), new Vector3f(1, 4, 8));

    root.addChild(wallLeft);
    root.addChild(wallRight);

    return root;
  }

  // ---------------------------------------------------
  // CEILING (Head Bumping)
  // ---------------------------------------------------

  private SceneNode createCeilingZone() {
    return grayBox.blockOutBox(
        "Ceiling", new Vector3f(-30, -(capsuleHeight + 0.025f), -10), new Vector3f(2, 0.1f, 5));
  }

  // ---------------------------------------------------
  // THIN PILLAR (precision sweep test)
  // ---------------------------------------------------

  private SceneNode createThinPillarZone() {

    return grayBox.blockOutBox("ThinPillar", new Vector3f(0, 0, -20), new Vector3f(0.5f, 8, 0.5f));
  }

  // ---------------------------------------------------
  // RAMP (slope test)
  // ---------------------------------------------------

  private SceneNode createRampZone() {

    SceneNode ramp = grayBox.blockOutBox("Ramp", new Vector3f(20, 0, -15), new Vector3f(8, 2, 8));

    // rotate to create slope
    //    ramp.getTransform().setRotationEuler(-20, 0, 0);

    return ramp;
  }

  // ---------------------------------------------------
  // STEP (auto step test)
  // ---------------------------------------------------

  private SceneNode createStepZone() {

    return grayBox.blockOutBox("Step", new Vector3f(20, 0, 5), new Vector3f(4, stepHeight, 4));
  }

  // ---------------------------------------------------
  // HIGH WALL (blocking test)
  // ---------------------------------------------------

  private SceneNode createWallZone() {

    return grayBox.blockOutBox("HighWall", new Vector3f(0, 0, 30), new Vector3f(20, 12, 1));
  }
}
