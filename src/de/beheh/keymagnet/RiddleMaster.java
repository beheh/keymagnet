package de.beheh.keymagnet;

import de.beheh.keymagnet.candidates.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Distributes incoming riddles among the collectors and displays results.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class RiddleMaster implements Runnable {

	private Thread thread;

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public Thread getThread() {
		return thread;
	}

	private final ArrayBlockingQueue<Riddle> queue;

	public ArrayBlockingQueue<Riddle> getQueue() {
		return queue;
	}

	public RiddleMaster(ArrayBlockingQueue<Riddle> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		System.out.println("[i] Waiting for riddles");
		while(true) {
			// get new Riddle
			Riddle riddle = null;
			try {
				riddle = queue.take();
			} catch(InterruptedException ex) {
			}
			if(riddle == null || riddle.toString() == null || riddle.toString().trim().length() == 0) {
				continue;
			}

			String text = riddle.toString().replaceAll("\\n|\\r|\\t", " ").trim();
			System.out.println("[i] " + dateFormat.format(new Date()) + " New Riddle received: \"" + text.substring(0, Math.min(text.length(), 50)) + "...\"");

			// give it to the candidates
			List<Candidate> candidates = new ArrayList<>();

			TextCandidate textCandidate = new TextCandidate(riddle, this);
			new Thread(textCandidate).start();
			candidates.add(textCandidate);

			ImageCandidate imageCandidate = new ImageCandidate(riddle, this);
			new Thread(imageCandidate).start();
			candidates.add(imageCandidate);

			ImageUrlCandidate imageUrlCandidate = new ImageUrlCandidate(riddle, this);
			new Thread(imageUrlCandidate).start();
			candidates.add(imageUrlCandidate);

			// prepare result list
			List<RiddleResult> results = new ArrayList<>();

			// now let the candidates riddle around
			int initialCandidateCount = candidates.size();
			while(!candidates.isEmpty()) {
				try {
					Thread.sleep(5000);
					System.out.print("[i] " + candidates.size() + "/" + initialCandidateCount);
					if(candidates.size() == 1) {
						System.out.println(" candidate still solving...");
					} else {
						System.out.println(" candidates still solving...");

					}
				} catch(InterruptedException ex) {
				}
				Iterator<Candidate> iterator = candidates.iterator();

				// ask all candidates
				while(iterator.hasNext()) {

					// check if candidate has completed his riddling
					Candidate candidate = iterator.next();
					if(candidate.isFinished()) {

						// collect his results
						List<RiddleResult> candidateResults = candidate.getResults();

						// display results
						Iterator<RiddleResult> candidateResultIterator = candidateResults.iterator();
						int candidateResultCount = 0;
						while(candidateResultIterator.hasNext()) {
							RiddleResult candidateResult = candidateResultIterator.next();

							// check if we have already found this result before
							Iterator<RiddleResult> resultIterator = results.iterator();
							boolean found = false;
							while(resultIterator.hasNext()) {
								RiddleResult result = resultIterator.next();

								if(result.toString().equals(candidateResult.toString())) {
									result.increment();
									found = true;
									break;
								}
							}
							if(!found) {
								results.add(candidateResult);
								candidateResultCount++;
								if(candidateResult.getConfidence() > 0.7f) {
									System.out.print("[!] *** CONFIDENT RESULT *** ");

									// save our confident key in a file
									/*try {
									 File file = new File("/tmp/hearthstonekeys.txt");
									 PrintWriter printwriter = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
									 printwriter.write(candidateResult.toString() + "\n\n");
									 printwriter.write("date: " + dateFormat.format(new Date()) + "\n");
									 printwriter.write("confidence: " + candidateResult.getConfidence() + "\n");
									 printwriter.write("original riddle: \"" + candidateResult.getRiddle().getOriginalRiddle().toString().trim() + "\"\n\n");
									 printwriter.close();
									 } catch(IOException ex) {
									 }*/
								} else if(candidateResult.getConfidence() < 0.7f) {
									continue;
								} else {
									System.out.print("[i] Result");
								}
								System.out.println("(from=" + candidate.getClass().getSimpleName() + ", p=" + candidateResult.getConfidence() + "):");
								System.out.println("[*] " + candidateResult);
							}
						}
						//System.out.print(candidate.getClass().getSimpleName() + " has finished");
						//System.out.println(" (" + candidateResultCount + " new, " + (candidateResults.size() - candidateResultCount) + " old)");

						// remove candidate from the list
						iterator.remove();
					}
				}
			}
			/*System.out.print("[i] Riddle processed, " + results.size() + " result");
			 if(results.size() != 1) {
			 System.out.print("s");
			 }
			 System.out.println(" found");*/
		}
	}
}
