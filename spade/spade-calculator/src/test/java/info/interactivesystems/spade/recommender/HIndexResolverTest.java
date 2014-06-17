package info.interactivesystems.spade.recommender;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class HIndexResolverTest extends AbstractTestNGSpringContextTests {

    @Resource
    private HIndexResolver resolver;

    @Test
    public void resolveHIndex() {
        resolver.resolveHIndex(5.0);
    }
}