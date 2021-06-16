package edu.guym.errantj.lang.en.align;

import edu.guym.errantj.lang.en.classify.rules.common.CommonPredicates;
import edu.guym.errantj.lang.en.utils.lemmatize.Lemmatizer;
import edu.guym.spacyj.api.containers.Token;
import edu.guym.aligner.utils.AlignerUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public class TokenSubstituteCost implements BiFunction<Token, Token, Double> {

    private final Lemmatizer lemmatizer;

    public TokenSubstituteCost(Lemmatizer lemmatizer) {
        this.lemmatizer = lemmatizer;
    }

    @Override
    public Double apply(Token source, Token target) {
        if (source.lower().equals(target.lower())) {
            return 0.0;
        } else {
            return lemmaCost(source, target) +
                    posCost(source, target) +
                    charCost(source, target);
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

        if (source.matches(CommonPredicates.isContentWord()) && target.matches(CommonPredicates.isContentWord())) {
            return 0.25;
        }

        return 0.5;
    }

    private double charCost(Token s, Token t) {
        return AlignerUtils.charEditRatio(s.text(), t.text());
    }
}
