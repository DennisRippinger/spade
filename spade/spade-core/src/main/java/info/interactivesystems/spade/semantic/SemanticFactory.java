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
package info.interactivesystems.spade.semantic;

import de.linguatools.disco.DISCO;
import edu.smu.tspell.wordnet.WordNetDatabase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.IOException;

/**
 * Basic factory to create a {@link DISCO} instance.
 *
 * @author Dennis Rippinger
 */
@Slf4j
public final class SemanticFactory {

	private static final Boolean LOAD_TO_RAM = false;

	private static PropertiesConfiguration configuration;
	private static String discoDir;

	static {
		try {
			configuration = new PropertiesConfiguration("disco.properties");
			discoDir = configuration.getString("disco.english.wikipedia");

			System.setProperty("wordnet.database.dir", "/Users/dennisrippinger/Downloads/WordNet-3.0/dict");
		} catch (ConfigurationException e) {
			log.error("Could not load Disco Properties", e);
		}
	}

	/**
	 * Private Constructor
	 */
	private SemanticFactory() {

	}

	public static WordNetDatabase getWordnetDatabase() {
		try {
			return WordNetDatabase.getFileInstance();
		} catch (Exception e) {
			log.error("Could not load Wordnet Database", e);
		}
		return null;
	}

	/**
	 * Creates an {@link de.linguatools.disco.DISCO} Instance with English co-occurrence data.
	 *
	 * @return the english co-occurrence data as {@link DISCO} instance.
	 */
	public static DISCO getEnglishCoOccurrenceData() {
		DISCO disco = null;
		try {

			log.debug("Loading English word concurrence File from '{}'",
					discoDir);
			disco = new DISCO(discoDir, LOAD_TO_RAM);
			log.debug("Loaded English word concurrence into RAM = '{}'",
					LOAD_TO_RAM);

		} catch (IOException e) {
			log.error("Could not load Disco folder", e);
		}

		return disco;
	}

}
