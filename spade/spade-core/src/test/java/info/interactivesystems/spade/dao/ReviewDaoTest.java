package info.interactivesystems.spade.dao;

import static org.assertj.core.api.Assertions.assertThat;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.util.ProductCategory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class ReviewDaoTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReviewDao reviewDao;

    @Test
    public void getReviewsOfCategory() {
        List<Review> reviews = reviewDao.getReviewsOfCategory(ProductCategory.BlurayPlayer);

        assertThat(reviews).isNotNull();
        assertThat(reviews).isNotEmpty();
        // assertThat(reviews).hasSize(348);

    }
}
