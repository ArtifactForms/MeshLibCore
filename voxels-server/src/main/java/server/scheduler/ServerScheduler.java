package server.scheduler;

import java.util.PriorityQueue;

public class ServerScheduler {

  private static class ScheduledTask implements Comparable<ScheduledTask> {

    final long executeTick;
    final long period;
    final Runnable task;

    ScheduledTask(long executeTick, long period, Runnable task) {
      this.executeTick = executeTick;
      this.period = period;
      this.task = task;
    }

    @Override
    public int compareTo(ScheduledTask other) {
      return Long.compare(this.executeTick, other.executeTick);
    }
  }

  private final PriorityQueue<ScheduledTask> queue = new PriorityQueue<>();

  public void schedule(long currentTick, long delay, Runnable task) {
    queue.add(new ScheduledTask(currentTick + delay, -1, task));
  }

  public void scheduleRepeating(long currentTick, long delay, long period, Runnable task) {
    queue.add(new ScheduledTask(currentTick + delay, period, task));
  }

  public void tick(long currentTick) {

    while (!queue.isEmpty()) {

      ScheduledTask task = queue.peek();

      if (task.executeTick > currentTick) {
        break;
      }

      queue.poll();

      try {
        task.task.run();
      } catch (Exception e) {
        e.printStackTrace();
      }

      if (task.period > 0) {
        queue.add(new ScheduledTask(currentTick + task.period, task.period, task.task));
      }
    }
  }
}
