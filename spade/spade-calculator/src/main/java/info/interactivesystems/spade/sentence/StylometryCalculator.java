/**
 * Copyright 2014 Dennis Rippinger
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.interactivesystems.spade.sentence;

import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.Stylometry;
import info.interactivesystems.spade.nlp.SentenceDetector;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Calculates Stylometric statistic values for further compare operations.
 *
 * @author Dennis Rippinger
 */
@Component
public class StylometryCalculator {

	@Resource
	private ReviewContentService service;

	@Resource
	private SentenceDetector sentenceDetector;

	public void calculateStylometryValues(Review review) {
		Stylometry stylometry = new Stylometry();

		List<String> sentences = getSentences(review.getContent());
		List<String> words = getWords(sentences);

		stylometry.setWordLevel(getWordLevel(words));

		stylometry.setDigits(getDigits(review.getContent(), 1));
		stylometry.setDigitBigrams(getDigits(review.getContent(), 2));
		stylometry.setDigitTrigrams(getDigits(review.getContent(), 3));
		stylometry.setDollarOccurence(getSpecialCharacter('@', review.getContent()));
		stylometry.setExclamationMarkOccurence(getSpecialCharacter('!', review.getContent()));
		stylometry.setQuestionmarkOccurence(getSpecialCharacter('?', review.getContent()));
		stylometry.setCommaOccurence(getSpecialCharacter(',', review.getContent()));

		review.setStylometry(stylometry);

		service.saveReview(review);
	}

	private Double getSpecialCharacter(char character, String review) {
		String patter = String.format("[%s]{1}", character);
		Pattern charPattern = Pattern.compile(patter);

		Double result = 0.0;
		Matcher matcher = charPattern.matcher(review);
		while (matcher.find()) {
			result++;
		}

		result /= review.length();

		return result;
	}

	private Double getDigits(String review, Integer times) {
		String patter = String.format("[ ][0-9]{%s}[ ]", times);
		Pattern digitPattern = Pattern.compile(patter);

		Double result = 0.0;
		Matcher matcher = digitPattern.matcher(review);
		while (matcher.find()) {
			result++;
		}

		result /= review.length();

		return result;
	}

	private Double getWordLevel(List<String> words) {
		Double result = 0.0;
		for (String word : words) {
			result += word.length();
		}
		result /= words.size();

		return result;
	}

	private List<String> getSentences(String review) {
		return sentenceDetector.detectSentencesFromCorpus(review);
	}

	private List<String> getWords(List<String> sentences) {
		List<String> result = new LinkedList<>();

		for (String sentence : sentences) {
			String[] split = sentence.split(" ");
			result.addAll(Arrays.asList(split));
		}

		return result;
	}
}
