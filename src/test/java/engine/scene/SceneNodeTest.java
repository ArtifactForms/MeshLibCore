package engine.scene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import engine.components.AbstractComponent;
import engine.components.Component;
import engine.components.RenderableComponent;
import engine.components.Transform;
import math.Vector3f;

class SceneNodeTest {

	/*
	 * ------------------------------ Test doubles ------------------------------
	 */

	static class TestComponent extends AbstractComponent {
		boolean updated = false;
		boolean cleanedUp = false;

		@Override
		public void update(float tpf) {
			updated = true;
		}

//        @Override
//        public void cleanup() {
//            cleanedUp = true;
//        }
	}

	static class TestRenderable extends AbstractComponent implements RenderableComponent {
		boolean rendered = false;

		@Override
		public void render(workspace.ui.Graphics g) {
			rendered = true;
		}
	}

	static class RecordingVisitor implements SceneNodeVisitor {
		final List<SceneNode> visited = new ArrayList<>();

		@Override
		public void visit(SceneNode node) {
			visited.add(node);
		}
	}

	/*
	 * ------------------------------ Construction ------------------------------
	 */

	@Test
	void defaultConstructor_createsActiveRootNode() {
		SceneNode node = new SceneNode();

		assertTrue(node.isActive(), "Node should be active by default");
		assertTrue(node.isRoot(), "New node should be root");
		assertTrue(node.isLeaf(), "New node should be leaf");
		assertNull(node.getScene(), "Node should not belong to a scene initially");
	}

	@Test
	void namedConstructor_setsNameAndComponents() {
		TestComponent c1 = new TestComponent();
		TestComponent c2 = new TestComponent();

		SceneNode node = new SceneNode("Player", c1, c2);

		assertEquals("Player", node.getName());
		assertSame(c1, node.getComponent(TestComponent.class));
		assertEquals(2, node.getComponents(Component.class).size());
	}

	/*
	 * ------------------------------ Child hierarchy ------------------------------
	 */

	@Test
	void addChild_setsParentAndLeafState() {
		SceneNode parent = new SceneNode("Parent");
		SceneNode child = new SceneNode("Child");

		parent.addChild(child);

		assertFalse(parent.isLeaf());
		assertTrue(child.isLeaf());
		assertFalse(child.isRoot());
	}

	@Test
	void removeChild_detachesWithoutDestroying() {
		SceneNode parent = new SceneNode();
		SceneNode child = new SceneNode();

		parent.addChild(child);
		parent.removeChild(child);

		assertTrue(parent.isLeaf());
		assertTrue(child.isRoot());
	}

	/*
	 * ------------------------------ Components ------------------------------
	 */

	@Test
	void addAndRemoveComponent() {
		SceneNode node = new SceneNode();
		TestComponent component = new TestComponent();

		node.addComponent(component);
		assertSame(component, node.getComponent(TestComponent.class));

		node.removeComponent(component);
		assertNull(node.getComponent(TestComponent.class));
	}

	@Test
	void getComponents_returnsAllMatchingTypes() {
		SceneNode node = new SceneNode();
		TestComponent c1 = new TestComponent();
		TestComponent c2 = new TestComponent();

		node.addComponent(c1);
		node.addComponent(c2);

		List<TestComponent> components = node.getComponents(TestComponent.class);
		assertEquals(2, components.size());
	}

	/*
	 * ------------------------------ Active state ------------------------------
	 */

	@Test
	void setActive_propagatesToChildren() {
		SceneNode root = new SceneNode();
		SceneNode child = new SceneNode();
		SceneNode grandChild = new SceneNode();

		root.addChild(child);
		child.addChild(grandChild);

		root.setActive(false);

		assertFalse(root.isActive());
		assertFalse(child.isActive());
		assertFalse(grandChild.isActive());
	}

	/*
	 * ------------------------------ Visitor traversal
	 * ------------------------------
	 */

	@Test
	void accept_visitsNodesDepthFirst() {
		SceneNode root = new SceneNode("root");
		SceneNode a = new SceneNode("a");
		SceneNode b = new SceneNode("b");
		SceneNode c = new SceneNode("c");

		root.addChild(a);
		root.addChild(b);
		a.addChild(c);

		RecordingVisitor visitor = new RecordingVisitor();
		root.accept(visitor);

		assertEquals(List.of(root, a, c, b), visitor.visited, "Traversal should be depth-first, pre-order");
	}

	@Test
	void accept_nullVisitorThrows() {
		SceneNode node = new SceneNode();
		assertThrows(IllegalArgumentException.class, () -> node.accept(null));
	}

	/*
	 * ------------------------------ Lifecycle ------------------------------
	 */

	@Test
	void update_callsComponentUpdate() {
		SceneNode node = new SceneNode();
		TestComponent component = new TestComponent();

		node.addComponent(component);
		node.update(0.016f);

		assertTrue(component.updated);
	}

	@Test
	void cleanup_callsComponentCleanupRecursively() {
		SceneNode root = new SceneNode();
		SceneNode child = new SceneNode();

		TestComponent c1 = new TestComponent();
		TestComponent c2 = new TestComponent();

		root.addComponent(c1);
		child.addComponent(c2);
		root.addChild(child);

		root.cleanup();

		assertTrue(c1.cleanedUp);
		assertTrue(c2.cleanedUp);
	}

	@Test
	void destroy_detachesNodeButDoesNotCleanup() {
		SceneNode parent = new SceneNode();
		SceneNode child = new SceneNode();
		TestComponent component = new TestComponent();

		child.addComponent(component);
		parent.addChild(child);

		child.destroy();

		assertTrue(child.isRoot(), "Destroyed node should be detached");
		assertFalse(component.cleanedUp, "Destroy must not call cleanup");
	}

	/*
	 * ------------------------------ Transform & world position
	 * ------------------------------
	 */

	@Test
	void worldPosition_withoutParent_equalsLocalPosition() {
		SceneNode node = new SceneNode();
		Transform transform = node.getTransform();

		transform.setPosition(new Vector3f(1, 2, 3));

		assertEquals(new Vector3f(1, 2, 3), node.getWorldPosition());
	}
}
