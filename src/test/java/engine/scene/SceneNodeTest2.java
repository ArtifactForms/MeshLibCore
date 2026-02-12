package engine.scene;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import engine.components.AbstractComponent;
import engine.components.Transform;
import math.Vector3f;

public class SceneNodeTest2 {

	@Test
	public void testTransformIsNotNullByDefault() {
		SceneNode node = new SceneNode();
		Transform transform = node.getTransform();
		assertNotNull(transform);
	}

	@Test
	public void testTransformPositionIsAtWorldCenterByDefault() {
		Vector3f origin = new Vector3f();
		SceneNode node = new SceneNode();
		Transform transform = node.getTransform();
		assertEquals(origin, transform.getPosition());
	}

	@Test
	public void testSetName() {
		String expected = "NameABC123";
		SceneNode node = new SceneNode();
		node.setName(expected);
		assertEquals(expected, node.getName());
	}

	@Test
	public void testNodeIsActiveByDefault() {
		SceneNode node = new SceneNode();
		assertTrue(node.isActive());
	}

	@Test
	public void testNodeIsRootByDefault() {
		SceneNode node = new SceneNode();
		assertTrue(node.isRoot());
	}

	@Test
	public void testNodeIsLeafByDefault() {
		SceneNode node = new SceneNode();
		assertTrue(node.isLeaf());
	}

	@Test
	public void testIsNotRootAfterAddedToParent() {
		SceneNode parent = new SceneNode();
		SceneNode child = new SceneNode();
		parent.addChild(child);
		assertFalse(child.isRoot());
	}

	@Test
	public void testIsNotLeafAfterChildAdded() {
		SceneNode parent = new SceneNode();
		SceneNode child = new SceneNode();
		parent.addChild(child);
		assertFalse(parent.isLeaf());
	}

	@Test
	public void testConstructorWithName() {
		String expected = "Node-Name";
		SceneNode node = new SceneNode(expected);
		assertEquals(expected, node.getName());
	}

	@Test
	public void testGetOwnerAfterRemove() {
		SceneNode node = new SceneNode();
		TestComponent component = new TestComponent();
		node.addComponent(component);
		node.removeComponent(component);
		assertNull(component.getOwner());
	}

	@Test
	public void testGetOwnerAfterAddComponent() {
		SceneNode node = new SceneNode();
		TestComponent component = new TestComponent();
		node.addComponent(component);
		assertTrue(component.getOwner() == node);
	}

	@Test
	public void testAddComponentAttach() {
		SceneNode node = new SceneNode();
		TestComponent component = new TestComponent();

		assertFalse(component.attached);
		node.addComponent(component);
		assertTrue(component.attached);
	}

	public class TestComponent extends AbstractComponent {

		public int attachCount = 0;
		public int detachCount = 0;
		public boolean attached = false;

		@Override
		public void onAttach() {
			attachCount++;
			this.attached = true;
		}

		@Override
		public void onDetach() {
			detachCount++;
			this.attached = false;
		}

	}

}
