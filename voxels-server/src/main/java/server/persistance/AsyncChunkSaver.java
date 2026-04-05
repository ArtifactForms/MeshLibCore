package server.persistance;

import java.util.concurrent.ArrayBlockingQueue;

import common.logging.Log;

public class AsyncChunkSaver {

  private static final int QUEUE_SIZE = 10_000;

  private final ArrayBlockingQueue<Runnable> saveQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);

  private volatile boolean running = true;

  private final Thread workerThread;

  public AsyncChunkSaver() {
    workerThread = new Thread(this::run, "Chunk-Save-Worker");
    workerThread.setDaemon(true);
    workerThread.start();
  }

  private void run() {
    while (running) {
      try {
        Runnable task = saveQueue.take();
        task.run();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } catch (Exception e) {
        Log.error("Error while saving chunk async", e);
      }
    }
  }

  public void submit(Runnable task) {
    boolean success = saveQueue.offer(task);

    if (!success) {
      Log.warn("Chunk save queue FULL! Dropping save task!");
    }
  }

  public void shutdown() {
    running = false;
    workerThread.interrupt();
  }
}
