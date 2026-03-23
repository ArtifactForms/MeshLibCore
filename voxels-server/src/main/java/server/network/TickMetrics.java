package server.network;

public class TickMetrics {

  // ---- timing (nanoseconds)
  public long tickStart;

  public long processPacketsTime;
  public long updatePlayersTime;
  public long chunkStreamingTime;
  public long entityUpdateTime;
  public long flushNetworkTime;
  public long unloadChunksTime;

  public long tickDuration;

  // ---- counters
  public int inboundPackets;
  public int outboundPackets;

  public int loadedChunks;
  public int generatedChunks;
  public int recompressedChunks;

  public int chunksInMemory;
  public int dirtyChunks;

  public int saveQueueSize;
  public long saveDuration;

  public void reset() {
    processPacketsTime = 0;
    updatePlayersTime = 0;
    chunkStreamingTime = 0;
    entityUpdateTime = 0;
    flushNetworkTime = 0;
    unloadChunksTime = 0;

    inboundPackets = 0;
    outboundPackets = 0;

    loadedChunks = 0;
    generatedChunks = 0;
    recompressedChunks = 0;

    chunksInMemory = 0;
    dirtyChunks = 0;

    saveQueueSize = 0;
    saveDuration = 0;
  }

  private double nsToMs(long ns) {
	  return ns / 1_000_000.0;
	}
  
  @Override
  public String toString() {
    return String.format(
        """
        [PERF]
        Tick: total=%.2fms | packets=%.2fms | players=%.2fms | streaming=%.2fms | unload=%.2fms | entities=%.2fms | net=%.2fms

        [NET]
        inbound=%d | outbound=%d

        [CHUNKS]
        loaded=%d | generated=%d | recompressed=%d
        inMemory=%d | dirty=%d

        [SAVE]
        queue=%d | time=%.2fms
        """,

        nsToMs(tickDuration),
        nsToMs(processPacketsTime),
        nsToMs(updatePlayersTime),
        nsToMs(chunkStreamingTime),
        nsToMs(unloadChunksTime),
        nsToMs(entityUpdateTime),
        nsToMs(flushNetworkTime),

        inboundPackets,
        outboundPackets,

        loadedChunks,
        generatedChunks,
        recompressedChunks,

        chunksInMemory,
        dirtyChunks,

        saveQueueSize,
        nsToMs(saveDuration)
    );
  }
}
