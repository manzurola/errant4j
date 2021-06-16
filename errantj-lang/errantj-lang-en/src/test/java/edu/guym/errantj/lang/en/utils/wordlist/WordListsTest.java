package edu.guym.errantj.lang.en.utils.wordlist;

import edu.guym.errantj.lang.en.utils.wordlist.HunspellWordList;
import edu.guym.errantj.lang.en.utils.wordlist.WordList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WordListsTest {

    @Test
    void testHunspell() {
        WordList wordList = new HunspellWordList();
        Assertions.assertTrue(wordList.contains("good"));
    }
}
