package edu.guym.errantj.lang.en.utils.wordlist;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class HunspellWordList implements WordList {

    private final static String PATH = "/wordlist/hunspell/en_GB-large.txt";
    private final Set<String> words;

    public HunspellWordList() {
        InputStream inputStream = HunspellWordList.class.getResourceAsStream(PATH);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        this.words = reader.lines().collect(Collectors.toCollection(ConcurrentSkipListSet::new));
    }

    @Override
    public boolean contains(String word) {
        return words.contains(word);
    }
}
