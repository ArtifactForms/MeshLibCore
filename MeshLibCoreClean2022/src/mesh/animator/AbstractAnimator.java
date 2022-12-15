package mesh.animator;

public abstract class AbstractAnimator implements IAnimator {

    private boolean enabled;
    private boolean finished;

    public AbstractAnimator() {
	enabled = true;
    }

    @Override
    public boolean isEnabled() {
	return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    @Override
    public boolean isFinished() {
	return finished;
    }

    @Override
    public void setFinished(boolean finished) {
	this.finished = finished;
    }

    @Override
    public void update(float tpf) {
	if (!enabled || finished)
	    return;
	onUpdate(tpf);
    }

}
