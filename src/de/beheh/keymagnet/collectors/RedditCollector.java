package de.beheh.keymagnet.collectors;

import de.beheh.keymagnet.Collector;
import de.beheh.keymagnet.Riddle;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Collects riddles from a subreddit.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class RedditCollector extends Collector {

	private final String subreddit;
	private final int pastPosts;
	private static final int REQUEST_FREQUENCY = 30000;

	public RedditCollector(Queue<Riddle> queue, String subreddit) {
		super(queue);
		this.subreddit = subreddit;
		this.pastPosts = 0;
	}
	public RedditCollector(Queue<Riddle> queue, String subreddit, int pastPosts) {
		super(queue);
		this.subreddit = subreddit;
		this.pastPosts = pastPosts;
	}

	@Override
	public void run() {
		String last = "";
		URL url;
		while(true) {
			try {
				// query reddit
				int limit = this.pastPosts;
				if(!last.isEmpty()) {
					// subsequent requests
					limit = 25;
				}
				url = new URL("http://www.reddit.com/r/" + subreddit + "/new.json?limit=" + limit + "&before=" + last);
				URLConnection urlconnection = url.openConnection();
				urlconnection.setRequestProperty("User-Agent", "KeyMagnet/0.5-dev; http://github.com/beheh/keymagnet");
				BufferedReader read = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));

				//System.out.println("[i] Querying reddit... ");

				// parse result
				JSONObject json = (JSONObject) JSONValue.parse(read);
				if(!json.get("kind").equals("Listing")) {
					throw new Exception("unexpected kind returned from reddit API");
				}

				// traverse posts
				JSONArray children = (JSONArray) ((JSONObject) json.get("data")).get("children");
				Collections.reverse(children); // reverse the result so that we parse old->new
				Iterator<JSONObject> iterator = children.iterator();
				String name = last;
				while(iterator.hasNext()) {
					// grab and parse through post
					JSONObject post = (JSONObject) iterator.next().get("data");
					name = (String) post.get("name");
					if(name.equals(last) || (last.isEmpty() && pastPosts == 0)) {
						break;
					}
					String message = (String) post.get("selftext");
					if(!(boolean) post.get("is_self")) {
						// selfposts have an url instead of selftext
						message = (String) post.get("url");
					}
					// add riddle to RiddleMasters' queue
					Riddle riddle = new Riddle(message);
					queue.add(riddle);
				}
				last = name; // save last id so we can start there next request

			} catch(Exception ex) {
				ex.printStackTrace(System.err);
			}
			try {
				Thread.sleep(RedditCollector.REQUEST_FREQUENCY); // as according to reddit api guidelines
			} catch(InterruptedException ex) {
			}
		}
	}

}
