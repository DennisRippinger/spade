package info.interactivesystems.spade.sentence;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@ContextConfiguration(locations = {"classpath:beans.xml"})
public class StylometryCalculatorTest extends AbstractTestNGSpringContextTests {

	@Resource
	private ReviewContentService service;

	@Resource
	private StylometryCalculator calculator;

	@Test
	public void calculateStylometryValues() {
		for (Long id = 1L; id <= 6643623L; id++) {
			User user = service.findUserByID(id);
			List<Review> reviews = service.findReviewFromUser(user.getId());

			for (Review review : reviews) {
				calculator.calculateStylometryValues(review);
			}

			// Reduce output noise
			Integer rand = ThreadLocalRandom.current().nextInt(1, 2000);
			if (rand == 500) {
				log.info("Current User '{}'. Position '{}'", user.getName(), user.getRandomID());
			}
		}

	}
}
