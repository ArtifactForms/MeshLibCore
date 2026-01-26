package engine.demos.jam26.combat;

import engine.components.AbstractComponent;
import engine.components.Geometry;
import engine.components.StaticGeometry;
import engine.debug.core.DebugDraw;
import engine.demos.jam26.assets.AssetRefs;
import engine.demos.jam26.enemy.HitReactionComponent;
import engine.demos.ray.RaycastHit;
import engine.demos.ray.RaycastQuery;
import engine.demos.ray.Raycaster;
import engine.input.Input;
import engine.scene.Scene;
import engine.scene.SceneNode;
import engine.scene.audio.SoundManager;
import engine.scene.camera.Camera;
import math.Bounds;
import math.Color;
import math.Ray3f;

public class ShootComponent extends AbstractComponent {
  private final Input input;
  private final float range = 1000f;
  private boolean lastPressed;
  private Bounds bounds;
  private Ray3f ray;

  public ShootComponent(Input input) {
    this.input = input;
  }

  @Override
  public void onUpdate(float tpf) {
    boolean pressed = input.isMousePressed(Input.LEFT);
    if (pressed && !lastPressed) {
      shoot();
      SoundManager.playEffect(AssetRefs.SOUND_SHOOT_KEY);
    }
    lastPressed = pressed;
    Camera camera = getOwner().getScene().getActiveCamera();
    ray = Raycaster.crossHairRay(camera);
    RaycastQuery query = new RaycastQuery(ray);
    getOwner().getScene().visitRootNodes(query);
    RaycastHit hit = query.getResult();
    if (hit != null) {
      Geometry geometry = hit.getTarget().getComponent(Geometry.class);
      if (geometry != null) {
        bounds = geometry.getWorldBounds();
      }
      StaticGeometry geometry2 = hit.getTarget().getComponent(StaticGeometry.class);
      if (geometry2 != null) {
        bounds = geometry2.getWorldBounds();
      }
    } else {
      bounds = null;
    }
    DebugDraw.drawBounds(bounds, Color.YELLOW);
  }

  private void shoot() {
    Scene scene = getOwner().getScene();
    Camera cam = scene.getActiveCamera();
    Ray3f ray = Raycaster.crossHairRay(cam);
    RaycastSphereQuery query = new RaycastSphereQuery(ray);
    scene.visitRootNodes(query);
    RaycastHit hit = query.getResult();
    if (hit != null) {
      SceneNode target = hit.getTarget();
      if (target != null) {
        HitReactionComponent component = target.getComponent(HitReactionComponent.class);
        if (component != null) {
          component.hit(hit);
        }
      }
    }
  }
}
