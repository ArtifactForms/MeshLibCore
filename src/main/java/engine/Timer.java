package engine;

/**
 * This class implements a simple timer. The timer calculates the total time (in
 * seconds) since the first update of this timer, the number of frames per
 * second, the time per frame and the total number of frames since the first
 * update. Furthermore it provides time scaling for slow motion effects and
 * speed up the game.
 * 
 * @author - Simon
 * @version 0.2, 5 December 2014
 */
public class Timer {

	// Used to calculate the frames per second.
	private long lastTime;

	// Used to calculate the frames per second.
	private float time;

	// Used to calculate the frames per second.
	private long secondCount;

	// Used to calculate the frames per second.
	private int lastFrameCount;

	/**
	 * Frames per second.
	 */
	private int fps;

	/**
	 * The time in milliseconds that has passed since the first update of this
	 * timer.
	 */
	private long totalTime;

	/**
	 * The scale at which the time is passing. This can be used for slow motion
	 * effects.
	 */
	private float timeScale;

	/**
	 * The total number of frames that have passed since the first update of
	 * this timer.
	 */
	private int frameCount;

	/**
	 * Constructs a new instance of this timer.
	 */
	public Timer() {
		this.lastTime = System.currentTimeMillis();
		this.time = 0;
		this.totalTime = 0;
		this.timeScale = 1f;
		this.frameCount = 0;
	}

	/**
	 * Returns the frames per second.
	 * 
	 * @return the frames per second
	 */
	public float getFrameRate() {
		return fps;
	}

	/**
	 * Updates the frames per second value.
	 */
	private void updateFPS() {
		secondCount += time;
		if (secondCount >= 1000) {
			secondCount = 0;
			fps = frameCount - lastFrameCount;
			lastFrameCount = frameCount;
		}
	}

	/**
	 * Updates this timer. This method must invoked each frame to ensure
	 * accurate timer functionality.
	 */
	public void update() {
		time = System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
		totalTime += time;
		frameCount++;
		updateFPS();
	}

	/**
	 * Returns the scaled time in seconds that has passed since the first update
	 * of this timer. This is usually the time in seconds since the start of the
	 * game multiplied by the time scale.
	 * 
	 * @return the scaled time in seconds that has passed since the first update
	 *         of this timer
	 * @see #getUnscaledTotalTime()
	 */
	public float getTotalTime() {
		return totalTime / 1000.0f * timeScale;
	}

	/**
	 * Returns the time scale independent time in seconds that has passed since
	 * the first update of this timer. This is usually the time in seconds since
	 * the start of the game.
	 * 
	 * @return the time in seconds that has passes since the first update of
	 *         this timer
	 * @see #getTotalTime()
	 */
	public float getUnscaledTotalTime() {
		return totalTime / 1000.0f;
	}

	/**
	 * Returns a string representation of the scaled total time with the
	 * following format: <b>hours:minutes:seconds</b>. The total time is the
	 * time that has passed since the first update of this timer.
	 * 
	 * @return a formatted string representation of totalTime
	 * @see #getUnscaledFormattedTotalTime()
	 * @see #getTotalTime()
	 * @see #getUnscaledTotalTime()
	 */
	public String getFormattedTotalTime() {
		int s = (int) (this.totalTime * timeScale / 1000);
		String result = String.format("%d:%02d:%02d", s / 3600,
				(s % 3600) / 60, (s % 60));
		return result;
	}

	/**
	 * Returns a time scale independent string representation of the total time
	 * with the following format: <b>hours:minutes:seconds</b>. The total time
	 * is the time that has passed since the first update of this timer.
	 * 
	 * @return a formatted, time scale independent string representation of
	 *         total time
	 * @see #getFormattedTotalTime()
	 * @see #getTotalTime()
	 * @see #getUnscaledTotalTime()
	 */
	public String getUnscaledFormattedTotalTime() {
		int s = (int) (this.totalTime / 1000);
		String result = String.format("%d:%02d:%02d", s / 3600,
				(s % 3600) / 60, (s % 60));
		return result;
	}

	/**
	 * Returns the time in seconds it took to complete the last frame.
	 * 
	 * @return the time in seconds it took to complete the last frame
	 */
	public float getTimePerFrame() {
		return time / 1000.0f * timeScale;
	}
	
	/**
	 * Returns the time scale independent time in seconds it took to complete the last frame.
	 * 
	 * @return the time scale independent time in seconds it took to complete the last frame
	 */
	public float getUnscaledTimePerFrame() {
		return time / 1000.0f;
	}

	/**
	 * Returns the scale at which time is passing.
	 * 
	 * @return the scale at which time is passing
	 */
	public float getTimeScale() {
		return timeScale;
	}

	/**
	 * Sets the scale at which time is passing to the specified new value. This
	 * can be used for slow motion effects.
	 * 
	 * @param timeScale
	 *            the specified new value
	 */
	public void setTimeScale(float timeScale) {
		this.timeScale = timeScale;
	}

	/**
	 * Returns the total number of frames that have passed since the first
	 * update of this timer.
	 * 
	 * @return the total number of frames that have passed
	 */
	public int getFrameCount() {
		return frameCount;
	}

	/**
	 * Returns a string representation of this timer.
	 */
	@Override
	public String toString() {
		return "Timer [secondCount=" + secondCount + ", lastFrameCount="
				+ lastFrameCount + ", fps=" + fps + ", lastTime=" + lastTime
				+ ", time=" + time + ", totalTime=" + totalTime
				+ ", timeScale=" + timeScale + ", frameCount=" + frameCount
				+ "]";
	}

}
