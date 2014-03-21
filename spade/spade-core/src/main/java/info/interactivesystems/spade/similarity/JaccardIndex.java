package info.interactivesystems.spade.similarity;

import info.interactivesystems.spade.entities.SimilartyMesurement;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class JaccardIndex implements SimilarityCalculator {

	@Override
	public SimilartyMesurement calculateSimilarity(String corpusFirst,
			String corpusSecond) {
		Set<String> setCurpusFirst = getSetOfString(corpusFirst);
		Set<String> setCurpusSecound = getSetOfString(corpusSecond);

		Set<String> intersection = new HashSet<String>(setCurpusFirst);
		intersection.retainAll(setCurpusSecound);

		Set<String> merged = new HashSet<String>(setCurpusFirst);
		merged.addAll(setCurpusSecound);

		Double conclusion = intersection.size() * 1.0 / merged.size() * 1.0;

		SimilartyMesurement result = new SimilartyMesurement();

		result.setSimilarty(conclusion);

		return result;
	}

	private Set<String> getSetOfString(String corpus) {
		StringTokenizer tokenizer = new StringTokenizer(corpus);
		Set<String> result = new HashSet<String>();

		while (tokenizer.hasMoreElements()) {
			result.add(tokenizer.nextToken());
		}

		return result;
	}
}
