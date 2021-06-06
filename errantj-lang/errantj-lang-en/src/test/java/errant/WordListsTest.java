package errant;

import edu.guym.errantj.lang.en.wordlist.HunspellEnglishWordList;
import edu.guym.errantj.wordlist.WordList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WordListsTest {

    @Test
    void testHunspell() {
        WordList wordList = new HunspellEnglishWordList();
        Assertions.assertTrue(wordList.contains("good"));
    }
}
