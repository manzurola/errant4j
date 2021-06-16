package edu.guym.errantj.lang.en.wordlist;

import edu.guym.errantj.lang.en.wordlist.HunspellWordList;
import edu.guym.errantj.lang.en.wordlist.WordList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WordListsTest {

    @Test
    void testHunspell() {
        WordList wordList = new HunspellWordList();
        Assertions.assertTrue(wordList.contains("good"));
    }
}
