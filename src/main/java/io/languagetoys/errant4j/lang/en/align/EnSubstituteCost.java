package io.languagetoys.errant4j.lang.en.align;

import io.languagetoys.aligner.metrics.SubstituteCost;
import io.languagetoys.aligner.utils.AlignerUtils;
import io.languagetoys.errant4j.lang.en.classify.rules.common.Predicates;
import io.languagetoys.errant4j.lang.en.utils.lemmatize.Lemmatizer;
import io.languagetoys.spacy4j.api.containers.Token;
import io.languagetoys.spacy4j.api.features.Pos;

import java.util.HashSet;
import java.util.Set;

public class EnSubstituteCost implements SubstituteCost<Token> {

    private final Lemmatizer lemmatizer;

    public EnSubstituteCost(Lemmatizer lemmatizer) {
        this.lemmatizer = lemmatizer;
    }

    @Override
    public double getCost(Token source, Token target) {
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
        Set<String> sourceLemmas = lemmatizer.lemmas(s.text());
        Set<String> targetLemmas = lemmatizer.lemmas(t.text());
        Set<String> intersection = new HashSet<>(sourceLemmas);
        intersection.retainAll(targetLemmas);
        return !intersection.isEmpty() ? 0 : 0.499;
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
