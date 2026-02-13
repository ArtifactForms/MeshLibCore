package mesh.animator;

import java.util.ArrayList;
import java.util.List;

public class Animation {

  private boolean finished;

  private float timePerFrame;

  private long lastTime;

  private boolean running;

  private List<Animator> animators;

  public Animation() {
    animators = new ArrayList<Animator>();
  }

  public void start() {
    if (running) return;
    running = true;
    lastTime = System.currentTimeMillis();
  }

  public void stop() {
    if (!running) return;
    running = false;
  }

  public void update() {
    if (!running) return;
    updateTime();
    updateAnimators(timePerFrame);
  }

  public void restore() {
    finished = false;
    for (Animator animator : animators) {
      animator.restore();
    }
  }

  private void updateTime() {
    long time = System.currentTimeMillis();
    long delta = time - lastTime;
    lastTime = time;
    timePerFrame = delta / 1000f;
  }

  private void updateAnimators(float tpf) {
    if (finished) return;
    int finishedAnimatorsCount = 0;
    for (Animator animator : animators) {
      if (!animator.isFinished()) {
        animator.update(tpf);
      } else {
        finishedAnimatorsCount++;
      }
    }
    this.finished = finishedAnimatorsCount == animators.size();
  }

  public void addAnimator(Animator animator) {
    animators.add(animator);
  }

  public void removeAnimator(Animator animator) {
    animators.add(animator);
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }

  public boolean isFinished() {
    return finished;
  }

  public boolean isRunning() {
    return running;
  }

  public void clear() {
    animators.clear();
  }
}
