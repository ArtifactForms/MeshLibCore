package engine.animation.skeleton;

import java.util.ArrayList;
import java.util.List;

public class Skeleton {

  private Bone rootBone;

  private final List<Bone> allBones;

  public Skeleton(Bone rootBone) {
    this.rootBone = rootBone;
    this.allBones = new ArrayList<>();
    collectBones(rootBone);
  }

  private void collectBones(Bone bone) {
    allBones.add(bone);
    for (Bone child : bone.getChildren()) {
      collectBones(child);
    }
  }

  public void update() {
    if (rootBone != null) {
      rootBone.updateGlobalTransform();
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
    return allBones;
  }
}
