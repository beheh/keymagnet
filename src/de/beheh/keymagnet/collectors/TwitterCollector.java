
package de.beheh.keymagnet.collectors;

import de.beheh.keymagnet.Collector;
import de.beheh.keymagnet.Riddle;
import java.util.Queue;

/**
 * Collects riddles from twitter.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class TwitterCollector extends Collector {

	public TwitterCollector(Queue<Riddle> queue) {
		super(queue);
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
