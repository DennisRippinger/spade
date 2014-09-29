package info.interactivesystems.spade.nlp;

import opennlp.tools.sentdetect.SentenceModel;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NlpModelFactoryTest {

	@Test(enabled = false)
	public void getSentenceModel() {
		SentenceModel sentenceModel = NlpModelFactory.getSentenceModel();

		assertThat(sentenceModel).isNotNull();
		assertThat(sentenceModel).isInstanceOf(SentenceModel.class);
	}
}
