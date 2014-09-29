/**
 *
 */
package info.interactivesystems.spade.sentence;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * @author Dennis Rippinger
 */
@Slf4j
@ContextConfiguration(locations = {"classpath:beans.xml"})
public class SentenceServiceTest extends AbstractTestNGSpringContextTests {

	@Resource
	private SentenceService sentenceService;

	@DataProvider(name = "providedStrings")
	public Object[][] provideData() {

		return new Object[][]{
				{"Unlike the other indices, the ARI, along with the Coleman-Liau, relies on a factor of characters per word, "
						+ "instead of the usual syllables per word. Although opinion varies on its accuracy as compared to the "
						+ "syllables/word and complex words indices, characters/word is often faster to calculate, as the number "
						+ "of characters is more readily and accurately counted by computer programs than syllables. In fact, "
						+ "this index was designed for real-time monitoring of readability on electric typewriters.", "ARI Text"},
				{
						"Das AS-AD-Modell ist ein Modell der Makroökonomie und beschreibt das gesamtwirtschaftliche Gleichgewicht in der geschlossenen Volkswirtschaft auf eine mittellange Frist. Das AS-AD-Modell soll die gesamt­wirtschaftlichen Auswirkungen von staatlichen und finanz­politischen Maßnahmen auf die Löhne, das Preisniveau und die Produktion beschreiben. Das Modell setzt sich aus dem Teil des aggregierten Angebotes (englisch für „aggregate supply“, Abkürzung „AS“) nach dem neoklassischen Ansatz und dem Teil der aggregierten Nachfrage („aggregate demand“, „AD“) nach John Maynard Keynes zusammen.",
						"AS-AD-Model"},
				{
						"Often, you will be told that programming languages do not matter much. What actually matters more is not clear; maybe tools, maybe methodology, maybe process. It is a pretty general rule that people arguing that language does not matter are simply trying to justify their use of bad languages. Let us come back to the Apple bug of a few weeks ago. Only a few weeks; the world has already moved to Heartbleed, but that is not a reason to sweep away the memory of the Apple bug and the language design that it reflects.",
						"Blog Text"}
		};
	}

	@Test(dataProvider = "providedStrings")
	public void calculateGFIndexTest(String review, String description) {
		Double readability = sentenceService.calculateGFIndex(review);
		log.info("Test String '{}' has a GFI readability of '{}'", description, readability);
	}

	@Test(dataProvider = "providedStrings")
	public void calculateARIndexTest(String review, String description) {
		Double readability = sentenceService.calculateARIndex(review);
		log.info("Test String '{}' has a ARI readability of '{}'", description, readability);
	}

	@Test(dataProvider = "providedStrings")
	public void calculateInformationDensityTest(String review, String description) {
		Double density = sentenceService.calculateInformationDensity(review);
		log.info("Test String '{}' has a Information Density of '{}'", description, density);
	}

}
