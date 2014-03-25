package info.interactivesystems.spade.calculation;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import info.interactivesystems.spade.util.ProductCategory;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class SentenceFrequencyAggregatorTest extends AbstractTestNGSpringContextTests {

    @Resource
    private SentenceFrequencyAggregator sentenceFrequency;

    @Test
    public void aggregateSenenceFrequency() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        sentenceFrequency.aggregateSentenceFrequency(ProductCategory.DIGITAL_CAMERA);
    }
}