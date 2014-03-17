// Copyright 2014 Dennis Rippinger
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package info.interacctivesystems.spade;

import static org.assertj.core.api.Assertions.assertThat;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Factory Methods for property files with example and test related information.
 * 
 * @author Dennis Rippinger
 * 
 */
@Slf4j
public final class PropertyUtil {

	/**
	 * Creates an {@link PropertiesConfiguration} instance for example
	 * sentences.
	 * 
	 * @return the example sentences
	 */
	public static PropertiesConfiguration getExampleSentences() {
		try {
			PropertiesConfiguration exampleSentences = new PropertiesConfiguration(
					"example_sentences.properties");

			assertThat(exampleSentences).isNotNull();
			assertThat(exampleSentences.containsKey("sentences.case1"))
					.isTrue();

			return exampleSentences;
		} catch (ConfigurationException e) {
			log.error("Could not load Example Sentences", e);
		}
		return null;
	}

	/**
	 * Private Constructor
	 */
	private PropertyUtil() {

	}
}
