package info.interactivesystems.spade.recommender;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class TargetItemRetrivalTest extends AbstractTestNGSpringContextTests {

    @Resource
    private TargetItemRetrival retrieval;

    @Test
    public void retrivalOfTargetItems() {
        retrieval.retrivalOfTargetItems();
    }
}
