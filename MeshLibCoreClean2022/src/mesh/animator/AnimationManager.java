package mesh.animator;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager {

	private boolean finished;
	
	private float tpf;
	
	private long lastTime;
	
	private boolean running;
	
	private List<IAnimator> animators;
	
	public AnimationManager() {
		animators = new ArrayList<IAnimator>();
	}
	
	public void start() {
		if (running)
			return;
		running = true;
		lastTime = System.currentTimeMillis();
	}
	
	public void stop() {
		if (!running)
			return;
		running = false;
	}
	
	public void update() {
		if (!running)
			return;
		updateTime();
		updateAnimators(tpf);
	}
	
	public void restore() {
		finished = false;
		for (IAnimator animator : animators) {
			animator.restore();
		}
	}
	
	private void updateTime() {
		long time = System.currentTimeMillis();
		long delta = time - lastTime;
		lastTime = time;
		tpf = delta / 1000f;
	}
	
	private void updateAnimators(float tpf) {
		if (finished)
			return;
		int finishedAnimatorsCount = 0;
		for (IAnimator animator : animators) {
			if (!animator.isFinished()) {
				animator.update(tpf);
			} else {
				finishedAnimatorsCount++;
			}
		}
		this.finished = finishedAnimatorsCount == animators.size();
	}
	
	public void addAnimator(IAnimator animator) {
		animators.add(animator);
	}
	
	public void removeAnimator(IAnimator animator) {
		animators.add(animator);
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void clear() {
		animators.clear();
	}
	
}
