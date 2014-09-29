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
package info.interactivesystems.spade.crawler.amazon;

import info.interactivesystems.spade.crawler.util.AmazonCategory;
import info.interactivesystems.spade.util.ProductCategory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * The Class AmazonProductCrawlerTest.
 *
 * @author Dennis Rippinger
 */
@ContextConfiguration(locations = {"classpath:beans.xml"})
public class AmazonProductCrawlerTest extends AbstractTestNGSpringContextTests {

	@Resource
	private AmazonProductCrawler crawler;

	@Test
	public void crawlingTest() throws InterruptedException {
		crawler.setCategory(AmazonCategory.MOBILEPHONE);
		crawler.setProductCategory(ProductCategory.MOBILEPHONE);

		crawler.crawlProducts();
	}
}
