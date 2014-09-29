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
package info.interactivesystems.spade.crawler.yelp;

import info.interactivesystems.spade.util.ProductCategory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * The Class YelpCrawlerTest.
 *
 * @author Dennis Rippinger
 */
@ContextConfiguration(locations = {"classpath:beans.xml"})
public class YelpCrawlerTest extends AbstractTestNGSpringContextTests {

	@Resource
	private YelpVenueCrawler crawler;

	@Test
	public void YelpCrawler() throws UnsupportedEncodingException, InterruptedException {
		crawler.setLocation("Jacksonville, FL, USA");
		crawler.setCategory(ProductCategory.RESTAURANT);

		crawler.setPageNo(63);
		crawler.crawlVenues();
	}
}