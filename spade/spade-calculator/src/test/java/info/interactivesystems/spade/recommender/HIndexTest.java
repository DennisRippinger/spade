package info.interactivesystems.spade.recommender;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class HIndexTest extends AbstractTestNGSpringContextTests {

    @Resource
    private HIndex hIndex;

    @Test
    public void calculateHIndex() {
        hIndex.calculateHIndex(1l, 2441053L);
    }
}
