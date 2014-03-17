package info.interacctivesystems.spade.nlp;

import static org.assertj.core.api.Assertions.assertThat;
import info.interacctivesystems.spade.PropertyUtil;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.testng.annotations.Test;

@Slf4j
public class SentenceDetectorTest {

	private SentenceDetector sentenceDetector;
	private PropertiesConfiguration exampleSentences;

	public SentenceDetectorTest() {
		sentenceDetector = new SentenceDetector();
		exampleSentences = PropertyUtil.getExampleSentences();

		assertThat(sentenceDetector).isNotNull();
		assertThat(exampleSentences).isNotNull();
	}

	@Test
	public void detectSentencesFromCorpusCase_1() {
		String case1 = exampleSentences.getString("sentences.case1");
		List<String> detectedSentences = sentenceDetector
				.detectSentencesFromCorpus(case1);

		log.info("Case 1 contains '{}' following sentences:",
				detectedSentences.size());
		for (String sentence : detectedSentences) {
			System.out.println(sentence);
		}

	}

	@Test
	public void detectSentencesFromCorpusCase_2() {
		String case2 = exampleSentences.getString("sentences.case2");
		List<String> detectedSentences = sentenceDetector
				.detectSentencesFromCorpus(case2);

		log.info("Case 2 contains '{}' following sentences:",
				detectedSentences.size());
		for (String sentence : detectedSentences) {
			System.out.println(sentence);
		}

	}

	@Test
	public void detectSentencesFromCorpusCase_3() {
		String case3 = exampleSentences.getString("sentences.case3");
		List<String> detectedSentences = sentenceDetector
				.detectSentencesFromCorpus(case3);

		log.info("Case 3 contains '{}' following sentences:",
				detectedSentences.size());
		for (String sentence : detectedSentences) {
			System.out.println(sentence);
		}

	}
}
