package engine;

/**
 * The {@code Timer} class provides a utility for tracking elapsed time, frames
 * per second (FPS), and time scaling for games or applications. It uses
 * nanosecond precision for timekeeping and offers features such as formatted
 * time representation, time-per-frame calculation, and slow-motion or speed-up
 * effects via time scaling.
 *
 * <p>
 * Key features include:
 * <ul>
 * <li>Tracking total elapsed time (scaled and unscaled).</li>
 * <li>Calculating frames per second (FPS).</li>
 * <li>Formatting time as hours:minutes:seconds.</li>
 * <li>Adjustable time scaling for slow-motion or fast-forward effects.</li>
 * </ul>
 * 
 * This class is designed to be updated on every frame of an application or
 * game.
 */
public class Timer {

	/**
	 * Real world time when the timer started.
	 */
	private long startTime;

	/** The last recorded time in nanoseconds. */
	private long lastTime;

	/** The time in milliseconds taken for the last frame. */
	private float time;

	/** Accumulates milliseconds for FPS calculation. */
	private long millisecondCounter;

	/** The frame count during the last FPS update. */
	private int lastFrameCount;

	/** The calculated frames per second (FPS). */
	private int fps;

	/** Total elapsed time in milliseconds. */
	private long totalTime;

	/** Scaling factor for time (default is 1.0 for real-time). */
	private float timeScale;

	/** Total number of frames since the Timer started. */
	private int frameCount;

	/**
	 * Constructs a {@code Timer} with a default time scale of 1.0.
	 */
	public Timer() {
		this.startTime = System.nanoTime();
		this.lastTime = System.nanoTime();
		this.time = 0;
		this.totalTime = 0;
		this.timeScale = 1f;
		this.frameCount = 0;
	}

	/**
	 * Constructs a {@code Timer} with the specified initial time scale.
	 *
	 * @param initialTimeScale the initial time scaling factor
	 */
	public Timer(float initialTimeScale) {
		this.timeScale = initialTimeScale;
	}

	/**
	 * Returns the current frames per second (FPS).
	 *
	 * @return the frames per second
	 */
	public float getFrameRate() {
		return fps;
	}

	/**
	 * Updates the FPS calculation based on the accumulated milliseconds.
	 */
	private void updateFPS() {
		millisecondCounter += time;
		if (millisecondCounter >= 1000) {
			millisecondCounter = 0;
			fps = frameCount - lastFrameCount;
			lastFrameCount = frameCount;
		}
	}

	/**
	 * Updates the Timer. This method must be called once per frame to ensure
	 * accurate time tracking.
	 */
	public void update() {
		long currentTime = System.nanoTime();
		time = (currentTime - lastTime) / 1_000_000.0f; // Convert to milliseconds
		lastTime = currentTime;
		totalTime += time;
		frameCount++;
		updateFPS();
	}

	/**
	 * Resets the {@code Timer} to its initial state, clearing all accumulated
	 * time and frame count values. This includes resetting the following:
	 * <ul>
	 * <li>The start time to the current system time.</li>
	 * <li>The last time recorded to the current system time.</li>
	 * <li>The total elapsed time to zero.</li>
	 * <li>The frame count to zero.</li>
	 * <li>The frames per second (FPS) to zero.</li>
	 * <li>The millisecond counter to zero.</li>
	 * <li>The last frame count for FPS calculation to zero.</li>
	 * </ul>
	 * <p>
	 * This method can be used when you need to restart the timer, such as for
	 * restarting the game / application or resetting the simulation state.
	 * </p>
	 */
	public void reset() {
		this.startTime = System.nanoTime();
		this.lastTime = startTime;
		this.time = 0;
		this.totalTime = 0;
		this.frameCount = 0;
		this.fps = 0;
		this.millisecondCounter = 0;
		this.lastFrameCount = 0;
	}

	/**
	 * Returns the total elapsed time in seconds, scaled by the current time
	 * scale.
	 *
	 * @return the scaled total elapsed time in seconds
	 */
	public float getTotalTime() {
		return totalTime / 1000.0f * timeScale;
	}

	/**
	 * Returns the total elapsed time in seconds, independent of the time scale.
	 *
	 * @return the unscaled total elapsed time in seconds
	 */
	public float getUnscaledTotalTime() {
		return totalTime / 1000.0f;
	}

	/**
	 * Returns a formatted string representing the scaled total time in the format
	 * HH:MM:SS.
	 *
	 * @return the formatted scaled total time
	 */
	public String getFormattedTotalTime() {
		return formatTime(getTotalTime());
	}

	/**
	 * Returns a formatted string representing the unscaled total time in the
	 * format HH:MM:SS.
	 *
	 * @return the formatted unscaled total time
	 */
	public String getUnscaledFormattedTotalTime() {
		return formatTime(getUnscaledTotalTime());
	}

	/**
	 * Returns the time it took to complete the last frame in seconds, scaled by
	 * the current time scale.
	 *
	 * @return the scaled time per frame in seconds
	 */
	public float getTimePerFrame() {
		return time / 1000.0f * timeScale;
	}

	/**
	 * Returns the time it took to complete the last frame in seconds, independent
	 * of the time scale.
	 *
	 * @return the unscaled time per frame in seconds
	 */
	public float getUnscaledTimePerFrame() {
		return time / 1000.0f;
	}

	/**
	 * Returns the current time scaling factor.
	 *
	 * @return the time scale
	 */
	public float getTimeScale() {
		return timeScale;
	}

	/**
	 * Sets the time scaling factor. A value of 1.0 represents real-time, values
	 * less than 1.0 slow down time, and values greater than 1.0 speed up time.
	 *
	 * @param timeScale the new time scaling factor
	 */
	public void setTimeScale(float timeScale) {
		this.timeScale = timeScale;
	}

	/**
	 * Returns the real-time elapsed since the game / application started,
	 * measured in seconds.
	 * 
	 * <p>
	 * This method uses `System.nanoTime()` to obtain a high-precision timestamp.
	 * The returned value is a `float` representing the elapsed time in seconds.
	 * </p>
	 *
	 * @return The real-time elapsed since the game started, in seconds.
	 */
	public float getRealtimeSinceStartup() {
		return (System.nanoTime() - startTime) / 1_000_000_000.0f;
	}

	/**
	 * Returns the real-time elapsed since the game / application started,
	 * measured in seconds, as a `double` value.
	 * 
	 * <p>
	 * This method uses `System.nanoTime()` to obtain a high-precision timestamp.
	 * The returned value is a `double` representing the elapsed time in seconds,
	 * providing higher precision than the `float` version.
	 * </p>
	 *
	 * @return The real-time elapsed since the game started, in seconds, as a
	 *         `double`.
	 */
	public double getRealtimeSinceStartupAsDouble() {
		return (System.nanoTime() - startTime) / 1_000_000_000.0;
	}

	/**
	 * Returns the total number of frames that have passed since the Timer
	 * started.
	 *
	 * @return the total frame count
	 */
	public int getFrameCount() {
		return frameCount;
	}

	/**
	 * Formats a time value in seconds into a string in the format HH:MM:SS.
	 *
	 * @param timeInSeconds the time in seconds to format
	 * @return the formatted time string
	 */
	private String formatTime(float timeInSeconds) {
		int s = (int) timeInSeconds;
		return String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);
	}

	/**
	 * Returns a string representation of this Timer, showing its current state.
	 *
	 * @return a string representation of the Timer
	 */
	@Override
	public String toString() {
		return "Timer [millisecondCounter=" + millisecondCounter
		    + ", lastFrameCount=" + lastFrameCount + ", fps=" + fps + ", lastTime="
		    + lastTime + ", time=" + time + ", totalTime=" + totalTime
		    + ", timeScale=" + timeScale + ", frameCount=" + frameCount + "]";
	}

}
