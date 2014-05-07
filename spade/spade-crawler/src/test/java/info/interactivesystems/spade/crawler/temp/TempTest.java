package info.interactivesystems.spade.crawler.temp;

import lombok.extern.slf4j.Slf4j;

import org.testng.annotations.Test;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

@Slf4j
public class TempTest {

    @Test
    public void wordnet() {
        System.setProperty("wordnet.database.dir", "/Users/dennisrippinger/Downloads/WordNet-3.0/dict");

        WordNetDatabase database = WordNetDatabase.getFileInstance();
        Synset[] manSynset = database.getSynsets("man", SynsetType.NOUN);
        Synset[] womanSynset = database.getSynsets("woman", SynsetType.NOUN);

        NounSynset manSyn = (NounSynset) (manSynset[0]);
        NounSynset womanSyn = (NounSynset) (womanSynset[0]);
        
        NounSynset[] manHySyn = manSyn.getHypernyms();
        NounSynset[] womanHySyn = womanSyn.getHypernyms();
        
        System.out.println(manHySyn[1].getDefinition());
        System.out.println(womanHySyn[1].getDefinition());


    }

}
