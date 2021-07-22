package com.github.manzurola.errant4j.core.align;

import com.github.manzurola.aligner.Aligner;
import com.github.manzurola.aligner.Alignment;
import com.github.manzurola.aligner.metrics.SubstituteCost;
import com.github.manzurola.aligner.utils.AlignerUtils;
import com.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import com.github.manzurola.spacy4j.api.containers.Token;
import com.github.manzurola.spacy4j.api.features.Pos;

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

    private static class TokenSubstituteCost implements SubstituteCost<Token> {

        @Override
        public final double getCost(Token source, Token target) {
            if (source.lower().equals(target.lower())) {
                return 0.0;
            } else {
                return lemmaCost(source, target) +
                       posCost(source, target) +
                       charCost(source, target) +
                       whitespaceCost(source, target);
            }
        }

        private double lemmaCost(Token s, Token t) {
            return s.lemma().equals(t.lemma()) ? 0.0 : 0.499;
        }

        private double posCost(Token source, Token target) {
            String sourcePos = source.pos();
            String targetPos = target.pos();
            if (sourcePos.equals(targetPos)) {
                return 0;
            }

            if (source.matches(Predicates.isContentWord()) && target.matches(Predicates.isContentWord())) {
                return 0.25;
            }

            return 0.5;
        }

        private double charCost(Token source, Token target) {
            return AlignerUtils.charEditRatio(source.text(), target.text());
        }

        private double whitespaceCost(Token source, Token target) {
            // special treatment for spacy whitespace tokens: penalize word - whitespace substitution
            if (Pos.SPACE.matches(source.pos()) && !Pos.SPACE.matches(target.pos()) ||
                !Pos.SPACE.matches(source.pos()) && Pos.SPACE.matches(target.pos())) {
                return 2.0;
            }
            return 0.0;
        }
    }
}
