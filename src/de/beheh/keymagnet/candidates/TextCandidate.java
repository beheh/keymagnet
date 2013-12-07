package de.beheh.keymagnet.candidates;

import de.beheh.keymagnet.Candidate;
import de.beheh.keymagnet.Riddle;
import de.beheh.keymagnet.RiddleMaster;
import de.beheh.keymagnet.RiddleResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solves riddles by doing multiple string transformations and weigths results.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class TextCandidate extends Candidate {

	public TextCandidate(Riddle riddle, RiddleMaster riddlemaster) {
		super(riddle, riddlemaster);
	}

	@Override
	public void run() {
		String query = " " + riddle.toString().toLowerCase() + " ";
		String[] digits = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
		String[] tens = {"", "teen", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
		String[] special = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen"};
		// iterate from high to low and replace
		// e.g. ninetynine -> 99, ninetyeight -> 98...
		int changes = 0;
		for(double i = 99; i >= 0; i--) {
			String number;
			// special?
			if(i >= 10 && i <= 18) {
				number = special[(int) i - 10];
			} else {
				// calculate both parts
				String ten = tens[(int) Math.floor(i / 10)];
				String digit = digits[(int) i - (int) Math.floor(i / 10) * 10];
				// assemble the number
				if(i / 10 == Math.floor(i / 10) && i != 0) {
					number = ten;
				} else if(i > 10 && i < 20) {
					number = digit + ten;
				} else {
					number = ten + digit;
				}
			}

			String old = query;
			query = query.replaceAll("((?![A-z]).)" + number + "((?![A-z]).)", "$1" + Integer.valueOf(Double.valueOf(i).intValue()).toString() + "$2");
			if(!query.equals(old)) {
				changes++;
			}
		}

		// @TODO Math operations


		// @TODO search and replace variables...?

		// tidy up
		query = query.trim();
		query = query.replaceAll("\\n|\\r|\\t", " ");

		// reverse if mentioned
		if(query.toLowerCase().matches(".*reverse.*")) {
			query = new StringBuffer(query).reverse().toString();
		}

		// filter out non-numerical
		query = query.replaceAll("(?![0-9]).", "");

		// @TODO Weighted Strings
		// find keys
		Pattern pattern = Pattern.compile("[0-9]{25}");
		Matcher matcher = pattern.matcher(query);
		while(matcher.find()) {
			String match = matcher.group();
			float probability = 0.5f + (changes / 10);
			if((query.length() % 25) == 0) {
				probability += 0.5f;
			}
			int k = 0;
			for(int i = 0; i < match.length() - 1; i++) {
				int digit = Character.getNumericValue(match.charAt(i));
				k += digit;
				if(i > 0 && digit == Character.getNumericValue(match.charAt(i-1))) {
					probability -= 0.05f;
				}
			}
			// average
			float average = ((float) k) / match.length();
			float distance = average;
			if(distance > 5) {
				distance = distance - 5;
			} else {
				distance = 5 - distance;
			}
			probability += (-distance / 10) + 0.25;
			results.add(new RiddleResult(match, probability, riddle));
		}
		this.complete();
	}

}
