package mesh.animator;

public interface IAnimator {
	
	public void update(float tpf);
	
	public void onUpdate(float tpf);
	
	public void restore();
	
	public boolean isEnabled();
	
	public void setEnabled(boolean enabled);

	public boolean isFinished();
	
	public void setFinished(boolean finished);
	
}

