package info.interactivesystems.spade.crawler;

import static org.assertj.core.api.Assertions.assertThat;
import info.interactivesystems.spade.crawler.util.CrawlerUtil;

import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;

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
