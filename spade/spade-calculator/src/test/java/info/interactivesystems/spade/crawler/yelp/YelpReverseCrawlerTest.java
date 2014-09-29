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

import info.interactivesystems.spade.dao.UserDao;
import info.interactivesystems.spade.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * The Class YelpReverseCrawlerTest.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@ContextConfiguration(locations = {"classpath:beans.xml"})
public class YelpReverseCrawlerTest extends AbstractTestNGSpringContextTests {

	@Resource
	private UserDao userDao;

	@Resource
	private YelpReverseUserCrawler crawler;

	@Test
	public void testReverseCrawling() {
		User user = userDao.find("--jSxZSrjvyIvR88zldm1A");

		try {
			crawler.setPageNo(0);
			crawler.crawlReverse(user);
		} catch (Exception e) {

			log.error("Unkown Error, marked user", e);
		}

	}

}
