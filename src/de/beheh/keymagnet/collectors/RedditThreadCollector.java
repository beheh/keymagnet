package de.beheh.keymagnet.collectors;

import de.beheh.keymagnet.Collector;
import de.beheh.keymagnet.Riddle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;

/**
 * Collects riddles from a subreddit.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class RedditThreadCollector extends Collector {

	private final String thread;
	private static final int REQUEST_FREQUENCY = 5000;

	public RedditThreadCollector(Queue<Riddle> queue, String thread) {
		super(queue);
		this.thread = thread;
	}

	@Override
	public void run() {
		String first = null;
		URL url;
		while(true) {
			try {
				// query reddit
				int limit = 1;
				if(first != null) {
					// subsequent requests
					limit = 25;
				}

				url = new URL("https://www.reddit.com/comments/" + thread + ".json?sort=new&limit=" + limit);
				URLConnection urlconnection = url.openConnection();
				urlconnection.setRequestProperty("User-Agent", "KeyMagnet/0.5-dev; http://github.com/beheh/keymagnet");
				Reader reader = new InputStreamReader(urlconnection.getInputStream());

				//System.out.println("[i] Querying reddit... ");

				// parse result
				JSONArray wrapper = (JSONArray) JSONValue.parse(reader);
				JSONObject json = (JSONObject) wrapper.get(1);
				if(!json.get("kind").equals("Listing")) {
					throw new Exception("unexpected kind returned from reddit API");
				}

				// traverse comments
				JSONArray children = (JSONArray) ((JSONObject) json.get("data")).get("children");
				Iterator<JSONObject> iterator = children.iterator();
				String id = "";
				String current = null;
				while(iterator.hasNext()) {
					// grab and parse through comment
					JSONObject comment = (JSONObject) iterator.next().get("data");
					id = (String) comment.get("id");
					if (current == null) {
						current = id;
					}
					if(first == null || id.equals(first)) {
						break;
					}
					String message = (String) comment.get("body");
					// add riddle to RiddleMasters' queue
					Riddle riddle = new Riddle(message);
					queue.add(riddle);
				}
				first = current; // save last id so we can start there next request

			} catch(Exception ex) {
				ex.printStackTrace(System.err);
			}
			try {
				Thread.sleep(RedditThreadCollector.REQUEST_FREQUENCY); // as according to reddit api guidelines
			} catch(InterruptedException ex) {
			}
		}
	}

}
