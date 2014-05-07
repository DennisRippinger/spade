package info.interactivesystems.spade.crawler.amazon;

import info.interactivesystems.spade.dao.ProductDao;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.util.ProductCategory;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Slf4j
@ContextConfiguration(locations = { "classpath:beans.xml" })
public class AmazonReviewCrawlerTest extends AbstractTestNGSpringContextTests {

//    @Resource
//    private AmazonReviewCrawler crawler;
//
//    @Resource
//    private ProductDao productDao;
//
//    private ProductCategory category = ProductCategory.DIGITAL_CAMERA;
//
//    @Test
//    public void f() {
//        List<Product> allOfCategory = productDao.getAllOfCategory(category);
//        log.info("Category '{}' has '{}' Products", category.toString(), allOfCategory.size());
//        Integer counter = 1;
//
//        for (Product product : allOfCategory) {
//            log.info("Item No '{}'", counter++);
//
//            crawler.setPage(1);
//            crawler.setNoReviews(10);
//
//            crawler.crawlReviews(product);
//        }
//    }
}
