// Java code to convert text to speech

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

import java.util.Scanner;

public class TextSpeech {

	public static void main(String[] args) {

		try {
			// Set property as Kevin Dictionary
			System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory");

			// Register Engine
			Central.registerEngineCentral("com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral");

			// Create a Synthesizer
			Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));

			// Allocate synthesizer
			synthesizer.allocate();

			// Resume Synthesizer
			synthesizer.resume();

			// Speaks the given text
			// until the queue is empty.
			synthesizer.speakPlainText("Hello World!", null);
			synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
			// TEMP START
			speak(synthesizer, "tempFile.txt");
			// TEMP STOP

			// Deallocate the Synthesizer.
			synthesizer.deallocate();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void speak(Synthesizer synth, String fileName) throws IllegalArgumentException, InterruptedException {
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
}