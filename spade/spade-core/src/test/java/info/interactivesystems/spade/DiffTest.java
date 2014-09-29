package info.interactivesystems.spade;

import info.interactivesystems.spade.dao.NilsimsaSimilarityDao;
import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import lombok.extern.slf4j.Slf4j;
import name.fraser.neil.plaintext.SemanticBreakScorer;
import name.fraser.neil.plaintext.diff_match_patch;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@ContextConfiguration(locations = {"classpath:beans.xml"})
public class DiffTest extends AbstractTestNGSpringContextTests {

	@Resource
	private ReviewContentService service;

	@Resource
	private NilsimsaSimilarityDao nilsimsa;

	@Test
	public void comparableTest() {

		diff_match_patch dmp = getDiffMatcher();
		List<NilsimsaSimilarity> relevantPairs = nilsimsa.find(0.915, false, 20, 30);
		for (NilsimsaSimilarity nilsimsaSimilarity : relevantPairs) {
//            Review reviewA = service.findReview(nilsimsaSimilarity.getReviewA());
//            Review reviewB = service.findReview(nilsimsaSimilarity.getReviewB());

//            LinkedList<Diff> diff_main = dmp.diff_main(reviewA.getContent(), reviewB.getContent());
//            String diff_prettyHtml = dmp.diff_prettyHtml(diff_main);
//            System.out.println("<p>Similarity: " + nilsimsaSimilarity.getSimilarity() + "</p><div style=\"background-color: #CCC3F3\"><p>" + diff_prettyHtml + "</p></div>");
		}

	}

	private diff_match_patch getDiffMatcher() {
		SemanticBreakScorer scorer = new SemanticBreakScorer() {

			@Override
			public int scoreBreakOver(String one, String two) {
				return 0;
			}
		};

		diff_match_patch dmp = new diff_match_patch(scorer);
		return dmp;
	}

}
