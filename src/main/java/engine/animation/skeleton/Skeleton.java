package engine.animation.skeleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Skeleton {

  private Bone rootBone;

  private final Map<String, Bone> allBones;

  public Skeleton(Bone rootBone) {
    this.rootBone = rootBone;
    this.allBones = new HashMap<>();
    collectBones(rootBone);
  }

  private void collectBones(Bone bone) {
    allBones.put(bone.getName(), bone);
    for (Bone child : bone.getChildren()) {
      collectBones(child);
    }
  }

  public Bone getBone(String name) {
    return allBones.get(name);
  }

  public void update() {
    if (rootBone != null) {
      // recursion from top to bottom
      updateBoneRecursively(rootBone);
    }
  }

  private void updateBoneRecursively(Bone bone) {
    bone.updateGlobalTransform(); // global = parent.global * local
    for (Bone child : bone.getChildren()) {
      updateBoneRecursively(child);
    }
  }

  public Bone getRootBone() {
    return rootBone;
  }

  public void setRootBone(Bone rootBone) {
    this.rootBone = rootBone;
    allBones.clear();
    collectBones(rootBone);
  }

  public List<Bone> getAllBones() {
    return new ArrayList<>(allBones.values());
  }

  public void printHierarchy() {
    printHierarchy(rootBone, 0);
  }

  private void printHierarchy(Bone bone, int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    System.out.println(bone.getName());
    for (Bone child : bone.getChildren()) {
      printHierarchy(child, level + 1);
    }
  }
}
