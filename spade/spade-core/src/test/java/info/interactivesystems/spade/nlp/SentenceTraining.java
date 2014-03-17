package info.interactivesystems.spade.nlp;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

import org.testng.annotations.Test;

public class SentenceTraining {
	@Test
	public void createModel() {
		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream;
		OutputStream modelOut = null;
		ObjectStream<SentenceSample> sampleStream = null;
		try {
			String locationEnAmazon = "/Users/dennisrippinger/git/spade/spade/spade-core/target/test-classes/en-amazon.train";
			// this.getClass()
			// .getResource("/en-amazon.train").toURI().toString();

			lineStream = new PlainTextByLineStream(new FileInputStream(
					locationEnAmazon), charset);

			sampleStream = new SentenceSampleStream(lineStream);

			@SuppressWarnings("deprecation")
			SentenceModel model = SentenceDetectorME.train("en", sampleStream,
					true, null, TrainingParameters.defaultParams());

			modelOut = new BufferedOutputStream(new FileOutputStream(
					"/Users/dennisrippinger/Desktop/en-amazon.bin"));

			model.serialize(modelOut);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (modelOut != null)
				try {
					modelOut.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			try {
				sampleStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
