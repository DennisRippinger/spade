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
package info.interactivesystems.spade.ui.util;

import javax.inject.Named;
import java.io.IOException;
import java.util.Properties;

/**
 * A factory for creating GitRepositoryState objects.
 *
 * @author Dennis Rippinger
 */
@Named
public class GitRepositoryStateFactory {

	private GitRepositoryState gitRepositoryState;

	public GitRepositoryState getGitRepositoryState() throws IOException {
		if (gitRepositoryState == null) {
			Properties properties = new Properties();
			properties.load(getClass().getClassLoader().getResourceAsStream("git.properties"));

			gitRepositoryState = new GitRepositoryState(properties);
		}

		return gitRepositoryState;
	}

}
