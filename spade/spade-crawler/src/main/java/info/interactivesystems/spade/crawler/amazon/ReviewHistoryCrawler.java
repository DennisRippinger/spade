/**
 * 
 */
package info.interactivesystems.spade.crawler.amazon;

import info.interactivesystems.spade.crawler.util.CrawlerUtil;
import info.interactivesystems.spade.dao.ReviewDao;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.exception.CrawlerException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author Dennis Rippinger
 * 
 */
@Slf4j
@Component
@Scope(value = "prototype")
public class ReviewHistoryCrawler {

    private static final SimpleDateFormat AMAZON_DATE = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
    private static final SimpleDateFormat EXPORT_DATE = new SimpleDateFormat("dd-MM-yyyy");
    private final WebClient webClient = CrawlerUtil.getChrome(false, false);
    // private final String BASE_URL =
    // "http://www.amazon.com/Medialink-Wireless-N-Broadband-Internal-Antennas/product-reviews/B00A3YN0Z0/?filterBy=addFiveStar&pageNumber=%s&sortBy=bySubmissionDateDescending";
    private final String BASE_URL =
        "http://www.amazon.com/RT-N66U-Dual-Band-Wireless-N900-Gigabit-Router/product-reviews/B006QB1RPY/?filterBy=addFiveStar&pageNumber=%s&showViewpoints=0";

    private final File reviewFrequency = new File("reviewFrequencyASUS.csv");

    private Map<Date, Integer> dateFrequency = new HashMap<Date, Integer>();

    @Resource
    private ReviewDao reviewDao;

    @Setter
    private Integer pageNo = 1;

    public void crawlTimeFrequency() {

        String amazonOverviewURL = getURL(pageNo);
        HtmlPage amazonPage;
        try {
            amazonPage = CrawlerUtil.getWebPage(webClient, amazonOverviewURL, 0);

            // First Page
            crawlProducts(amazonPage);
            pageNo++;

            for (; pageNo < 158; pageNo++) {

                try {
                    amazonOverviewURL = getURL(pageNo);
                    amazonPage = CrawlerUtil.getWebPage(webClient, amazonOverviewURL, 0);
                    crawlProducts(amazonPage);
                } catch (CrawlerException e) {
                    log.warn("Could not load a following page", e);
                }

            }
        } catch (CrawlerException e) {
            log.error("Could not load first page", e);
        }

        generateCsvFile();

    }

    private void crawlProducts(HtmlPage amazonPage) {
        @SuppressWarnings("unchecked")
        List<DomElement> domReviews = (List<DomElement>) amazonPage.getByXPath("//div[@style='margin-left:0.5em;']");

        for (DomElement domReview : domReviews) {
            Review review = new Review();

            extractDate(domReview, review);
            extractAuthorID(domReview, review);
            extractReview(domReview, review);
            review.setTimestamp(new Date());

            reviewDao.save(review);
        }

    }

    private void extractReview(DomElement domReview, Review review) {
        DomElement domReviewText = domReview.getFirstByXPath(".//div[@class='reviewText']");
        String reviewText = domReviewText.asText();

        review.setContent(reviewText);
    }

    private void extractAuthorID(DomElement domReview, Review review) {
        DomElement domUser = domReview.getFirstByXPath(".//div[@style='float:left;']/a");
        String userID = domUser.getAttribute("href");
        userID = userID.replace("/gp/pdp/profile/", "");
        userID = userID.replace("/ref=cm_cr_pr_pdp", "");

        review.setAuthorId(userID);
    }

    private void extractDate(DomElement domReview, Review review) {

        try {
            DomElement domDate = domReview.getFirstByXPath(".//span[@style='vertical-align:middle;']/nobr");
            Date date = AMAZON_DATE.parse(domDate.asText());
            review.setReviewDate(date);

            incrementReviewFrequency(date);
        } catch (ParseException e) {
            log.error("Couldn't parse date", e);
        }

    }

    private void generateCsvFile() {
        try (FileWriter writer = new FileWriter(reviewFrequency)) {
            for (Entry<Date, Integer> rating : dateFrequency.entrySet()) {
                writer.append(EXPORT_DATE.format(rating.getKey()));
                writer.append('\t');
                writer.append(rating.getValue().toString());
                writer.append('\n');
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error("Could not write CSV File", e);
        }
    }

    private String getURL(Integer pageNo) {
        return String.format(BASE_URL, pageNo);
    }

    private void incrementReviewFrequency(Date date) {
        Integer frequency = dateFrequency.get(date);
        if (frequency != null) {
            frequency++;
            dateFrequency.put(date, frequency);

            return;
        }
        dateFrequency.put(date, 1);
    }
}
