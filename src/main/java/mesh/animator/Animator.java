package mesh.animator;

public interface Animator {

  void update(float tpf);

  void onUpdate(float tpf);

  void restore();

  boolean isEnabled();

  void setEnabled(boolean enabled);

  boolean isFinished();

  void setFinished(boolean finished);
}
