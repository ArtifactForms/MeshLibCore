package engine.animation.skeleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import math.Matrix4;

public class Bone {

  private final String name;
  private Bone parent;
  private final List<Bone> children;
  private Matrix4 localTransform;
  private Matrix4 globalTransform;
  private Matrix4 inverseBindPose;
  private boolean transformDirty = true;
  private float weight;
  private int index;

  public Bone(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    this.name = name;
    this.children = new ArrayList<>();
    this.localTransform = new Matrix4().identity();
    this.globalTransform = new Matrix4().identity();
    this.weight = 1.0f;
  }

  public void updateGlobalTransform() {
    if (!transformDirty) return;
    globalTransform =
        (parent != null) ? parent.globalTransform.multiply(localTransform) : localTransform;
    transformDirty = false;
  }

  public void updateChildrenTransforms() {
    for (Bone child : children) {
      child.markDirty();
      child.updateGlobalTransform();
      child.updateChildrenTransforms();
    }
  }

  public void markDirty() {
    transformDirty = true;
  }

  public void addChild(Bone child) {
    if (child == null) {
      throw new IllegalArgumentException("Child bone cannot be null.");
    }
    if (isAncestorOf(child)) {
      throw new IllegalArgumentException("Cannot add a child that would create a cycle.");
    }
    if (child.getParent() != null) {
      child.getParent().removeChild(child);
    }
    children.add(child);
    child.setParent(this);
  }

  private boolean isAncestorOf(Bone bone) {
    Bone current = this;
    while (current != null) {
      if (current == bone) {
        return true;
      }
      current = current.getParent();
    }
    return false;
  }

  public void removeChild(Bone child) {
    if (children.remove(child)) {
      child.setParent(null);
    }
  }

  private void setParent(Bone parent) {
    if (this.parent == parent) return;
    this.parent = parent;
  }

  public String getName() {
    return name;
  }

  public Bone getParent() {
    return parent;
  }

  public List<Bone> getChildren() {
    return Collections.unmodifiableList(children);
  }

  public Matrix4 getLocalTransform() {
    return new Matrix4(localTransform);
  }

  public void setLocalTransform(Matrix4 localTransform) {
    if (localTransform == null) {
      throw new IllegalArgumentException("LocalTransform cannot be null.");
    }
    this.localTransform = localTransform;
    markDirty();
  }

  public Matrix4 getGlobalTransform() {
    return new Matrix4(globalTransform);
  }

  public Matrix4 getInverseBindPose() {
    return new Matrix4(inverseBindPose);
  }

  public void setInverseBindPose(Matrix4 bindPose) {
    this.inverseBindPose = bindPose.invert();
  }

  public float getWeight() {
    return weight;
  }

  public void setWeight(float weight) {
    if (weight < 0.0f || weight > 1.0f) {
      throw new IllegalArgumentException("Weight must be between 0.0 and 1.0.");
    }
    this.weight = weight;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public void printHierarchy(int depth) {
    System.out.println("  ".repeat(depth) + name);
    for (Bone child : children) {
      child.printHierarchy(depth + 1);
    }
  }
}
