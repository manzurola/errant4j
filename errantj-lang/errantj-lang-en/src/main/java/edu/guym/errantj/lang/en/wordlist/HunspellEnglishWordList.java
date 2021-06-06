package edu.guym.errantj.lang.en.wordlist;

import edu.guym.errantj.lang.en.utils.IOUtils;
import edu.guym.errantj.wordlist.WordListBase;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class HunspellEnglishWordList extends WordListBase {

    private final static String PATH = "/en/hunspell/en_GB-large.txt";

    public HunspellEnglishWordList() {
        super(() -> IOUtils.loadResourceAsLineStream(HunspellEnglishWordList.class, PATH, StandardCharsets.UTF_8)
                .collect(Collectors.toList())
        );
    }


}
