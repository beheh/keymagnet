package de.beheh.keymagnet;

import java.util.Queue;

/**
 * Collects riddles from a source.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public abstract class Collector implements Runnable {

	final protected Queue<Riddle> queue;

	public Collector(Queue<Riddle> queue) {
		this.queue = queue;
	}

}
