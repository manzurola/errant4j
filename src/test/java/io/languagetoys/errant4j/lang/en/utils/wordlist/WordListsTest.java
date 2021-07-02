package io.languagetoys.errant4j.lang.en.utils.wordlist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WordListsTest {

    @Test
    void testHunspell() {
        WordList wordList = new HunspellWordList();
        Assertions.assertTrue(wordList.contains("good"));
    }
}
