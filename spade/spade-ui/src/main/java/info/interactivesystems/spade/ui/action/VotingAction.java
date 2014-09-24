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
package info.interactivesystems.spade.ui.action;

import info.interactivesystems.spade.dao.VoteDao;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.entities.Review;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.entities.Vote;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Class VotingAction.
 *
 * @author Dennis Rippinger
 */
@Named
@Scope("session")
public class VotingAction implements Serializable {

	private static final long serialVersionUID = -8312158521289713226L;

	@Resource
	private VoteDao voteDao;

	@Resource
	private SimilaritiesAction similaritiesAction;

	@Getter
	private List<String> options;

	@Setter
	@Getter
	private String[] userAVotes;

	@Setter
	@Getter
	private String[] userBVotes;

	private ResourceBundle localization;

	/**
	 * Initializes Options.
	 */
	@PostConstruct
	public void init() {
		options = new LinkedList<>();

		localization = ResourceBundle.getBundle("MessageResources");

		options.add(localization.getString("info.reason.ospam"));
		options.add(localization.getString("info.reason.advertising"));
		options.add(localization.getString("info.reason.editFunction"));
		options.add(localization.getString("info.reason.angry"));
		options.add(localization.getString("info.reason.lazy"));
		options.add(localization.getString("info.reason.differentTopic"));
		options.add(localization.getString("info.reason.somethingElse"));
	}

	/**
	 * Submit of choices.
	 */
	public void submit(NilsimsaSimilarity similarPair) {

		if (userBVotes.length > 0) {
			Vote voteOnUserB = extractVoteEntity(similarPair.getUserB(), similarPair.getReviewB(), similarPair.getSimilarity(), userBVotes);
			userBVotes = new String[]{};
			voteDao.save(voteOnUserB);
		}

		if (userAVotes.length > 0) {
			Vote voteOnUserA = extractVoteEntity(similarPair.getUserA(), similarPair.getReviewA(), similarPair.getSimilarity(), userAVotes);
			userAVotes = new String[]{};
			voteDao.save(voteOnUserA);
		}

		similaritiesAction.next();

	}

	private Vote extractVoteEntity(User user, Review review, Double similarity, String[] userVotes) {
		Vote vote = new Vote();

		vote.setSessionID(similaritiesAction.getSessionID());
		vote.setUser(user);
		vote.setReview(review);
		vote.setSimilarity(similarity);

		List<String> votes = Arrays.asList(userVotes);

		isEditFunction(vote, votes);
		isAngry(vote, votes);
		isLazy(vote, votes);
		isOspam(vote, votes);
		isAdvertising(vote, votes);
		isDifferentTopic(vote, votes);
		isSomethingElse(vote, votes);

		return vote;
	}

	private void isSomethingElse(Vote vote, List<String> votes) {
		if (votes.contains(localization.getString("info.reason.somethingElse"))) {
			vote.setSomethingElse(true);
		}
	}

	private void isDifferentTopic(Vote vote, List<String> votes) {
		if (votes.contains(localization.getString("info.reason.differentTopic"))) {
			vote.setReviewIsNotReason(true);
		}
	}

	private void isAdvertising(Vote vote, List<String> votes) {
		if (votes.contains(localization.getString("info.reason.advertising"))) {
			vote.setAdvertising(true);
		}
	}

	private void isOspam(Vote vote, List<String> votes) {
		if (votes.contains(localization.getString("info.reason.ospam"))) {
			vote.setOspam(true);
		}
	}

	private void isLazy(Vote vote, List<String> votes) {
		if (votes.contains(localization.getString("info.reason.lazy"))) {
			vote.setLazyUser(true);
		}
	}

	private void isAngry(Vote vote, List<String> votes) {
		if (votes.contains(localization.getString("info.reason.angry"))) {
			vote.setAngryCustomer(true);
		}
	}

	private void isEditFunction(Vote vote, List<String> votes) {
		if (votes.contains(localization.getString("info.reason.editFunction"))) {
			vote.setUnknownEditFunction(true);
		}
	}

}
