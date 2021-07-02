package io.languagetoys.errant4j.lang.en.align;

import io.languagetoys.aligner.Aligner;
import io.languagetoys.aligner.Alignment;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import io.languagetoys.spacy4j.api.containers.Token;

import java.util.Comparator;
import java.util.List;

public class EnAligner implements Aligner<Token> {

    private final Aligner<Token> impl;

    public EnAligner(Lemmatizer lemmatizer) {
        this.impl = Aligner.damerauLevenshtein(
                (source, target) -> source.text().equals(target.text()),
                Comparator.comparing(Token::lower),
                new EnSubstituteCost(lemmatizer)
        );
    }

    @Override
    public Alignment<Token> align(List<Token> source, List<Token> target) {
        return impl.align(source, target);
    }
}
