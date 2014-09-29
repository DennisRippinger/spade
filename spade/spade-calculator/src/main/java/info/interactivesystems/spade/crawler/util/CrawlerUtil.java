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
package info.interactivesystems.spade.crawler.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import info.interactivesystems.spade.exception.CrawlerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * The Class CrawlerUtil provides different Browser Version and wait times to avoid a CAPTCHA.
 *
 * @author Dennis Rippinger
 */
@Slf4j
public final class CrawlerUtil {

	private static List<BrowserVersion> browserVersionDesktop = new LinkedList<>();

	static {
		browserVersionDesktop.add(BrowserVersion.CHROME);
		browserVersionDesktop.add(BrowserVersion.FIREFOX_24);
		browserVersionDesktop.add(BrowserVersion.INTERNET_EXPLORER_11);

		// Maybe add mobile browsers
	}

	/**
	 * Private Constructor
	 */
	private CrawlerUtil() {

	}

	public static WebClient getRandomDesktopWebClient(Boolean javascript, Boolean css) {
		BrowserVersion browserVersion = browserVersionDesktop.get(ThreadLocalRandom.current().nextInt(0, 2));
		log.info("Creating new '{}' Browser.", browserVersion.getApplicationVersion());
		WebClient webClient = new WebClient(browserVersion);
		webClient.getOptions().setJavaScriptEnabled(javascript);
		webClient.getOptions().setCssEnabled(css);

		return webClient;
	}

	public static WebClient getChrome(Boolean javascript, Boolean css) {
		BrowserVersion browserVersion = browserVersionDesktop.get(0);
		log.info("Creating new '{}' Browser.", browserVersion.getApplicationVersion());
		WebClient webClient = new WebClient(browserVersion);
		webClient.getOptions().setJavaScriptEnabled(javascript);
		webClient.getOptions().setCssEnabled(css);

		return webClient;
	}

	public static HtmlPage getWebPage(WebClient client, String url, Integer wait) throws CrawlerException {
		HtmlPage page = null;
		try {
			waitAroundTimeInSeconds(wait);
			page = client.getPage(url);

		} catch (FailingHttpStatusCodeException | IOException e) {
			// Retry
			waitAroundTimeInSeconds(5);
			try {
				page = client.getPage(url);
			} catch (FailingHttpStatusCodeException | IOException e1) {
				throw new CrawlerException("HTTP Error", e1);
			}

		}
		log.info("Page Title: '{}' for '{}'", page.getTitleText(), url);

		return page;
	}

	public static void waitAroundTimeInSeconds(Integer second) {
		Double dSecond = second * 1.0;
		NormalDistribution distribution = new NormalDistribution(dSecond, 0.3);
		Double sample = distribution.sample();

		waitInMillisecondsSeconds((int) (sample * 1000));
	}

	private static void waitInMillisecondsSeconds(Integer milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			log.error("Error interrupting sleep time", e);
		}
	}

}
