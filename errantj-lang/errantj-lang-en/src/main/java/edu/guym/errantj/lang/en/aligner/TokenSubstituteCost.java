package edu.guym.errantj.lang.en.aligner;

import edu.guym.errantj.lang.en.classiy.common.TokenPredicates;
import edu.guym.errantj.lang.en.lemmatize.Lemmatizer;
import edu.guym.spacyj.api.containers.Token;
import io.squarebunny.aligner.utils.AlignerUtils;

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
        if (source.lowerCase().equals(target.lowerCase())) {
            return 0.0;
        } else {
            return lemmaSubstituteCost(source, target) +
                    posSubstituteCost(source, target) +
                    charSubstituteCost(source, target);
        }
    }

    private double lemmaSubstituteCost(Token s, Token t) {
        Set<String> sourceLemmas = lemmatizer.lemmas(s.text());
        Set<String> targetLemmas = lemmatizer.lemmas(t.text());
        Set<String> intersection = new HashSet<>(sourceLemmas);
        intersection.retainAll(targetLemmas);
        return !intersection.isEmpty() ? 0 : 0.499;
    }

    private double posSubstituteCost(Token source, Token target) {
        String sourcePos = source.pos();
        String targetPos = target.pos();
        if (sourcePos.equals(targetPos)) {
            return 0;
        }

        if (source.matches(TokenPredicates.isContentWord()) && target.matches(TokenPredicates.isContentWord())) {
            return 0.25;
        }

        return 0.5;
    }

    private double charSubstituteCost(Token s, Token t) {
        return AlignerUtils.charEditRatio(s.text(), t.text());
    }
}
