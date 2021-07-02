package io.languagetoys.errant4j.lang.en.utils.lemmatize;

import java.util.Set;

public interface Lemmatizer {

    Set<String> lemmas(String word);
}
