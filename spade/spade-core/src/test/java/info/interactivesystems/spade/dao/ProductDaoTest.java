package info.interactivesystems.spade.dao;

import static org.assertj.core.api.Assertions.assertThat;
import info.interactivesystems.spade.entities.Product;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.util.ProductCategory;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Transactional
//@Test(groups = { "FunctionTest" })
@ContextConfiguration(locations = { "classpath:beans.xml" })
public class ProductDaoTest extends AbstractTestNGSpringContextTests {

    @Resource
    private ProductDao productDao;

    private Product demoValue;

    @BeforeTest
    private void getDemoProduct() {
        Product result = new Product();
        result.setId("AED12345");
        result.setName("Demo Product");
        result.setSource("Test source");
        result.setType(ProductCategory.DIGITAL_CAMERA.getId());
        result.setRating(4.5);

        Review review = new Review();
        review.setId("123");
        review.setAuthor("Test");
        review.setProduct(result);
        Set<Review> setReview = new LinkedHashSet<Review>();
        setReview.add(review);
        result.setReviews(setReview);

        demoValue = result;
    }

    @Test
    public void delete() {
        productDao.save(demoValue);
        Product found = productDao.find(demoValue.getId());

        assertThat(found).isNotNull();
        assertThat(found).isEqualsToByComparingFields(demoValue);

        productDao.delete(demoValue);

        found = productDao.find(found.getId());
        assertThat(found).isNull();
    }

    @Test
    public void find() {
        productDao.save(demoValue);
        Product found = productDao.find(demoValue.getId());

        assertThat(found).isNotNull();
        assertThat(found).isEqualsToByComparingFields(demoValue);

        productDao.delete(demoValue);

        found = productDao.find(found.getId());
        assertThat(found).isNull();
    }

    @Test
    public void save() {
        productDao.save(demoValue);
        Product found = productDao.find(demoValue.getId());

        assertThat(found).isNotNull();
        assertThat(found).isEqualsToByComparingFields(demoValue);

        productDao.delete(demoValue);

        found = productDao.find(found.getId());
        assertThat(found).isNull();
    }
}