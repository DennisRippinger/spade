package info.interactivesystems.spade.importer;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"classpath:beans.xml"})
public class UniqueReviewCrystalizerTest extends AbstractTestNGSpringContextTests {

	@Resource
	private UniqueReviewCrystalizer crystalizer;

	@Test
	public void tagUniqueReviews() {
		crystalizer.tagUniqueReviews();
	}
}
