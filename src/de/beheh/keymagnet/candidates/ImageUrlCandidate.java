package de.beheh.keymagnet.candidates;

import de.beheh.keymagnet.Candidate;
import de.beheh.keymagnet.Riddle;
import de.beheh.keymagnet.RiddleMaster;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solves riddles by extracting image urls and readding them to the riddle queue.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class ImageUrlCandidate extends Candidate {

	public ImageUrlCandidate(Riddle riddle, RiddleMaster riddlemaster) {
		super(riddle, riddlemaster);
	}

	@Override
	public void run() {
		Pattern pattern = Pattern.compile("https?://i\\.imgur\\.com/[A-z0-9]*\\.[A-z]{3}");
		Matcher matcher = pattern.matcher(riddle.toString());
		while(matcher.find()) {
			String url = matcher.group();
			if(url.equals(riddle.toString())) {
				continue;
			}
			riddleMaster.getQueue().add(new Riddle(url));
		}

		Pattern desktop = Pattern.compile("https?://imgur\\.com/([A-z0-9]*)");
		Matcher desktopmatcher = desktop.matcher(riddle.toString());
		while(desktopmatcher.find()) {
			String url = desktopmatcher.group(1);
			riddleMaster.getQueue().add(new Riddle("https://i.imgur.com/" + url + "\\.jpg"));
		}

		Pattern reddit = Pattern.compile("https?://i.redd.it/[A-z0-9]*\\.[A-z]{3}");
		Matcher redditmatcher = reddit.matcher(riddle.toString());
		while(redditmatcher.find()) {
			String url = redditmatcher.group();
			if(url.equals(riddle.toString())) {
				continue;
			}
			riddleMaster.getQueue().add(new Riddle(url));
		}

		this.complete();
	}
}
