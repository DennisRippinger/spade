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
package info.interactivesystems.spade.util;

import info.interactivesystems.spade.dto.DiffContainer;
import info.interactivesystems.spade.entities.Review;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import name.fraser.neil.plaintext.SemanticBreakScorer;
import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Component;

/**
 * The Class DiffCreator.
 * 
 * @author Dennis Rippinger
 */
@Slf4j
@Component
public class DiffCreator {

    private static final int BREAK_SCORE = 0;

    private diff_match_patch diff;

    /**
     * Creates a {@link DiffContainer} with the Diff as HTML representation and the number of differences.
     * 
     * @param one the first review.
     * @param two the second review.
     * @return the differences in a {@link DiffContainer}
     */
    public DiffContainer getDifferences(Review one, Review two) {
        String oneContent = decode(one.getContent());
        String twoContent = decode(two.getContent());
        List<Diff> differences = diff.diff_main(oneContent, twoContent);

        log.debug("Found '{}' differences.", differences.size());

        String html = createHTML(differences);
        html = html.replaceAll("style=\"background:#E6FFE6;", "class = \"added;");
        html = html.replaceAll("style=\"background:#FFE6E6;", "class = \"removed;");

        DiffContainer result = new DiffContainer();
        result.setHtml(html);
        result.setNumberOfDifferences(differences.size());

        return result;
    }

    @PostConstruct
    public void init() {
        log.debug("Create new DiffMatcher with a Break Score of '{}'", BREAK_SCORE);
        diff = getDiffMatcher();
    }

    /**
     * Factory Methods for own Score Breaker.
     * 
     * @return own Score Breaker
     */
    private diff_match_patch getDiffMatcher() {
        SemanticBreakScorer scorer = new SemanticBreakScorer() {

            @Override
            public int scoreBreakOver(String one, String two) {
                return BREAK_SCORE;
            }
        };

        return new diff_match_patch(scorer);
    }

    /**
     * Modified Method of diff_match_patch
     * 
     * @param diffs the list of diffs.
     * @return an HTML String.
     */
    private String createHTML(List<Diff> diffs) {
        StringBuilder html = new StringBuilder();
        for (Diff aDiff : diffs) {
            String text = aDiff.text.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\n", "&para;<BR>");
            switch (aDiff.operation) {
                case INSERT:
                    html.append("<ins class = \"added\">")
                        .append(text).append("</ins>");
                    break;
                case DELETE:
                    html.append("<del class = \"removed\">")
                        .append(text).append("</DEL>");
                    break;
                case EQUAL:
                    html.append("<span>").append(text)
                        .append("</span>");
                    break;
            }
        }
        return html.toString();
    }

    private String decode(String content) {
        return StringEscapeUtils.unescapeHtml4(content);
    }

}
