package mesh.animator;

public interface IAnimator {

	void update(float tpf);

	void onUpdate(float tpf);

	void restore();

	boolean isEnabled();

	void setEnabled(boolean enabled);

	boolean isFinished();

	void setFinished(boolean finished);

}
