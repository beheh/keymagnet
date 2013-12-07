package de.beheh.keymagnet;

/**
 * A single riddle consisting of a riddle string and an origin, if riddle is
 * result from different riddle.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class Riddle {

	private final String riddle;
	private final Riddle originalRiddle;

	@SuppressWarnings("LeakingThisInConstructor")
	public Riddle(String riddle) {
		this.riddle = riddle;
		this.originalRiddle = this;
	}

	public Riddle(String riddle, Riddle originaRiddle) {
		this.riddle = riddle;
		this.originalRiddle = originaRiddle;
	}

	public Riddle getOriginalRiddle() {
		return originalRiddle;
	}

	@Override
	public final String toString() {
		return riddle;
	}

}
