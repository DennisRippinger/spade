package info.interactivesystems.spade.crawler.yelp;

import info.interactivesystems.spade.crawler.util.CrawlerUtil;
import info.interactivesystems.spade.dao.ReviewDao;
import info.interactivesystems.spade.dao.UserDao;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.exception.CrawlerException;
import info.interactivesystems.spade.util.Authority;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class YelpReviewCrawler {

    private static final String YELP_URL = "http://www.yelp.com%s?start=%s";
    private static final SimpleDateFormat YELP_DATE = new SimpleDateFormat("YYYY-MM-dd");

    private final Pattern starPattern = Pattern.compile("\\d[.\\d]*");

    private WebClient webClient = CrawlerUtil.getRandomDesktopWebClient(false, false);

    @Resource
    private ReviewDao reviewDao;

    @Resource
    private UserDao userDao;

    @Setter
    private Integer page = 0;

    public void crawlReviews(Product yelpVenue) {
        String yelpOverviewURL = getURL(yelpVenue.getId());
        HtmlPage yelpPage;
        try {
            yelpPage = CrawlerUtil.getWebPage(webClient, yelpOverviewURL, 0);
            Integer numberOfPages = getMaximumItems(yelpPage, yelpVenue);

            crawlReviews(yelpPage, yelpVenue);

            for (; page < numberOfPages; page++) {

                try {
                    yelpOverviewURL = getURL(yelpVenue.getId());
                    yelpPage = CrawlerUtil.getWebPage(webClient, yelpOverviewURL, 3);
                    crawlReviews(yelpPage, yelpVenue);
                } catch (CrawlerException e) {
                    log.warn("Could not load a following page", e);
                }
            }
        } catch (CrawlerException e1) {
            log.error("Could not load first page", e1);
        }

    }

    private void crawlReviews(HtmlPage yelpPage, Product yelpVenue) {
        @SuppressWarnings("unchecked")
        List<DomElement> reviews = (List<DomElement>) yelpPage
            .getByXPath("//*[@id='super-container']/div/div[1]/div[1]/div[3]/div[1]/div[2]/ul/li");

        for (DomElement domReview : reviews) {
            try {
                Review review = new Review();

                extractReviewID(review, domReview);
                extractReview(review, domReview);
                extractReviewDate(review, domReview);
                extractRating(review, domReview);
                extractUser(review, domReview);
                extractCheckins(review, domReview);
                extractVotings(review, domReview);

                review.setProduct(yelpVenue);

                if (!reviewDao.checkIfAlreadyExists(review.getId())) {
                    reviewDao.save(review);
                }

            } catch (ParseException e) {
                log.warn("Could not parse the date", e);
            } catch (NullPointerException e) {
                log.warn("NullPointer thrown, possible captcha", e);
            }
        }
    }

    private void extractVotings(Review review, DomElement domReview) {
        @SuppressWarnings("unchecked")
        List<DomElement> votings = (List<DomElement>) domReview.getByXPath(".//span[@class='count']");
        if (votings != null) {
            String useful = votings.get(0).asText();
            String funny = votings.get(1).asText();
            String cool = votings.get(2).asText();

            if (!useful.isEmpty()) {
                Integer usefulCount = Integer.parseInt(useful);
                review.setVoteUseful(usefulCount);
            }
            if (!funny.isEmpty()) {
                Integer funnyCount = Integer.parseInt(funny);
                review.setVoteFunny(funnyCount);
            }
            if (!cool.isEmpty()) {
                Integer coolCount = Integer.parseInt(cool);
                review.setVoteCool(coolCount);
            }

        }

    }

    private void extractCheckins(Review yelpReview, DomElement domReview) {
        DomElement domChecking = domReview
            .getFirstByXPath(".//span[@class='i-wrap ig-wrap-common i-checkin-burst-blue-small-common-wrap badge checkin checkin-irregular']");
        if (domChecking != null) {
            String checkingString = domChecking.asText();
            Matcher matcher = starPattern.matcher(checkingString);
            if (matcher.find()) {
                String rating = matcher.group();
                Integer result = Integer.parseInt(rating);
                yelpReview.setCheckins(result);
            }
        }
    }

    private void extractRating(Review review, DomElement domReview) {
        DomElement domRating = domReview.getFirstByXPath(".//div[@class='rating-very-large']/i");
        String ratingString = domRating.getAttribute("title");
        Matcher matcher = starPattern.matcher(ratingString);
        if (matcher.find()) {
            String rating = matcher.group();
            Double result = Double.parseDouble(rating);
            review.setRating(result);
        }
    }

    private void extractReview(Review review, DomElement domReview) {
        DomElement domReviewText = domReview.getFirstByXPath(".//p[@itemprop='description']");
        if (domReviewText != null) {
            // Yelp allows empty reviews ... whatever.
            String reviewText = domReviewText.asText();

            review.setContent(reviewText);
        }

    }

    private void extractReviewDate(Review review, DomElement domReview) throws ParseException {
        DomElement domPublished = domReview.getFirstByXPath(".//meta[@itemprop='datePublished']");
        String dateString = domPublished.getAttribute("content");
        Date published = YELP_DATE.parse(dateString);
        review.setReviewDate(published);
    }

    private void extractReviewID(Review review, DomElement domReview) {
        DomElement domID = domReview.getFirstByXPath(".//div[@itemprop='review']");
        String reviewID = domID.getAttribute("data-review-id");

        review.setId(reviewID);

    }

    private void extractUser(Review review, DomElement domReview) {
        User user = new User();

        DomElement domUser = domReview.getFirstByXPath(".//a[@class='user-display-name']");
        DomElement domNoReviews = domReview
            .getFirstByXPath(".//span[@class='i-wrap ig-wrap-common i-star-orange-common-wrap']/b");

        String userURL = domUser.getAttribute("href");
        String userId = getUserID(userURL);
        String userName = domUser.getTextContent();
        Integer noReviews = Integer.parseInt(domNoReviews.getTextContent());

        user.setId(userId);
        user.setName(userName);
        user.setNumberOfReviews(noReviews);
        user.setAuthority(Authority.YELP);

        review.setAuthorId(userId);

        // Random ID eats Update.
        if (!userDao.checkIfAlreadyExists(userId)) {
            userDao.save(user);
        }
    }

    private Integer getMaximumItems(HtmlPage yelpPage, Product yelpVenue) {
        DomElement paginationElement = yelpPage.getFirstByXPath("//div[@class='page-of-pages']");
        String paginationString = paginationElement.getTextContent();

        String[] split = paginationString.trim().split(" ");
        if (split.length >= 3) {
            String value = split[split.length - 1];
            Integer result = Integer.parseInt(value.trim());
            log.info("Venue '{}' has '{}' pages of reviews.", yelpVenue.getName(), result);
            return result;
        }
        log.error("Could not find pagination info");
        return 0;
    }

    private String getURL(String venue) {
        Integer from = page * 40;
        String url = String.format(YELP_URL, venue, from);

        return url;
    }

    private String getUserID(String userURL) {
        // e.g. '/user_details?userid=BnQBu7KH6nBZ25E2tkajng'
        String[] userIDArray = userURL.split("=");
        return userIDArray[userIDArray.length - 1];
    }

}
