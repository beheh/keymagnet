package de.beheh.keymagnet.candidates;

import de.beheh.keymagnet.Candidate;
import de.beheh.keymagnet.Riddle;
import de.beheh.keymagnet.RiddleMaster;
import de.beheh.keymagnet.RiddleResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solves riddles by simply trying to find the first match.
 *
 * @deprecated Use NumberWordCandidate instead
 * @author Benedict Etzel <developer@beheh.de
 */
public class PlainCandidate extends Candidate {

	public PlainCandidate(Riddle riddle, RiddleMaster riddlemaster) {
		super(riddle, riddlemaster);
	}

	@Override
	public void run() {
		String query = riddle.toString();
		Pattern pattern = Pattern.compile("[0-9]{25}");
		Matcher matcher = pattern.matcher(query);
		while(matcher.find()) {
			results.add(new RiddleResult(matcher.group(), 1, riddle));
		}
		this.complete();
	}

}
