/**
 * Copyright 2014 Dennis Rippinger
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.interactivesystems.spade.nlp;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Allows to detect individual sentences in a given corpus of a review.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class SentenceDetector {

	private SentenceModel model;
	private SentenceDetectorME sentenceDetector;

	public SentenceDetector() {
		model = NlpModelFactory.getSentenceModel();
		sentenceDetector = new SentenceDetectorME(model);
		log.info("Created Sentence Detector '{}'", sentenceDetector.toString());
	}

	/**
	 * Detect sentences from a given corpus.
	 *
	 * @param corpus the text corpus from a given review.
	 * @return a list of individual sentences.
	 */
	public List<String> detectSentencesFromCorpus(@NonNull String corpus) {
		String[] arraySentences = sentenceDetector.sentDetect(corpus);
		List<String> result = Arrays.asList(arraySentences);

		return result;
	}
}
