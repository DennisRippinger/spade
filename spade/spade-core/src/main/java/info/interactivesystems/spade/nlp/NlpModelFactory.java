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

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.IOException;
import java.io.InputStream;

/**
 * A factory for creating NlpModel objects.
 *
 * @author Dennis Rippinger
 */
@Slf4j
public final class NlpModelFactory {

	private static final String SENTENCE_MODEL = "/en-sent.bin";

	/**
	 * Private Constructor.
	 */
	private NlpModelFactory() {

	}

	/**
	 * Creates a Sentence Model for the Apache NLP Tooling
	 *
	 * @return the sentence model
	 */
	public static SentenceModel getSentenceModel() {

		try (InputStream is = NlpModelFactory.class
				.getResourceAsStream(SENTENCE_MODEL);) {
			SentenceModel model = new SentenceModel(is);
			log.debug("Create Sentence Model for '{}' language",
					model.getLanguage());
			return model;
		} catch (IOException e) {
			log.error("Could not load sentence model '{}'", SENTENCE_MODEL, e);
		}

		return null;
	}
}