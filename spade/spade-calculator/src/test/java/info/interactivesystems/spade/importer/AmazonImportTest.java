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
package info.interactivesystems.spade.importer;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * The Class AmazonImportTest.
 *
 * @author Dennis Rippinger
 */
@ContextConfiguration(locations = {"classpath:beans.xml"})
public class AmazonImportTest extends AbstractTestNGSpringContextTests {

	@Resource
	private AmazonImport importer;

	@Test
	public void amazonImporterTest() throws InterruptedException, ExecutionException {
		File amazonFile = new File("/Volumes/Extended/Thesis/all.txt");

		importer.importAmazonDataset(amazonFile);
	}

}
