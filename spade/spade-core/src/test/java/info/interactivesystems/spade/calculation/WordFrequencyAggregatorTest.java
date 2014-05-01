package info.interactivesystems.spade.calculation;

import info.interactivesystems.spade.util.Authority;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:beans.xml" })
public class WordFrequencyAggregatorTest extends AbstractTestNGSpringContextTests {

    @Resource
    WordFrequencyAggregator wfa;

    @Test
    public void f() {
        wfa.aggregateWordFrequency(Authority.AMAZON);
    }
}
