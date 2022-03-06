// Java code to convert text to speech

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Locale;
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

import java.util.Scanner;

public class TextSpeech {

	private static HashMap<String, String> responses = new HashMap<>();

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			// Setting up the Text to Speech voice
			System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory");
			Central.registerEngineCentral("com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral");
			Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
			synthesizer.allocate();
			synthesizer.resume();

			// Setting up the responses for the chat bot
			setUp();
			readingInput(synthesizer);
			
			// Deallocate the Synthesizer.
			synthesizer.deallocate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads, line by line, the fileName.txt out loud.
	 * 
	 * @param synth    A proper Synthesizer object
	 * @param fileName A valid text file name that exists on the same level as the
	 *                 src folder
	 * 
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 */
	public static void speakFile(Synthesizer synth, String fileName)
			throws IllegalArgumentException, InterruptedException {
		try {

			Scanner fileReader = new Scanner(new File(fileName));
			while (fileReader.hasNextLine()) {
				synth.speakPlainText(fileReader.nextLine(), null);
				synth.waitEngineState(Synthesizer.QUEUE_EMPTY);
			}

		} catch (FileNotFoundException e) {

			System.out.println("File not found.");

		}

	}

	/**
	 * 
	 * @throws FileNotFoundException
	 */
	public static void setUp() throws FileNotFoundException {
		Scanner fileReader = new Scanner(new File("text.txt"));
		while (fileReader.hasNextLine()) {
			responses.put(fileReader.nextLine(), fileReader.nextLine());
		}
	}

	/**
	 * 
	 * @param synthesizer
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 */
	public static void readingInput(Synthesizer synthesizer) throws IllegalArgumentException, InterruptedException {
		Scanner scanner = new Scanner(System.in);
		loop: while (true) {
			String input = scanner.nextLine();

			if (input.compareTo("exit") == 0)
				break loop;

			if (responses.get(input) != null) {
				synthesizer.speakPlainText(responses.get(input), null);
				synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
			}
		}

		scanner.close();
	}
	// EOF
}