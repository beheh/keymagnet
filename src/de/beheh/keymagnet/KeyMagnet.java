package de.beheh.keymagnet;

import de.beheh.keymagnet.collectors.RedditCollector;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Collects keys from multiple sources and attempts to solve them.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class KeyMagnet {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// @TODO args for controlling collectors and candidates
		ArrayBlockingQueue<Riddle> queue = new ArrayBlockingQueue<>(100);

		// initialidze riddle master
		RiddleMaster riddlemaster = new RiddleMaster(queue);
		Thread thread = new Thread(riddlemaster);
		riddlemaster.setThread(thread);
		thread.start();

		// initialize reddit collector
		new Thread(new RedditCollector(queue, "hearthstone")).start();

		// various tests
		//queue.add(new Riddle("018three1482zero73035eight972224490nine"));
		//queue.add(new Riddle("http://imgur.com/4gmeYMM"));
		//queue.add(new Riddle("ads da wd http://i.imgur.com/4gmeYMM.jpg ds adsdsadsasda"));
		//queue.add(new Riddle("15\\t9\\t10\\t4\\t10\\n9\\t2\\t5\\t5\\t4\\n14\\t4\\t6\\t9\\t7\\n9\\t10\\t3\\t3\\t8\\n14\\t5\\t3\\t8\\t3\\n\\nI wonder how long it will take? :)\\n(hint: Geography may be important)\\n\\nEdit: Hint:\\nGeography is only relevant for the way a date is written"));
		//queue.add(new Riddle("hi, somewhere here is a 634554489349096913594763 six key w/ 25 chars"));
	}

}
