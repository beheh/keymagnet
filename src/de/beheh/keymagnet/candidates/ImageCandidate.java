package de.beheh.keymagnet.candidates;

import de.beheh.keymagnet.Candidate;
import de.beheh.keymagnet.Riddle;
import de.beheh.keymagnet.RiddleMaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import net.sourceforge.tess4j.*;

/**
 * Solves an image riddle, readding results to the riddle queue.
 *
 * @author Benedict Etzel <developer@beheh.de>
 */
public class ImageCandidate extends Candidate {

	public ImageCandidate(Riddle riddle, RiddleMaster riddlemaster) {
		super(riddle, riddlemaster);
	}

	@Override
	public void run() {
		if(!riddle.toString().matches("^http://.*\\.((jpg)|(png))$")) {
			this.complete();
			return;
		}

		// initialize target file
		String filename = riddle.toString().substring(riddle.toString().lastIndexOf("/"));
		String image = riddle.toString();
		File imageFile = new File("/tmp" + filename);

		try {
			// download image from url
			URL url = new URL(image);
			OutputStream outputStream;
			try(InputStream inputStream = url.openStream()) {
				outputStream = new FileOutputStream(imageFile);
				byte[] b = new byte[2048];
				int length;
				while((length = inputStream.read(b)) != -1) {
					outputStream.write(b, 0, length);
				}
			}
			outputStream.close();

			// set up OCR enginge
			Tesseract instance = Tesseract.getInstance();  // via JNA Interface Mapping
			instance.setLanguage("eng");
			instance.setOcrEngineMode(2); // see http://www.emgu.com/wiki/files/2.3.0/document/html/a4eee77d-90ad-4f30-6783-bc3ef71f8d49.htm
			instance.setTessVariable("tessedit_char_whitelist", "0123456789"); // only allow digits

			try {
				String result = instance.doOCR(imageFile);
				riddleMaster.getQueue().add(new Riddle(result));
			} catch(TesseractException ex) {
				ex.printStackTrace(System.err);
			}
		} catch(javax.imageio.IIOException ex) {
			System.out.println("[i] invalid format");
		} catch(IOException ex) {
			ex.printStackTrace(System.err);
		}
		if(imageFile.exists()) {
			imageFile.delete();
		}
		this.complete();
	}

}
