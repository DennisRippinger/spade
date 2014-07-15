package info.interactivesystems.spade.importer;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class UniqueReviewCrystalizerTest extends AbstractTestNGSpringContextTests {

    @Resource
    private UniqueReviewCrystalizer crystalizer;

    @Test
    public void tagUniqueReviews() {
        crystalizer.tagUniqueReviews();
    }
}
