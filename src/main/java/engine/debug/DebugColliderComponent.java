package engine.debug;

import java.util.List;

import engine.collision.collider.AABBCollider;
import engine.collision.collider.CapsuleCollider;
import engine.collision.collider.Collider;
import engine.collision.collider.SphereCollider;
import engine.collision.component.ColliderComponent;
import engine.components.AbstractComponent;
import engine.debug.core.DebugDraw;
import engine.scene.SceneNode;
import math.Bounds;
import math.Color;
import math.Vector3f;

/**
 * Debug component that renders all collider shapes in the scene.
 *
 * This component traverses the scene graph every frame and visualizes:
 * - Sphere colliders
 * - AABB colliders
 * - Capsule colliders
 *
 * Intended for development/debugging only.
 */
public class DebugColliderComponent extends AbstractComponent {

    private final Color color;

    public DebugColliderComponent() {
        this(Color.BLUE);
    }

    public DebugColliderComponent(Color color) {
        this.color = color;
    }

    @Override
    public void onUpdate(float tpf) {
        getOwner().getScene().visitRootNodes(this::visitNode);
    }

    /* =========================================================
       Scene Traversal
       ========================================================= */

    private void visitNode(SceneNode node) {

        List<ColliderComponent> components =
                node.getComponents(ColliderComponent.class);

        for (ColliderComponent component : components) {
            renderCollider(component);
        }
    }

    /* =========================================================
       Rendering
       ========================================================= */

    private void renderCollider(ColliderComponent component) {

        Collider collider = component.getCollider();
        Vector3f worldPos = component.getWorldPosition();

        switch (collider.getType()) {

            case SPHERE -> renderSphere((SphereCollider) collider, worldPos);

            case AABB -> renderAABB((AABBCollider) collider, worldPos);

            case CAPSULE -> renderCapsule((CapsuleCollider) collider, worldPos);
        }
    }

    private void renderSphere(SphereCollider collider, Vector3f position) {

        DebugDraw.drawSphere(
                position,
                collider.getRadius(),
                color
        );
    }

    private void renderAABB(AABBCollider collider, Vector3f position) {

        Bounds bounds = Bounds.fromCenterSize(
                position,
                collider.getHalfExtents().mult(2f)
        );

        DebugDraw.drawBounds(bounds, color);
    }

    private void renderCapsule(CapsuleCollider collider, Vector3f position) {

        DebugDraw.drawCapsule(
                collider.getRadius(),
                collider.getHalfHeight(),
                position,
                color
        );
    }
}