package info.interactivesystems.spade.crawler.util;

import info.interactivesystems.spade.exception.CrawlerException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 
 * @author Dennis Rippinger
 * 
 */
@Slf4j
public final class CrawlerUtil {

    private static List<BrowserVersion> browserVersionDesktop = new LinkedList<BrowserVersion>();

    static {
        browserVersionDesktop.add(BrowserVersion.CHROME);
        browserVersionDesktop.add(BrowserVersion.FIREFOX_24);
        browserVersionDesktop.add(BrowserVersion.INTERNET_EXPLORER_11);

        // Maybe add mobile browsers
    }

    public static WebClient getRandomDesktopWebClient(Boolean javascript, Boolean css) {
        BrowserVersion browserVersion = browserVersionDesktop.get(ThreadLocalRandom.current().nextInt(0, 2));
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
                throw new CrawlerException("HTTP Error", e);
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
