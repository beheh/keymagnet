package de.beheh.keymagnet;

/**
 * The result of a single riddle.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class RiddleResult {

	private final String result;

	@Override
	public String toString() {
		return result;
	}

	private float confidence = 0;

	public float getConfidence() {
		return confidence;
	}

	private final Riddle riddle;

	public Riddle getRiddle() {
		return riddle;
	}

	private int count = 0;

	public void increment() {
		count++;
	}

	public int getCount() {
		return count;
	}

	public RiddleResult(String result, float confidence, Riddle riddle) {
		this.result = result;
		this.confidence = confidence;
		this.riddle = riddle;
	}

}
