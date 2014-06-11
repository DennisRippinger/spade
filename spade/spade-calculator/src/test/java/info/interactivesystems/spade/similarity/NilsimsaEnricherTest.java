package info.interactivesystems.spade.similarity;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class NilsimsaEnricherTest extends AbstractTestNGSpringContextTests {

    @Resource
    private NilsimsaEnricher enricher;

    @Test
    public void enrichNilsimsaValues() {
        enricher.enrichNilsimsaValues(0.95);
    }
}
