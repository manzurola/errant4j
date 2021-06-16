package edu.guym.errantj.lang.en.wordlist;

import edu.guym.errantj.lang.en.utils.IOUtils;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class HunspellWordList implements WordList {

    private final static String PATH = "/wordlist/hunspell/en_GB-large.txt";
    private final Set<String> words;

    public HunspellWordList() {
        this.words = IOUtils.loadResourceAsLineStream(HunspellWordList.class, PATH, StandardCharsets.UTF_8)
                .collect(Collectors.toCollection(ConcurrentSkipListSet::new));
    }

    @Override
    public boolean contains(String word) {
        return words.contains(word);
    }
}
