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
package info.interactivesystems.spade.semantic;

import info.interactivesystems.spade.dto.SynSetWrapper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

/**
 * The Class WordnetResemblance.
 * 
 * @author Dennis Rippinger
 */
public class WordnetResemblance implements SemanticResemblance {

    private WordNetDatabase database;

    @PostConstruct
    public void init() {
        database = SemanticFactory.getWordnetDatabase();
    }

    @Override
    public Float calculateSimilarity(String wordOne, String wordTwo) {
        getPathToRoot(wordOne);

        return null;
    }

    private void getPathToRoot(String word) {
        Boolean isRoot = false;
        List<SynSetWrapper> synsets = new LinkedList<SynSetWrapper>();

        Synset[] baseSynset = database.getSynsets(word);
        synsets.add(new SynSetWrapper(baseSynset));

        
//        while (!isRoot) {
//            NounSynset manSyn = (NounSynset) (manSynset[0]);
//        }

    }

}
