package io.github.manzurola.errant4j.core.align;

import com.github.manzurola.aligner.metrics.SubstituteCost;
import com.github.manzurola.aligner.utils.AlignerUtils;
import io.github.manzurola.errant4j.lang.en.classify.rules.common.Predicates;
import io.github.manzurola.spacy4j.api.containers.Token;
import io.github.manzurola.spacy4j.api.features.Pos;

class TokenSubstituteCost implements SubstituteCost<Token> {

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

        if (source.matches(Predicates.isContentWord()) &&
            target.matches(Predicates.isContentWord())) {
            return 0.25;
        }

        return 0.5;
    }

    private double charCost(Token source, Token target) {
        return AlignerUtils.charEditRatio(source.text(), target.text());
    }

    private double whitespaceCost(Token source, Token target) {
        // special treatment for spacy whitespace tokens: penalize word -
        // whitespace substitution
        if (Pos.SPACE.matches(source.pos()) &&
            !Pos.SPACE.matches(target.pos()) ||
            !Pos.SPACE.matches(source.pos()) &&
            Pos.SPACE.matches(target.pos())) {
            return 2.0;
        }
        return 0.0;
    }
}
