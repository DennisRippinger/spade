package info.interactivesystems.spade.calculation;

import static org.assertj.core.api.Assertions.assertThat;
import info.interactivesystems.spade.entities.SentenceSimilarity;
import info.interactivesystems.spade.util.ProductCategory;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class ReactorTest extends AbstractTestNGSpringContextTests {

    @Resource
    private Reactor reactor;

    @Test
    public void agregateSentenceSimilarity() {
        List<SentenceSimilarity> agregateSentenceSimilarity = reactor.agregateSentenceSimilarity(ProductCategory.DIGITAL_CAMERA);

        assertThat(agregateSentenceSimilarity).isNotEmpty();

    }
}
