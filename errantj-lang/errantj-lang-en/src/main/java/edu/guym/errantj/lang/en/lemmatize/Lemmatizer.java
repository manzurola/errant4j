package edu.guym.errantj.lang.en.lemmatize;

import java.util.Set;

public interface Lemmatizer {

    Set<String> lemmas(String word);
}
