package edu.guym.errantj.lang.en.align;

import edu.guym.aligner.Aligner;
import edu.guym.aligner.Alignment;
import edu.guym.errantj.lang.en.utils.lemmatize.Lemmatizer;
import edu.guym.spacyj.api.containers.Token;

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
