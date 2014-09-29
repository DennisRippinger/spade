package info.interactivesystems.spade.recommender;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"classpath:beans.xml"})
public class TargetItemRetrivalTest extends AbstractTestNGSpringContextTests {

	@Resource
	private TargetItemRetrieval retrieval;

	@Test
	public void retrivalOfTargetItems() {
		retrieval.retrievalOfTargetItems();
	}
}
