package de.beheh.keymagnet;

import java.util.ArrayList;
import java.util.List;

/**
 * Solves a specific type of riddle.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public abstract class Candidate implements Runnable {

	protected final Riddle riddle;
	protected final RiddleMaster riddleMaster;
	private boolean finished;

	public final boolean isFinished() {
		return finished;
	}

	protected final List<RiddleResult> results;

	public final List<RiddleResult> getResults() {
		return results;
	}

	public Candidate(Riddle riddle, RiddleMaster riddleMaster) {
		results = new ArrayList<>();
		this.riddle = riddle;
		this.riddleMaster = riddleMaster;
	}

	protected void complete() {
		finished = true;
		riddleMaster.getThread().interrupt();
	}
}
