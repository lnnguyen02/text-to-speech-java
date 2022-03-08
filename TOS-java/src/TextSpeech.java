// Java code to convert text to speech

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
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
		// Setting up the casual responses
		Scanner fileReader = new Scanner(new File("text.txt"));
		while (fileReader.hasNextLine()) {
			String[] line = fileReader.nextLine().split("\\.");
			responses.put(line[0].trim().toLowerCase(), line[1].trim());
		}
		// Setting up the commands
		fileReader = new Scanner(new File("commands.txt"));
		while (fileReader.hasNextLine()) {
			String[] line = fileReader.nextLine().split("\\.");
			responses.put(line[0].trim().toLowerCase(), line[1].trim());
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
				switch (responses.get(input)) {
				case "time":
					time(synthesizer);
					break;
				case "date":
					break;
				default:
					synthesizer.speakPlainText(responses.get(input), null);
					synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
					break;
				}
			}
		}

		scanner.close();
	}
	// EOF
	
	
	public static void time(Synthesizer synthesizer) throws IllegalArgumentException, InterruptedException {
		String [] time = java.time.LocalTime.now().toString().split(":");
		int hour = Integer.parseInt(time[0]);
		
		String speechLine = "";
		if(hour==0)
			speechLine += "It is twelve " + time[1] + " am";
		else if(hour<12)
			speechLine += "It is "+ numberToText(hour) + time[1] + " am";
		else if(hour==12)
			speechLine += "It is "+ numberToText(hour) + time[1] + " pm";
		else if(hour>12) {
			hour = hour - 12;
			speechLine += "It is "+ numberToText(hour) + " " + time[1] + " pm";
		}
		synthesizer.speakPlainText(speechLine, null);
		synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
	}
	
	private static String numberToText(int number) {
		switch (number) {
		case 1:
			return "one";
		case 2:
			return "two";
		case 3:
			return "three";
		case 4:
			return "four";
		case 5:
			return "five";
		case 6:
			return "six";
		case 7:
			return "seven";
		case 8:
			return "eight";
		case 9:
			return "nine";
		case 10:
			return "ten";
		case 11:
			return "eleven";
		default:
			return "";
		}
	}
	
	public static void date(Synthesizer synthesizer) throws IllegalArgumentException, InterruptedException {
		
	}
}