package com.github.manzurola.errant4j.core.align;

import com.github.manzurola.aligner.Aligner;
import com.github.manzurola.aligner.Alignment;
import com.github.manzurola.spacy4j.api.containers.Token;

import java.util.Comparator;
import java.util.List;

public class TokenAligner implements Aligner<Token> {

    private final Aligner<Token> impl;

    public TokenAligner() {
        this.impl = Aligner.damerauLevenshtein(
            (source, target) -> source.text().equals(target.text()),
            Comparator.comparing(Token::lower),
            new TokenSubstituteCost()
        );
    }

    @Override
    public Alignment<Token> align(List<Token> source, List<Token> target) {
        return impl.align(source, target);
    }

}
