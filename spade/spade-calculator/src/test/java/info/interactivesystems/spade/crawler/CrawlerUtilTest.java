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
package info.interactivesystems.spade.crawler;

import static org.assertj.core.api.Assertions.assertThat;
import info.interactivesystems.spade.crawler.util.CrawlerUtil;

import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * The Class CrawlerUtilTest.
 * 
 * @author Dennis Rippinger
 */
public class CrawlerUtilTest {

    @Test
    public void getRandomDesktopWebClient() {
        WebClient randomDesktopWebClient = CrawlerUtil.getRandomDesktopWebClient(false, false);
        assertThat(randomDesktopWebClient).isInstanceOf(WebClient.class);
    }

    @Test
    public void waitAroundTimeInSeconds() {
        CrawlerUtil.waitAroundTimeInSeconds(1);
    }
}
