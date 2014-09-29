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
package info.interactivesystems.spade;

import org.apache.commons.cli.Options;

/**
 * The Class CliCommands.
 *
 * @author Dennis Rippinger
 */
public final class CliCommands {

	public static final String NISLIMSA = "nilsimsa";
	public static final String HINDEX = "hindex";
	public static final String HINDEXRESOLVER = "hindexResolver";
	public static final String UNIQUE = "unique";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String CATEGORY = "category";

	/**
	 * Private Constructor.
	 */
	private CliCommands() {

	}

	/**
	 * Creates a set of possible CLI Commands.
	 *
	 * @return the CLI options
	 */
	public static Options getCliOptions() {

		Options options = new Options();
		options.addOption("f", FROM, true, "From a given value");
		options.addOption("t", TO, true, "To a given value");
		options.addOption("c", CATEGORY, true, "a given category");
		options.addOption("u", UNIQUE, false, "removes non Unique Reviews");

		options.addOption("h", HINDEX, false, "Calculates the HIndex");
		options.addOption("hr", HINDEXRESOLVER, true, "Calculates the most variant review");
		options.addOption("n", NISLIMSA, false, "Calculates the nilsimsa similarity for a given category");

		return options;
	}

}
