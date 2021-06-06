package edu.guym.errantj.wordlist;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Supplier;

public abstract class WordListBase implements WordList {

    private final Set<String> words;

    public WordListBase(Supplier<List<String>> supplier) {
        this.words = new ConcurrentSkipListSet<>(supplier.get());
    }

    @Override
    public boolean contains(String word) {
        return words.contains(word);
    }
}
