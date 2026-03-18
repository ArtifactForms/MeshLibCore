package server.scheduler;

import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerScheduler {

  public static class Task {
    private boolean cancelled = false;

    public void cancel() {
      cancelled = true;
    }

    public boolean isCancelled() {
      return cancelled;
    }
  }

  private static class ScheduledTask implements Comparable<ScheduledTask> {
    long executeTick;
    final long period;
    final Runnable runnable;
    final Task handle;

    ScheduledTask(long executeTick, long period, Runnable runnable, Task handle) {
      this.executeTick = executeTick;
      this.period = period;
      this.runnable = runnable;
      this.handle = handle;
    }

    @Override
    public int compareTo(ScheduledTask other) {
      return Long.compare(this.executeTick, other.executeTick);
    }
  }

  private final PriorityQueue<ScheduledTask> queue = new PriorityQueue<>();
  private final Set<Task> activeTasks = ConcurrentHashMap.newKeySet();

  public Task schedule(long currentTick, long delay, Runnable runnable) {
    Task handle = new Task();
    activeTasks.add(handle);
    queue.add(new ScheduledTask(currentTick + delay, -1, runnable, handle));
    return handle;
  }

  public Task scheduleRepeating(long currentTick, long delay, long period, Runnable runnable) {
    Task handle = new Task();
    activeTasks.add(handle);
    queue.add(new ScheduledTask(currentTick + delay, period, runnable, handle));
    return handle;
  }

  public void cancelAll() {
    for (Task task : activeTasks) {
      task.cancel();
    }
    activeTasks.clear();
    queue.clear();
  }

  public void tick(long currentTick) {
    while (!queue.isEmpty()) {
      ScheduledTask sTask = queue.peek();
      if (sTask.executeTick > currentTick) break;

      queue.poll();

      if (!sTask.handle.isCancelled()) {
        try {
          sTask.runnable.run();
        } catch (Exception e) {
          e.printStackTrace();
        }

        if (sTask.period > 0 && !sTask.handle.isCancelled()) {
          sTask.executeTick = currentTick + sTask.period;
          queue.add(sTask);
        } else {
          activeTasks.remove(sTask.handle);
        }
      } else {
        activeTasks.remove(sTask.handle);
      }
    }
  }
}
