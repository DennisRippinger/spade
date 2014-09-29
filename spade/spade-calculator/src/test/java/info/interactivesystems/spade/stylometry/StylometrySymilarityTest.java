package info.interactivesystems.spade.stylometry;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"classpath:beans.xml"})
public class StylometrySymilarityTest extends AbstractTestNGSpringContextTests {

	@Resource
	private ReviewContentService service;

	@Resource
	private StylometrySimilarity pca;

	@Test
	public void testCalculatePCA() {

		for (Long id = 1L; id <= 6643623L; id++) {
			User user = service.findUserByID(id);

			if (user != null && user.getNumberOfReviews() < 1000) {
				pca.calculateCosSimilarity(user);
			}
		}

	}
}