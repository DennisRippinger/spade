package info.interactivesystems.spade.similarity;

import info.interactivesystems.spade.entities.SimilartyMesurement;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Calculates the Tanimoto resemblance.
 * 
 * @author Dennis Rippinger
 * 
 */
public class TanimotoResemblance implements SimilarityCalculator {

	/**
	 * Calculates the Tanimot resemblance. The result value is between 0 and 1,
	 * where 1 is equality.
	 * 
	 * The vector multiplication is performed by simply counting the equal words
	 * within the word bags. To avoid doublets a found word is taken out of the
	 * second vector.
	 */
	@Override
	public SimilartyMesurement calculateSimilarity(String corpusFirst,
			String corpusSecond) {
		List<String> listCorpusFirst = getListOfString(corpusFirst);
		List<String> listCorpusSecound = getListOfString(corpusSecond);

		Integer firstSize = listCorpusFirst.size();
		Integer secondSize = listCorpusSecound.size();

		Integer numerator = getSameTokenCount(listCorpusFirst,
				listCorpusSecound);
		Integer denominator = firstSize + secondSize - numerator;

		Double conclusion = numerator * 1.0 / denominator * 1.0;
		SimilartyMesurement result = new SimilartyMesurement();
		result.setSimilarty(conclusion);

		return result;
	}

	private Integer getSameTokenCount(List<String> listCorpusFirst,
			List<String> listCorpusSecound) {
		Integer result = 0;

		for (String token : listCorpusFirst) {
			if (listCorpusSecound.contains(token)) {
				listCorpusSecound.remove(token);
				result++;
			}
		}
		return result;

	}

	private List<String> getListOfString(String corpus) {
		StringTokenizer tokenizer = new StringTokenizer(corpus);
		List<String> result = new ArrayList<String>();

		while (tokenizer.hasMoreElements()) {
			result.add(tokenizer.nextToken());
		}

		return result;
	}

}
